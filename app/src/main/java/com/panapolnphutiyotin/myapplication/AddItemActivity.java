package com.panapolnphutiyotin.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddItemActivity extends AppCompatActivity {

    private ImageButton addImageButton;
    private EditText Barcode;
    private EditText Name;
    private EditText Type;
    private EditText Unit;
    private EditText Price;
    private EditText Madein;
    private Button Submit;
    private Uri imageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");

        mProgress = new ProgressDialog(this);


        addImageButton = (ImageButton) findViewById(R.id.addImageButton);
        Barcode = (EditText)findViewById(R.id.addBarcodeEdit);
        Name = (EditText)findViewById(R.id.addNameEdit);
        Type = (EditText)findViewById(R.id.addTypeEdit);
        Unit = (EditText)findViewById(R.id.addUnitEdit);
        Price = (EditText)findViewById(R.id.addPriceEdit);
        Madein = (EditText)findViewById(R.id.addMadeinEdit);
        Submit = (Button)findViewById(R.id.addSubmitButton);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String barcode_val = Barcode.getText().toString().trim();
                final String name_val    = Name.getText().toString().trim();
                final String type_val    = Type.getText().toString().trim();
                final String unit_val    = Unit.getText().toString().trim();
                final String price_val   = Price.getText().toString().trim();
                final String madein_val  = Madein.getText().toString().trim();


                if(imageUri != null) {

                    mProgress.setMessage("Adding to inventory . . .");
                    mProgress.show();

                    StorageReference filepath = mStorage.child("Item_Image").child(imageUri.getLastPathSegment());

                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            DatabaseReference newItem = mDatabase.push();
                            newItem.child("Barcode").setValue(barcode_val);
                            newItem.child("Name").setValue(name_val);
                            newItem.child("Type").setValue(type_val);
                            newItem.child("Unit").setValue(unit_val);
                            newItem.child("Price").setValue(price_val);
                            newItem.child("Madein").setValue(madein_val);
                            newItem.child("Image").setValue(downloadUrl.toString());

                            mProgress.dismiss();
                            finish();

                        }
                    });
                }
                else{

                    mProgress.setMessage("Adding to inventory . . .");
                    mProgress.show();

                    DatabaseReference newItem = mDatabase.push();
                    newItem.child("Barcode").setValue(barcode_val);
                    newItem.child("Name").setValue(name_val);
                    newItem.child("Type").setValue(type_val);
                    newItem.child("Unit").setValue(unit_val);
                    newItem.child("Price").setValue(price_val);
                    newItem.child("Madein").setValue(madein_val);
                    newItem.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/examplelogin-a0b4b.appspot.com/o/Item_Image%2Fimage%3A58?alt=media&token=3cdc2779-fa01-436e-93b1-4a8c836ab422");

                    mProgress.dismiss();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==  GALLERY_REQUEST && resultCode == RESULT_OK){

            imageUri = data.getData();
            addImageButton.setImageURI(imageUri);
        }
    }
}

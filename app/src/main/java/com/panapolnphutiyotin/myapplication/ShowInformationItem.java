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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ShowInformationItem extends AppCompatActivity {

    private ImageButton addImageButton2;
    private EditText Barcode2;
    private EditText Name2;
    private EditText Type2;
    private EditText Unit2;
    private EditText Price2;
    private EditText Madein2;
    private Button Submit2;
    private Button Submit3;
    private String image;
    private Uri imageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_information_item);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("key");

        addImageButton2 = (ImageButton) findViewById(R.id.addImageButton2);
        Barcode2 = (EditText) findViewById(R.id.addBarcodeEdit2);
        Name2 = (EditText) findViewById(R.id.addNameEdit2);
        Type2 = (EditText) findViewById(R.id.addTypeEdit2);
        Unit2 = (EditText) findViewById(R.id.addUnitEdit2);
        Price2 = (EditText) findViewById(R.id.addPriceEdit2);
        Madein2 = (EditText) findViewById(R.id.addMadeinEdit2);
        Submit2 = (Button) findViewById(R.id.addSubmitButton2);
        Submit3 = (Button) findViewById(R.id.addSubmitButton3);

        final ProgressDialog mProgress = new ProgressDialog(this);


        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items").child(value);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item i2 = dataSnapshot.getValue(item.class);
                Picasso.with(ShowInformationItem.this).load(i2.getImage()).fit().placeholder(R.mipmap.ic_launcher).into(addImageButton2);
                Barcode2.setText(i2.getBarcode());
                Name2.setText(i2.getName());
                Type2.setText(i2.getType());
                Unit2.setText(i2.getUnit());
                Price2.setText(i2.getPrice());
                Madein2.setText(i2.getMadein());
                image = i2.getImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        Submit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String barcode_val = Barcode2.getText().toString().trim();
                final String name_val = Name2.getText().toString().trim();
                final String type_val = Type2.getText().toString().trim();
                final String unit_val = Unit2.getText().toString().trim();
                final String price_val = Price2.getText().toString().trim();
                final String madein_val = Madein2.getText().toString().trim();


                if (imageUri != null) {

                    mProgress.setMessage("Changing information . . .");
                    mProgress.show();

                    StorageReference filepath = mStorage.child("Item_Image").child(imageUri.getLastPathSegment());

                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            DatabaseReference newItem = mDatabase;
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
                } else {

                    mProgress.setMessage("Adding to inventory . . .");
                    mProgress.show();

                    DatabaseReference newItem = mDatabase;
                    newItem.child("Barcode").setValue(barcode_val);
                    newItem.child("Name").setValue(name_val);
                    newItem.child("Type").setValue(type_val);
                    newItem.child("Unit").setValue(unit_val);
                    newItem.child("Price").setValue(price_val);
                    newItem.child("Madein").setValue(madein_val);
                    newItem.child("Image").setValue(image);

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
                System.out.println(imageUri);
                addImageButton2.setImageURI(imageUri);
            }
        }


    }


package com.panapolnphutiyotin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Firebase ref;
    private DatabaseReference mDatabase;
    private DatabaseReference sDatabase;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonAdditem;
    private EditText editTextProductName;
    private EditText editTextBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");

        if (firebaseAuth.getCurrentUser() == null) {
           // finish();
           // startActivity(new Intent(this, LoginActivity.class));
        }


        buttonAdditem = (Button) findViewById(R.id.buttonAdditem);

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome " + user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
        buttonAdditem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAdditem) {
            startActivity(new Intent(this, ShowList.class));
        }
        if (v == buttonLogout) {
            /*firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            */
            startActivity(new Intent(this, AddItemActivity.class));

            /*sDatabase = FirebaseDatabase.getInstance().getReference().child("system").child("items");
            Query query = sDatabase.orderByChild("Barcode").equalTo("123");
            query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        System.out.println(postSnapshot.getKey());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
*/
        }
    }

    public void addItem() {
        String productName = editTextProductName.getText().toString().trim();
        String Barcode = editTextBarcode.getText().toString().trim();

        Map<String, String> post1 = new HashMap<String, String>();
        post1.put("Name", productName);
        post1.put("Barcode", Barcode);

        mDatabase.push().setValue(post1);
    }

    public void check() {

        mDatabase.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    item post = postSnapshot.getValue(item.class);
                    System.out.println(post.getBarcode() + "  " + post.getName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}

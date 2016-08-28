package com.panapolnphutiyotin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
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

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        buttonAdditem = (Button) findViewById(R.id.buttonAdditem);
        editTextBarcode = (EditText) findViewById(R.id.editTextProductName);
        editTextProductName = (EditText) findViewById(R.id.editTextBarcode);
        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome "+ user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
        buttonAdditem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdditem ){
            addItem();
        }
        if(v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void addItem(){
        String productName = editTextProductName.getText().toString().trim();
        String Barcode = editTextBarcode.getText().toString().trim();

        Map<String, String> post1 = new HashMap<String, String>();
        post1.put("Name", productName);
        post1.put("Barcode", Barcode);
        mDatabase.push().setValue(post1);
//
    }
}
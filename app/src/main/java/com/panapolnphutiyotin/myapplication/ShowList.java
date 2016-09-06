package com.panapolnphutiyotin.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.squareup.picasso.Picasso;

public class ShowList extends AppCompatActivity {

    private RecyclerView mList;
    private ImageButton Additem;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private item i2;
    private boolean additem ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        Additem = (ImageButton) findViewById(R.id.Additem);

        imageView = (ImageView) findViewById(R.id.imageItem);
        firebaseAuth = FirebaseAuth.getInstance();
        additem = false;

        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");

        mList = (RecyclerView) findViewById(R.id.item_list);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        Additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowList.this, AddItemActivity.class));
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<item, ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<item, ItemViewHolder>(

                item.class,
                R.layout.itemlist,
                ItemViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, item model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setVolumn(model.getUnit());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mList ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);
                            s.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    item item = dataSnapshot.getValue(item.class);
                                    int i = Integer.parseInt(item.getUnit());
                                    i = i - 1;
                                    s.child("Unit").setValue(Integer.toString(i));

                                    }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                    }
                    @Override public void onLongItemClick(View view, int position) {
                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowList.this);
                        builder.setTitle("How much decrease ?");
                        final EditText input = new EditText(ShowList.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setGravity(Gravity.CENTER);
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String m = input.getText().toString();
                                final int j = Integer.parseInt(m);
                                s.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        item item = dataSnapshot.getValue(item.class);
                                        int i = Integer.parseInt(item.getUnit());
                                        i = i - j;
                                        s.child("Unit").setValue(Integer.toString(i));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                    }
                })
        );

        mList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView ;
        }
        public void setName(String Name){
            TextView name = (TextView) mView.findViewById(R.id.nameItem);
            name.setText(Name);
        }
        public void setVolumn(String Volumn){
            TextView volumn = (TextView) mView.findViewById(R.id.volumnItem);
            volumn.setText(Volumn);
        }
        public void setImage(Context ctx , String image){
            ImageView imageView = (ImageView) mView.findViewById(R.id.imageItem);
            Picasso.with(ctx).load(image).resize(200,200).placeholder(R.mipmap.ic_launcher).into(imageView);

        }


    }
}

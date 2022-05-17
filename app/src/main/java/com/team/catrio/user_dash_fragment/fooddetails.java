package com.team.catrio.user_dash_fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.org.Food;

public class fooddetails extends AppCompatActivity {
    TextView tvfoodlist,tvfoodtype,tvfooprice,tvorgname,tvorgaddress,tvorgcontact,order_page;
    ValueEventListener valueEventListener,valueEventListener2;
    ImageView ivfoodpic;
    RatingBar ratingBar;
    String foodid;
    DatabaseReference databaseReference,databaseReference2,ref3;
    Food food;
    Organization org;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetails);
        foodid=getIntent().getStringExtra("FOODID");
        tvfoodlist=findViewById(R.id.fooddetails_itemlist);
        tvfoodtype=findViewById(R.id.fooddeatils_foodtype);
        tvfooprice=findViewById(R.id.fooddeatil_price);
        tvorgname=findViewById(R.id.fooddeatils_orgname);
        tvorgaddress=findViewById(R.id.fooddeatils_orgnaddress);
        tvorgcontact=findViewById(R.id.fooddeatils_orgcontact);
        ratingBar=findViewById(R.id.fooddeatils_orgrating);
        ivfoodpic=findViewById(R.id.fooddetails_pic);
        order_page=findViewById(R.id.fooditemlist_order);
        ref3=FirebaseDatabase.getInstance().getReference().child("Organization");
        Toast.makeText(this,foodid,Toast.LENGTH_LONG).show();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Food").child(foodid);

      order_page.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i=new Intent(fooddetails.this,order.class);
              i.putExtra("FOODID",foodid);
              startActivity(i);
          }
      });

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                         food=snapshot.getValue(Food.class);
                          databaseReference2=FirebaseDatabase.getInstance().getReference().child("Organization").child(food.Org_id);
                          databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot snapshot) {
                                   org=snapshot.getValue(Organization.class);

                                  tvfoodlist.setText(food.Food_list);
                                  tvfoodtype.setText(food.food_category);
                                 tvfooprice.setText(String.valueOf(food.price));
                                  tvorgaddress.setText(org.address);
                                  tvorgcontact.setText(org.contactnum);
                                  tvorgname.setText(org.name);
                                  Picasso.get().load(food.imageuri).into(ivfoodpic);
                                  ratingBar.setRating(Float.parseFloat(String.valueOf(org.rating)));
                              }

                              @Override
                              public void onCancelled( DatabaseError error) {
                                  Toast.makeText(fooddetails.this, error.getMessage(),Toast.LENGTH_LONG).show();
                              }
                          });





            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(fooddetails.this, error.getMessage(),Toast.LENGTH_LONG).show();

            }
        };

        valueEventListener2=new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                databaseReference.addListenerForSingleValueEvent(valueEventListener);
            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
      databaseReference.addValueEventListener(valueEventListener);
      ref3.addValueEventListener(valueEventListener2);

    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
        ref3.removeEventListener(valueEventListener);
    }
}
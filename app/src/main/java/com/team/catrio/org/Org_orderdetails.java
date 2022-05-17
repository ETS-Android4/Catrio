package com.team.catrio.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.catrio.Order;
import com.team.catrio.R;
import com.team.catrio.User;

import java.text.SimpleDateFormat;

public class Org_orderdetails extends AppCompatActivity {
    TextView tvfoodlist,tvadress,tvcount,tvtype,tvtotalamount,tvcustname,tvcustcontact,tvorderdate,tvdeliverydate,tvcancel,tvconfirm,tvstaus;
    ValueEventListener valueEventListener;
    DatabaseReference ref1,ref2,ref3,ref4;
    ImageView foodimage;
    Order order;
    User user;
    Food food;
    int complete;
    ProgressDialog progressDialog;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_orderdetails);
        complete=getIntent().getIntExtra("COMPLETE",0);
        foodimage=findViewById(R.id.rg_orderdeails_foodimage);
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        String orderid=getIntent().getStringExtra("ORDERID");
        tvfoodlist=findViewById(R.id.org_orderdeails_foodlist);
        tvadress=findViewById(R.id.org_orderdeails_adress);
        tvtype=findViewById(R.id.org_orderdeails_foodtype);
        tvstaus=findViewById(R.id.org_orderdeails_status);
        ref1=FirebaseDatabase.getInstance().getReference().child("Order").child(orderid);
        tvcount=findViewById(R.id.org_orderdeails_food_count);
        tvtotalamount=findViewById(R.id.org_orderdeails_food_totalamount);
        tvcustname=findViewById(R.id.org_orderdeails_food_custname);
        tvcustcontact=findViewById(R.id.org_orderdeails_food_custcontactno);
        tvorderdate=findViewById(R.id.org_orderdeails_food_orderdate);
        tvdeliverydate=findViewById(R.id.org_orderdeails_food_deliveridate);
        tvcancel=findViewById(R.id.org_orderdeails_food_btncancelorder);
        tvconfirm=findViewById(R.id.org_orderdeails_food_btconfirmmorder);
        if(complete==1){
            tvcancel.setVisibility(View.GONE);
            tvconfirm.setVisibility(View.GONE);
        }
        tvconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                FirebaseDatabase.getInstance().getReference().child("Order").child(orderid).child("status").setValue("S").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Org_orderdetails.this,"CONFIRMED",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alerBuilder=new AlertDialog.Builder(Org_orderdetails.this);
                        alerBuilder.setTitle("SUCCESFULLY CONFIRMED").setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                        progressDialog.dismiss();
                        alerBuilder.show();


                    }
                });
            }
        });
        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Order").child(orderid).child("status").setValue("C").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Org_orderdetails.this,"CONFIRMED",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alerBuilder=new AlertDialog.Builder(Org_orderdetails.this);
                        alerBuilder.setTitle("SUCCESFULLY CANCELED").setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                        progressDialog.dismiss();
                        alerBuilder.show();
                    }
                });

            }
        });




        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                 order=snapshot.getValue(Order.class);
                ref2= FirebaseDatabase.getInstance().getReference().child("User").child(order.userid);
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        user=snapshot.getValue(User.class);
                        ref3= FirebaseDatabase.getInstance().getReference().child("Food").child(order.foodid);
                        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange( DataSnapshot snapshot) {
                                food=snapshot.getValue(Food.class);
                                tvfoodlist.setText(food.Food_list);
                                tvadress.setText(order.address);
                                tvcount.setText(order.count+"");
                                tvtype.setText(food.food_category);
                                tvtotalamount.setText(""+order.amoutn);
                                tvcustname.setText(user.name);
                                tvstaus.setText(order.status);
                                tvcustcontact.setText(user.contactnum);
                                Picasso.get().load(food.imageuri).into(foodimage);
                                tvdeliverydate.setText(simpleDateFormat.format(order.orderdate)+"");
                                tvorderdate.setText(simpleDateFormat.format(order.timestamp)+"");
                                progressDialog.dismiss();

                            }

                            @Override
                            public void onCancelled( DatabaseError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Org_orderdetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });



                    }

                    @Override
                    public void onCancelled( DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Org_orderdetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });



            }

            @Override
            public void onCancelled( DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(Org_orderdetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        };





    }

    @Override
    protected void onStart() {

        ref1.addValueEventListener(valueEventListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        ref1.removeEventListener(valueEventListener);
        super.onStop();
    }
}
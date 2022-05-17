package com.team.catrio.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.catrio.Organization;
import com.team.catrio.R;

public class adimi_org_details extends AppCompatActivity {
    TextView txtname,txtaddress,txtcontact,txtstate,txtcity,btnverify,btndelete;
    FirebaseAuth auth;
    ImageView im;
    DatabaseReference ref1,ref2;
    ProgressDialog progressDialog;
    Organization org;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i=getIntent();


        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.setCancelable(false);
        auth=FirebaseAuth.getInstance();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();
        setContentView(R.layout.activity_adimi_org_details);
        im=findViewById(R.id.admin_org_details_image);
        txtname=findViewById(R.id.admin_org_details_name);
        txtaddress=findViewById(R.id.admin_org_details_address);
        txtcontact=findViewById(R.id.admin_org_details_contact);
        btnverify=findViewById(R.id.admin_org_details_verify);
        btndelete=findViewById(R.id.admin_org_details_delete);
        txtcity=findViewById(R.id.admin_org_details_city);
        txtstate=findViewById(R.id.admin_org_details_state);

        if(i.getStringExtra("DELETE").equals("YES")){

            btnverify.setVisibility(View.GONE);

        }else{

            btndelete.setVisibility(View.GONE);
        }
        ref2=FirebaseDatabase.getInstance().getReference().child("Organization").child(getIntent().getStringExtra("ORGID")).child("verified");
        ref1=FirebaseDatabase.getInstance().getReference().child("Organization").child(getIntent().getStringExtra("ORGID"));
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                org=snapshot.getValue(Organization.class);
                if(org.verified){
                    btnverify.setText("REMOVE VERIFICATION");
                }else{
                    btnverify.setText("VERIFY");
                }
                txtname.setText(org.name);
                txtaddress.setText(org.address);
                txtcontact.setText(org.contactnum);
                txtcity.setText(org.city);
                txtstate.setText(org.state);
                progressDialog.dismiss();
                Picasso.get().load(org.imageuri).into(im);


            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnverify.getText().equals("VERIFY")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(adimi_org_details.this);
                    builder.setTitle("VERIFY IT");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ref2.setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(adimi_org_details.this);
                                    builder1.setTitle("SUCCESSFULLY VERIFIED");
                                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                                    builder1.show();

                                }

                            });
                            dialog.dismiss();


                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(adimi_org_details.this);
                    builder.setTitle("REMOVE VERIFICATION");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ref2.setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(adimi_org_details.this);
                                    builder1.setTitle("SUCCESSFULLY UNVERIFIED");
                                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                                    builder1.show();

                                }

                            });
                            dialog.dismiss();


                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();



                }
            }
        });





    }
}
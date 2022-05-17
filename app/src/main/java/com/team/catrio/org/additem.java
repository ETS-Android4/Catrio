package com.team.catrio.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team.catrio.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class additem extends AppCompatActivity {
    private static final int IMAGE_UPLODE_REQUEST =123 ;
    EditText txt_item_list;
    EditText txt_price;
    Spinner spinner_type;
    ImageView food_pic;
    TextView add_button;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    StorageReference storage;
    String foodtype;
    Uri Imageuri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        txt_item_list=findViewById(R.id.food_list);
        txt_price=findViewById(R.id.food_price);
        spinner_type=findViewById(R.id.food_type);
        food_pic=findViewById(R.id.food_pic);
        ArrayAdapter<CharSequence> spiineradapter = ArrayAdapter.createFromResource(this, R.array.food_type, android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(spiineradapter);
        add_button=findViewById(R.id.food_submit);

        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.setCancelable(false);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodtype = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        food_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,IMAGE_UPLODE_REQUEST);
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String itemlist=txt_item_list.getText().toString().trim();
                int price=Integer.parseInt(txt_price.getText().toString().trim());



                auth=FirebaseAuth.getInstance();
                databaseReference= FirebaseDatabase.getInstance().getReference();
                databaseReference=databaseReference.child("Food");
                storage=FirebaseStorage.getInstance().getReference("Food_image");
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
                String type = getExtension(Imageuri);

                storage = storage.child(df.format(new Date()) + "." + type);
                storage.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                databaseReference=databaseReference.push();

                                  Food food=new Food(auth.getCurrentUser().getUid(),itemlist,uri.toString(),foodtype,databaseReference.getKey(),price);
                                  databaseReference.setValue(food).addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void unused) {
                                          progressDialog.dismiss();
                                          finish();
                                      }
                                  });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure( Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(additem.this,e.getMessage(),Toast.LENGTH_LONG);


                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(additem.this,e.getMessage(),Toast.LENGTH_LONG);
                    }
                });


            }
        });


    }

    private String getExtension(Uri uri) {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mm=MimeTypeMap.getSingleton();
        return  mm.getExtensionFromMimeType(cr.getType(uri));

    }
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_UPLODE_REQUEST){
            if(resultCode==RESULT_OK){
                Imageuri=data.getData();
                food_pic.setImageURI(data.getData());

            }
        }


    }




}
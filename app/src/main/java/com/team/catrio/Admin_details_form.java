package com.team.catrio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Admin_details_form extends AppCompatActivity {
    private static final int IMAGE_UPLODE_REQUEST = 123;
    Uri Imageuri;
    EditText et_name,et_contact,et_address,et_city,et_state;
    ImageView im_adminimage;
    TextView btn_signup;
    FirebaseAuth auth;
    DatabaseReference databaseref;
    StorageReference storeref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details_form);
        im_adminimage=findViewById(R.id.admin_sinup_image);
        et_name=findViewById(R.id.admin_sinup_name);
        et_contact=findViewById(R.id.admin_sinup_contact);
        et_address=findViewById(R.id.admin_sinup_address);
        et_city=findViewById(R.id.admin_sinup_city);
        et_state=findViewById(R.id.admin_sinup_state);
        btn_signup=findViewById(R.id.admin_sinup_button);

        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("UPDATE YOUR DETAILS...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        im_adminimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,IMAGE_UPLODE_REQUEST);
            }
        });


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth=FirebaseAuth.getInstance();
                String input_name,input_contact,input_address,input_city,input_state;
                input_name=et_name.getText().toString().trim();
                input_contact=et_contact.getText().toString().trim();
                input_address=et_address.getText().toString().trim();
                input_city=et_city.getText().toString().trim();
                input_state=et_state.getText().toString().trim();


                if(Pattern.matches("[a-zA-z ]{4,}",input_name)){
                    if(Pattern.matches("[\\+]?\\d{10,14}",input_contact)){

                        if(Pattern.matches(".{7,}",input_address)){




                                    if(Pattern.matches(".{3,}",input_city)){
                                        if(Pattern.matches(".{3,}",input_state)){



                                                if (Imageuri != null) {
                                                    progressDialog.show();

                                                    String uid = auth.getCurrentUser().getUid();
                                                    databaseref = FirebaseDatabase.getInstance().getReference();
                                                    databaseref = databaseref.child("Admin");
                                                    SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
                                                    String type = getExtension(Imageuri);
                                                    storeref = FirebaseStorage.getInstance().getReference().child("userimages");
                                                    storeref = storeref.child(df.format(new Date()) + "." + type);
                                                    storeref.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                            storeref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {

                                                                    Admin admin=new Admin(input_name,input_contact,uri.toString(),input_address,input_city,input_state,auth.getCurrentUser().getEmail());
                                                                    databaseref=databaseref.child(auth.getCurrentUser().getUid());
                                                                    databaseref.setValue(admin);
                                                                    progressDialog.dismiss();
                                                                    Intent i=new Intent(Admin_details_form.this,admin_dash.class);
                                                                    startActivity(i);
                                                                    finish();
                                                                }
                                                            });

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure( Exception e) {
                                                            progressDialog.show();
                                                            Toast.makeText(Admin_details_form.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                }else{
                                                    Toast.makeText(Admin_details_form.this,"UPLOAD IMAGE",Toast.LENGTH_LONG).show();
                                                }
                                        }else{
                                            Toast.makeText(Admin_details_form.this,"INVALID STATE NAME",Toast.LENGTH_LONG).show();
                                        }

                                    }else {
                                        Toast.makeText(Admin_details_form.this,"INVALID CITY NAME",Toast.LENGTH_LONG).show();
                                    }

                        }else {
                            Toast.makeText(Admin_details_form.this,"INVALID ADDRESS",Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(Admin_details_form.this,"INVALID CONTACT NO",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Admin_details_form.this,"INVALID NAME",Toast.LENGTH_LONG).show();
                }


            }
        });


    }
    private String getExtension(Uri uri) {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mm=MimeTypeMap.getSingleton();
        return  mm.getExtensionFromMimeType(cr.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_UPLODE_REQUEST){
            if(resultCode==RESULT_OK){
                Imageuri=data.getData();
                im_adminimage.setImageURI(data.getData());

            }
        }


    }
}
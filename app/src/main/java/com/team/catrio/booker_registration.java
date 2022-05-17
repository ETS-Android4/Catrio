package com.team.catrio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class booker_registration extends AppCompatActivity {

    private static final int IMAGE_UPLODE_REQUEST = 123;
    Uri Imageuri;
    EditText et_name,et_email,et_contact,et_password,et_confirmpass,et_address,et_city,et_state;
    ImageView im_userimage;
    TextView btn_signup,txt_passwordindicator;
    FirebaseAuth auth;
    DatabaseReference databaseref;
    StorageReference storeref;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booker_registration);

        im_userimage=findViewById(R.id.user_sinup_image);
        et_name=findViewById(R.id.user_sinup_name);
        et_email=findViewById(R.id.user_sinup_EMAIL);
        et_contact=findViewById(R.id.user_sinup_contact);
        et_password=findViewById(R.id.user_sinup_password);
        et_confirmpass=findViewById(R.id.user_sinup_confirmpassword);
        et_address=findViewById(R.id.user_sinup_address);
        et_city=findViewById(R.id.user_sinup_city);
        et_state=findViewById(R.id.user_sinup_state);
        btn_signup=findViewById(R.id.user_sinup_button);
        txt_passwordindicator=findViewById(R.id.user_sinup_confir_indicator);

         progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("REGISTRING");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        et_confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_password.getText().toString().equals(et_confirmpass.getText().toString())){
                    txt_passwordindicator.setBackground(getResources().getDrawable(R.drawable.passwordconfirmer));
                }else{
                    txt_passwordindicator.setBackground(getResources().getDrawable(R.drawable.passwordnotconfirmed));
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et_password.getText().toString().equals(et_confirmpass.getText().toString())){
                    txt_passwordindicator.setBackground(getResources().getDrawable(R.drawable.passwordconfirmer));
                }else{
                    txt_passwordindicator.setBackground(getResources().getDrawable(R.drawable.passwordnotconfirmed));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        im_userimage.setOnClickListener(new View.OnClickListener() {
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

               String input_name,input_contact, input_email,input_password,input_confirmpass,input_adress,input_city,input_state;
               input_name=et_name.getText().toString().trim();
               input_contact=et_contact.getText().toString().trim();
               input_email=et_email.getText().toString().trim();
               input_adress=et_address.getText().toString().trim();
               input_city=et_city.getText().toString().trim();
               input_state=et_state.getText().toString().trim();
               input_password=et_password.getText().toString().trim();
               input_confirmpass=et_confirmpass.getText().toString().trim();
               if(Pattern.matches("[a-zA-z ]{4,}",input_name)){
                   if(Pattern.matches("[\\+]?\\d{10,14}",input_contact)){

                       if(Pattern.matches(".{7,}",input_adress)){
                           if(Pattern.matches(".{8,20}",input_password)){

                               if((input_password.equals(input_confirmpass))){

                                   if(Pattern.matches(".{3,}",input_city)){
                                       if(Pattern.matches(".{3,}",input_state)){

                                           if(Pattern.matches(".+@.*\\..*",input_email)){

                                               if (Imageuri != null) {

                                                   progressDialog.show();

                                                   auth = FirebaseAuth.getInstance();
                                                   auth.createUserWithEmailAndPassword(input_email, input_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                       @Override
                                                       public void onSuccess(AuthResult authResult) {
                                                           String uid = auth.getCurrentUser().getUid();
                                                           databaseref = FirebaseDatabase.getInstance().getReference();
                                                           databaseref = databaseref.child("User");
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

                                                                           User user = new User(input_name, input_contact, uri.toString(), input_adress, input_city, input_state, input_email);
                                                                           databaseref = databaseref.child(auth.getCurrentUser().getUid());
                                                                           databaseref.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                               @Override
                                                                               public void onSuccess(Void unused) {
                                                                                   databaseref=FirebaseDatabase.getInstance().getReference().child("Idmap");
                                                                                   databaseref.child(auth.getCurrentUser().getUid()).setValue(new Idmap(auth.getCurrentUser().getUid(),"U")).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                       @Override
                                                                                       public void onSuccess(Void unused) {
                                                                                           auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                               @Override
                                                                                               public void onSuccess(Void unused) {
                                                                                                   progressDialog.dismiss();
                                                                                                   AlertDialog.Builder builder = new AlertDialog.Builder(booker_registration.this);
                                                                                                   Intent i = new Intent(booker_registration.this, Login.class);
                                                                                                   builder.setTitle("VERIFY THE EMAIL").setMessage("WE SEND A VERIFY EMAIL TO YOU SO BEFORE LOGIN PLEASE VERIGY IT")
                                                                                                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                                               @Override
                                                                                                               public void onClick(DialogInterface dialog, int which) {
                                                                                                                   startActivity(i);
                                                                                                                   dialog.dismiss();
                                                                                                                   finish();
                                                                                                               }
                                                                                                           });
                                                                                                   builder.show();




                                                                                               }
                                                                                           });



                                                                                       }
                                                                                   }).addOnFailureListener(new OnFailureListener() {
                                                                                       @Override
                                                                                       public void onFailure(Exception e) {
                                                                                           Toast.makeText(booker_registration.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                                                       }
                                                                                   });
                                                                               }
                                                                           }).addOnFailureListener(new OnFailureListener() {
                                                                               @Override
                                                                               public void onFailure(Exception e) {
                                                                                   progressDialog.dismiss();
                                                                                   Toast.makeText(booker_registration.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                                                               }
                                                                           });


                                                                       }
                                                                   });

                                                               }
                                                           }).addOnFailureListener(new OnFailureListener() {
                                                               @Override
                                                               public void onFailure(Exception e) {
                                                                   progressDialog.dismiss();
                                                                   Toast.makeText(booker_registration.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                               }
                                                           });


                                                       }
                                                   }).addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(Exception e) {
                                                           progressDialog.dismiss();
                                                           Toast.makeText(booker_registration.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                       }
                                                   });


                                               }else{
                                                   Toast.makeText(booker_registration.this,"UPLOAD IMAGE",Toast.LENGTH_LONG).show();
                                               }

                                           }else{


                                               Toast.makeText(booker_registration.this,"INVALID EMAIL ID",Toast.LENGTH_LONG).show();

                                           }
                                       }else{
                                           Toast.makeText(booker_registration.this,"INVALID STATE NAME",Toast.LENGTH_LONG).show();
                                       }

                                   }else {
                                       Toast.makeText(booker_registration.this,"INVALID CITY NAME",Toast.LENGTH_LONG).show();
                                   }
                               }else{
                                   Toast.makeText(booker_registration.this," PASSWORDS ARE NOT EQUAL"+input_confirmpass+" "+input_password,Toast.LENGTH_LONG).show();
                               }


                           }else{
                               Toast.makeText(booker_registration.this,"INVALID PASSWORD",Toast.LENGTH_LONG).show();
                           }
                       }else {
                           Toast.makeText(booker_registration.this,"INVALID ADDRESS",Toast.LENGTH_LONG).show();
                       }

                   }else{
                       Toast.makeText(booker_registration.this,"INVALID CONTACT NO",Toast.LENGTH_LONG).show();
                   }
               }else{
                   Toast.makeText(booker_registration.this,"INVALID NAME",Toast.LENGTH_LONG).show();
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
                im_userimage.setImageURI(data.getData());

            }
        }


    }
}
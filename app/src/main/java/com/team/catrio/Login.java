package com.team.catrio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;

public class Login extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    private String email;
    private String password;
    TextView tv_loginbtn,tv_visibe,login_to_user_signup,login_to_org_signup;
    ProgressDialog progressDialog;
    boolean visible=false;
    DatabaseReference reference;
    FirebaseAuth  auth;
    String TAG="hello";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout mainlayout;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences("login", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        scrollView=findViewById(R.id.sublayout);
        et_email=findViewById(R.id.login_email);
        et_password=findViewById(R.id.login_password);
        tv_loginbtn=findViewById(R.id.login_button);
        tv_visibe=findViewById(R.id.login_pass_visible);
        mainlayout=findViewById(R.id.main_layout);
        login_to_user_signup=findViewById(R.id.login_user_signup);
        login_to_org_signup=findViewById(R.id.login_org_signup);
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.setCancelable(false);
        auth=FirebaseAuth.getInstance();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        login_to_user_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,booker_registration.class));
            }
        });
        login_to_org_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,cateringorgregistration.class));
            }
        });



        tv_visibe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible){
                    visible=false;
                    tv_visibe.setBackground(getResources().getDrawable(R.drawable.ic_visible));
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }else{
                    visible=true;
                    tv_visibe.setBackground(getResources().getDrawable(R.drawable.ic_notvisible));
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT);

                }
            }
        });
        tv_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                email=et_email.getText().toString().trim();
                password=et_password.getText().toString().trim();
                auth=FirebaseAuth.getInstance();


                reference= FirebaseDatabase.getInstance().getReference().child("Idmap");


                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if (true){


                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    progressDialog.dismiss();
                                    Log.d(TAG, "ondatachange :  ");
                                    String uid = auth.getCurrentUser().getUid();
                                    Boolean found = false;
                                    Idmap idmap = null;

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        idmap = snapshot1.getValue(Idmap.class);
                                        if (auth.getCurrentUser().getUid().equals(idmap.id)) {
                                            found = true;
                                            break;

                                        }


                                    }
                                    Intent i;
                                    progressDialog.dismiss();
                                    if (found) {

                                        Log.d(TAG, "onDataChange: found " + idmap.desc);


                                        if (idmap.desc.equals("O")) {
                                            editor.putBoolean("remember",true);
                                            editor.putString("username",email);
                                            editor.putString("password",password);
                                            editor.putString("type","O");
                                            editor.commit();
                                            i = new Intent(Login.this, organization_dash.class);
                                            startActivity(i);
                                        } else if (idmap.desc.equals("U")) {
                                            editor.putBoolean("remember",true);
                                            editor.putString("username",email);
                                            editor.putString("password",password);
                                            editor.putString("type","U");
                                            editor.commit();
                                            i = new Intent(Login.this, user_dash.class);
                                            startActivity(i);
                                        }

                                    } else {
                                        reference = FirebaseDatabase.getInstance().getReference();
                                        reference = reference.child("Admin").child(auth.getCurrentUser().getUid());
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange( DataSnapshot snapshot) {
                                                Intent i;
                                                Admin admin=snapshot.getValue(Admin.class);
                                                if(admin==null){
                                                    i = new Intent(Login.this, Admin_details_form.class);
                                                    startActivity(i);

                                                }else {
                                                    editor.putBoolean("remember",true);
                                                    editor.putString("username",email);
                                                    editor.putString("password",password);
                                                    editor.putString("type","A");
                                                    editor.commit();
                                                    i = new Intent(Login.this, admin_dash.class);
                                                    startActivity(i);

                                                }
                                            }

                                            @Override
                                            public void onCancelled( DatabaseError error) {

                                            }
                                        });



                                    }

                                    finish();

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    progressDialog.dismiss();

                                }
                            });
                    }else{


                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                            builder.setTitle("VERIFY THE EMAIL").setMessage("WE ALREADY  SENT A  EMAIL TO VERYFY YOUR ACCOUNT PLEASE CONFIRM IT")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    }).setNegativeButton("SEND AGAIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.getCurrentUser().sendEmailVerification();
                                }
                            });
                            progressDialog.dismiss();
                            builder.show();



                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onDataChange: error found ");
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });







            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPreferences.getBoolean("remember",false)){


            progressDialog.setMessage("LOADING");
            progressDialog.show();
            mainlayout.setBackground(getResources().getDrawable(R.drawable.catrio));
            scrollView.setVisibility(View.GONE);

            String rememail=sharedPreferences.getString("username","");
            String rempassword=sharedPreferences.getString("password","");
            auth.signInWithEmailAndPassword(rememail, rempassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser FUser = auth.getCurrentUser();



                    if(sharedPreferences.getString("type","no").equals("U")) {

                        Intent i = new Intent(Login.this, user_dash.class);
                        startActivity(i);
                        progressDialog.dismiss();
                        finish();

                    }
                    if(sharedPreferences.getString("type","no").equals("O")) {

                        Intent i = new Intent(Login.this, organization_dash.class);
                        startActivity(i);
                        progressDialog.dismiss();
                        finish();

                    }
                    if(sharedPreferences.getString("type","no").equals("A")) {

                        Intent i = new Intent(Login.this, admin_dash.class);
                        startActivity(i);
                        progressDialog.dismiss();
                        finish();

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
    }
}
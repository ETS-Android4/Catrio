package com.team.catrio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.catrio.org.user_complete_order;
import com.team.catrio.user_dash_fragment.home;
import com.team.catrio.user_dash_fragment.user_pending_order;
import com.team.catrio.user_dash_fragment.user_profile;

public class user_dash extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    View headerview;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    NavigationView navigationView;
    FrameLayout frameLayout;
    ImageView imageView;
    User user;
    TextView name, email;
    DatabaseReference ref;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    long backpresstime;
    DatabaseReference ref4,ref5;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash);
        drawerLayout = findViewById(R.id.user_dashboard_drawer);
        toolbar = findViewById(R.id.user_dash_toolbar);
        navigationView = findViewById(R.id.user_dash_nav);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drwer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        headerview = navigationView.getHeaderView(0);
        name = headerview.findViewById(R.id.user_dash_header_name);
        navigationView.setNavigationItemSelectedListener(this);
        email = headerview.findViewById(R.id.user_dashboard_header_email);
        imageView = headerview.findViewById(R.id.user_dash_header_pic);
        ref5=FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());
        getSupportFragmentManager().beginTransaction().replace(R.id.user_dash_frame, new home(), "home").commit();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                name.setText(user.name);
                email.setText(user.email);
                Picasso.get().load(user.imageuri).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        return true;

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            if (System.currentTimeMillis() - backpresstime < 2000) {
                super.onBackPressed();

            } else {
                backpresstime = System.currentTimeMillis();
                Toast.makeText(user_dash.this, "PRESS AGAIN TO EXIT ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.user_dashboard_home_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_dash_frame, new home(), "home").commit();
                drawerLayout.closeDrawer(GravityCompat.START);


                break;
            case R.id.user_dashboard_profile_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_dash_frame, new user_profile(), "home").commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.user_dash_pending_transaction:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_dash_frame, new user_pending_order(), "home").commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.user_dash_completed_transaction:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_dash_frame, new user_complete_order(), "home").commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.user__dash_delete_account:
                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder=new AlertDialog.Builder(user_dash.this);
                builder.setTitle("DELETE ACCOUNT").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                        editor=sharedPreferences.edit();
                        editor.putBoolean("remember",false);
                        editor.remove("username");
                        editor.remove("password");
                        editor.remove("type");
                        editor.commit();
                        ref4=FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("deleted");
                        ref4.setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                                ref5.child("Idmap").child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        auth.getCurrentUser().delete();
                                        AlertDialog.Builder builder1 =new AlertDialog.Builder(user_dash.this);
                                        builder1.setTitle("SUCCESSFULLY DELETED");
                                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i= new Intent(user_dash.this,Login.class);
                                                startActivity(i);
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                        builder1.show();
                                    }
                                });


                            }
                        });


                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;



            case R.id.user_dash_share_menu:

                Intent intent=new Intent();
                intent.setType("text/");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"DOWLOAD CATRIO APP");
                Intent chose=Intent.createChooser(intent,null);
                startActivity(chose);

                break;
            case R.id.user_dash_about_menu:

                AlertDialog.Builder builder1=new AlertDialog.Builder(user_dash.this);
                builder1.setTitle("CATRIO APP FOR FOOD DELIVERY")   ;
                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })      ;

                builder1.show();
                break;
            case R.id.user_dash_signout_menu:
                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder2=new AlertDialog.Builder(user_dash.this);
                builder2.setTitle("SIGNOUT").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                        editor=sharedPreferences.edit();
                        editor.putBoolean("remember",false);
                        editor.remove("username");
                        editor.remove("password");
                        editor.remove("type");
                        editor.commit();
                        Intent i= new Intent(user_dash.this,Login.class);
                        startActivity(i);
                        dialog.dismiss();
                        finish();

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder2.show();
                break;

        }

        return true;

    }
}
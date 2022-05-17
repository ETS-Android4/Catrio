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
import android.service.voice.AlwaysOnHotwordDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.catrio.admin.admin_organization_list;
import com.team.catrio.admin.admin_profile;
import com.team.catrio.admin.adminhome;
import com.team.catrio.org.org_home;

public class
admin_dash extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    View headerview;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    FrameLayout frameLayout;
    ImageView imageView;
    Admin admin;
    TextView name,email;
    DatabaseReference ref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth auth;
    long backpresstime;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        drawerLayout=findViewById(R.id.admin_dashboard_drawer);
        toolbar=findViewById(R.id.admin_dash_toolbar);
        navigationView=findViewById(R.id.admin_dash_nav);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drwer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        headerview =navigationView.getHeaderView(0);
        name=headerview.findViewById(R.id.admin_dash_header_name);
        email=headerview.findViewById(R.id.admin_dashboard_header_email);
        imageView=headerview.findViewById(R.id.admin_dash_header_pic);
        auth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("Admin").child(auth.getCurrentUser().getUid());
        getSupportFragmentManager().beginTransaction().replace(R.id.admin_dash_frame, new adminhome(), "admin_dash_nav").commit();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                admin=snapshot.getValue(Admin.class);
                name.setText(admin.name);
                email.setText(admin.email);
                Picasso.get().load(admin.imageuri).into(imageView);
            }

            @Override
            public void onCancelled( DatabaseError error) {

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
            if(System.currentTimeMillis()-backpresstime<2000) {
                super.onBackPressed();

            }else {
                backpresstime=System.currentTimeMillis();
                Toast.makeText(admin_dash.this,"PRESS AGAIN TO EXIT ",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        switch (item.getItemId()){

            case R.id.admin_dashboard_home_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_dash_frame, new adminhome(), "admin_dash_nav").commit();
                drawerLayout.closeDrawer(GravityCompat.START);

                break;
            case R.id.admin_dashboard_profile_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_dash_frame, new admin_profile(), "admin_dash_nav").commit();
                drawerLayout.closeDrawer(GravityCompat.START);

                break;
            case R.id.admin_dash_signout_menu:
                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder=new AlertDialog.Builder(admin_dash.this);
                builder.setTitle("SIGNOUT").setPositiveButton("YES", new DialogInterface.OnClickListener() {
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
                        Intent i= new Intent(admin_dash.this,Login.class);
                        startActivity(i);
                        dialog.dismiss();;
                        finish();

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.admin_dash_share_menu:

                  Intent intent=new Intent();
                                intent.setType("text/");
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT,"DOWLOAD CATRIO APP");
                                Intent chose=Intent.createChooser(intent,null);
                                startActivity(chose);
                                drawerLayout.closeDrawer(GravityCompat.START);






                break;
            case R.id.admin_dash_about_menu:
                         AlertDialog.Builder builder1=new AlertDialog.Builder(admin_dash.this);
                          builder1.setTitle("CATRIO APP FOR FOOD DELIVERY")   ;
                          builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                              }
                          })      ;

                          builder1.show();
                break;





        }





        return false;
    }
}
package com.team.catrio.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.catrio.Organization;
import com.team.catrio.R;

import java.util.ArrayList;


public class admin_organization_list extends Fragment  implements admin_organization_recycle.onItemCLickListner,admin_organization_recycle.onItemLongClickLisner{


    ArrayList<Organization> organizationArrayList;
    ArrayList<String> orgkeys;
    DatabaseReference reference;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    Organization org;
    admin_organization_recycle recycle;
    ValueEventListener valueEventListener;
    ProgressDialog progressDialog;
    public admin_organization_list() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View v= inflater.inflate(R.layout.fragment_admin_organization_list, container, false);

        progressDialog =new ProgressDialog(getContext());
        progressDialog.setMessage("LOADING...");
        progressDialog.setCancelable(false);
        auth=FirebaseAuth.getInstance();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        orgkeys=new ArrayList<>();
        organizationArrayList=new ArrayList<>();
        recycle=new admin_organization_recycle(organizationArrayList,this,this,getContext());
        recyclerView=v.findViewById(R.id.adminhome_recycleview);
        recyclerView.setAdapter(recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        reference= FirebaseDatabase.getInstance().getReference().child("Organization");
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                organizationArrayList.clear();
                orgkeys.clear();

                for(DataSnapshot ds:snapshot.getChildren()){
                    org=ds.getValue(Organization.class);
                    if(org.verified==true && org.deleted==false) {
                        orgkeys.add(ds.getKey());
                        organizationArrayList.add(org);
                    }
                }

                recycle.notifyDataSetChanged();


            }

            @Override
            public void onCancelled( DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        };





        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        reference.addValueEventListener(valueEventListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        reference.removeEventListener(valueEventListener);

    }

    @Override
    public void onClick(int position) {
        Intent i=new Intent(getContext(),adimi_org_details.class);
        i.putExtra("DELETE","YES");
        i.putExtra("ORGID",orgkeys.get(position));
        startActivity(i);

    }

    @Override
    public void onLonglick(int position) {

    }
}
package com.team.catrio.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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


public class adminhome extends Fragment implements  admin_organization_recycle.onItemCLickListner,admin_organization_recycle.onItemLongClickLisner{

    ArrayList<Organization> organizationArrayList,filteredorganizationArrayList;
    ArrayList<String> orgkeys;
    ArrayList<String> filteredorgkeys;
    DatabaseReference reference;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    Organization org;
    admin_organization_recycle recycle;
    ValueEventListener valueEventListener;
    ProgressDialog progressDialog;
    Spinner spinner;
    String searchtype="ALL";
    public adminhome() {

    }

  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


                View v=inflater.inflate(R.layout.fragment_adminhome, container, false);
                orgkeys=new ArrayList<>();

      ArrayAdapter<CharSequence> spiineradapter = ArrayAdapter.createFromResource(getContext(), R.array.verify, android.R.layout.simple_spinner_dropdown_item);
      spinner = v.findViewById(R.id.admin_spinner);
                organizationArrayList=new ArrayList<>();
                filteredorgkeys = new ArrayList<>();
                filteredorganizationArrayList=new ArrayList<>();
                recycle=new admin_organization_recycle(filteredorganizationArrayList,this,this,getContext());
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

                               orgkeys.add(ds.getKey());
                               organizationArrayList.add(org);

                        }

                        setrecycle();


                    }

                    @Override
                    public void onCancelled( DatabaseError error) {

                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                };




      spinner.setAdapter(spiineradapter);
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              searchtype = parent.getItemAtPosition(position).toString();
              setrecycle();

          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });


                return v;
  }

    @Override
    public void onStart() {
        super.onStart();

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
        i.putExtra("DELETE","NO");
        i.putExtra("ORGID",filteredorgkeys.get(position));
        startActivity(i);



    }

    @Override
    public void onLonglick(int position) {

    }

    public void setrecycle() {

        filteredorganizationArrayList.clear();
        filteredorgkeys.clear();
        for (int i = 0; i < organizationArrayList.size(); i++) {


            if (searchtype.equals("VERIFIED")) {
                if (organizationArrayList.get(i).verified==true) {

                    filteredorganizationArrayList.add(organizationArrayList.get(i));
                    filteredorgkeys.add(orgkeys.get(i));

                }
            }
            if (searchtype.equals("NOT VERIFIED")) {
                if (organizationArrayList.get(i).verified==false) {

                    filteredorganizationArrayList.add(organizationArrayList.get(i));
                    filteredorgkeys.add(orgkeys.get(i));

                }
            }

            if(searchtype.equals(("ALL"))) {

                filteredorganizationArrayList.add(organizationArrayList.get(i));
                filteredorgkeys.add(orgkeys.get(i));

            }



        }
        recycle.notifyDataSetChanged();

    }


}
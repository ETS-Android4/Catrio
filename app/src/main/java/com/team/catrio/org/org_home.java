package com.team.catrio.org;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.User;
import com.team.catrio.user_dash_fragment.organization_card_view_recycle;

import java.util.ArrayList;


public class org_home extends Fragment implements org_foodcardview1.onItemCLickListner,org_foodcardview1.onItemLongClickLisner{
    ArrayList<Food> foodArrayList;
    DatabaseReference ref1,ref2,ref3;
    Organization organization;
    org_foodcardview1 recycle;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    Food food;
    public org_home() {

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        foodArrayList=new ArrayList<>();
        View v=inflater.inflate(R.layout.fragment_org_home, container, false);
        recycle=new org_foodcardview1(foodArrayList,this,this,getContext());
        recyclerView=v.findViewById(R.id.org_foodlistview_recycle);
        auth=FirebaseAuth.getInstance();
        recyclerView.setAdapter(recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ref1= FirebaseDatabase.getInstance().getReference().child("Organization").child(auth.getCurrentUser().getUid());
        ref2= FirebaseDatabase.getInstance().getReference().child("Food");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {

                organization=snapshot.getValue(Organization.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                foodArrayList.clear();
                for(DataSnapshot d:snapshot.getChildren()){
                    food=d.getValue(Food.class);
                    if(food.Org_id.equals(auth.getCurrentUser().getUid()) && food.enabled==true){
                        foodArrayList.add(food);

                    }

                }
                recycle.notifyDataSetChanged();

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        };


        return v;
    }

    @Override
    public void onClick(int position) {



    }

    @Override
    public void onLonglick(int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("REMOVE ITEM");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ref3= FirebaseDatabase.getInstance().getReference().child("Food").child(foodArrayList.get(position).Food_id).child("enabled");
                ref3.setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        AlertDialog.Builder builder1=new AlertDialog.Builder(getContext());
                        builder.setTitle("SUCCESFULLY REMOVED").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder1.show();
                    }
                });


            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    public void onStart() {
        ref2.addValueEventListener(valueEventListener);
        super.onStart();
    }

    @Override
    public void onStop() {
        ref2.removeEventListener(valueEventListener);
        super.onStop();
    }
}
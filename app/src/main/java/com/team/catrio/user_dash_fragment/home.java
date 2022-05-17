package com.team.catrio.user_dash_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
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
import com.team.catrio.Order;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.User;
import com.team.catrio.org.Food;

import java.util.ArrayList;

import okio.Timeout;


public class home extends Fragment implements organization_card_view_recycle.onItemCLickListner,organization_card_view_recycle.onItemLongClickLisner {

    ArrayList<Food> FoodArraylist1,foodArrayList;
    ArrayList<Food> filteredfoodArrayList;
    ArrayList<Organization> organizationArrayList, filterdorganizationArrayList;
    DatabaseReference databaseReference, ref2, ref3;
    DatabaseReference databaseReference1;
    String q = "";
    User user;
    FirebaseAuth auth;
    Organization organization;
    organization_card_view_recycle recycle;
    ValueEventListener valueEventListener;
    ValueEventListener valueEventListener2;
    RecyclerView recyclerView;
    int i = 0;
    String searchtype="ALL";
    Spinner spinner;


    public home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayAdapter<CharSequence> spiineradapter = ArrayAdapter.createFromResource(getContext(), R.array.search_type, android.R.layout.simple_spinner_dropdown_item);
        spinner = v.findViewById(R.id.home_spinnerview);
        foodArrayList = new ArrayList<>();
        FoodArraylist1=new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        filterdorganizationArrayList = new ArrayList<>();
        organizationArrayList = new ArrayList<>();
        filteredfoodArrayList = new ArrayList<>();
        recycle = new organization_card_view_recycle(filteredfoodArrayList, filterdorganizationArrayList, this, this, getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food");
        ref2 = FirebaseDatabase.getInstance().getReference().child("Organization");
        recyclerView = v.findViewById(R.id.user_org_recycle_view);
        recyclerView.setAdapter(recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
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


        valueEventListener = new ValueEventListener() {
            @Override

             public  void onDataChange(DataSnapshot snapshot) {
                    FoodArraylist1.clear();
                    organizationArrayList.clear();
                    foodArrayList.clear();
                    boolean flag;
                    for (DataSnapshot d : snapshot.getChildren()) {
                        flag=true;
                        Food f = d.getValue(Food.class);
                        for(Food f1 :FoodArraylist1){
                            if(f.Food_id.equals(f1.Food_id)){
                                flag=false;
                                break;
                            }


                        }
                        if(flag) {
                            FoodArraylist1.add(f);
                        }

                    }

                        organizationArrayList.clear();
                        foodArrayList.clear();
                        i = 0;
                        for (Food f : FoodArraylist1) {
                            i++;
                            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Organization").child(f.Org_id);
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    boolean flag=true;
                                    organization = snapshot.getValue(Organization.class);
                                    if (organization.verified == true && organization.deleted == false) {

                                        for(Food food2:foodArrayList){
                                            if(f.Food_id.equals(food2.Food_id)){
                                                flag=false;
                                                break;
                                            }
                                        }

                                        if(flag) {
                                            organizationArrayList.add(organization);
                                            foodArrayList.add(f);
                                        }
                                    }

                                    if (i == FoodArraylist1.size()) {
                                        setrecycle();
                                    }


                                }


                                @Override
                                public void onCancelled(DatabaseError error) {


                                }
                            });


                        }


                }

                @Override
                public void onCancelled (DatabaseError error){

                }

        };



        valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                databaseReference.addListenerForSingleValueEvent(valueEventListener);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };

        return v;
    }

    @Override
    public void onStart() {


        ref3 = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                databaseReference.addValueEventListener(valueEventListener);
                ref2.addValueEventListener(valueEventListener2);



            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
        ref2.removeEventListener(valueEventListener2);

    }

    @Override
    public void onClick(int position) {
        Intent i = new Intent(getContext(), fooddetails.class);
        i.putExtra("FOODID", filteredfoodArrayList.get(position).Food_id);
        startActivity(i);

    }

    @Override
    public void onLonglick(int position) {

    }

    public void setrecycle() {
        Log.d("hello1", "setrecycle: "+" "+foodArrayList.size());
        filterdorganizationArrayList.clear();
        filteredfoodArrayList.clear();
        for (int i = 0; i < organizationArrayList.size(); i++) {


            if (searchtype.equals("CITY")) {
                if (organizationArrayList.get(i).city.equals(user.city)) {

                    filterdorganizationArrayList.add(organizationArrayList.get(i));
                    filteredfoodArrayList.add(foodArrayList.get(i));

                }
            }
            else if (searchtype.equals("STATE")) {
                if (organizationArrayList.get(i).state.equals(user.state)) {

                    filterdorganizationArrayList.add(organizationArrayList.get(i));
                    filteredfoodArrayList.add(foodArrayList.get(i));

                }
            }

            else if(searchtype.equals(("ALL"))) {

                filterdorganizationArrayList.add(organizationArrayList.get(i));
                filteredfoodArrayList.add(foodArrayList.get(i));

            }



        }
            recycle.notifyDataSetChanged();

        }
    }

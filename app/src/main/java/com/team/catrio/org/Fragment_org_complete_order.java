package com.team.catrio.org;

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
import com.team.catrio.Order;
import com.team.catrio.Organization;
import com.team.catrio.R;

import java.util.ArrayList;


public class Fragment_org_complete_order extends Fragment implements order_recyle.onItemLongClickLisner,order_recyle.onItemCLickListner{

    ArrayList<Food> foodArrayList;
    ArrayList<Order> orderArrayList;
    ArrayList<Order> recycle_orderArrayList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    String q = "";
    Organization organization;
    order_recyle recycle;
    Food food;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    int i=0;
    FirebaseAuth auth;

    public Fragment_org_complete_order() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_org_complete_order, container, false);
        foodArrayList = new ArrayList<>();
        orderArrayList=new ArrayList<>();
        Toast.makeText(getContext(),"COMPLETE ORDER",Toast.LENGTH_LONG).show();
        recycle_orderArrayList=new ArrayList<>();
        recycle=new order_recyle(recycle_orderArrayList,foodArrayList,this,this,getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");
        Toast.makeText(getContext(),databaseReference.getKey(),Toast.LENGTH_LONG).show();
        recyclerView = v.findViewById(R.id.complete_order_recycle);
        recyclerView.setAdapter(recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        auth=FirebaseAuth.getInstance();



        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderArrayList.clear();
                recycle_orderArrayList.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Order o= d.getValue(Order.class);
                    if(!o.status.equals("P")) {

                        orderArrayList.add(o);
                    }

                }


                foodArrayList.clear();
                i=0;
                for(Order o : orderArrayList){
                    i++;
                    databaseReference1=FirebaseDatabase.getInstance().getReference().child("Food").child(o.foodid);
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            food =snapshot.getValue(Food.class);
                            if(food.Org_id.equals(auth.getCurrentUser().getUid())) {
                                foodArrayList.add(food);
                                recycle_orderArrayList.add(o);
                            }else{

                            }
                            if(i==orderArrayList.size())
                                recycle.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled( DatabaseError error) {



                        }
                    });



                }
                Toast.makeText(getContext(),i+"",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        };

        return v;

    }

    @Override
    public void onStart() {
        databaseReference.addValueEventListener(valueEventListener);
        super.onStart();
    }

    @Override
    public void onStop() {
        databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }

    @Override
    public void onClick(int position) {

        Intent i=new Intent(getContext(),Org_orderdetails.class);
        i.putExtra("ORDERID",recycle_orderArrayList.get(position).orderid);
        i.putExtra("COMPLETE",1);
        startActivity(i);
    }

    @Override
    public void onLonglick(int position) {

    }

}
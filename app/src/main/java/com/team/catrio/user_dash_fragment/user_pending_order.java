package com.team.catrio.user_dash_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.team.catrio.org.Food;
import com.team.catrio.org.Org_orderdetails;
import com.team.catrio.org.order_recyle;

import java.util.ArrayList;


public class user_pending_order extends Fragment implements order_recycle_view.onItemCLickListner,order_recycle_view.onItemLongClickLisner{


    ArrayList<Food> foodArrayList;
    ArrayList<Order> orderArrayList,order_recyle_list;
    ArrayList<Organization> organizationArrayList;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    String q = "";
    Organization organization;
    order_recycle_view recycle;
    Food food;
    Organization org;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    int i=0;
    FirebaseAuth auth;



    public user_pending_order() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v=inflater.inflate(R.layout.fragment_user_pending_order, container, false);
        foodArrayList = new ArrayList<>();
        orderArrayList=new ArrayList<>();
        order_recyle_list=new ArrayList<>();
        organizationArrayList=new ArrayList<>();
        recycle=new order_recycle_view(orderArrayList,foodArrayList,organizationArrayList,this,this,getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");
        Toast.makeText(getContext(),databaseReference.getKey(),Toast.LENGTH_LONG).show();
        recyclerView = v.findViewById(R.id.user_pending_order_recycleview);
        recyclerView.setAdapter(recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        auth=FirebaseAuth.getInstance();
      valueEventListener=new ValueEventListener() {
          @Override
          public void onDataChange( DataSnapshot snapshot) {
              orderArrayList.clear();

              for (DataSnapshot d : snapshot.getChildren()) {
                  Order o= d.getValue(Order.class);
                  if(o.status.equals("P")&&(o.userid.equals(auth.getCurrentUser().getUid()))) {

                      orderArrayList.add(o);
                  }


              }

            organizationArrayList.clear();
              foodArrayList.clear();
              i=0;
              for(Order o : orderArrayList){
                  i++;
                  databaseReference1=FirebaseDatabase.getInstance().getReference().child("Food").child(o.foodid);
                  databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot snapshot) {

                          food =snapshot.getValue(Food.class);
                          foodArrayList.add(food);
                            databaseReference2=FirebaseDatabase.getInstance().getReference().child("Organization").child(food.Org_id);
                            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    org=snapshot.getValue(Organization.class);
                                    organizationArrayList.add(org);
                                    if(i==orderArrayList.size())
                                        recycle.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled( DatabaseError error) {
                                    Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });





                          }


                      @Override
                      public void onCancelled( DatabaseError error) {

                          Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

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
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onClick(int position) {
        Intent i=new Intent(getContext(), user_orgdetails.class);
        i.putExtra("ORDERID",orderArrayList.get(position).orderid);
        i.putExtra("COMPLETE",0);
        startActivity(i);


    }

    @Override
    public void onLonglick(int position) {

    }
}
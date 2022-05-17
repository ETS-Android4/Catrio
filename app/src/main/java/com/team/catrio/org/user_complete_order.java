package com.team.catrio.org;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.catrio.Order;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.Rating;
import com.team.catrio.user_dash_fragment.order_recycle_view;
import com.team.catrio.user_dash_fragment.user_orgdetails;

import java.util.ArrayList;

public class user_complete_order extends Fragment   implements order_recycle_view.onItemCLickListner,order_recycle_view.onItemLongClickLisner {

    ArrayList<Food> foodArrayList;
    ArrayList<Order> orderArrayList,order_recyle_list;
    ArrayList<Organization> organizationArrayList;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,ref,ref1,ref2,ref3,ref4;
    String q = "";
    Organization organization;
    order_recycle_view recycle;
    Food food;
    Rating rating,rating1;
    Organization org,org1;

    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    int i=0;
    View v;
    FirebaseAuth auth;

    public user_complete_order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v=inflater.inflate(R.layout.fragment_user_complete_order, container, false);
        foodArrayList = new ArrayList<>();
        orderArrayList=new ArrayList<>();
        order_recyle_list=new ArrayList<>();
        organizationArrayList=new ArrayList<>();
        recycle=new order_recycle_view(orderArrayList,foodArrayList,organizationArrayList,this,this,getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");
        Toast.makeText(getContext(),databaseReference.getKey(),Toast.LENGTH_LONG).show();
        recyclerView = v.findViewById(R.id.user_complete_order_recycle);
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
                    if((!o.status.equals("P"))&&(o.userid.equals(auth.getCurrentUser().getUid()))) {

                        orderArrayList.add(o);
                    }


                }


                foodArrayList.clear();
                organizationArrayList. clear();
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
        i.putExtra("COMPLETE",1);
        startActivity(i);



    }

    @Override
    public void onLonglick(int position) {
        if(!orderArrayList.get(position).status.equals("C")) {
            rating1 = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("RATE US");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    ViewGroup viewGroup = v.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.ratingsubmit, viewGroup, false);
                    RatingBar ratingBar = dialogView.findViewById(R.id.user_order_complete_ratingbar);
                    TextView submit = dialogView.findViewById(R.id.user_order_complete_submit);

                    TextView cancel = dialogView.findViewById(R.id.user_order_complete_cancel);


                    builder1.setView(dialogView);
                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                    Food food1 = foodArrayList.get(position);
                    Toast.makeText(getContext(), position + "", Toast.LENGTH_LONG).show();
                    ref = FirebaseDatabase.getInstance().getReference().child("Organization").child(food1.Org_id);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            org1 = snapshot.getValue(Organization.class);

                            ref1 = FirebaseDatabase.getInstance().getReference().child("rating");
                            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    int c = 0;
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        rating = ds.getValue(Rating.class);
                                        if (rating != null) {
                                            if (rating.ratingid.equals(auth.getCurrentUser().getUid() + food1.Org_id)) {
                                                Toast.makeText(getContext(), "FOUND", Toast.LENGTH_LONG).show();
                                                ratingBar.setRating(rating.rating.floatValue());

                                            }


                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }
                            });

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alertDialog.cancel();
                                    ref2 = FirebaseDatabase.getInstance().getReference().child("rating").child(auth.getCurrentUser().getUid() + food1.Org_id);
                                    String key = ref2.getKey();
                                    Rating rating2 = new Rating(key, auth.getCurrentUser().getUid(), food1.Org_id, Double.parseDouble(String.valueOf(ratingBar.getRating())));
                                    ref2.setValue(rating2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            ref3 = FirebaseDatabase.getInstance().getReference().child("rating");
                                            ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    Rating rating3;
                                                    int count = 0;
                                                    double sum = 0;
                                                    for (DataSnapshot d : snapshot.getChildren()) {
                                                        rating3 = d.getValue(Rating.class);
                                                        if (rating3.orgid.equals(food1.Org_id)) {
                                                            sum = sum + rating3.rating;
                                                            count = count + 1;
                                                        }

                                                    }
                                                    double avg = sum / ((double) count);
                                                    ref3 = FirebaseDatabase.getInstance().getReference().child("Organization").child(food1.Org_id).child("rating");
                                                    ref3.setValue(avg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "RATING UPDATED", Toast.LENGTH_LONG).show();

                                                        }
                                                    });


                                                }

                                                @Override
                                                public void onCancelled(DatabaseError error) {

                                                }
                                            });


                                        }
                                    });


                                }
                            });


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });


                }
            });
            builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }

    }
}
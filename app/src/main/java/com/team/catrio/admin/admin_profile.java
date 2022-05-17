package com.team.catrio.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.catrio.R;
import com.team.catrio.User;


public class admin_profile extends Fragment {


    TextView tvname,tvemail,tvadress,tvcontact,tvstate,tvcity;
    ImageView ivprofilepic;

    ValueEventListener valueEventListener;
    DatabaseReference ref1,ref2;
    User user;
    EditText ed;
    FirebaseAuth auth;

    public admin_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View v=inflater.inflate(R.layout.fragment_admin_profile, container, false);

        tvname=v.findViewById(R.id.admin_profile_name);
        tvemail=v.findViewById(R.id.admin_profile_email);
        tvadress=v.findViewById(R.id.admin_profile_adress);
        tvcontact=v.findViewById(R.id.admin_profile_contact);
        tvstate=v.findViewById(R.id.admin_profile_state);
        tvcity=v.findViewById(R.id.admin_profile_city);
        ivprofilepic=v.findViewById(R.id.admin_profile_pic);

        auth=FirebaseAuth.getInstance();
        ref1= FirebaseDatabase.getInstance().getReference().child("Admin").child(auth.getCurrentUser().getUid());
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user=snapshot.getValue(User.class);
                tvname.setText(user.name);
                tvadress.setText(user.address);
                tvemail.setText(user.email);
                tvcontact.setText(user.contactnum);
                tvstate.setText(user.state);
                tvcity.setText(user.city);
                Picasso.get().load(user.imageuri).into(ivprofilepic);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };



        tvname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed=new EditText(getContext());
                ed.setText(tvname.getText());
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setView(ed);
                builder.setTitle("UPDATE YOUR  NAME").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name1=ed.getText().toString();
                        ref2=FirebaseDatabase.getInstance().getReference().child("Admin").child(auth.getCurrentUser().getUid()).child("name");
                        ref2.setValue(name1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"SUCCESUULY UPDATED",Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
        tvadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed=new EditText(getContext());
                ed.setText(tvadress.getText());
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setView(ed);
                builder.setTitle("UPDATE YOUR  ADDRESS").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name1=ed.getText().toString();
                        ref2=FirebaseDatabase.getInstance().getReference().child("Admin").child(auth.getCurrentUser().getUid()).child("address");
                        ref2.setValue(name1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"SUCCESUULY UPDATED",Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
        tvcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed=new EditText(getContext());
                ed.setText(tvcontact.getText());
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setView(ed);
                builder.setTitle("UPDATE YOUR  CONTACT").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name1=ed.getText().toString();
                        ref2=FirebaseDatabase.getInstance().getReference().child("Admin").child(auth.getCurrentUser().getUid()).child("contactnum");
                        ref2.setValue(name1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"SUCCESUULY UPDATED",Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });

        tvcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed=new EditText(getContext());
                ed.setText(tvcity.getText());
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setView(ed);
                builder.setTitle("UPDATE YOUR  CITY").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name1=ed.getText().toString();
                        ref2=FirebaseDatabase.getInstance().getReference().child("Admin").child(auth.getCurrentUser().getUid()).child("city");
                        ref2.setValue(name1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"SUCCESUULY UPDATED",Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
        tvstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed=new EditText(getContext());
                ed.setText(tvstate.getText());
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setView(ed);
                builder.setTitle("UPDATE YOUR  STATE").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name1=ed.getText().toString();
                        ref2=FirebaseDatabase.getInstance().getReference().child("Admin").child(auth.getCurrentUser().getUid()).child("state");
                        ref2.setValue(name1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"SUCCESUULY UPDATED",Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        ref1.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        ref1.removeEventListener(valueEventListener);
    }
}
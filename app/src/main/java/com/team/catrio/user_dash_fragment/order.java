package com.team.catrio.user_dash_fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.catrio.Order;
import com.team.catrio.R;
import com.team.catrio.org.Food;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class order extends AppCompatActivity {

    EditText etdate,ettime,etaddress,etcount;
    TextView order_btn,tvamount,tvamountdisplay;
    Calendar cal;
    String foodid;
    DatabaseReference reference,reference2;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    Food food;
    int count;
    Boolean isnumber=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        etdate=findViewById(R.id.order_date);
        ettime=findViewById(R.id.order_time);
        etaddress=findViewById(R.id.order_addrs);
        order_btn=findViewById(R.id.order_order);
        tvamount=findViewById(R.id.count_price);
        etcount=findViewById(R.id.no_of_plate);
        tvamountdisplay=findViewById(R.id.txtamountdisplay);
        etcount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String no=etcount.getText().toString().trim();
                isnumber=no.length()>0?true:false;
                for(int i=0;i<no.length();i++){
                    if(!(Character.isDigit(no.charAt(i)))){
                        isnumber=false;
                        break;
                    }

                }
                if(isnumber) {
                    count = Integer.parseInt(no);
                    tvamount.setVisibility(View.VISIBLE);
                    tvamountdisplay.setVisibility(View.VISIBLE);
                    tvamount.setText((count * food.price)+"");
                    tvamountdisplay.setText("TOTAL AMOUNT : ");
                }else{
                    tvamount.setVisibility(View.GONE);
                    tvamountdisplay.setVisibility(View.GONE);


                }
            }
        });



         Calendar currentcal= Calendar.getInstance();
        Calendar ordercal=Calendar.getInstance();
        auth=FirebaseAuth.getInstance();
        foodid=getIntent().getStringExtra("FOODID");
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        reference= FirebaseDatabase.getInstance().getReference().child("Order");
        reference2=FirebaseDatabase.getInstance().getReference().child("Food").child(foodid);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                food=snapshot.getValue(Food.class);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled( DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(order.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });




        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day=currentcal.get(Calendar.DAY_OF_MONTH);
                int year=currentcal.get(Calendar.YEAR);
                int month=currentcal.get(Calendar.MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(order.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ordercal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        ordercal.set(Calendar.MONTH,month);
                        ordercal.set(Calendar.YEAR,year);
                        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMMM-yyy EEEE");

                        etdate.setText(dateFormat.format(ordercal.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        ettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int hour=currentcal.get(Calendar.HOUR_OF_DAY);
                int min=currentcal.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(order.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ordercal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        ordercal.set(Calendar.MINUTE,minute);
                        ordercal.set(Calendar.SECOND,00);
                        SimpleDateFormat timeformate=new SimpleDateFormat("hh : mm :ss a");
                        ettime.setText(timeformate.format(ordercal.getTime()));

                    }
                },hour,min,false);
                timePickerDialog.show();
            }
        });

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                int cout_plate = Integer.parseInt(etcount.getText().toString().trim());
                int amount = Integer.parseInt(tvamount.getText().toString().trim());


                String address = etaddress.getText().toString().trim();
                Calendar cday = Calendar.getInstance();
                long dif = ordercal.getTimeInMillis() - cday.getTimeInMillis();

                double dif_in_days = dif / (1000 * 60 * 60 * 24.0);
                if (dif_in_days > 1.999) {

                    if (Pattern.matches(".{6,}", address)) {

                        if (cout_plate >= 10) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(order.this);
                            builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog.show();
                                    reference = reference.push();
                                    String key = reference.getKey();
                                    Order order1 = new Order(key, address, count, amount, auth.getCurrentUser().getUid(), foodid, "P", ordercal.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
                                    reference.setValue(order1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            AlertDialog.Builder alerBuilder1 = new AlertDialog.Builder(order.this);
                                            alerBuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                            progressDialog.dismiss();
                                            alerBuilder1.setTitle("ORDER IS SUCCEFULY REGISTERD");
                                            alerBuilder1.show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(order.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }
                            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setTitle("CLICK CONFIRM TO REGISTER ORDER");
                            builder.show();


                        } else {
                            Toast.makeText(order.this, "NO OF PLATE IS TOO LESS", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(order.this, "ADRESS IS TO SHORT", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(order.this, "WE CANT DELIVER AT THAT DATE", Toast.LENGTH_LONG).show();


                }


            }catch(NumberFormatException e){
                Toast.makeText(order.this,"PLEASE ENTER PROPER NUMBER OF PLATE",Toast.LENGTH_LONG).show();
            }catch (Exception e){
                    Toast.makeText(order.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }   
        });



    }
}
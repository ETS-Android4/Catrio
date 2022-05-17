package com.team.catrio.user_dash_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.team.catrio.Order;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.org.Food;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class order_recycle_view extends RecyclerView.Adapter<order_recycle_view.Holder>{


    ArrayList<Order> orderArrayList;
    ArrayList<Food> foodArrayList;
    ArrayList<Organization> organizationArrayList;
    Context context;
    onItemCLickListner onitemCLickListner;
    onItemLongClickLisner onitemLongClickLisner;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public  order_recycle_view(ArrayList<Order> orderArrayList, ArrayList<Food> foodArrayList, ArrayList<Organization> organizationArrayList,order_recycle_view.onItemCLickListner itemCLickListner, order_recycle_view.onItemLongClickLisner onItemLongClickLisner1, Context context){
        this.orderArrayList=orderArrayList;
        this.foodArrayList=foodArrayList;
        this.organizationArrayList=organizationArrayList;
        onitemCLickListner=itemCLickListner;
        onitemLongClickLisner=onItemLongClickLisner1;
        this.context=context;

    }



    public order_recycle_view.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_recycle,parent,false);
        order_recycle_view.Holder holder=new order_recycle_view.Holder(view,onitemCLickListner,onitemLongClickLisner);
        return holder;
    }

    @Override
    public void onBindViewHolder( order_recycle_view.Holder holder, int position) {
        holder.foodlist.setText(foodArrayList.get(position).Food_list);
        Date d= new Date(orderArrayList.get(position).orderdate);
        holder.organisatonname.setText(organizationArrayList.get(position).name);
        holder.date.setText(simpleDateFormat.format(d)+"");
        holder.ordertype.setText(orderArrayList.get(position).status);
        if(orderArrayList.get(position).status.equals("P")){
            holder.ordertype.setBackground(context.getResources().getDrawable(R.drawable.status_pending));
        }
        else if(orderArrayList.get(position).status.equals("C")){
            holder.ordertype.setBackground(context.getResources().getDrawable(R.drawable.status_cancel));
        }
        else if(orderArrayList.get(position).status.equals("S")){
            holder.ordertype.setBackground(context.getResources().getDrawable(R.drawable.status));
        }

    }

    @Override
    public int getItemCount() {


        return organizationArrayList.size();


    }




    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView foodlist, date,organisatonname,ordertype;

        order_recycle_view.onItemCLickListner clickListner;
        order_recycle_view.onItemLongClickLisner longClickLisner;


        public Holder(View itemView, final order_recycle_view.onItemCLickListner clickListner, final order_recycle_view.onItemLongClickLisner longClickLisner) {
            super(itemView);
            ordertype=itemView.findViewById(R.id.user_order_type);
            foodlist = itemView.findViewById(R.id.user_order_foodlist);
            date = itemView.findViewById(R.id.user_order_time);
            organisatonname = itemView.findViewById(R.id.user_order_organisationname);
            this.clickListner = clickListner;
            this.longClickLisner = longClickLisner;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }
        @Override
        public void onClick(View v) {
            clickListner.onClick(getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View v) {
            longClickLisner.onLonglick(getAdapterPosition());
            return true;
        }
    }




   public  interface  onItemCLickListner{
        void onClick(int position);
    }
   public  interface  onItemLongClickLisner{
        void onLonglick(int position);
    }
}

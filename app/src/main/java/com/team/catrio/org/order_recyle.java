package com.team.catrio.org;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.catrio.Order;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.User;
import com.team.catrio.user_dash_fragment.organization_card_view_recycle;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class order_recyle extends RecyclerView.Adapter<order_recyle.Holder> {
    ArrayList<Order> orderArrayList;
    ArrayList<Food> foodArrayList;
    order_recyle.onItemCLickListner onItemCLickListner;
    order_recyle.onItemLongClickLisner onItemLongClickLisner;
    Context context;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public  order_recyle(ArrayList<Order> orderArrayList, ArrayList<Food> foodArrayList, order_recyle.onItemCLickListner itemCLickListner, order_recyle.onItemLongClickLisner onItemLongClickLisner1, Context context){
        this.orderArrayList=orderArrayList;
        this.foodArrayList=foodArrayList;
        onItemCLickListner=itemCLickListner;
        onItemLongClickLisner=onItemLongClickLisner1;
        this.context=context;

    }







    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView foodlist, date,address,ordertype;

        onItemCLickListner clickListner;
        onItemLongClickLisner longClickLisner;


        public Holder(View itemView, final onItemCLickListner clickListner, final onItemLongClickLisner longClickLisner) {
            super(itemView);
            ordertype=itemView.findViewById(R.id.org_order_recycle_ordertype);
            foodlist = itemView.findViewById(R.id.org_order_recycle_itemlist);
            date = itemView.findViewById(R.id.org_order_recycle_date);
            address = itemView.findViewById(R.id.org_order_recycle_addres);
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




    interface  onItemCLickListner{
        void onClick(int position);
    }
    interface  onItemLongClickLisner{
        void onLonglick(int position);
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.org_order_recycle,parent,false);
         order_recyle.Holder holder=new order_recyle.Holder(view,onItemCLickListner,onItemLongClickLisner);
        return holder;
    }

    @Override
    public void onBindViewHolder( order_recyle.Holder holder, int position) {
        holder.foodlist.setText(foodArrayList.get(position).Food_list);
        Date d= new Date(orderArrayList.get(position).orderdate);
        holder.address.setText(orderArrayList.get(position).address);
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


            return foodArrayList.size();


    }

}

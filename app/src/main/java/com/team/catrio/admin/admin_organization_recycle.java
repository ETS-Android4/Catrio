package com.team.catrio.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.catrio.Order;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.org.Food;
import com.team.catrio.org.order_recyle;

import java.util.ArrayList;


public class admin_organization_recycle extends RecyclerView.Adapter<admin_organization_recycle.Holder>  {

    ArrayList<Organization> organizationArrayList;

    admin_organization_recycle.onItemCLickListner onItemCLickListner;
    admin_organization_recycle.onItemLongClickLisner onItemLongClickLisner;
    Context context;



    public  admin_organization_recycle(ArrayList<Organization> orglist, admin_organization_recycle.onItemCLickListner itemCLickListner, admin_organization_recycle.onItemLongClickLisner onItemLongClickLisner1, Context context){
        organizationArrayList=orglist;
        onItemCLickListner=itemCLickListner;
        onItemLongClickLisner=onItemLongClickLisner1;
        this.context=context;

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView name, city;
        ImageView imageView;

        admin_organization_recycle.onItemCLickListner clickListner;
        admin_organization_recycle.onItemLongClickLisner longClickLisner;


        public Holder(View itemView, final admin_organization_recycle.onItemCLickListner clickListner, final admin_organization_recycle.onItemLongClickLisner longClickLisner) {
            super(itemView);
            name=itemView.findViewById(R.id.admin_organization_recycle_name);
            city = itemView.findViewById(R.id.admin_organization_recycle_city);
            imageView = itemView.findViewById(R.id.admin_organization_recycle_image);
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






    @NonNull

    @Override
    public admin_organization_recycle.Holder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adimin_org_recycle,parent,false);
        admin_organization_recycle.Holder holder=new admin_organization_recycle.Holder(view,onItemCLickListner,onItemLongClickLisner);
        return holder;

    }

    @Override
    public void onBindViewHolder( admin_organization_recycle.Holder holder, int position) {


        holder.city.setText(organizationArrayList.get(position).city);
        holder.name.setText(organizationArrayList.get(position).name);
        Picasso.get().load(organizationArrayList.get(position).imageuri).into(holder.imageView);

    }




    @Override
    public int getItemCount() {
        return organizationArrayList.size();
    }

    interface  onItemCLickListner{
        void onClick(int position);
    }
    interface  onItemLongClickLisner{
        void onLonglick(int position);
    }


}

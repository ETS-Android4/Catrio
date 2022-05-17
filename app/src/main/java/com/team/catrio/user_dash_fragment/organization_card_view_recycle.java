package com.team.catrio.user_dash_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.org.Food;

import java.util.ArrayList;

public class organization_card_view_recycle extends RecyclerView.Adapter<organization_card_view_recycle.Holder> {

    ArrayList<Food> foodArrayList;
    ArrayList<Organization> OrganizationList;
    onItemCLickListner onItemCLickListner;
    onItemLongClickLisner onItemLongClickLisner;
    Context context;
    public  organization_card_view_recycle(ArrayList<Food> foodArrayList,ArrayList<Organization> organizationList,onItemCLickListner itemCLickListner,onItemLongClickLisner onItemLongClickLisner1,Context context){
        this.foodArrayList=foodArrayList;
        OrganizationList=organizationList;
        onItemCLickListner=itemCLickListner;
        onItemLongClickLisner=onItemLongClickLisner1;
        this.context=context;
    }

    @Override
    public organization_card_view_recycle.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.organization_card_view,parent,false);
        Holder holder=new Holder(view,onItemCLickListner,onItemLongClickLisner);

        return holder;
    }

    @Override
    public void onBindViewHolder(organization_card_view_recycle.Holder holder, int position) {

        Picasso.get().load(foodArrayList.get(position).imageuri).into(holder.image);
        holder.Foodlist.setText(foodArrayList.get(position).Food_list);
        holder.city.setText(OrganizationList.get(position).city);
        holder.name.setText(OrganizationList.get(position).name);
        holder.ratingBar.setRating(Float.parseFloat(String.valueOf(OrganizationList.get(position).rating)));

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView image;
        TextView name, city, Foodlist;
        RatingBar ratingBar;
        onItemCLickListner clickListner;
        onItemLongClickLisner longClickLisner;

        public Holder(View itemView, final onItemCLickListner clickListner, final onItemLongClickLisner longClickLisner) {
            super(itemView);
            image = itemView.findViewById(R.id.organization_card_recycle_image);
            name = itemView.findViewById(R.id.organization_card_recycle_name);
            city = itemView.findViewById(R.id.organization_card_recycle_city);
            Foodlist = itemView.findViewById(R.id.organization_card_recycle_Food_list);
            ratingBar=itemView.findViewById(R.id.organization_card_recycle_rating);
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


    @Override
    public int getItemCount() {
        if(foodArrayList.size()<OrganizationList.size()){
            return foodArrayList.size();

        }else {
            return OrganizationList.size();
        }
    }

interface  onItemCLickListner{
    void onClick(int position);
}
interface  onItemLongClickLisner{
    void onLonglick(int position);
}


}

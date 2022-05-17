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
import com.team.catrio.Organization;
import com.team.catrio.R;
import com.team.catrio.user_dash_fragment.organization_card_view_recycle;


import java.util.ArrayList;

public class org_foodcardview1 extends RecyclerView.Adapter<org_foodcardview1.Holder>{

    ArrayList<Food> foodArrayList;
  onItemCLickListner onItemCLickListner;
    onItemLongClickLisner onItemLongClickLisner;
    Context context;
    public  org_foodcardview1(ArrayList<Food> foodArrayList, onItemCLickListner itemCLickListner, onItemLongClickLisner onItemLongClickLisner1, Context context){
        this.foodArrayList=foodArrayList;
        onItemCLickListner=itemCLickListner;
        onItemLongClickLisner=onItemLongClickLisner1;
        this.context=context;
    }

    @NonNull

    @Override
    public org_foodcardview1.Holder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.org_foodlistcardview,parent,false);
        Holder holder=new Holder(view,onItemCLickListner,onItemLongClickLisner);
        return holder;
    }

    @Override
    public void onBindViewHolder( org_foodcardview1.Holder holder, int position) {


        Picasso.get().load(foodArrayList.get(position).imageuri).into(holder.image);
        holder.Foodlist.setText(foodArrayList.get(position).Food_list);
        holder.type.setText(foodArrayList.get(position).food_category);
        holder.price.setText(foodArrayList.get(position).price+"");




    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView image;
        TextView type, price, Foodlist;
        RatingBar ratingBar;
       onItemCLickListner clickListner;
       onItemLongClickLisner longClickLisner;

        public Holder(View itemView, final onItemCLickListner clickListner, final onItemLongClickLisner longClickLisner) {
            super(itemView);
            image = itemView.findViewById(R.id.org_foodlistcardview_image);
            Foodlist = itemView.findViewById(R.id.org_foodlistcardview_Food_items);
            price = itemView.findViewById(R.id.org_foodlistcardview_food_cost);
            type = itemView.findViewById(R.id.org_foodlistcardview_food_type);
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
        return foodArrayList.size();
    }


    interface  onItemCLickListner{
        void onClick(int position);
    }
    interface  onItemLongClickLisner{
        void onLonglick(int position);
    }

}

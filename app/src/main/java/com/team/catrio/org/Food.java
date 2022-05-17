package com.team.catrio.org;

public class Food {
    public String Food_id;
    public String Org_id;
    public String Food_list;
    public String imageuri;
    public String food_category;
    public int price=20;
    public boolean enabled;

    public Food() {
    }

    public Food(String org_id, String food_list, String imageuri, String food_category, String food_id, int price) {
        Org_id = org_id;
        Food_list = food_list;
        this.imageuri = imageuri;
        this.food_category = food_category;
        Food_id = food_id;
        this.price = price;
        enabled=true;
    }
}

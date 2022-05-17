package com.team.catrio;

public class Organization {

    public String name;
    public String contactnum;
    public String imageuri;
    public String address;
    public String city;
    public String state;
    public String email;

    public double rating;
    public Boolean verified;
    public Boolean deleted;


    public Organization(String name, String contactnum, String imageuri, String address, String city, String state, String email,double rating) {
        this.name = name;
        this.contactnum = contactnum;
        this.imageuri = imageuri;
        this.address = address;
        this.city = city;
        this.state = state;
        this.email = email;
        this.rating=rating;
        verified=false;
        this.deleted=false;
    }

    public Organization() {
    }
}

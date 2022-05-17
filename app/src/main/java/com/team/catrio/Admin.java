package com.team.catrio;

public class Admin {

    public String name;
    public String contactnum;
    public String imageuri;
    public String address;
    public String city;
    public String state;
    public String email;

    public Admin(String name, String contactnum, String imageuri, String address, String city, String state, String email) {
        this.name = name;
        this.contactnum = contactnum;
        this.imageuri = imageuri;
        this.address = address;
        this.city = city;
        this.state = state;
        this.email = email;
    }

    public Admin() {
    }
}

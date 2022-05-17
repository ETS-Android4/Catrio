package com.team.catrio;

public class Order {

    public String orderid;
    public  String userid;
    public  String foodid;
    public  String status;
    public long timestamp;
    public long orderdate;
    public String address;
    public int count;
    public  int amoutn;

    public Order(String orderid,String address,int count,int amount,String userid, String foodid, String status,long orderdate,long timestamp) {
        this.orderid=orderid;
        this.userid = userid;
        this.foodid = foodid;
        this.count=count;
        this.amoutn=amount;
        this.address=address;
        this.status = status;
        this.orderdate=orderdate;
        this.timestamp=timestamp;

    }

    public Order() {
    }
}

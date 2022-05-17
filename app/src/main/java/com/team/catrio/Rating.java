package com.team.catrio;

public class Rating {
    public String userid;
    public String ratingid;
    public String orgid;
    public Double rating;

    public Rating(String ratingid,String userid, String orgid, Double rating) {
        this.userid = userid;
        this.orgid = orgid;
        this.rating = rating;
        this.ratingid=ratingid;
    }

    public Rating() {
    }
}

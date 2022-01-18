package com.example.firestoreapp;

public class promodel {
    private String hname,req,id;
    public promodel(String hname,String req,String id)
    {
        this.hname=hname;
        this.req=req;
        this.id=id;
    }
    public String gethname() {
        return hname;
    }

    public void sethname(String hname) {
        this.hname = hname;
    }

    public String getreq() {
        return req;
    }

    public void setreq(String req) {
        this.req = req;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}

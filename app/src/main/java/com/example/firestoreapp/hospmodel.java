package com.example.firestoreapp;

public class hospmodel {
    private String pname,avail,id;
    public hospmodel(String pname,String avail,String id)
    {
        this.pname=pname;
        this.avail=avail;
        this.id=id;
    }
    public String getpname() {
        return pname;
    }

    public void setpname(String pname) {
        this.pname = pname;
    }

    public String getavail() {
        return avail;
    }

    public void setavail(String avail) {
        this.avail = avail;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}

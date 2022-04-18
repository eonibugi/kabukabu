package com.example.kabukabu;

public class SingleItem {
    String name;
    String mobile;

    //생성
    public SingleItem(String name, String mobile){
        this.name = name;
        this.mobile = mobile;

    }
    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile(){
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString(){
        return "SingleItem{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}

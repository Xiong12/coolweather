package com.example.coolweather.db;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class HourlyWeather {
    public HourlyWeather(String data,String txt,String tmp){
        this.data=data;
        this.txt=txt;
        this.tmp=tmp;
    }
    public HourlyWeather(){
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    private String data;
    private String tmp;
    private String txt;

}

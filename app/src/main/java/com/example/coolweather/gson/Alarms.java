package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class Alarms {
    @SerializedName("level")
    public String level;

    @SerializedName("type")
    public String type;

    @SerializedName("title")
    public String title;

    @SerializedName("txt")
    public String txt;
}

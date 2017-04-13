package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class HourlyForecast {
    public String date;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;


    public class More {

        @SerializedName("txt")
        public String info;

    }

}

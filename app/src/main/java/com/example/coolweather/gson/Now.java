package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("hum")
    public String hum;

    @SerializedName("pcpn")
    public String pcpn;

    @SerializedName("pres")
    public String pres;

    @SerializedName("vis")
    public String vis;

    @SerializedName("cond")
    public More more;

    @SerializedName("wind")
    public Wind wind;

    public class More {

        @SerializedName("txt")
        public String info;

    }

    public class Wind {

        @SerializedName("deg")
        public String deg;

        @SerializedName("dir")
        public String dir;

        @SerializedName("sc")
        public String sc;

        @SerializedName("spd")
        public String spd;
    }
}

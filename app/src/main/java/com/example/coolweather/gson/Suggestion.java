package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("drsg")
    public Drsg drsg;

    @SerializedName("flu")
    public Flu flu;

    @SerializedName("trav")
    public Trav trav;

    @SerializedName("uv")
    public Uv uv;

    public Sport sport;


    //舒适度指数
    public class Comfort {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;

    }

    //洗车指数
    public class CarWash {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;

    }

    //运动指数
    public class Sport {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;

    }

    //穿衣指数
    public class Drsg {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;

    }

    //感冒指数
    public class Flu {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;

    }

    //旅游指数
    public class Trav {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;

    }

    //紫外线指数
    public class Uv {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;

    }
}

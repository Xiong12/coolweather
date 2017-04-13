package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.adapter.HourlyAdapter;
import com.example.coolweather.db.HourlyWeather;
import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.HourlyForecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView updateTimeText;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;
    private RecyclerView recycleView;

    private TextView aqiText;

    private TextView qltyText;

    private TextView pm25Text;

    private TextView pm10Text;

    private TextView so2Text;

    private TextView no2Text;

    private TextView coText;

    private TextView o3Text;

    private TextView comfortText;

    private TextView drsgText;

    private TextView fluText;

    private TextView travText;

    private TextView uvText;

    private TextView carWashText;

    private TextView sportText;

    private TextView dirText;

    private TextView scText;

    private TextView spdText;

    private TextView titleText;

    private TextView leveltupeText;

    private TextView txtText;

    private ArcProgress arcProgress1;
    private ImageView bingPicImg;
    private List<HourlyWeather> hourlyList = new ArrayList<HourlyWeather>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            getWindow().setStatusBarColor( Color.TRANSPARENT );
        }
        setContentView( R.layout.activity_weather );
        // 初始化各控件
        recycleView = (RecyclerView) findViewById( R.id.recycle_list );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
        recycleView.setLayoutManager( layoutManager );
        HourlyAdapter adapter = new HourlyAdapter( hourlyList );
        recycleView.setAdapter( adapter );

        bingPicImg = (ImageView) findViewById( R.id.bing_pic_img );
        weatherLayout = (ScrollView) findViewById( R.id.weather_layout );
        titleCity = (TextView) findViewById( R.id.title_city );
        titleUpdateTime = (TextView) findViewById( R.id.title_update_time );
        degreeText = (TextView) findViewById( R.id.degree_text );
        weatherInfoText = (TextView) findViewById( R.id.weather_info_text );
        forecastLayout = (LinearLayout) findViewById( R.id.forecast_layout );
        updateTimeText = (TextView) findViewById( R.id.updateTime_text );
        aqiText = (TextView) findViewById( R.id.aqi_text );
        arcProgress1 = (ArcProgress) findViewById( R.id.arc_progress );
        qltyText = (TextView) findViewById( R.id.qlty_text );
        pm25Text = (TextView) findViewById( R.id.pm25_text );
        pm10Text = (TextView) findViewById( R.id.pm10_text );
        so2Text = (TextView) findViewById( R.id.SO2_text );
        coText = (TextView) findViewById( R.id.CO_text );
        o3Text = (TextView) findViewById( R.id.O3_text );
        no2Text = (TextView) findViewById( R.id.NO2_text );
        comfortText = (TextView) findViewById( R.id.comfort_text );
        carWashText = (TextView) findViewById( R.id.car_wash_text );
        travText = (TextView) findViewById( R.id.trav_text );
        drsgText = (TextView) findViewById( R.id.drsg_text );
        fluText = (TextView) findViewById( R.id.flu_text );
        uvText = (TextView) findViewById( R.id.uv_text );
        sportText = (TextView) findViewById( R.id.sport_text );
        dirText = (TextView) findViewById( R.id.dir_text );
        scText = (TextView) findViewById( R.id.sc_text );
        spdText = (TextView) findViewById( R.id.spd_text );
        titleText = (TextView) findViewById( R.id.title_text );
        leveltupeText = (TextView) findViewById( R.id.level_type_text );
        txtText = (TextView) findViewById( R.id.txt_text );
        swipeRefresh = (SwipeRefreshLayout) findViewById( R.id.swipe_refresh );
        swipeRefresh.setColorSchemeResources( R.color.colorPrimary );
        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
        navButton = (Button) findViewById( R.id.nav_button );
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this );
        String weatherString = prefs.getString( "weather", null );
        final String weatherId;
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse( weatherString );
            weatherId = weather.basic.weatherId;
            showWeatherInfo( weather );
        } else {
            // 无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra( "weather_id" );
            weatherLayout.setVisibility( View.INVISIBLE );
            requestWeather( weatherId );
        }
        swipeRefresh.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather( weatherId );
            }
        } );
        navButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer( GravityCompat.START );
            }
        } );
        String bingPic = prefs.getString( "bing_pic", null );
        if (bingPic != null) {
            Glide.with( this ).load( bingPic ).into( bingPicImg );
        } else {
            loadBingPic();
        }
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=27de6f29df21463ea66b7132acbf0cbd";
        HttpUtil.sendOkHttpRequest( weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse( responseText );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals( weather.status )) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences( WeatherActivity.this ).edit();
                            editor.putString( "weather", responseText );
                            editor.apply();
                            showWeatherInfo( weather );
                        } else {
                            Toast.makeText( WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT ).show();
                        }
                        swipeRefresh.setRefreshing( false );
                    }
                } );
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT ).show();
                        swipeRefresh.setRefreshing( false );
                    }
                } );
            }
        } );
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest( requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences( WeatherActivity.this ).edit();
                editor.putString( "bing_pic", bingPic );
                editor.apply();
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Glide.with( WeatherActivity.this ).load( bingPic ).into( bingPicImg );
                    }
                } );
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        } );
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split( " " )[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;

        titleCity.setText( cityName );
        titleUpdateTime.setText( updateTime );
        updateTimeText.setText( "获取时间:" + updateTime );
        dirText.setText( "风  向  :    " + weather.now.wind.dir );
        spdText.setText( "风  速  :    " + weather.now.wind.spd );
        scText.setText( "风力等级:    " + weather.now.wind.sc );
        degreeText.setText( degree );
        weatherInfoText.setText( weatherInfo );
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from( this ).inflate( R.layout.forecast_item, forecastLayout, false );
            TextView dateText = (TextView) view.findViewById( R.id.date_text );
            TextView infoText = (TextView) view.findViewById( R.id.info_text );
            TextView maxText = (TextView) view.findViewById( R.id.max_text );
            TextView minText = (TextView) view.findViewById( R.id.min_text );
            dateText.setText( forecast.date );
            infoText.setText( forecast.more.info );
            maxText.setText( forecast.temperature.max );
            minText.setText( forecast.temperature.min );
            forecastLayout.addView( view );
        }
        if (weather.hourlyForecastList != null) {
            for (HourlyForecast hourlyforecast : weather.hourlyForecastList) {
                HourlyWeather hourlyWeather = new HourlyWeather( hourlyforecast.date.split( " " )[1], hourlyforecast.more.info, hourlyforecast.temperature );
                hourlyList.add( hourlyWeather );

            }
        }
        if (weather.aqi != null) {
            arcProgress1.setProgress( Integer.valueOf( weather.aqi.city.aqi ) );
            aqiText.setText( "空气质量:" + weather.aqi.city.qlty );
            qltyText.setText( "空气质量:" + weather.aqi.city.qlty );
            pm25Text.setText( weather.aqi.city.pm25 );
            pm10Text.setText( weather.aqi.city.pm10 );
            so2Text.setText( weather.aqi.city.so2 );
            no2Text.setText( weather.aqi.city.no2 );
            coText.setText( weather.aqi.city.co );
            o3Text.setText( weather.aqi.city.o3 );
        }
        if (weather.alarms != null) {
            titleText.setText( weather.alarms.title + weather.alarms );
            leveltupeText.setText( "预警等级: " + weather.alarms.level + "。   " + "预警天气类型: " + weather.alarms.type + "。" );
            txtText.setText( "详情" + weather.alarms.txt );
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.brf + "。" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.brf + "。" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.brf + "。" + weather.suggestion.sport.info;
        String drsg = "穿衣指数：" + weather.suggestion.drsg.brf + "。" + weather.suggestion.drsg.info;
        String flu = "感冒指数：" + weather.suggestion.flu.brf + "。" + weather.suggestion.flu.info;
        String trav = "旅游指数：" + weather.suggestion.trav.brf + "。" + weather.suggestion.trav.info;
        String uv = "紫外线指数：" + weather.suggestion.uv.brf + "。" + weather.suggestion.uv.info;
        comfortText.setText( comfort );
        carWashText.setText( carWash );
        sportText.setText( sport );
        drsgText.setText( drsg );
        fluText.setText( flu );
        travText.setText( trav );
        uvText.setText( uv );
        weatherLayout.setVisibility( View.VISIBLE );
        Intent intent = new Intent( this, AutoUpdateService.class );
        startService( intent );
    }

}

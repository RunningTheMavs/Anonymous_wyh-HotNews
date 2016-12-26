package com.example.administrator.hotnews.home.settings.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.constants.Constants;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.settings.adapter.CityAdapter;
import com.example.administrator.hotnews.home.settings.adapter.ProvinceAdapter;
import com.example.administrator.hotnews.home.settings.adapter.SearchCityAdapter;
import com.example.administrator.hotnews.home.settings.bean.City;
import com.example.administrator.hotnews.home.settings.bean.Province;
import com.example.administrator.hotnews.home.settings.listener.MyLocationListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择城市页面
 * Created by Anonymous_W on 2016/11/18.
 */
public class ChooseCityActivity extends BaseActivity implements View.OnClickListener {
    private ListView listViewProvince, listViewCity;
    private ListView listViewSearch;
    private Button bt_choose_city_location;
    private SearchView sv_choose_city;
    private LinearLayout linearLayout_choose;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<City> chooseCityList;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private SearchCityAdapter searchCityAdapter;
    public LocationClient mLocationClient;
    public MyLocationListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_choose_position);
        listViewProvince = (ListView) findViewById(R.id.listView_province);
        listViewCity = (ListView) findViewById(R.id.listView_city);
        listViewSearch = (ListView) findViewById(R.id.lv_choose_city);
        bt_choose_city_location = (Button) findViewById(R.id.bt_choose_city_location);
        sv_choose_city = (SearchView) findViewById(R.id.sv_choose_city);
        linearLayout_choose = (LinearLayout) findViewById(R.id.linearLayout_choose);
    }

    @Override
    public void initData() {
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        chooseCityList = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(this);
        cityAdapter = new CityAdapter(this);
        searchCityAdapter = new SearchCityAdapter(this);
        InputStream is = null;
        try {
            is = this.getAssets().open("provinces.xml");
            new ParseProvinceData().execute(is);
            InputStream is2 = this.getAssets().open("cities.xml");
            new ParseCityData().execute(is2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listViewProvince.setAdapter(provinceAdapter);
        listViewCity.setAdapter(cityAdapter);
        listViewSearch.setAdapter(searchCityAdapter);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.getData().getString("cityName").equals("")
                        || msg.getData().getString("cityName") == null) {
                    bt_choose_city_location.setText("定位失败");
                } else {
                    bt_choose_city_location.setText(msg.getData().getString("cityName"));
                }
            }
        };
        //初始化百度地图LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mListener = new MyLocationListener(mLocationClient, handler);
        getLocation();

    }

    private void getLocation() {
        mLocationClient.registerLocationListener(mListener);
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void initListener() {
        listViewProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                chooseCityList.clear();
                String province_id = provinceList.get(position).getProvince_id();
                for (int i = 0; i < cityList.size(); i++) {
                    if (cityList.get(i).getProvince_id().equals(province_id)) {
                        chooseCityList.add(cityList.get(i));
                    }
                }
                cityAdapter.initData(chooseCityList);
            }
        });
        listViewCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City city = null;
                if (i <= chooseCityList.size()) {
                    city = chooseCityList.get(i);
                } else {
                    city = cityList.get(i);
                }
                String name = city.getName();
                String city_num = city.getCity_num();
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", name);
                bundle.putString("CITY_NUM", city_num);
                resultIntent.putExtra("BUNDLE_DATA", bundle);
                ChooseCityActivity.this.setResult(Constants.WEATHER_RESULT_CODE,
                        resultIntent);
                finish();
                playAnimation();
            }
        });
        bt_choose_city_location.setOnClickListener(this);
        sv_choose_city.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewSearch.setVisibility(View.VISIBLE);
                linearLayout_choose.setVisibility(View.GONE);
                final List<City> newList = getAutoCompleteStringList(newText);
                searchCityAdapter.refresh(newList);
                listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long
                            id) {
                        sv_choose_city.setQuery(newList.get(position).getName(), true);
                        String city_num = newList.get(position).getCity_num();
                        String name = newList.get(position).getName();
                        back(city_num, name);
                    }
                });
                return true;
            }
        });
    }

    private List<City> getAutoCompleteStringList(String newText) {
        List<City> tempList = new ArrayList<>();
        for (City x : cityList) {
            if (x.getName().contains(newText)) {
                tempList.add(x);
            }
        }
        if (newText.length() <= 0 || newText == null || newText == "") {
            listViewSearch.setVisibility(View.GONE);
            linearLayout_choose.setVisibility(View.VISIBLE);
        }
        return tempList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_choose_city_location:
                String cityName = bt_choose_city_location.getText().toString();
                if (cityName != null) {
                    if (cityName.equals("定位失败")) {
                        finish();
                        playAnimation();
                    } else {
                        int cityInt = cityName.indexOf("市");
                        String substring = cityName.substring(0, cityInt);
                        for (int i = 0; i < cityList.size(); i++) {
                            if (cityList.get(i).getName().contains(substring)) {
                                City city = cityList.get(i);
                                String name = city.getName();
                                String city_num = city.getCity_num();
                                back(city_num, name);
                            }
                        }
                    }
                }
                break;
        }
    }

    private void back(String city_num, String name) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        bundle.putString("CITY_NUM", city_num);
        resultIntent.putExtra("BUNDLE_DATA", bundle);
        ChooseCityActivity.this.setResult(Constants.WEATHER_RESULT_CODE,
                resultIntent);
        finish();
        playAnimation();
    }

    public class ParseProvinceData extends AsyncTask<InputStream, Void, List<Province>> {
        @Override
        protected List<Province> doInBackground(InputStream... is) {
            XmlPullParserFactory factory = null;
            Province province = null;
            try {
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(is[0], "utf-8");

                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String nameTag = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (nameTag.equals("RECORD")) {
                                province = new Province();
                            } else if (nameTag.equals("name")) {
                                String name = xmlPullParser.nextText();
                                province.setName(name);
                            } else if (nameTag.equals("province_id")) {
                                String province_id = xmlPullParser.nextText();
                                province.setProvince_id(province_id);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (nameTag.equals("RECORD")) {
                                provinceList.add(province);
                                province = null;
                            }
                            break;
                        default:
                            break;

                    }

                    eventType = xmlPullParser.next();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return provinceList;
        }

        @Override
        protected void onPostExecute(List<Province> provinces) {
            if (provinces != null) {
                provinceAdapter.initData(provinces);
            }
        }
    }

    public class ParseCityData extends AsyncTask<InputStream, Void, List<City>> {
        @Override
        protected List<City> doInBackground(InputStream... is) {
            XmlPullParserFactory factory = null;
            City city = null;
            try {
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(is[0], "utf-8");

                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String nameTag = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (nameTag.equals("RECORD")) {
                                city = new City();
                            } else if (nameTag.equals("name")) {
                                String name = xmlPullParser.nextText();
                                city.setName(name);
                            } else if (nameTag.equals("province_id")) {
                                String province_id = xmlPullParser.nextText();
                                city.setProvince_id(province_id);
                            } else if (nameTag.equals("city_num")) {
                                String city_num = xmlPullParser.nextText();
                                city.setCity_num(city_num);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (nameTag.equals("RECORD")) {
                                cityList.add(city);
                                city = null;
                            }
                            break;
                        default:
                            break;

                    }

                    eventType = xmlPullParser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cityList;
        }

        @Override
        protected void onPostExecute(List<City> cities) {
            if (cities != null) {
                cityAdapter.initData(cities);
            }
        }
    }

    private void playAnimation() {
        this.overridePendingTransition(R.anim.anim_alpha_in,
                R.anim.anim_activity_righttop_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            playAnimation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

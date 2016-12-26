package com.example.administrator.hotnews.home.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.constants.Constants;
import com.example.administrator.hotnews.common.utils.JsonUtils;
import com.example.administrator.hotnews.common.widget.RoundImageView;
import com.example.administrator.hotnews.home.settings.Class.ClearCeash;
import com.example.administrator.hotnews.home.settings.Class.JudgeNetState;
import com.example.administrator.hotnews.home.settings.Class.MyDialog;
import com.example.administrator.hotnews.home.settings.Class.QuitApp;
import com.example.administrator.hotnews.home.settings.activity.ChangeYourInfo;
import com.example.administrator.hotnews.home.settings.activity.ChooseCityActivity;
import com.example.administrator.hotnews.home.settings.activity.CollectionActivity;
import com.example.administrator.hotnews.home.settings.activity.DayOrNightActivity;
import com.example.administrator.hotnews.home.settings.activity.FindPassword;
import com.example.administrator.hotnews.home.settings.activity.FontSizeActivity;
import com.example.administrator.hotnews.home.settings.activity.LoginActivity;
import com.example.administrator.hotnews.home.settings.bean.Weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * 设置Fragment页面
 * Created by Anonymous_W on 2016/11/10.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout setting_update_information;
    private RelativeLayout setting_find_password;
    private RelativeLayout setting_font;
    private RelativeLayout setting_pic_mode;
    private RelativeLayout setting_notification;
    private RelativeLayout setting_clear_cache;
    private RelativeLayout setting_exit;
    private RelativeLayout setting_collection;
    private RoundImageView imageView;
    private TextView account;
    private ImageView iv_weather_icon;
    private Button bt_setting_position;
    private TextView clearCache;
    private TextView tv_setting_city, tv_setting_weather;

    private static final int CODE_GALLERY_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_RESULT_REQUEST = 3;

    private static String path = "/sdcard/myHead";
    private Bitmap head;
    private SharedPreferences preferences;
    private SharedPreferences cityPreferences;
    private String city_name;
    private String city_num;
    ScaleAnimation scaleAnimation;
    SwitchCompat push;
    SwitchCompat noPic;
    SwitchCompat light;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        imageView = (RoundImageView) view.findViewById(R.id.setting_header);
        iv_weather_icon = (ImageView) view.findViewById(R.id.iv_setting_weather_icon);
//        setting_update_information = (RelativeLayout) view.findViewById(R.id
//                .setting_update_information);
        setting_find_password = (RelativeLayout) view.findViewById(R.id.setting_find_password);
        setting_font = (RelativeLayout) view.findViewById(R.id.setting_font);
        setting_pic_mode = (RelativeLayout) view.findViewById(R.id.setting_pic_mode);
        setting_notification = (RelativeLayout) view.findViewById(R.id.setting_notification);
        setting_clear_cache = (RelativeLayout) view.findViewById(R.id.setting_clear_cache);
        setting_exit = (RelativeLayout) view.findViewById(R.id.setting_exit);
        setting_collection = (RelativeLayout) view.findViewById(R.id.setting_collection);
        bt_setting_position = (Button) view.findViewById(R.id.bt_setting_position);
        tv_setting_city = (TextView) view.findViewById(R.id.tv_setting_city);
        tv_setting_weather = (TextView) view.findViewById(R.id.tv_setting_weather);
        account = (TextView) view.findViewById(R.id.account);
        push = (SwitchCompat) view.findViewById(R.id.sendMessage);
        noPic = (SwitchCompat) view.findViewById(R.id.noPic);
        light = (SwitchCompat) view.findViewById(R.id.light);


        clearCache = (TextView) view.findViewById(R.id.clear_cache);
        String cache = ClearCeash.GetTotalCache(getActivity());
        clearCache.setText(cache);
        preferences = getActivity().getSharedPreferences("USER_INFO", Context
                .MODE_PRIVATE);
        String count = preferences.getString("Account", "请登录");
        account.setText(count);
        account.setOnClickListener(this);

        //获取存储的城市信息 并刷新天气
        cityPreferences = getActivity().getSharedPreferences("CITY_INFO", Context
                .MODE_PRIVATE);
        if (cityPreferences != null) {
            city_name = cityPreferences.getString("name", "北京");
            city_num = cityPreferences.getString("city_num", "101010100");
        }
        //获取天气数据
        initWeather(city_num);
        //初始化动画
        scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(400);
        scaleAnimation.setInterpolator(new LinearInterpolator());

        tv_setting_city.setText(city_name);
        bt_setting_position.setOnClickListener(this);
        imageView.setOnClickListener(this);
//        setting_update_information.setOnClickListener(this);
        setting_find_password.setOnClickListener(this);
        setting_font.setOnClickListener(this);
        setting_pic_mode.setOnClickListener(this);
        setting_notification.setOnClickListener(this);
        setting_clear_cache.setOnClickListener(this);
        setting_exit.setOnClickListener(this);
        setting_collection.setOnClickListener(this);

        Bitmap bitmap = BitmapFactory.decodeFile(path + "head.jpg");
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.people);
            if (preferences.getBoolean("LOGIN_STATE", false)) {
                //从网络读取图片

                imageView.setImageBitmap(bitmap);

                BmobQuery query = new BmobQuery();
                query.findObjects(new FindListener() {
                    @Override
                    public void done(List list, BmobException e) {

                    }

                    @Override
                    public void done(Object o, Object o2) {

                    }
                });
            } else {
                imageView.setImageResource(R.drawable.people);
            }

        }
        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    BmobInstallation.getCurrentInstallation().save();
                    BmobPush.startWork(getActivity());
                } else {

                }
            }
        });

        noPic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    JudgeNetState state = new JudgeNetState();
                    /**
                     * 如果报错可试一下用   getContext获取上下文
                     */
                    ConnectivityManager manager = (ConnectivityManager) getActivity()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    state.CheckNetState(manager, getActivity());
                } else {

                }
            }
        });
        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startActivity(new Intent(getActivity(), DayOrNightActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), DayOrNightActivity.class));
                }
            }
        });

        return view;
    }

    private void initWeather(String cityNum) {
        new ParseWeatherAsyncTask().execute(cityNum);
    }

    private class ParseWeatherAsyncTask extends AsyncTask<String, Void, Weather> {
        Weather weather;

        @Override
        protected Weather doInBackground(String... strings) {
            try {
                URL url = new URL(Constants.BEGIN_WEATHER_URL + strings[0] + Constants
                        .END_WEATHER_URL);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("GET");
                huc.setRequestProperty("Charset", "utf-8");
                huc.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                InputStream inputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader bufferedReader = null;

                StringBuffer stringBuffer = new StringBuffer();
                String tempLine = null;

                if (huc.getResponseCode() >= 300) {
                    throw new Exception("Http访问没有成功，返回的响应码是："
                            + huc.getResponseCode());
                }
                //得到与服务器建立的字节流链接
                inputStream = huc.getInputStream();
                //将字节流转化为字符流
                inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                //将字符流转化为缓冲字符流
                bufferedReader = new BufferedReader(inputStreamReader);

                while ((tempLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(tempLine);
                }
                //返回数据json字符串
                String result = stringBuffer.toString();
                //关闭流
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                //解析Json数据
                Map<String, Object> json = JsonUtils.getMapObj(result);
                Map<String, Object> realtime = JsonUtils.getMapObj(json.get("realtime").toString());
                String weather1 = realtime.get("weather").toString();

                Map<String, Object> today = JsonUtils.getMapObj(json.get("today").toString());
                String tempMin = today.get("tempMin").toString();
                String tempMax = today.get("tempMax").toString();
                weather = new Weather();
                weather.setTemperature(tempMin + "~" + tempMax + "℃");
                weather.setWeather(weather1);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            if (weather != null) {
                tv_setting_weather.setText(weather.getWeather() + "  " + weather.getTemperature());
                setWeatherImage(iv_weather_icon, weather.getWeather());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_header:
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View layout = inflater.inflate(R.layout.my_alertdialog, null);
                final MyDialog myDialog = new MyDialog(getActivity());
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog.setContentView(layout);
                myDialog.show();
                Button local = (Button) layout.findViewById(R.id.from_local);
                Button photo = (Button) layout.findViewById(R.id.take_photo);
                Button cancel = (Button) layout.findViewById(R.id.cancel);

                local.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choseHeadImageFromGallery();
                        myDialog.dismiss();
                    }

                });

                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choseHeadImageFromCameraCapture();
                        myDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                break;
//            case R.id.setting_update_information:
//                startActivity(new Intent(getActivity(), ChangeYourInfo.class));
//                getActivity().overridePendingTransition(R.anim.anim_activity_slide_in_right,
//                        R.anim.anim_activity_slide_out_left);
//                break;
            case R.id.setting_find_password:
                startActivity(new Intent(getActivity(), FindPassword.class));
                getActivity().overridePendingTransition(R.anim.anim_activity_slide_in_right,
                        R.anim.anim_activity_slide_out_left);
                break;
            case R.id.setting_font:
                startActivity(new Intent(getActivity(), FontSizeActivity.class));
                getActivity().overridePendingTransition(R.anim.anim_activity_slide_in_right,
                        R.anim.anim_activity_slide_out_left);
                break;
            case R.id.setting_pic_mode:

                break;
            case R.id.setting_notification:
                /**
                 * 报错同上
                 */
                break;
            case R.id.setting_clear_cache:
                /**
                 * 报错同上
                 */
                ClearCeash clearCeash = new ClearCeash();
                clearCeash.clearAllCache(getActivity());
                clearCache.setText("0M");

                break;
            case R.id.setting_exit:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("LOGIN_STATE", false);
                editor.commit();
                Toast.makeText(getActivity(), "正在退出", Toast.LENGTH_SHORT).show();
                QuitApp quitApp = new QuitApp();
                quitApp.Quit();
                break;
            //跳转选择城市
            case R.id.bt_setting_position:
                bt_setting_position.startAnimation(scaleAnimation);
                Intent intent = new Intent(getActivity(), ChooseCityActivity.class);
                intent.putExtra("CITY", tv_setting_city.getText().toString());
                startActivityForResult(intent, Constants.WEATHER_REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.anim_activity_righttop_in,
                        R.anim.anim_alpha_out);
                break;
            //跳转收藏
            case R.id.setting_collection:
                Intent intentCollect = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intentCollect);
                getActivity().overridePendingTransition(R.anim.anim_activity_slide_in_right,
                        R.anim.anim_activity_slide_out_left);

                break;
            //跳转登录界面
            case R.id.account:
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 123);
                getActivity().overridePendingTransition(R.anim.anim_activity_slide_in_right,
                        R.anim.anim_activity_slide_out_left);
                break;
        }
    }


    public void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    public void choseHeadImageFromCameraCapture() {

        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "head.jpg")));
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        if (resultCode == RESULT_CANCELED) {
        //            Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
        //            return;
        //        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(data.getData());
                break;
            case CODE_CAMERA_REQUEST:
                File temp = new File(Environment.getExternalStorageDirectory(), "head.jpg");
                cropRawPhoto(Uri.fromFile(temp));
                break;
            case CODE_RESULT_REQUEST:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    head = bundle.getParcelable("data");
                    if (head != null) {
                        setImageToHeadView(head);
                        imageView.setImageBitmap(head);
                        UpLoadImg();
                    }
                }
                break;
            case Constants.WEATHER_REQUEST_CODE:
                if (data != null) {
                    Bundle bundle = data.getBundleExtra("BUNDLE_DATA");
                    String city_name = bundle.getString("NAME");
                    String city_num = bundle.getString("CITY_NUM");
                    tv_setting_city.setText(city_name);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences
                            ("CITY_INFO",
                                    MODE_PRIVATE).edit();
                    editor.putString("name", city_name);
                    editor.putString("city_num", city_num);
                    editor.commit();
                    initWeather(city_num);
                }
                break;
            case 123:
                String userName = data.getStringExtra("ACCOUNT");
                account.setText(userName);
                break;
        }
    }

    public void UpLoadImg() {

    }

    /**
     * 设置天气图片
     *
     * @param imageView
     * @param weather
     */
    private void setWeatherImage(ImageView imageView, String weather) {
        String[] strs = weather.split("转");
        switch (strs[0]) {
            case "暴雪":
                imageView.setBackgroundResource(R.drawable.weather_baoxue);
                break;
            case "暴雨":
                imageView.setBackgroundResource(R.drawable.weather_baoyu);
                break;
            case "大暴雨":
                imageView.setBackgroundResource(R.drawable.weather_dabaoyu);
                break;
            case "大雪":
                imageView.setBackgroundResource(R.drawable.weather_daxue);
                break;
            case "大雨":
                imageView.setBackgroundResource(R.drawable.weather_dayu);
                break;
            case "冻雨":
                imageView.setBackgroundResource(R.drawable.weather_dongyu);
                break;
            case "多云":
                imageView.setBackgroundResource(R.drawable.weather_duoyun);
                break;
            case "浮尘":
                imageView.setBackgroundResource(R.drawable.weather_fuchen);
                break;
            case "雷阵雨":
                imageView.setBackgroundResource(R.drawable.weather_leizhenyu);
                break;
            case "霾":
                imageView.setBackgroundResource(R.drawable.weather_mai);
                break;
            case "强沙尘暴":
                imageView.setBackgroundResource(R.drawable.weather_qiangshachenbao);
                break;
            case "晴":
                imageView.setBackgroundResource(R.drawable.weather_qing);
                break;
            case "沙尘暴":
                imageView.setBackgroundResource(R.drawable.weather_shachenbao);
                break;
            case "雾":
                imageView.setBackgroundResource(R.drawable.weather_wu);
                break;
            case "小雪":
                imageView.setBackgroundResource(R.drawable.weather_xiaoxue);
                break;
            case "小雨":
                imageView.setBackgroundResource(R.drawable.weather_xiaoyu);
                break;
            case "扬沙":
                imageView.setBackgroundResource(R.drawable.weather_yangsha);
                break;
            case "阴":
                imageView.setBackgroundResource(R.drawable.weather_yin);
                break;
            case "雨夹雪":
                imageView.setBackgroundResource(R.drawable.weather_yujiaxue);
                break;
            case "阵雪":
                imageView.setBackgroundResource(R.drawable.weather_zhenxue);
                break;
            case "阵雨":
                imageView.setBackgroundResource(R.drawable.weather_zhenyu);
                break;
            case "阵雨冰雹":
                imageView.setBackgroundResource(R.drawable.weather_zhenyubingbao);
                break;
            case "中雪":
                imageView.setBackgroundResource(R.drawable.weather_zhongxue);
                break;
            case "中雨":
                imageView.setBackgroundResource(R.drawable.weather_zhongyu);
                break;
            default:
                imageView.setBackgroundResource(R.drawable.weather_qing);
                break;
        }
    }

    private void cropRawPhoto(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 180);
        intent.putExtra("outputY", 180);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);

    }

    public void setImageToHeadView(Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            /**
             * 检查是否挂载,未挂载就退出
             */
            Toast.makeText(getActivity(), "SD卡没有挂载", Toast.LENGTH_SHORT).show();
            return;
        }
        FileOutputStream fileOutputStream = null;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = path + "head.jpg";
        try {
            fileOutputStream = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);//30代表压缩率70
            // 压缩率，如果报错可能是OOM，降低压缩率
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}



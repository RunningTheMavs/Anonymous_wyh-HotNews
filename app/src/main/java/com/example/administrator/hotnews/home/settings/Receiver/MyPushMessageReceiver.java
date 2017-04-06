package com.example.administrator.hotnews.home.settings.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.widget.ImageView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.main.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {

    /**
     * Bmob云推送，可自定义推送内容，title ，content，image
     * @param context
     * @param intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
                String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
                String title=null;
                String content=null;
                String imageUrl=null;
            try {
                /**
                 * 此处为自定义的云端推送内容
                 */
                JSONObject jsonObject=new JSONObject(jsonStr);
                title = jsonObject.getString("title");
                content = jsonObject.getString("content");
                imageUrl = jsonObject.getString("image");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            final Notification.Builder builder = new Notification.Builder(context);
            PendingIntent pending = PendingIntent.getActivity(context,1000,new Intent(context, HomeActivity.class),PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pending);
            builder.setContentTitle(title);
            builder.setContentText(content);

            RequestQueue queue= Volley.newRequestQueue(context);
            ImageRequest request=new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    builder.setSmallIcon(Icon.createWithBitmap(response));
                }
            }, 40, 40, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                }
            });
            queue.add(request);

//            Notification notification=new Notification.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("")
//                    .setContentText(content).build(
            Notification notification=builder.build();

            notification.defaults |= Notification.DEFAULT_SOUND; //设置声音
            notification.defaults |= Notification.DEFAULT_VIBRATE; //设置震动
            notification.defaults |= Notification.DEFAULT_LIGHTS; //设置指示灯

            manager.notify(1,notification);
        }
    }
}

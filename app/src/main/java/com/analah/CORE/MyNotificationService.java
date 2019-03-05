package com.analah.CORE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.analah.AppController;
import com.analah.Global;
import com.analah.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MyNotificationService extends Service {




    String msg_type="1";
    String uid;
    String fetch_data="true",JSON;
    int shid;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ResultReceiver myReciver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 2000, 5 * 1000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myReciver=intent.getParcelableExtra("myReciver");

	/*	sessionId=intent.getStringExtra("sid");
		UserId=intent.getStringExtra("uid");
		msg_type=intent.getStringExtra("mt");
		fetch_data=intent.getStringExtra("fd");*/


        return super.onStartCommand(intent, flags, startId);
    }

    private Timer mTimer;

    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            JSON = "{\"session\":\""+Global.Session+"\",\"module_name\":\"C_Call_Initiate\",\"query\":\"c_call_initiate_cstm.lead_status_c='false' and c_call_initiate_cstm.lead_created_by_id_c='"+Global.customerid+"'\",\"order_by\":\"\",\"offset\":0,\"select_fields\":[\"id\",\"name\",\"lead_phone_c\",\"lead_created_by_id_c\",\"lead_status_c\",\"lead_bean_id_c\",\"auto_id_c\"],\"max_results\":5,\"deleted\":0}";

            getCalling();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mTimer.cancel();
            timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getCalling() {
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    {
                       /* Gson gson = new Gson();
                        Call_Responce notificationResponse = gson.fromJson(response, Call_Responce.class);*/

                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        String restoredText = prefs.getString("id", null);
                        if (restoredText != null) {
                            uid = prefs.getString("id", "0");//"No name defined" is the default value.
                        }

                        JSONArray array =object.getJSONArray("entry_list");

                        for (int i = 0  ; i < array.length() ; i++){

                            JSONObject jsonObject = array.getJSONObject(i);

                            JSONObject Name_Value_list = jsonObject.getJSONObject("name_value_list");
                             String id = Name_Value_list.getJSONObject("auto_id_c").getString("value");
                            Log.d("id :",id);
                            int iid = Integer.parseInt(id);
                            if (uid != null){
                                shid = Integer.parseInt(uid);}

                           if (iid >shid){

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("id", id);
                                editor.apply();

                               String title="ANANLAH Call Invitation";
                               String subject=Name_Value_list.getJSONObject("name").getString("value");

                               createNotification(title,subject);
                               Bundle bundle = new Bundle();
                               bundle.putString("Notification","Notification");
                               myReciver.send(18,bundle);

                           }




                    }


                        //  models=notificationResponse.getEntryList();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> param = new HashMap<String,String>();

                param.put("method","get_entry_list");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",JSON);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification(String title, String message)
    {
        /**Creates an explicit intent for an Activity in your app**/
        NotificationCompat.BigTextStyle style=new NotificationCompat.BigTextStyle();
        style.bigText(message);
        style.setBigContentTitle(title);
        style.setSummaryText("ANANLAH" );


        Intent intent =new Intent(getApplicationContext(),Notification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(Notification.class);
        stackBuilder.addNextIntentWithParentStack(intent);*/

        NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(this,"analah" )
                .setSmallIcon(R.drawable.ic_analah)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setStyle(style).setBadgeIconType(R.drawable.ic_analah)
                .setPriority(Notification.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ic_analah))
                .setSmallIcon(R.drawable.ic_analah)
                .setGroup("ANANLAH")
                .setTicker("ANANLAH");

        PendingIntent pendingIntent =PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        nbuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext() .getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1", "Analah Notification", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(alarmSound,att);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            nbuilder.setChannelId("1");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }else {
            long[] vibrate = { 0, 100, 200, 300 };
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            nbuilder.setSound(alarmSound);
            nbuilder.setVibrate(vibrate);

        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, nbuilder.build());
    }
}



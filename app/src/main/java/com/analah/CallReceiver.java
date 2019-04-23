package com.analah;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Toast;

import com.analah.Activity.Form_Detail;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class CallReceiver extends PhonecallReceiver {


    String set_entry_JSON;
    String  _audioBase64;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        //
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        //
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        //
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        //
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        //
        Log.d("Success","True");
        Toast.makeText(ctx, "Call Recorded Successfully",Toast.LENGTH_LONG).show();
        encodeAudio(Global.audiofile ,ctx);

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        //
    }

    private void encodeAudio(File audioFile, Context ctx) {
        try {

            byte[] audioBytes;

            // Just to check file size.. Its is correct i-e; Not Zero
            long fileSize = audioFile.length();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = null;
            fis = new FileInputStream(audioFile);

            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            // Here goes the Base64 string
              _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);

            _audioBase64 = _audioBase64.replaceAll("\\s","");

            MediaPlayer mp = MediaPlayer.create(ctx, Uri.parse(audioFile.getPath()));
            long duration = TimeUnit.MILLISECONDS.toMinutes(mp.getDuration());
            Log.d("duration", String.valueOf(duration));

            set_Entry(ctx,String.valueOf(duration));

            // set_Entry(_audioBase64);



            Log.d("Base64",_audioBase64);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void set_Entry(final Context ctx, String s) {

         set_entry_JSON = "{\"session\":\""+ Global.Session+"\"," +
                "\"module_name\":\"Notes\"," +
                 "\"name_value_list\":[{\"name\":\"name\",\"value\":\"Example Note\"}," +
                 "{\"name\":\"description\",\"value\":\"Test content for note again\"}," +
                 "{\"name\":\"parent_type\",\"value\":\"Leads\"},{\"name\":\"parent_id\",\"value\":\""+Global.set_id+"\",\"name\":\""+s+"\"}]}";


        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    set_entry_JSON = "{\"session\":\""+ Global.Session+"\"," +
                            "\"module_name\":\"Leads\"," +
                            "\"module_id\":\""+Global.set_id+"\"," +
                            "\"link_field_name\":\"notes\"," +
                            "\"related_ids\":[\""+object.getString("id")+"\"]}";

                    Global.set_Entry_ID = object.getString("id");
                    set_relationship(set_entry_JSON,ctx);





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

                param.put("method","set_entry");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",set_entry_JSON);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    private class GetAlbum extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        Context ctx;

        public GetAlbum(Context ctx) {
            this.ctx = ctx;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }



        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://analah.demobox.online/service/v4_1/rest.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("method", params[0])
                        .appendQueryParameter("input_type", params[1])
                        .appendQueryParameter("response_type", params[2])
                        .appendQueryParameter("rest_data", params[3])
                        ;
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());


                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // pdLoading.dismiss();

            try {
                JSONObject jsonObject =new JSONObject(s);
                if (jsonObject.has("id")){
                    Toast.makeText(ctx, "Recording Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ctx, "Recording Upload Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
    private void set_relationship(final String RestData, final Context ctx) {
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);



                    new GetAlbum(ctx).execute("set_note_attachment","JSON","JSON","{\"session\":\""+Global.Session+"\",\"note\":{\"id\":\""+Global.set_Entry_ID+"\",\"filename\":\""+Global.audiofile.getName()+"\",\"file\":\""+_audioBase64+"\"}}");


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

                param.put("method","set_relationship");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",RestData);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);


    }

}
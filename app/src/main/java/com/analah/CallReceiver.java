package com.analah;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class CallReceiver extends PhonecallReceiver {

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
            String  _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);

            set_Entry(_audioBase64);



            Log.d("Base64",_audioBase64);


    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void set_Entry(final String Base64) {
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    Log.d("Responce_Callrecord",response);
                  //  Global.set_Entry_ID = object.getString("id");
                 //   set_relationship(set_entry_JSON);





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

                param.put("method","set_note_attachment");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data","{\"session\":\""+Global.Session+"\",\"note\":{\"id\":\""+Global.set_Entry_ID+"\",\"filename\":\"Record\",\"file\":\""+Base64+"\"}}");
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);


    }

}
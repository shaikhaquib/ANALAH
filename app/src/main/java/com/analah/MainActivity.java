package com.analah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.analah.CORE.SQLiteHandler;
import com.analah.CORE.SessionManager;
import com.analah.Login_responce.Response_Login;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edtEmail ,edtPassword;
    String email,password ,JSON;
    ProgressDialog progressDialog ;
    SQLiteHandler db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        db=new SQLiteHandler(this);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MainActivity.this, Call_List.class);
            startActivity(intent);
            finish();
        }

    }

    public void dologin(View view) {
    //    startActivity(new Intent(getApplicationContext(),Call_List.class));

        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        boolean cancel= false;
        View focusView = null;

        if (TextUtils.isEmpty(email)){
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(password)){
            edtPassword.setError(getString(R.string.error_field_required));
            focusView = edtPassword;
            cancel = true;
        }if (cancel) {
            focusView.requestFocus();
        }
        else {
            //      startActivity(new Intent(getApplicationContext(),MainActivity.class));
            JSON = "{\"user_auth\":{\"user_name\":\""+email+"\"," +
                    "\"password\":\""+md5(password)+"\"}," +
                    "\"application_name\":\"Analah\"," +
                    "\"name_value_list\":[]}";
            Aunthanticate();

        }


    }

    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void Aunthanticate() {
        progressDialog.setMessage("Authenticating..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if (object.has("name")){
                        Global.diloge(MainActivity.this,object.getString("name"),object.getString("description"));

                    }else {
                        Gson gson = new Gson();
                        Response_Login notificationResponse = gson.fromJson(response, Response_Login.class);
                        String admin;
                        session.setLogin(true);
                        if (notificationResponse.getNameValueList().getUserIsAdmin().isValue()){
                            admin = "1";
                        }else{
                            admin = "0";
                        }

                        db.addUser(
                                notificationResponse.getNameValueList().getUserId().getValue(),
                                notificationResponse.getId(),
                                notificationResponse.getNameValueList().getUserLanguage().getValue(),
                                notificationResponse.getNameValueList().getUserCurrencyName().getValue(),
                                admin,
                                notificationResponse.getNameValueList().getUserName().getValue(),
                                notificationResponse.getNameValueList().getUserDefaultDateformat().getValue(),
                                notificationResponse.getNameValueList().getUserCurrencyId().getValue(),
                                String.valueOf(notificationResponse.getNameValueList().getUserDefaultTeamId().getValue()));
                        startActivity(new Intent(getApplicationContext(),Call_List.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> param = new HashMap<String,String>();

                param.put("method","login");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",JSON);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

}

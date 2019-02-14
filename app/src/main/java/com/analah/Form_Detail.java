package com.analah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.analah.CORE.SQLiteHandler;
import com.analah.CORE.SessionManager;
import com.analah.DetailsRespoone.DetailResponse;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Form_Detail extends AppCompatActivity {

    LinearLayout LeadView,CallBackview;
    RadioButton Lead , callback ,notIntrest,notQualified;
    ProgressDialog progressDialog;
    private SQLiteHandler db;
    private SessionManager session;
    String JSON;
    TextView campName;
    EditText edtName ,edtTitle ,edtDepartment,edtAccountName,edtPrimoryadd,edtEmail,edtMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_detail);

        ActionBar actionBar = getSupportActionBar();
        Global.actionbar(Form_Detail.this,actionBar,"Details");


        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(this);
        progressDialog = new ProgressDialog(this);
        LeadView = findViewById(R.id.leadViw);
        CallBackview = findViewById(R.id.CallBack);
        Lead = findViewById(R.id.rbLead);
        callback = findViewById(R.id.rbCallback);
        notIntrest = findViewById(R.id.rbNotIntrest);
        notQualified = findViewById(R.id.rbNotQualifed);

        edtName         = findViewById(R.id.cmpname);
        edtTitle        = findViewById(R.id.cmptitle);
        edtDepartment   = findViewById(R.id.cmpdept);
        edtAccountName  = findViewById(R.id.cmpacctname);
        edtPrimoryadd   = findViewById(R.id.cmpadd);
        edtEmail        = findViewById(R.id.cmpemail);
        edtMobile       = findViewById(R.id.cmpMobile);
        campName       = findViewById(R.id.campName);



        Lead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LeadView.setVisibility(View.VISIBLE);
                }else {
                    LeadView.setVisibility(View.GONE);
                }
            }
        });
        callback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    CallBackview.setVisibility(View.VISIBLE);
                }else {
                    CallBackview.setVisibility(View.GONE);
                }
            }
        });
        JSON = "{\"session\":\""+Global.Session+"\",\"module_name\":\"Leads\",\"id\":\""+getIntent().getStringExtra("id")+"\",\"select_fields\":[\"id\",\"name\",\"phone_mobile\",\"account_name\",\"email1\",\"phone_work\",\"title\",\"department\",\"description\",\"status\",\"lead_source\"],\"link_name_to_fields_array \":0,\"track_view\":\"false\"}";
        getDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDetails() {
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
                        session.setLogin(false);
                        db.deleteUsers();
                        Global.diloge(Form_Detail.this,object.getString("name"),object.getString("description"));
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }else {
                        Gson gson = new Gson();
                        DetailResponse notificationResponse = gson.fromJson(response, DetailResponse.class);

                        campName      .setText(notificationResponse.getEntryList().get(0).getModuleName());
                        edtName       .setText(notificationResponse.getEntryList().get(0).getNameValueList().getName().getValue());
                        edtTitle      .setText(notificationResponse.getEntryList().get(0).getNameValueList().getTitle().getValue());
                        edtDepartment .setText(notificationResponse.getEntryList().get(0).getNameValueList().getDepartment().getValue());
                        edtAccountName.setText(notificationResponse.getEntryList().get(0).getNameValueList().getAccountName().getValue());
                        edtPrimoryadd .setText(notificationResponse.getEntryList().get(0).getNameValueList().getDescription().getValue());
                        edtEmail      .setText(notificationResponse.getEntryList().get(0).getNameValueList().getEmail1().getValue());
                        edtMobile     .setText(notificationResponse.getEntryList().get(0).getNameValueList().getPhoneMobile().getValue());



                        //  models=notificationResponse.getEntryList();
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

                param.put("method","get_entry");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",JSON);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}

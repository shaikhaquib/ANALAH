package com.analah.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.analah.AppController;
import com.analah.CORE.SQLiteHandler;
import com.analah.CORE.SessionManager;
import com.analah.DetailsRespoone.DetailResponse;
import com.analah.Global;
import com.analah.R;
import com.analah.RadioButtonWithTableLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Form_Detail extends AppCompatActivity {

    LinearLayout LeadView,CallBackview;
    CardView btnFeedBack;
    Button save;
    RadioButton converted, callback ,notIntrest, notcontected ,interested;
    ProgressDialog progressDialog;
    private SQLiteHandler db;
    private SessionManager session;
    String JSON ,feedback;
    TextView campName;
    EditText edtName ,edtTitle ,edtDepartment,edtAccountName,edtPrimoryadd,edtEmail,edtMobile,fileName,edtRemark;
    String set_entry_JSON,base64String;
    File Uplaodfile;
    private static final int OPEN_REQUEST_CODE = 41;
    com.analah.RadioButtonWithTableLayout RadioButtonWithTableLayout;

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
        save         = findViewById(R.id.save);
        btnFeedBack  = findViewById(R.id.addfeedabck);

        converted    = findViewById(R.id.rbconverted);
        callback     = findViewById(R.id.rbCallback);
        notIntrest   = findViewById(R.id.rbNotIntrest);
        notcontected = findViewById(R.id.rbNotcontected);
        interested   = findViewById(R.id.rbInterested);
        edtRemark    = findViewById(R.id.cmpDesc);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        edtName         = findViewById(R.id.cmpname);
        edtTitle        = findViewById(R.id.cmptitle);
        edtDepartment   = findViewById(R.id.cmpdept);
        edtAccountName  = findViewById(R.id.cmpacctname);
        edtPrimoryadd   = findViewById(R.id.cmpadd);
        edtEmail        = findViewById(R.id.cmpemail);
        edtMobile       = findViewById(R.id.cmpMobile);
        campName       = findViewById(R.id.campName);
        fileName       = findViewById(R.id.fileName);


        interested.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    feedback = "Contacted_Interested";
                }
            }
        }); notIntrest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    feedback = "Contacted_Not_Interested";
                }
            }
        }); callback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    feedback = "Contacted_Call_Back";
                }
            }
        }); notcontected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    feedback = "Not_Contacted";
                }
            }
        }); converted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    feedback = "Contacted_Converted";
                }
            }
        });


        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(Form_Detail.this, feedback, Toast.LENGTH_SHORT).show();

                if (!edtRemark.getText().toString().isEmpty()){

                String Rest = "{\"session\":\""+Global.Session+"\"," +
                        "\"module_name\":\"F_FeedBack\"," +
                        "\"name_value_list\":[{\"name\":\"call_result_c\",\"value\":\""+feedback+"\"}," +
                        "{\"name\":\"assigned_user_id\",\"value\":\""+Global.customerid+"\"}," +
                        "{\"name\":\"remark_c\",\"value\":\""+edtRemark.getText().toString()+"\"}," +
                        "{\"name\":\"follow_up_date_c_date\",\"value\":\""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\"}," +
                        "{\"name\":\"scrip_name_c\",\"value\":\"scrip name\"}," +
                        "{\"name\":\"scrip_quantity_c\",\"value\":\"scrip qtt\"}," +
                        "{\"name\":\"scrip_rate_c\",\"value\":\"scrip rate\"}]}";

                setfeedback(Rest);
                }else {
                    Global.diloge(Form_Detail.this,"Field Required","Remark is required");
                    edtRemark.setError("Field Required");
                }
            }
        });

        set_entry_JSON = "{\"session\":\""+Global.Session+"\"," + "\"module_name\":\"Notes\",\"name_value_list\":[\"name\",\"value\"]}";

       /* converted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LeadView.setVisibility(View.VISIBLE);
                }else {
                    LeadView.setVisibility(View.GONE);
                }
            }
        });*/
/*
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
*/
        JSON = "{\"session\":\""+Global.Session+"\",\"module_name\":\"Leads\",\"id\":\""+getIntent().getStringExtra("id")+"\",\"select_fields\":[\"id\",\"name\",\"phone_mobile\",\"account_name\",\"email1\",\"phone_work\",\"title\",\"department\",\"description\",\"status\",\"lead_source\"],\"link_name_to_fields_array \":0,\"track_view\":\"false\"}";
        getDetails();
        set_Entry();
    }

    private void setfeedback(final String rest) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    set_entry_JSON = "{\"session\":\""+Global.Session+"\"," +
                            "\"module_name\":\"Leads\"," +
                            "\"module_id\":\""+getIntent().getStringExtra("id")+"\"," +
                            "\"link_field_name\":\"leads_f_feedback_1\"," +
                            "\"related_ids\":[\""+object.getString("id")+"\"]}";

                    Global.diloge(Form_Detail.this,"Success","Your feedback has been recorded \n Thank you ! for yor valuable feedback.");

                   // Global.set_Entry_ID = object.getString("id");
                    set_relationship(set_entry_JSON);





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

                param.put("method","set_entry");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",rest);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);


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

    private void set_Entry() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    set_entry_JSON = "{\"session\":\""+Global.Session+"\"," +
                            "\"module_name\":\"Leads\"," +
                            "\"module_id\":\""+getIntent().getStringExtra("id")+"\"," +
                            "\"link_field_name\":\"notes\"," +
                            "\"related_ids\":[\""+object.getString("id")+"\"]}";

                    Global.set_Entry_ID = object.getString("id");
                    set_relationship(set_entry_JSON);





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

                param.put("method","set_entry");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",set_entry_JSON);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    private void set_relationship(final String RestData) {
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);





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


    private void getDetails() {
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, "http://analah.demobox.online/service/v4_1/rest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if (object.has("name")){
                        db.deleteUsers();
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(Form_Detail.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(Form_Detail.this);
                        }
                        builder.setCancelable(false);
                        builder.setTitle(object.getString("name"))
                                .setMessage(object.getString("description"))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                      //  db.deleteUsers();
                                      //  startActivity(new Intent(getApplicationContext(),Call_List.class));
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

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

    public void OpenFile(View view) {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,1);
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
                System.out.println(uri);


                getPath(uri);

                encodeAudio(new File(getPath(uri)),Form_Detail.this);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String readFileContent(Uri uri) throws IOException {

        InputStream inputStream =
                getContentResolver().openInputStream(uri);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while ((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    private void encodeAudio(File audioFile, Context ctx) {
        try {

            byte[] audioBytes;

            save.setVisibility(View.VISIBLE);
            fileName.setEnabled(true);
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
            base64String = Base64.encodeToString(audioBytes, Base64.DEFAULT);

            base64String = base64String.replaceAll("\\s","");

            Uplaodfile = audioFile;

            fileName.setText(Uplaodfile.getName());

            // set_Entry(_audioBase64);



            Log.d("Base64",base64String);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Save() {
        new GetAlbum().execute("set_note_attachment","JSON","JSON","{\"session\":\""+Global.Session+"\",\"note\":{\"id\":\""+Global.set_Entry_ID+"\",\"filename\":\""+Uplaodfile.getName()+"\",\"file\":\""+base64String+"\"}}");
    }

    private class GetAlbum extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        ProgressDialog pDialog = new ProgressDialog(Form_Detail.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Uploading...");
            pDialog.show();
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
            pDialog.dismiss();
            fileName.setEnabled(false);


            try {
                JSONObject jsonObject =new JSONObject(s);
                if (jsonObject.has("id")){
                    Global.diloge(Form_Detail.this,"SUCCESS","You have have successfully uploaded recording");
                    save.setVisibility(View.GONE);
                }else {
                    Global.diloge(Form_Detail.this,"FAILED","Some thing went wrong please try again later.");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}

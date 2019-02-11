package com.analah;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.analah.CALLING_Responce.Call_Responce;
import com.analah.CALLING_Responce.EntryListItem;
import com.analah.CORE.SQLiteHandler;
import com.analah.CORE.SessionManager;
import com.analah.Login_responce.Response_Login;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Call_List extends AppCompatActivity {

    RecyclerView rv_Callist;
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    private SQLiteHandler db;
    private SessionManager session;
    ProgressDialog progressDialog;
    private List<EntryListItem> models;
    String JSON;

    HashMap<String, String> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllist);
        setTitle("CALL LIST");
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(this);
        progressDialog = new ProgressDialog(this);
        user = db.getUserDetails();
        Global.name = user.get("name");
        Global.customerid= user.get("customer_id");
        Global.Session = user.get("lang");

        rv_Callist=findViewById(R.id.rv_Callist);
        rv_Callist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        JSON = "{\"session\":\""+Global.Session+"\"," +
                "\"module_name\":\"Leads\",\"query\":\"\"," +
                "\"order_by\":\"\",\"offset\":0," +
                "\"select_fields\":[\"id\",\"name\",\"phone_mobile\"],\"max_results\":30,\"deleted\":0}\n";
        getCalling();

        rv_Callist.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(Call_List.this).inflate(R.layout.calllist_adapt,viewGroup,false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

                final Holder holder = (Holder)viewHolder;

              //  final EntryListItem model = models.get(i);
                holder.Call.setTag(i);

                holder.name.setText(models.get(i).getNameValueList().getName().getValue());
                holder.desc.setText(models.get(i).getNameValueList().getPhoneMobile().getValue());

                holder.Call.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT < 23) {
                            //Do not need to check the permission
                        } else {
                            if (checkAndRequestPermissions()) {
                      /* Intent intent = new Intent(context, Main2Activity.class);
                       intent.putExtra("mobielno",current.LeadMobile);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       context.startActivity(intent);*/

                       /* Uri packageURI = Uri.parse("package:"+"dealwithusmailcom.dwsales");
                        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                        context.startActivity(uninstallIntent);*/

                                try {
                                    // Initiate DevicePolicyManager.
                                    mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                                    mAdminName = new ComponentName(Call_List.this, DeviceAdminDemo.class);
                                    //   getSupportActionBar().hide();
                                    if (!mDPM.isAdminActive(mAdminName)) {
                                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                                        startActivityForResult(intent, REQUEST_CODE);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), TService.class);
                                        startService(intent);
                                        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+models.get(i).getNameValueList().getPhoneMobile().getValue() ));
                                        startActivity(intent1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }


                    }
                });


            }

            @Override
            public int getItemCount() {
                return 4;
            }

            class Holder extends RecyclerView.ViewHolder {
                CardView Call;
                TextView name ,desc;
                public Holder(@NonNull View itemView) {
                    super(itemView);
                    Call=itemView.findViewById(R.id.call);
                    name=itemView.findViewById(R.id.name);
                    desc=itemView.findViewById(R.id.desc);
                }
            }

        });
    }
    private void getCalling() {
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
                        Global.diloge(Call_List.this,object.getString("name"),object.getString("description"));
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }else {
                        Gson gson = new Gson();
                        Call_Responce notificationResponse = gson.fromJson(response, Call_Responce.class);
                        models=notificationResponse.getEntryList();
                        rv_Callist.getAdapter().notifyDataSetChanged();
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

                param.put("method","get_entry_list");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",JSON);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public void setTitle(String title){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

    private boolean checkAndRequestPermissions() {
        int CALLPERMITION = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        int WRITEStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int READStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int RECORDAUDIO = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (CALLPERMITION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        } if (WRITEStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } if (READStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        } if (RECORDAUDIO != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Call_List.this,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }



        return true;
    }


}

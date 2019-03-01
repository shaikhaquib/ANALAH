package com.analah.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.analah.AppController;
import com.analah.CORE.MyNotificationService;
import com.analah.CORE.SQLiteHandler;
import com.analah.CORE.SessionManager;
import com.analah.Calling_model;
import com.analah.Global;
import com.analah.R;
import com.analah.TService;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Call_List extends AppCompatActivity {
    ArrayList<Calling_model> models;

    RecyclerView rv_Callist;
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    private SQLiteHandler db;
    private SessionManager session;
    ProgressDialog progressDialog;
    String JSON , CountJSON;
    MenuItem menuItem;
    HashMap<String, String> user;
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    LinearLayoutManager manager;
    int offset = 15 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllist);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(this);
        progressDialog = new ProgressDialog(this);
        user = db.getUserDetails();
        Global.name = user.get("name");
        Global.customerid= user.get("customer_id");
        Global.Session = user.get("lang");


        setTitle("CALL LIST");
        getSupportActionBar().setSubtitle(Global.name);
        rv_Callist=findViewById(R.id.rv_Callist);
        manager = new LinearLayoutManager(getApplicationContext());
        rv_Callist.setLayoutManager(manager);

        JSON = "{\"session\":\""+Global.Session+"\"," +
                "\"module_name\":\"Leads\",\"query\":\"\"," +
                "\"order_by\":\"date_entered desc\",\"offset\":0," +
                "\"select_fields\":[\"id\",\"name\",\"phone_mobile\"],\"max_results\":30,\"deleted\":0}\n";

        CountJSON = "{\"session\":\""+Global.Session+"\",\"module_name\":\"C_Call_Initiate\",\"query\":\"c_call_initiate_cstm.lead_status_c='false' and c_call_initiate_cstm.lead_created_by_id_c='"+Global.customerid+"'\",\"order_by\":\"\",\"offset\":0,\"select_fields\":[\"id\",\"name\",\"lead_phone_c\",\"lead_created_by_id_c\",\"lead_status_c\",\"lead_bean_id_c\",\"auto_id_c\"],\"max_results\":5,\"deleted\":0}";

        getCalling();
        getCount();

        models = new ArrayList<>();
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

                final Calling_model model = models.get(i);
                    holder.Call.setTag(i);
                holder.Dtails.setTag(i);

                holder.name.setText(model.Name);
                holder.campName.setText(model.Name);
                holder.desc.setText(model.Phone_no);

                holder.Dtails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Form_Detail.class).putExtra("id",model.id));
                    }
                });

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


                                try {
                                    // Initiate DevicePolicyManager.

                                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O){
                                    Intent intent = new Intent(getApplicationContext(), TService.class);
                                    intent.putExtra("id",model.id);
                                    startService(intent);
                                    }
                                    Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+model.Phone_no));
                                    startActivity(intent1);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }


                    }
                });

                startService(new Intent(getApplicationContext(), MyNotificationService.class));
            }

            @Override
            public int getItemCount() {
                return models.size();
            }

            class Holder extends RecyclerView.ViewHolder {
                CardView Call,Dtails;
                TextView name ,desc,campName;
                public Holder(@NonNull View itemView) {
                    super(itemView);
                    Call=itemView.findViewById(R.id.call);
                    Dtails=itemView.findViewById(R.id.Dtails);
                    name=itemView.findViewById(R.id.name);
                    desc=itemView.findViewById(R.id.desc);
                    campName=itemView.findViewById(R.id.campName);
                }
            }

        });
        rv_Callist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;

                    offset = offset+15;

                    JSON = "{\"session\":\""+Global.Session+"\"," +
                            "\"module_name\":\"Leads\",\"query\":\"\"," +
                            "\"order_by\":\"\",\"offset\":"+String.valueOf(offset)+"," +
                            "\"select_fields\":[\"id\",\"name\",\"phone_mobile\"],\"max_results\":15,\"deleted\":0}\n";

                    getCalling();
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



                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(Call_List.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(Call_List.this);
                        }
                        builder.setCancelable(false);
                        builder.setTitle(object.getString("name"))
                                .setMessage(object.getString("description"))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.setLogin(false);
                                        dialog.dismiss();
                                        db.deleteUsers();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();


                    }else {
                       /* Gson gson = new Gson();
                        Call_Responce notificationResponse = gson.fromJson(response, Call_Responce.class);*/

                       JSONArray array =object.getJSONArray("entry_list");

                       for (int i = 0  ; i < array.length() ; i++){

                           JSONObject jsonObject = array.getJSONObject(i);
                           Calling_model calling_model =new Calling_model();

                           calling_model.id =jsonObject.getString("id");
                           JSONObject Name_Value_list = jsonObject.getJSONObject("name_value_list");
                           calling_model.Phone_no=Name_Value_list.getJSONObject("phone_mobile").getString("value");
                           calling_model.Name=Name_Value_list.getJSONObject("name").getString("value");
                           models.add(calling_model);
                           models.size();

                           rv_Callist.getAdapter().notifyDataSetChanged();

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

/*
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
*/

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);


        menuItem = menu.findItem(R.id.notification);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logout:
                session.setLogin(false);
                db.deleteUsers();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                break;
                case R.id.notification:
                startActivity(new Intent(getApplicationContext(),Notification.class));
                break;

        }
        return true;
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.action_bar_notifitcation_icon, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            if (count > 10)
            textView.setText("9+");
            else
                textView.setText("" + count);

        }

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }


    private void  getCount() {
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
                        //session.setLogin(false);
                       // db.deleteUsers();
                      //  Global.diloge(Call_List.this,object.getString("name"),object.getString("description"));
                       // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                      //  finish();
                    }else {
                       /* Gson gson = new Gson();
                        Call_Responce notificationResponse = gson.fromJson(response, Call_Responce.class);*/

                       int Count = Integer.parseInt(object.getString("total_count"));

                       if (Count > 0){
                           if (Count > 10)
                              menuItem.setIcon(buildCounterDrawable(9,  R.drawable.ic_notifications));
                           else
                               menuItem.setIcon(buildCounterDrawable(Count,  R.drawable.ic_notifications));

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
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> param = new HashMap<String,String>();

                param.put("method","get_entry_list");
                param.put("input_type","JSON");
                param.put("response_type","JSON");
                param.put("rest_data",CountJSON);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

}

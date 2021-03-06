package beautymentor.in;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.My_order_detail_adapter;
import Adapter.OrderStatusAdapter;
import Config.BaseURL;
import Model.My_order_detail_model;
import Model.OrderStatusModel;
import Module.Module;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonArrayRequest;
import util.CustomVolleyJsonRequest;
import util.Session_management;

public class MyOrderDetail extends AppCompatActivity {
    private static String TAG = MyOrderDetail.class.getSimpleName();
    Module module;
    Dialog dialog;
    EditText et_remark;
    Button btn_yes,btn_no;
    private TextView tv_date, tv_time, tv_total, tv_delivery_charge;
    private RelativeLayout btn_cancle;
    private RecyclerView rv_detail_order,rv_order_status;
Dialog loadingBar ;
    private String sale_id, status;
    ImageView back_button;
     SharedPreferences preferences;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    private List<OrderStatusModel> order_status_list = new ArrayList<>();
    OrderStatusAdapter orderStatusAdapter;
    public MyOrderDetail() {
        // Required empty public constructor
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_order_detail);

        loadingBar=new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        dialog=new Dialog(MyOrderDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_cancel_order_layout);
        dialog.setCanceledOnTouchOutside(false);
        module=new Module();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.order_detail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrderDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tv_date = (TextView) findViewById(R.id.tv_order_Detail_date);
        tv_time = (TextView) findViewById(R.id.tv_order_Detail_time);
        tv_delivery_charge = (TextView) findViewById(R.id.tv_order_Detail_deli_charge);
        tv_total = (TextView) findViewById(R.id.tv_order_Detail_total);
        btn_cancle = (RelativeLayout) findViewById(R.id.btn_order_detail_cancle);
        rv_detail_order = (RecyclerView) findViewById(R.id.rv_order_detail);
        rv_order_status = (RecyclerView) findViewById(R.id.rv_order_status);

        rv_detail_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_order_status.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        sale_id = getIntent().getStringExtra("sale_id");
        String total_rs = getIntent().getStringExtra("total");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
         status = getIntent().getStringExtra("status");
        String deli_charge = getIntent().getStringExtra("deli_charge");

        if (status.equals("0")) {
            btn_cancle.setVisibility(View.VISIBLE);
        } else {
            btn_cancle.setVisibility(View.GONE);
        }

        tv_total.setText(total_rs);
        tv_date.setText(getResources().getString(R.string.date) + date);
        preferences = getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");
        if (language.contains("spanish")) {
           time=time.replace("pm","م");
           time=time.replace("am","ص");
            tv_time.setText(getResources().getString(R.string.time) + time);

        }else {
            tv_time.setText(getResources().getString(R.string.time) + time);

        }
        if (deli_charge.equalsIgnoreCase("0"))
        {
            tv_delivery_charge.setText("FREE");

        }
        else {
            tv_delivery_charge.setText(getResources().getString(R.string.delivery_charge) + deli_charge);
        }



        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
            makeOrderStatusHistry(sale_id);
        } else {
            Toast.makeText(MyOrderDetail.this, "Error Network Issues", Toast.LENGTH_SHORT).show();
            // ((MainActivity) getApplication()).onNetworkConnectionChanged(false);
        }

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                btn_no=(Button)dialog.findViewById(R.id.btn_no);
                btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
                et_remark=(EditText) dialog.findViewById(R.id.et_remark);
                dialog.show();

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Session_management sessionManagement = new Session_management(MyOrderDetail.this);
                        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                        String remark=et_remark.getText().toString();
                        if(remark.isEmpty())
                        {
                            et_remark.setError("Please provide a reason");
                            et_remark.requestFocus();
                        }
                        else if(remark.length()<20)
                        {
                            et_remark.setError("Too short");
                            et_remark.requestFocus();

                        }
                        else
                        {
                            if (ConnectivityReceiver.isConnected()) {
                                makeDeleteOrderRequest(sale_id, user_id,remark);

                            }

                        }
                        // check internet connection
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });


                //finish();
            }
        });

    }

    // alertdialog for cancle order
    private void showDeleteDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyOrderDetail.this);
        alertDialog.setMessage(getResources().getString(R.string.cancle_order_note));
        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Session_management sessionManagement = new Session_management(MyOrderDetail.this);
                String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    //makeDeleteOrderRequest(sale_id, user_id);
                }

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetOrderDetailRequest(String sale_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.ORDER_DETAIL_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("details..", response.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    Toast.makeText(MyOrderDetail.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( MyOrderDetail.this,""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeDeleteOrderRequest(String sale_id, String user_id,String remark) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_delete_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);
        params.put("user_id", user_id);
        params.put("remark", remark);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DELETE_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        dialog.dismiss();
                        Toast.makeText(MyOrderDetail.this, "" + msg, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MyOrderDetail.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        // ((MainActivity) MyOrderDetail.this).onBackPressed();

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(MyOrderDetail.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( MyOrderDetail.this,""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
    private void makeOrderStatusHistry(final String sale_id) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.URL_ORDER_STATUS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("order_status", response.toString());
                loadingBar.dismiss();

                try {
                   if (response.getJSONArray("data").length()>0) {
                       Gson gson = new Gson();
                       Type listType = new TypeToken<List<OrderStatusModel>>() {
                       }.getType();

                       order_status_list = gson.fromJson(response.getJSONArray("data").toString(), listType);
//                    order_status_list.add(new OrderStatusModel("7",sale_id,"7",new SimpleDateFormat("yyyy-MM-dd").format(new Date()),"","","",""))

                       orderStatusAdapter = new OrderStatusAdapter((ArrayList<OrderStatusModel>) order_status_list, MyOrderDetail.this);
                       rv_order_status.setAdapter(orderStatusAdapter);
                       orderStatusAdapter.notifyDataSetChanged();

                       switch (status) {
                           case "0":
                               order_status_list.get(0).setChecked(true);
                               break;
                           case "1":
                               order_status_list.get(0).setChecked(true);
                               order_status_list.get(1).setChecked(true);
                               break;
                           case "2":
                               order_status_list.get(0).setChecked(true);
                               order_status_list.get(1).setChecked(true);
                               order_status_list.get(2).setChecked(true);
                               break;
                           case "4":
                               order_status_list.get(0).setChecked(true);
                               order_status_list.get(1).setChecked(true);
                               order_status_list.get(2).setChecked(true);
                               order_status_list.get(3).setChecked(true);
                               break;
                           case "3":
                               order_status_list.remove(1);
                               order_status_list.remove(2);
                               order_status_list.remove(3);
                               order_status_list.add(new OrderStatusModel(true, "4", sale_id, "4", "", "4", new SimpleDateFormat("yyyy-MM-dd").format(new Date()), ""));
                               break;


                       }
                       orderStatusAdapter.notifyDataSetChanged();
                   }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( MyOrderDetail.this,""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}

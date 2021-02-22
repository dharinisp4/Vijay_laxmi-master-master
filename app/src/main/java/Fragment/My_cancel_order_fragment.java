package Fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.My_Cancel_Order_adapter;
import Config.BaseURL;
import Model.My_Cancel_order_model;

import Module.Module;
import beautymentor.in.AppController;
import beautymentor.in.MainActivity;
import beautymentor.in.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonArrayRequest;
import util.Session_management;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 02,December,2019
 */
public class My_cancel_order_fragment extends Fragment {

    private RecyclerView rv_mycancel;

    private List<My_Cancel_order_model> my_order_modelList = new ArrayList<>();
    TabHost tHost;
   Dialog loadingBar ;
    Module module;

    ImageView no_orders ;
    String user_id ="";
    public My_cancel_order_fragment() {
    }

    private static String TAG = My_cancel_order_fragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cancel_order, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        no_orders = view.findViewById( R.id.no_order );
        module = new Module();
        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

//                    Fragment fm = new Home_fragment();
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_mycancel = (RecyclerView) view.findViewById(R.id.rv_mycancel);
        rv_mycancel.setLayoutManager(new LinearLayoutManager(getActivity()));

        Session_management sessionManagement = new Session_management(getActivity());
         user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        // check internet connection


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ConnectivityReceiver.isConnected())

        {
            makeGetOrderRequest(user_id);
        } else

        {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }
    }

    private void makeGetOrderRequest(String userid) {
        String tag_json_obj = "json_socity_req";
        loadingBar.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.GET_CANCEL_ORDERS, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
               loadingBar.dismiss();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_Cancel_order_model>>() {
                }.getType();

                my_order_modelList = gson.fromJson(response.toString(), listType);
                My_Cancel_Order_adapter myPendingOrderAdapter = new My_Cancel_Order_adapter(my_order_modelList);
                rv_mycancel.setAdapter(myPendingOrderAdapter);
                myPendingOrderAdapter.notifyDataSetChanged();

                if(response.length()<=0)
                {
                    no_orders.setVisibility(View.VISIBLE);
                    rv_mycancel.setVisibility(View.GONE);
                }
                else
                {
                    no_orders.setVisibility( View.GONE );
                    rv_mycancel.setVisibility( View.VISIBLE );
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               loadingBar.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    }

package Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Home_adapter;
import Adapter.View_time_adapter;
import Config.BaseURL;
import Model.Category_model;
import Module.Module;
import binplus.vijaylaxmi.AppController;
import binplus.vijaylaxmi.MainActivity;
import binplus.vijaylaxmi.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import util.Session_management;

public class View_time_fragment extends Fragment {

    private static String TAG = View_time_fragment.class.getSimpleName();

    private RecyclerView rv_time;
   Module module;
    private List<String> time_list = new ArrayList<>();
    private List<Category_model> category_modelList = new ArrayList<>();
    private Home_adapter adapter;

    private String getdate;
    Dialog loadingBar ;
    private Session_management sessionManagement;

    public View_time_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_list, container, false);
       module=new Module();
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery_time));
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);;

        sessionManagement = new Session_management(getActivity());

        rv_time = (RecyclerView) view.findViewById(R.id.rv_times);
        rv_time.setLayoutManager(new LinearLayoutManager(getActivity()));

        getdate = getArguments().getString("date");

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetTimeRequest(getdate);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        rv_time.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_time, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String gettime = time_list.get(position);

                sessionManagement.cleardatetime();

                sessionManagement.creatdatetime(getdate,gettime);

                ((MainActivity) getActivity()).onBackPressed();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetTimeRequest(String date) {

        // Tag used to cancel the request
        String tag_json_obj = "json_time_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("date",date);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TIME_SLOT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        for(int i=0;i<response.getJSONArray("times").length();i++) {
                            time_list.add(""+response.getJSONArray("times").get(i));
                        }

                        View_time_adapter adapter = new View_time_adapter(time_list);
                        rv_time.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( getActivity(),""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}

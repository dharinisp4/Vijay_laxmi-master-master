package Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Model.Support_info_model;
import Module.Module;
import binplus.vijaylaxmi.AppController;
import binplus.vijaylaxmi.MainActivity;
import binplus.vijaylaxmi.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class Terms_and_Condition_fragment extends Fragment {

    private static String TAG = Terms_and_Condition_fragment.class.getSimpleName();

    private TextView tv_info;
    Dialog loadingBar ;
    Module module;

    public Terms_and_Condition_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_terms_condition, container, false);


        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        module=new Module();
        tv_info = (TextView) view.findViewById(R.id.txt_terms);
//
//        String geturl = getArguments().getString("url");
//        //   String title = getArguments().getString("title");
//
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.condition));

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetInfoRequest() {
        loadingBar.show();


        String json_tag="json_request_about";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_TERMS_URL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try
                {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        JSONArray array=response.getJSONArray("data");
                        for(int i=0; i<array.length();i++)
                        {
                            JSONObject object=array.getJSONObject(i);
                            String support=object.getString("pg_descri");
                            tv_info.setText(Html.fromHtml(support));
                        }
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( getActivity(),""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }

}


package Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Model.Support_info_model;
import Module.Module;
import beautymentor.in.AppController;
import beautymentor.in.MainActivity;
import beautymentor.in.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class Contact_Us_fragment extends Fragment {

    private static String TAG = Contact_Us_fragment.class.getSimpleName();

    private TextView tv_info,tv_web,tv_number,tv_email,tv_address;
    Dialog loadingBar;
    Module module;
    String website;
    public Contact_Us_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_web = (TextView) view.findViewById(R.id.tv_web);
        tv_number = (TextView) view.findViewById(R.id.tv_number);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
    module=new Module();
//        String geturl = getArguments().getString("url");
//        //   String title = getArguments().getString("title");
//
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.contact));
//
//        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            getAppSettingData();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        tv_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWebsite(website);
            }
        });

        return view;
    }

    public void getAppSettingData()
    {
        loadingBar.show();
        String json_tag="json_app_tag";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_VERSTION_DATA, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try
                {
                    boolean sts=response.getBoolean("responce");

                    if(sts)
                    {
                        JSONObject object=response.getJSONObject("data");
                        tv_number.setText(Html.fromHtml(object.getString("contact_no")));
                        tv_web.setText(Html.fromHtml(object.getString("website")));
                        tv_address.setText(Html.fromHtml(object.getString("address")));
                        tv_email.setText(Html.fromHtml(object.getString("email")));
                        website=object.getString("website");


                    }
                    else
                    {
                        Toast.makeText(getActivity(),""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex)
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
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request,json_tag);
    }

    public void goToWebsite(String url){
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(Uri.parse(url));
        getActivity().startActivity(in);
    }
}


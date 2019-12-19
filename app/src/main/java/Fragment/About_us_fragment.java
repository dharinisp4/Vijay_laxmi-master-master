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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Model.Support_info_model;
import binplus.vijaylaxmi.MainActivity;
import binplus.vijaylaxmi.R;
import util.ConnectivityReceiver;

import static binplus.vijaylaxmi.AppController.TAG;

public class About_us_fragment extends Fragment {

    private static String TAG = About_us_fragment.class.getSimpleName();
//
    private TextView txt_info;
Dialog loadingBar ;
    public About_us_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        txt_info = (TextView) view.findViewById(R.id.txt_info);
//
//        String geturl = getArguments().getString("url");
//        //   String title = getArguments().getString("title");
//
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.nav_about));
//
//        // check internet connection
       if (ConnectivityReceiver.isConnected()) {
           makeGetInfoRequest();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    private void makeGetInfoRequest() {
    }


}


package Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Adapter.VideosAdapter;
import Config.BaseURL;
import Model.VideoModel;
import Module.Module;
import beautymentor.in.AppController;
import beautymentor.in.R;
import android.app.Fragment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import beautymentor.in.networkconnectivity.NetworkConnection;
import beautymentor.in.networkconnectivity.NoInternetConnection;
import util.CustomVolleyJsonRequest;
import util.Session_management;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllVideos extends Fragment {
    RecyclerView rv_videos;
    VideosAdapter videosAdapter;
    SwipeRefreshLayout swipeRefreshLayout ;
    RelativeLayout rel_no_items;
    String u_id = "", u_name = "";

    Dialog loadingBar;

    Module module;
    ArrayList<VideoModel> video_list;
    String user_type;

    Session_management sessionManagment;
    boolean is_refreshing=false;
    private final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

    public AllVideos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_videos, container, false);
        rv_videos = view.findViewById(R.id.rv_videos);
        rel_no_items = view.findViewById(R.id.rel_no_items);

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setRefreshing(false);


        sessionManagment = new Session_management(getActivity());

        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView(R.layout.progressbar);
        loadingBar.setCanceledOnTouchOutside(false);
        module = new Module();
        video_list = new ArrayList<>();


      if (NetworkConnection.connectionChecking(getActivity()))
      {
          getVideos();
      }
      else
      {
          Intent intent = new Intent(getActivity(), NoInternetConnection.class);
          getActivity().startActivity(intent);
      }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                is_refreshing=true;
                getVideos();
            }
        });
        return view;
    }




    public void getVideos ()
    {
        loadingBar.show();
        video_list.clear();
        HashMap<String,String> params = new HashMap<>();
        CustomVolleyJsonRequest jsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_VIDEOS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                if(is_refreshing)
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.e("getVideo",response.toString());
                try {
                    JSONArray v_arr = response.getJSONArray("data");
                    for (int i=0 ;i<v_arr.length();i++)
                    {
                        JSONObject object = v_arr.getJSONObject(i);
                        VideoModel model = new VideoModel();
                        model.setId(object.getString("id"));
                        model.setTitle(object.getString("title"));
                        model.setDescription(object.getString("description"));
                        model.setVid_url(object.getString("vid_url"));
                        model.setVid_img(object.getString("vid_img"));
                        model.setStatus(object.getString("status"));
                        if (model.getStatus().equals("1")) {
                            video_list.add(model);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (video_list.size()>0)
                {
                    rel_no_items.setVisibility(View.GONE);
                    rv_videos.setVisibility(View.VISIBLE);
                    videosAdapter = new VideosAdapter(getActivity(),video_list);
                    rv_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_videos.setAdapter(videosAdapter);
                }
                else
                {
                    rel_no_items.setVisibility(View.VISIBLE);
                    rv_videos.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg = module.VolleyErrorMessage(error);
                if (msg.isEmpty())
                {

                }
                else
                {
                    Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();}

            }
        });

        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

//    private final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

}

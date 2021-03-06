package Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Shop_Now_adapter;
import Adapter.TopBrandsAdapter;
import Config.BaseURL;
import Model.BrandModel;
import Model.ShopNow_model;
import Module.Module;
import beautymentor.in.AppController;
import beautymentor.in.MainActivity;
import beautymentor.in.No_intenet_Activity;
import beautymentor.in.R;
import beautymentor.in.networkconnectivity.NoInternetConnection;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;


public class Shop_Now_fragment extends Fragment {
    private static String TAG = Shop_Now_fragment.class.getSimpleName();
    private RecyclerView rv_items;
    private List<ShopNow_model> category_modelList = new ArrayList<>();
    private List<BrandModel> brand_modelList = new ArrayList<>();
    private Shop_Now_adapter adapter;
    private boolean isSubcat = false;
    String getid;
    String getcat_title;
    Dialog loadingBar;
    ImageView no_product;
    Module module;
    private TopBrandsAdapter topBrandsAdapter;
    String type ="";
    TextView tv_title;

    public Shop_Now_fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_now, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        setHasOptionsMenu(true);
       module=new Module();
        no_product=(ImageView)view.findViewById(R.id.no_product);
       tv_title=(TextView) view.findViewById(R.id.firebase);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.shop_now));

        type = getArguments().getString("type");
        if (type.equalsIgnoreCase("category"))
        {
            tv_title.setText("Shop By Category");
        }
        else
        {
            tv_title.setText("Shop By Brands");
        }


        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_items.setLayoutManager(gridLayoutManager);
       // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);
        rv_items.setItemViewCacheSize(10);
        rv_items.setDrawingCacheEnabled(true);
        rv_items.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);


        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();

                if (type.equalsIgnoreCase("brand")){
                args.putString("brand_id",brand_modelList.get(position).getBrand_id());
                args.putString("cat_title",brand_modelList.get(position).getBrand_name());}
                else
                {  getid = category_modelList.get(position).getId();
                    getcat_title = category_modelList.get(position).getTitle();
                    args.putString("cat_id", getid);
                    args.putString("cat_title", getcat_title);
                }

                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        if (ConnectivityReceiver.isConnected()) {
            if (type.equalsIgnoreCase("category")) {
                makeGetCategoryRequest();
            }
            else
            {
                makeGetBrandsRequest();
            }

        }
        else
        {
            startActivity(new Intent(getActivity(), NoInternetConnection.class));
        }

        return view;
    }


    private void makeGetCategoryRequest() {
        loadingBar.show();
        String tag_json_obj = "json_category_req";

        isSubcat = false;

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<ShopNow_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new Shop_Now_adapter(category_modelList);

                            rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if(category_modelList.size()==0)
                            {
                                rv_items.setVisibility(View.GONE);
                                no_product.setVisibility(View.VISIBLE);
                            }
                            loadingBar.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
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
                    Toast.makeText( getActivity(),""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void makeGetBrandsRequest() {
        loadingBar.show();
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("brand_id", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_BRANDS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "brands" +response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            loadingBar.dismiss();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<BrandModel>>() {
                            }.getType();
                            brand_modelList = gson.fromJson(response.getString("data"), listType);
                            topBrandsAdapter= new TopBrandsAdapter(brand_modelList,getActivity());
                           rv_items.setAdapter( topBrandsAdapter );
                            //   rv_items.setAdapter(adapter);
                            topBrandsAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
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
                    Toast.makeText( getActivity(),""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


}

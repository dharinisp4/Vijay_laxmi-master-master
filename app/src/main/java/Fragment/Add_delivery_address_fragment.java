package Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import Module.Module;
import beautymentor.in.AppController;
import beautymentor.in.MainActivity;
import beautymentor.in.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.Session_management;


public class Add_delivery_address_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Add_delivery_address_fragment.class.getSimpleName();

    private EditText et_phone, et_name,  et_address;
    private AutoCompleteTextView et_pin;
    Module module;
    private RelativeLayout btn_update;
    private TextView tv_phone, tv_name, tv_pin, tv_address, tv_socity, btn_socity;
    private String getsocity = "";
    LinearLayout lay_office,lay_home;
    private Session_management sessionManagement;
  ImageView image_office,image_home;
    private boolean isEdit = false;
    Dialog loadingBar ;
    int add_type=0;
    private String getlocation_id;
    private String[] pincodes ={"452001","452002","452003","452004","452005","452006","452007","452008","452009","452010","452011","452012","452013"
    ,"452014","452015","452016","452017","452018","452019","452020"};

    public Add_delivery_address_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.add));
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);

        module=new Module();
        sessionManagement = new Session_management(getActivity());
        image_home=(ImageView)view.findViewById(R.id.image_home);
        image_office=(ImageView)view.findViewById(R.id.image_office);
        lay_home=(LinearLayout)view.findViewById(R.id.lay_home);
        lay_office=(LinearLayout)view.findViewById(R.id.lay_office);
        et_phone = (EditText) view.findViewById(R.id.et_add_adres_phone);
        et_name = (EditText) view.findViewById(R.id.et_add_adres_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_add_adres_phone);
        tv_name = (TextView) view.findViewById(R.id.tv_add_adres_name);
        tv_pin = (TextView) view.findViewById(R.id.tv_add_adres_pin);
        et_pin = (AutoCompleteTextView) view.findViewById(R.id.et_add_adres_pin);
        et_address = (EditText) view.findViewById(R.id.et_add_adres_home);
        tv_address =(TextView)view.findViewById( R.id.tv_add );
        //tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_update = (RelativeLayout) view.findViewById(R.id.btn_add_adres_edit);
      //  btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);

        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        //String getsocity_id = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);
        String getsocity_id = "1";
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,pincodes);
//        et_pin.setThreshold( 1 );
        et_pin.setAdapter( arrayAdapter );
        et_pin.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_pin.showDropDown();
                return false;
            }
        } );


        Bundle args = getArguments();

        if (args != null) {
            getlocation_id = getArguments().getString("location_id");
            String get_name = getArguments().getString("name");
            String get_phone = getArguments().getString("mobile");
            String get_pine = getArguments().getString("pincode");
            String get_socity_id = getArguments().getString("socity_id");
            String get_socity_name = getArguments().getString("socity_name");
            String get_add = getArguments().getString("house");
            String get_address_type = getArguments().getString("address_type");

            if (TextUtils.isEmpty(get_name) && get_name == null) {
                isEdit = false;
            } else {
                isEdit = true;

             //   Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();

                et_name.setText(get_name);
                et_phone.setText(get_phone);
                et_pin.setText(get_pine);
                et_address.setText(get_add);
                if(get_address_type.equals("home"))
                {
                    image_home.setVisibility(View.VISIBLE);
                    image_office.setVisibility(View.GONE);
                    add_type=1;
                }
                else if(get_address_type.equals("office"))
                {
                    image_office.setVisibility(View.VISIBLE);
                    image_home.setVisibility(View.GONE);
                    add_type=2;
                }
              //  btn_socity.setText(get_socity_name);

               // sessionManagement.updateSocity(get_socity_name, get_socity_id);
            }
        }

        lay_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_office.setVisibility(View.VISIBLE);
                image_home.setVisibility(View.GONE);
                add_type=2;
            }
        });
        lay_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_home.setVisibility(View.VISIBLE);
                image_office.setVisibility(View.GONE);
                add_type=1;
            }
        });
       /* if (!TextUtils.isEmpty(getsocity_name)) {

            btn_socity.setText(getsocity_name);
            sessionManagement.updateSocity(getsocity_name, getsocity_id);
        }*/

        btn_update.setOnClickListener(this);
       // btn_socity.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        } /*else if (id == R.id.btn_add_adres_socity) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/

//                Bundle args = new Bundle();
//                Fragment fm = new Socity_fragment();
//                //args.putString("pincode", getpincode);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/


    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_address.setText("Address");
      //  tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_address.setTextColor(getResources().getColor(R.color.dark_gray));
       // tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpin = et_pin.getText().toString();
        String getadd = et_address.getText().toString();
       // String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);
        String getsocity = "1";

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            et_phone.setError("Enter Mobile Number");
           // tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            et_phone.setError(getResources().getString(R.string.phone_too_short));
            //tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            et_name.setError("Enter Name");
           // tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpin)) {
            et_pin.setError("Enter PIN Code");
           // tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_pin;
            cancel = true;
        }

        if (TextUtils.isEmpty(getadd)) {
            et_address.setError("Enter Address");
            //tv_address.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_address;
            cancel = true;
        }

        if(add_type==0)
        {
            Toast.makeText(getActivity(),"Please select any one option",Toast.LENGTH_LONG).show();
            cancel = true;
        }


        /*if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                String address_type="";
                if(add_type==1)
                {
                    address_type="home";
                }
                else if(add_type==2)
                {
                    address_type="office";
                }
                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    if (isEdit) {
                        String pin=et_pin.getText().toString();
                       boolean ch_pin= checkPinCode(pincodes,pin);
                       if(ch_pin)
                       {
                           makeEditAddressRequest(getlocation_id, getpin,getsocity, getadd, getname, getphone,address_type);
                       }
                       else
                       {
                              Toast.makeText(getActivity(),"We don't deliver this pin code \n Please select any one in options.",Toast.LENGTH_LONG).show();
                       }

                    } else {

                        String pin=et_pin.getText().toString();
                        boolean ch_pin= checkPinCode(pincodes,pin);
                        if(ch_pin)
                        {
                            makeAddAddressRequest(user_id, getpin, getsocity,getadd, getname, getphone,address_type);
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"We don't deliver this pin code \n Please select any one in options.",Toast.LENGTH_LONG).show();
                        }


                    }
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(String user_id, String pincode,String socity_id,
                                       String address, String receiver_name, String receiver_mobile,String address_type) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pincode", pincode);
        params.put("socity_id", "11");
        params.put("house_no", address);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
        params.put("address_type", address_type);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        ((MainActivity) getActivity()).onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadingBar.dismiss();
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

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeEditAddressRequest(String location_id, String pincode,String socity_id,
                                        String add, String receiver_name, String receiver_mobile,String address_type) {
        loadingBar.show();

        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("location_id", location_id);
        params.put("pincode", pincode);
        params.put("socity_id", "11");
        params.put("house_no", add);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
        params.put("address_type", address_type);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.EDIT_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        ((MainActivity) getActivity()).onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadingBar.dismiss();
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

    public boolean checkPinCode(String[] pincodes,String value)
    {
        boolean tr=false;
        for (String element : pincodes) {
            if (element.equals(value)) {
                tr=true;
                break;
            }
            else

                tr=false;

        }
      return tr;
    }

}

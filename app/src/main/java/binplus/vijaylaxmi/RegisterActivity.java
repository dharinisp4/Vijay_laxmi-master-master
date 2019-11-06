package binplus.vijaylaxmi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class
RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getSimpleName();

    private EditText et_phone, et_name, et_password, et_email ,et_saloon_name,et_city,et_address,et_propretier,et_staff,et_years ,et_turnover;
    private RelativeLayout btn_register;
    private AutoCompleteTextView et_pin;
    private TextView tv_phone, tv_name, tv_password, tv_email ,tv_saloon_name , tv_city ,tv_add ,tv_prop ,tv_staff,tv_years,tv_turnover,tv_pin;
    private LinearLayout linear_saloon ;
    private CheckBox check_user , check_owner;
    private RadioGroup radioGroup_checked ;
    String getsaloon_name = "";
    String getsaloon_add = "";
    String getturn_over = "";
    String getstaff = "";
    String getsaloon_city = "";
    String getyear = "";
    String getprop = "";
    String getpincode = "";

    private String [] pincodes ={"202002","2222222" , "284001","248001"};
    @Override
    protected void attachBaseContext(Context newBase) {

        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title

        setContentView(R.layout.activity_register);

        et_phone = (EditText) findViewById(R.id.et_reg_phone);
        et_name = (EditText) findViewById(R.id.et_reg_name);
        et_password = (EditText) findViewById(R.id.et_reg_password);
        et_email = (EditText) findViewById(R.id.et_reg_email);
        tv_password = (TextView) findViewById(R.id.tv_reg_password);
        tv_phone = (TextView) findViewById(R.id.tv_reg_phone);
        tv_name = (TextView) findViewById(R.id.tv_reg_name);
        tv_email = (TextView) findViewById(R.id.tv_reg_email);
        tv_saloon_name=(TextView) findViewById(R.id.tv_saloon_name);
        tv_prop=(TextView) findViewById(R.id.tv_proprietor);
        tv_add =(TextView) findViewById(R.id.tv_address);
        tv_city=(TextView) findViewById(R.id.tv_city);
        tv_pin =(TextView) findViewById(R.id.tv_pin);
        linear_saloon=(LinearLayout)findViewById( R.id.linear_saloon );
        et_saloon_name=(EditText)findViewById( R.id.et_saloon_name );
        et_pin =(AutoCompleteTextView) findViewById( R.id.et_pin );
        et_city=(EditText)findViewById( R.id.et_city );
        et_address=(EditText)findViewById( R.id.et_address );
        et_propretier=(EditText)findViewById( R.id.et_proprietor );
        et_staff =(EditText)findViewById( R.id.et_staff);
        et_years =(EditText)findViewById( R.id.et_year );
        et_turnover=(EditText)findViewById( R.id.et_turnover );
        radioGroup_checked=(RadioGroup)findViewById( R.id.radio_checked );
        check_owner=(CheckBox)findViewById( R.id.chk_owner );
        check_user=(CheckBox)findViewById( R.id.chk_user );

        check_user.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true) {
                    check_owner.setChecked( false );
                    linear_saloon.setVisibility( View.GONE );
                }
                else
                {
                    check_owner.setChecked( true );
                    linear_saloon.setVisibility( View.VISIBLE );
                }
            }
        } );


        check_owner.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    check_user.setChecked( false );
                    linear_saloon.setVisibility( View.VISIBLE );
                }
                else
                {
                    check_user.setChecked( true );
                    linear_saloon.setVisibility( View.GONE );
                }

            }
        } );


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,pincodes);
       et_pin.setOnTouchListener( new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {
               et_pin.showDropDown();
               return false;
           }
       } );
        et_pin.setAdapter( arrayAdapter );
    btn_register = (RelativeLayout) findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getphone = et_phone.getText().toString();
                String  getname = et_name.getText().toString();
                String  getpassword = et_password.getText().toString();
                String  getemail = et_email.getText().toString();
                Toast.makeText( RegisterActivity.this,"" +getphone ,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void attemptRegister() {


        tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        tv_password.setText(getResources().getString(R.string.tv_login_password));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_password.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));

   String getphone = et_phone.getText().toString();
     String  getname = et_name.getText().toString();
     String  getpassword = et_password.getText().toString();
    String  getemail = et_email.getText().toString();

        if (check_owner.isChecked())
        {
            getsaloon_name = et_saloon_name.getText().toString();
            getsaloon_add = et_address.getText().toString();
            getturn_over = et_turnover.getText().toString();
            getstaff = et_staff.getText().toString();
            getsaloon_city = et_city.getText().toString();
            getyear = et_years.getText().toString();
            getprop = et_propretier.getText().toString();
            getpincode = et_pin.getText().toString();
        }



        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.black));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.black));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.black));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.black));
            focusView = et_password;
            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(R.string.password_too_short));
            tv_password.setTextColor(getResources().getColor(R.color.black));
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(R.string.invalide_email_address));
            tv_email.setTextColor(getResources().getColor(R.color.black));
            focusView = et_email;
            cancel = true;
        }

//        if (TextUtils.isEmpty(getpincode))
//        {
//            tv_pin.setTextColor(getResources().getColor(R.color.black));
//            focusView = et_pin;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(getsaloon_name))
//        {
//            tv_saloon_name.setTextColor(getResources().getColor(R.color.black));
//            focusView = et_pin;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(getsaloon_add))
//        {
//            tv_add.setTextColor(getResources().getColor(R.color.black));
//            focusView = et_pin;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(getsaloon_city))
//        {
//            tv_city.setTextColor(getResources().getColor(R.color.black));
//            focusView = et_pin;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(getprop))
//        {
//            tv_prop.setTextColor(getResources().getColor(R.color.black));
//            focusView = et_pin;
//            cancel = true;
//        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {
                //  Toast.makeText( RegisterActivity.this,"sada",Toast )
                makeRegisterRequest(getname, getphone, getemail, getpassword,getsaloon_name,getprop,getsaloon_city,getstaff,getyear,getturn_over,getsaloon_add,getpincode);
            }
            else
            {

            }
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeRegisterRequest(String name, String mobile,String email, String password ,
                                     String saloon_name , String propritior ,String saloon_city , String staff ,
                                     String establishyear ,String turn_over, String saloon_add ,String pincode ) {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);
        params.put ("saloon_name",saloon_name);
        params.put("proprietor",propritior);
        params.put("saloon_city",saloon_city);
        params.put("staff",staff);
        params.put("establishyear",establishyear);
        params.put("turn_over",turn_over);
        params.put("saloon_address",saloon_add);
        params.put("saloon_pin",pincode);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        btn_register.setEnabled(false);

                    } else {
                        String error = response.getString("error");
                        btn_register.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}

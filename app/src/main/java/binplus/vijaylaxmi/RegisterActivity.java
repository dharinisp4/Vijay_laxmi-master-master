package binplus.vijaylaxmi;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import Module.Module;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class
RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getSimpleName();
   String number="";
    private EditText et_phone, et_name, et_password, et_email,et_cpass,et_saloon_name,et_city,et_address,et_propretier,et_staff,et_years ,et_turnover;
    private RelativeLayout btn_register;
    private AutoCompleteTextView et_pin;
    private TextView tv_phone, tv_name, tv_password, tv_email,tv_cpass ,tv_saloon_name , tv_city ,tv_add ,tv_prop ,tv_staff,tv_years,tv_turnover,tv_pin;
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
    String getname ,getphone ,getpassword,getemail ,getcpass;
    Dialog loadingBar ;
    int flag=1;

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
        loadingBar=new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);

        number=getIntent().getStringExtra("number");
        et_phone = (EditText) findViewById(R.id.et_reg_phone);
        et_name = (EditText) findViewById(R.id.et_reg_name);
        et_password = (EditText) findViewById(R.id.et_reg_password);
        et_cpass = (EditText) findViewById(R.id.et_reg_cpassword);
        et_email = (EditText) findViewById(R.id.et_reg_email);
        tv_password = (TextView) findViewById(R.id.tv_reg_password);
        tv_cpass = (TextView) findViewById(R.id.tv_reg_cpassword);
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

        et_phone.setText(number);
        et_phone.setEnabled(false);
        check_user.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true) {
                    check_owner.setChecked( false );
                    linear_saloon.setVisibility( View.GONE );
                    flag=1;

//                    check_user.setChecked( true );
//                    linear_saloon.setVisibility( View.GONE );
//                    getphone = et_phone.getText().toString();
//                    getname = et_name.getText().toString();
//                    getpassword = et_password.getText().toString();
//                    getemail = et_email.getText().toString();
//                    getcpass =et_cpass.getText().toString();
//                    getsaloon_name = "";
//                    getsaloon_add = "";
//                    getturn_over = "";
//                    getstaff = "";
//                    getsaloon_city = "";
//                    getyear = "";
//                    getprop = "";
//                    getpincode = "";
                }
                else
                {
                    check_owner.setChecked( true );
                    linear_saloon.setVisibility( View.VISIBLE );
                    flag=2;
//                    getphone = et_phone.getText().toString();
//                    getname = et_name.getText().toString();
//                    getpassword = et_password.getText().toString();
//                    getcpass = et_cpass.getText().toString();
//                    getemail = et_email.getText().toString();
//                    getsaloon_name = et_saloon_name.getText().toString();
//                    getsaloon_add = et_address.getText().toString();
//                    getturn_over = et_turnover.getText().toString();
//                    getstaff = et_staff.getText().toString();
//                    getsaloon_city = et_city.getText().toString();
//                    getyear = et_years.getText().toString();
//                    getprop = et_propretier.getText().toString();
//                    getpincode = et_pin.getText().toString();
                }
            }
        } );


        check_owner.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    check_user.setChecked( false );
                    linear_saloon.setVisibility( View.VISIBLE );
                    flag=2;
//                    getphone = et_phone.getText().toString();
//                    getname = et_name.getText().toString();
//                    getpassword = et_password.getText().toString();
//                    getcpass = et_cpass.getText().toString();
//                    getemail = et_email.getText().toString();
//                    getsaloon_name = et_saloon_name.getText().toString();
//                    getsaloon_add = et_address.getText().toString();
//                    getturn_over = et_turnover.getText().toString();
//                    getstaff = et_staff.getText().toString();
//                    getsaloon_city = et_city.getText().toString();
//                    getyear = et_years.getText().toString();
//                    getprop = et_propretier.getText().toString();
//                    getpincode = et_pin.getText().toString();
                }
                else
                {
                    check_user.setChecked( true );
                    linear_saloon.setVisibility( View.GONE );
                    flag=1;
//                   getphone = et_phone.getText().toString();
//                   getname = et_name.getText().toString();
//                     getpassword = et_password.getText().toString();
//                    getemail = et_email.getText().toString();
//                    getcpass =et_cpass.getText().toString();
//                    getsaloon_name = "";
//                     getsaloon_add = "";
//                  getturn_over = "";
//                    getstaff = "";
//                    getsaloon_city = "";
//                    getyear = "";
//                 getprop = "";
//                    getpincode = "";
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

                    getphone = et_phone.getText().toString();
                    getname = et_name.getText().toString();
                    getpassword = et_password.getText().toString();
                    getemail = et_email.getText().toString();
                    getcpass =et_cpass.getText().toString();

                    if(TextUtils.isEmpty(getname))
                    {
                        et_name.setError( "Enter Name" );
                        et_name.requestFocus();
                    }
                    else if(TextUtils.isEmpty(getemail))
                    {
                        et_email.setError( "Enter email address" );
                        et_email.requestFocus();
                    }
                    else if(TextUtils.isEmpty(getphone))
                    {
                        et_phone.setError( "Enter phone number" );
                        et_phone.requestFocus();
                    }else if(TextUtils.isEmpty(getpassword))
                    {
                        et_password.setError( "Enter password" );
                        et_password.requestFocus();
                    }else if(TextUtils.isEmpty(getcpass))
                    {
                        et_cpass.setError( "Enter confirm password" );
                        et_cpass.requestFocus();
                    } else if(!isEmailValid(getemail))
                    {
                        et_email.setError( "Invalid Email" );
                        et_email.requestFocus();
                    }
                    else if(!isPhoneValid( getphone ))
                    {
                        et_phone.setError( "Invalid phone number" );
                        et_phone.requestFocus();
                    }
                    else if(!isPasswordValid( getpassword ))
                    {
                        et_password.setError( "Invalid password" );
                        et_password.requestFocus();
                    }
                    else if(!isPasswordValid( getcpass ))
                    {
                        et_cpass.setError( "Invalid confirm password" );
                        et_cpass.requestFocus();
                    }
                    else
                    {
                        if(flag==1)
                        {
                            getsaloon_name = "";
                            getsaloon_add = "";
                            getturn_over = "";
                            getstaff = "";
                            getsaloon_city = "";
                            getyear = "";
                            getprop = "";
                            getpincode = "";

                            makeRegisterRequest( getname, getphone, getemail, getpassword, getsaloon_name, getprop, getsaloon_city, getstaff, getyear, getturn_over, getsaloon_add, getpincode );
                        }
                        else if(flag==2)
                        {
                            getsaloon_name = et_saloon_name.getText().toString();
                            getsaloon_add = et_address.getText().toString();
                            getturn_over = et_turnover.getText().toString();
                            getstaff = et_staff.getText().toString();
                            getsaloon_city = et_city.getText().toString();
                            getyear = et_years.getText().toString();
                            getprop = et_propretier.getText().toString();
                            getpincode = et_pin.getText().toString();

                            if(TextUtils.isEmpty(getsaloon_name))
                            {
                                et_saloon_name.setError("Enter Saloon Name");
                                et_saloon_name.requestFocus();
                            }
                            else if(TextUtils.isEmpty(getprop))
                            {
                                et_propretier.setError("Enter Proprietor");
                                et_propretier.requestFocus();
                            }else if(TextUtils.isEmpty(getsaloon_city))
                            {
                                et_city.setError("Enter Saloon city");
                                et_city.requestFocus();
                            }else if(TextUtils.isEmpty(getsaloon_add))
                            {
                                et_address.setError("Enter Saloon address");
                                et_address.requestFocus();
                            }else if(TextUtils.isEmpty(getpincode))
                            {
                                et_pin.setError("Enter Pin code");
                                et_pin.requestFocus();
                            }
                            else {
                                makeRegisterRequest( getname, getphone, getemail, getpassword, getsaloon_name, getprop, getsaloon_city, getstaff, getyear, getturn_over, getsaloon_add, getpincode );
                            }


                        }

//                        Toast.makeText( RegisterActivity.this,"name : "+getname+"\n phone :"+getphone+"\n email :"+getemail+"\n pass :"+getpassword
//                                +"\n s_name :"+getsaloon_name+"\n p :"+getprop+"\n city :"+getsaloon_city+"\n staff :"+getstaff+"\n year :"+getyear+"\n tn :"+getturn_over+"\n add :"+getsaloon_add+"\n pin :"+getpincode,Toast.LENGTH_LONG ).show();


                    }





            }
        });
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
    private void makeRegisterRequest(String name, String mobile, String email, String password ,
                                     final String saloon_name , String propritior , String saloon_city , String staff ,
                                     String establishyear , String turn_over, String saloon_add , String pincode ) {

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
                Toast.makeText(RegisterActivity.this, "" + saloon_name, Toast.LENGTH_SHORT).show();
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
                String errormsg = Module.VolleyErrorMessage(error);
                Toast.makeText( RegisterActivity.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}

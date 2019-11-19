package binplus.vijaylaxmi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import Module.Module;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import Config.BaseURL;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = ForgotActivity.class.getSimpleName();
    private static final long START_TIME_IN_MILLI=120000;
    boolean mmTimerRunning;
    private long mTimeLeftINMILLIS=START_TIME_IN_MILLI;
    public String otp="";
    CountDownTimer countDownTimer;
    public static String number="";
    LinearLayout lin_verify_otp,lin_send_otp;
    private RelativeLayout btn_continue,btnVerify;
    private EditText et_mobile,et_verify_otp;
    private TextView tv_countdown;
    String lan;
    String type="";
    SharedPreferences preferences;
   Dialog loadingBar;
    @Override
    protected void attachBaseContext(Context newBase) {



        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title

        setContentView(R.layout.activity_forgot);
        loadingBar=new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        // Call the function callInstamojo to start payment here

        type=getIntent().getStringExtra("type");
        lin_verify_otp=(LinearLayout)findViewById(R.id.lin_verify_otp);
        lin_send_otp=(LinearLayout)findViewById(R.id.lin_send_otp);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_verify_otp = (EditText) findViewById(R.id.et_verify_otp);
        tv_countdown = (TextView) findViewById(R.id.tv_countdown);
        btn_continue = (RelativeLayout) findViewById(R.id.btnContinue);
        btnVerify = (RelativeLayout) findViewById(R.id.btnVerify);

        btn_continue.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        preferences = getSharedPreferences("lan", MODE_PRIVATE);
        lan=preferences.getString("language","");


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnContinue) {
            attemptForgot();
        }
        else if(id == R.id.btnVerify)
        {
             verifyOTP();
        }
    }

    private void verifyOTP() {

        String otp_code=et_verify_otp.getText().toString().trim();

        if(otp_code.isEmpty())
        {
            et_verify_otp.setError("Enter OTP");
            et_verify_otp.requestFocus();
        }
        else
        {
            String timout=tv_countdown.getText().toString();

            if(timout.equals("timeout"))
            {
                Toast.makeText(ForgotActivity.this,"Timeout",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(otp_code.equals(otp))
                {
                    number=et_mobile.getText().toString().trim();
                    if(type.equals("l"))
                    {
                        Toast.makeText(ForgotActivity.this,"Reset",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ForgotActivity.this,ReserPasswordActivity.class);
                        intent.putExtra("number",number);
                        startActivity(intent);
                        finish();
                    }
                    else if(type.equals("r"))
                    {
                        Toast.makeText(ForgotActivity.this,"Register",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ForgotActivity.this,RegisterActivity.class);
                        intent.putExtra("number",number);
                        startActivity(intent);
                        finish();
                    }
                    // Toast.makeText(PasswordActivity.this,"Verification is completed...",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(ForgotActivity.this,"Invalid OTP",Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    private void attemptForgot() {


        String getemail = et_mobile.getText().toString();
        String mobile = et_mobile.getText().toString();
        int first = mobile.charAt(0);

        if(first<6 )
        {
            et_mobile.setError( "invalid mobile no" );
            et_mobile.requestFocus();
        }

        else if(mobile.contains( "+" ))
        {
            et_mobile.setError( "invalid mobile no" );
            et_mobile.requestFocus();
        }
        else if (mobile.length()<9)
        {
            et_mobile.setError( "invalid mobile no" );
            et_mobile.requestFocus();
        }

//        boolean cancel = false;
//        View focusView = null;
//
//        if (TextUtils.isEmpty(getemail)) {
//            et_mobile.setError("Enter Mobile Number");
//
//            focusView = et_mobile;
//            cancel = true;
//        } else if (!isPhoneValid(getemail)) {
//            et_mobile.setError("Invalid Mobile Number");
//            focusView = et_mobile;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            if (focusView != null)
//                focusView.requestFocus();
//        }
      else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {


                if(type.equals("l"))
                {
                    otp=getRandomKey(6);
                    makeForgotRequest(getemail,otp);
                }
                else if(type.equals("r"))
                {
                    otp=getRandomKey(6);
                   makeVerificationRequest(getemail,otp);
                }
            }
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeForgotRequest(String phone,String otp) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_forgot_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", phone);
        params.put("otp", otp);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GENOTP_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");

                   // String error_arb=response.getString("error_arb");
                    if (status) {

                         lin_send_otp.setVisibility(View.GONE);
                         lin_verify_otp.setVisibility(View.VISIBLE);
                         statTimer();
//                        Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(ForgotActivity.this, LoginActivity.class);
//                        startActivity(i);
//                        finish();

                    } else {
                        String error = response.getString("error");

                            Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                       /* else {
                            Toast.makeText(ForgotActivity.this, "" + error_arb, Toast.LENGTH_SHORT).show();

                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String errormsg = Module.VolleyErrorMessage(error);
                Toast.makeText( ForgotActivity.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void makeVerificationRequest(String phone,String otp) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_forgot_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", phone);
        params.put("otp", otp);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.VERIFY_MOBILE_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");

                   // String error_arb=response.getString("error_arb");
                    if (status) {

                         lin_send_otp.setVisibility(View.GONE);
                         lin_verify_otp.setVisibility(View.VISIBLE);
                         statTimer();
//                        Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(ForgotActivity.this, LoginActivity.class);
//                        startActivity(i);
//                        finish();

                    } else {
                        String error = response.getString("error");

                            Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();

                       /* else {
                            Toast.makeText(ForgotActivity.this, "" + error_arb, Toast.LENGTH_SHORT).show();

                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String errormsg = Module.VolleyErrorMessage(error);
                Toast.makeText( ForgotActivity.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    public static String getRandomKey(int i)
    {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }

    public void statTimer()
    {
        countDownTimer=new CountDownTimer(mTimeLeftINMILLIS,1000) {
            @Override
            public void onTick(long l) {

                mTimeLeftINMILLIS=l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                otp="";
                tv_countdown.setText("timeout");
                tv_countdown.setTextColor(Color.RED);


            }
        }.start();
        mmTimerRunning=true;
    }

    private void updateCountDownText() {
        int minutes=(int)(mTimeLeftINMILLIS/1000)/60;
        int seconds=(int)(mTimeLeftINMILLIS/1000)%60;
        String timeLeftForamatedd=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        tv_countdown.setText(timeLeftForamatedd);

    }

}

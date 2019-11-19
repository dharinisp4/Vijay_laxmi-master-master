package binplus.vijaylaxmi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;

import Module.Module;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;

import static com.android.volley.VolleyLog.TAG;

public class ReserPasswordActivity extends Activity  {
   Dialog loadingBar ;
   EditText et_password,et_con_pass;
   RelativeLayout btn_reset;
   String number="";
    @Override
    protected void attachBaseContext(Context newBase) {



        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        loadingBar=new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
       number=getIntent().getStringExtra("number");
        et_con_pass=(EditText)findViewById(R.id.et_con_pass);
        et_password=(EditText)findViewById(R.id.et_password);

        btn_reset=(RelativeLayout)findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_password.getText().toString().length() == 0) {
                    et_password.setError("Enter Password");
                    et_password.requestFocus();
                } else if (et_password.getText().toString().length() < 6 || et_password.getText().toString().length() > 32) {
                    et_password.setError("Password length must be between 6-32 characters");
                    et_password.requestFocus();
                } else if (!isValidPass(et_password.getText().toString().trim())) {
                    et_password.setError("Please enter password with letter & numbers");
                    et_password.requestFocus();
                } else if (et_con_pass.getText().toString().equals("")) {
                    et_con_pass.setError("Enter Confirm Password");
                    et_con_pass.requestFocus();
                } else if (!et_con_pass.getText().toString().equals(et_password.getText().toString())) {
                    et_con_pass.setError("Password & Confirm Password doesn't match");
                    et_con_pass.requestFocus();
                }
                else
                {
                    String pass=et_password.getText().toString().trim();
                    String conpass=et_con_pass.getText().toString().trim();

                    update_password(number,pass);




                }

            }
        });




    }

    private void update_password(String number, String pass) {
        loadingBar.show();
        String json_tag="json_reset_tag";
        HashMap<String,String> map=new HashMap<>();
        map.put("user_phone",number);
        map.put("user_password",pass);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.FORGOT_URL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                     loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        String error = response.getString("error");

                        Toast.makeText(ReserPasswordActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ReserPasswordActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = response.getString("error");

                        Toast.makeText(ReserPasswordActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                Toast.makeText( ReserPasswordActivity.this,""+ errormsg,Toast.LENGTH_LONG ).show();
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);

    }


    public boolean isValidPass(String s) {
        String n = ".*[0-9].*";
        String a = ".*[a-zA-Z].*";
        return s.matches(n) && s.matches(a);
    }

}

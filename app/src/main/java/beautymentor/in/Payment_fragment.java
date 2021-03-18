package beautymentor.in;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import Config.SharedPref;
import Module.Module;

import beautymentor.in.payment.ServiceWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseCartHandler;
import util.Session_management;

import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;
import static com.android.volley.VolleyLog.TAG;


public class Payment_fragment extends AppCompatActivity {
    //RelativeLayout confirm;
    Module module;
    Button confirm;
    private DatabaseCartHandler db_cart;
    private Session_management sessionManagement;
    TextView payble_ammount, my_wallet_ammount, used_wallet_ammount, used_coupon_ammount, order_ammount;
    private String getlocation_id = "";
    private String getstore_id = "";
    Activity ctx = Payment_fragment.this;
    private double wamt=0;
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private Double rewards;
    RadioButton rb_Cod,rb_pay;
    CheckBox checkBox_Wallet;
    CheckBox checkBox_coupon;
    EditText et_Coupon;
    String getvalue;
    String text;
    String cp;
    Dialog loadingBar;
    String Used_Wallet_amount , Wallet_amount;
    String total_amount;
    String order_total_amount;
    RadioGroup radioGroup;
    String Prefrence_TotalAmmount;
    String getwallet;
    LinearLayout Promo_code_layout, Coupon_and_wallet;
    RelativeLayout Apply_Coupon_Code, Relative_used_wallet, Relative_used_coupon;
    //payments
    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;

    String  txnid ="", amount ="", phone ="",
            prodname ="", firstname ="", email ="",
            merchantId ="", merchantkey="";
//    merchantId ="6367063", merchantkey="5bMBTT5D";
    public Payment_fragment() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.activity_payment_method, container, false);
//        ((MainActivity) ctx).setTitle(getResources().getString(R.string.payment));

        loadingBar=new Dialog(ctx,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);

        Prefrence_TotalAmmount = SharedPref.getString(ctx, BaseURL.TOTAL_AMOUNT);
        module=new Module();
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                getvalue = radioButton.getText().toString();
            }
        });


        Typeface font = Typeface.createFromAsset(ctx.getAssets(), "Font/Bold.ttf");
        checkBox_Wallet = (CheckBox)findViewById(R.id.use_wallet);
        checkBox_Wallet.setTypeface(font);

        rb_Cod = (RadioButton)findViewById(R.id.use_COD);
        rb_Cod.setTypeface(font);
        rb_pay = (RadioButton)findViewById(R.id.pay_now);
        rb_pay.setTypeface(font);
        checkBox_coupon = (CheckBox)findViewById(R.id.use_coupon);
        checkBox_coupon.setTypeface(font);
        et_Coupon = (EditText)findViewById(R.id.et_coupon_code);
        Promo_code_layout = (LinearLayout)findViewById(R.id.prommocode_layout);
        Apply_Coupon_Code = (RelativeLayout)findViewById(R.id.apply_coupoun_code);
        sessionManagement = new Session_management(ctx);


        Coupon_and_wallet = (LinearLayout)findViewById(R.id.coupon_and_wallet);
        Relative_used_wallet = (RelativeLayout)findViewById(R.id.relative_used_wallet);
        Relative_used_coupon = (RelativeLayout)findViewById(R.id.relative_used_coupon);

        //Show  Wallet
        getwallet = SharedPref.getString(ctx, BaseURL.KEY_WALLET_Ammount);
        my_wallet_ammount = (TextView)findViewById(R.id.user_wallet);
       // my_wallet_ammount.setText(getwallet+ctx.getString(R.string.currency));
        db_cart = new DatabaseCartHandler(ctx);
//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener()
//
//        {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    Fragment fm = new Home_fragment();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
//                    return true;
//                }
//                return false;
//            }
//        });


        total_amount = getIntent().getStringExtra("total");
        order_total_amount = getIntent().getStringExtra("total");
        getdate = getIntent().getStringExtra("getdate");
        gettime = getIntent().getStringExtra("gettime");
        getlocation_id = getIntent().getStringExtra("getlocationid");
        getstore_id = getIntent().getStringExtra("getstoreid");
        payble_ammount = (TextView)findViewById(R.id.payable_ammount);
        order_ammount = (TextView)findViewById(R.id.order_ammount);
      //  used_wallet_ammount = (TextView)findViewById(R.id.used_wallet_ammount);
       // used_coupon_ammount = (TextView)findViewById(R.id.used_coupon_ammount);
        payble_ammount.setText(total_amount+ctx.getString(R.string.currency));
        order_ammount.setText(order_total_amount+ctx.getString(R.string.currency));

        confirm = (Button)findViewById(R.id.confirm_order);
        confirm.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
               // Toast.makeText( ctx,"amount" +total_amount,Toast.LENGTH_LONG ).show();

                if (ConnectivityReceiver.isConnected()) {

                    checked();

                } else {
                    confirm.setEnabled(true);

                    ((MainActivity) ctx).onNetworkConnectionChanged(false);
                }
            }
        });
//        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
       // Toast.makeText(ctx,""+getuser_id,Toast.LENGTH_LONG).show();
        getWalletAmount(getuser_id);


    }

    private void getWalletAmount(String user_id)
    {
        String json_wallet_tag="json_wallet_tag";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("user_id",user_id);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,BaseURL.WALLET_AMOUNT_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    //Toast.makeText(ctx,"Something went wrong"+response,Toast.LENGTH_LONG).show();
                    String status=response.getString("status");
                    if(status.equals("success"))
                    {
                      wamt=Double.parseDouble(response.getString("data"));
                    }
                    else if(status.equals("failed"))
                    {
                        wamt=Double.parseDouble(response.getString("data"));
                    }
                    my_wallet_ammount.setText(ctx.getString(R.string.currency)+" "+wamt);
                }
                catch (Exception ex)
                {
                   // Toast.makeText(ctx,"Something went wrong"+ex.getMessage(),Toast.LENGTH_LONG).show();
                }

               // Toast.makeText(ctx,"Response :"+response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( ctx,""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_wallet_tag);
    }

    private void attemptOrder(String mode ,String t_id,String id_paid) {
        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
        //rewards = Double.parseDouble(db_cart.getColumnRewards());
        rewards = Double.parseDouble("0");
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
               // String unt=
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("product_name", map.get("product_name")+map.get("attr_color"));
                    jObjP.put("qty", map.get("qty"));
                    jObjP.put("unit_value", map.get("unit_price"));
                    jObjP.put("unit", map.get("unit"));
                    jObjP.put("price", map.get("price"));
                    jObjP.put("rewards", "0");
                    jObjP.put("atr_img",map.get("attr_img"));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {
                Date date=new Date();

                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String g=dateFormat.format(date);
                SimpleDateFormat dateFormat1=new SimpleDateFormat("hh:mm a");
                String t=dateFormat1.format(date);

                gettime=t+" - "+t.toString();

              getdate=g;

                //gettime="03:00 PM - 03:30 PM";
               // getdate="2019-7-23";
                Log.e(TAG, "from:" +"03:00 PM - 03:30 PM" + "\ndate:" + "2019-7-23" +
                        "\n" + "\nuser_id:" + getuser_id + "\n l" + getlocation_id + getstore_id + "\ndata:" + passArray.toString());
//Toast.makeText(ctx, "from:" + gettime + "\ndate:" + getdate +
//        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:" + passArray.toString(),Toast.LENGTH_LONG).show();

    makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, total_amount,t_id,mode,is_paid, passArray);


            }
        }
    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location,String tot_amount, String transaction_id,String method,String is_paid,JSONArray passArray) {

        loadingBar.show();
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("is_paid",is_paid);
        params.put("location", location);
        params.put("txn_id", transaction_id);
        params.put("total_ammount",tot_amount);
        params.put("payment_method", method);
        params.put("data", passArray.toString());
       // Toast.makeText(ctx,""+passArray,Toast.LENGTH_LONG).show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        String msg = response.getString("data");
                       // String msg_arb=response.getString("data_arb");
//                        db_cart.clearCart();
//                        updateData();
//                        loadingBar.dismiss();
//                        Bundle args = new Bundle();
//                        Fragment fm = new Thanks_fragment();
//                        args.putString("msg", msg);
//                       // args.putString("msgarb",msg_arb);
//                        fm.setArguments(args);
//                        FragmentManager fragmentManager = getFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                .addToBackStack(null).commit();

                  //      Toast.makeText(ctx,"success",Toast.LENGTH_LONG).show();
                    }
                    else

                    {
                        loadingBar.dismiss();
                        Toast.makeText(ctx,"Something went wrong",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
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
                    Toast.makeText( ctx,""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void Usewalletfororder(String userid, String Wallet) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("wallet_amount", Wallet);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Wallet_CHECKOUT, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Toast.makeText(ctx,""+response,Toast.LENGTH_LONG).show();
                 //   String status = response.getString("responce");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( ctx,""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void checked() {
        Date d = new Date();
        SimpleDateFormat d_f = new SimpleDateFormat("ddMMyyyyHHmmss");
        String dd = d_f.format(d);
        Log.e("trns_id",dd.toString());
        if (checkBox_Wallet.isChecked()) {

           // Toast.makeText(ctx,"checkBox_Wallet",Toast.LENGTH_LONG).show();
            double t=Double.parseDouble(total_amount);

            if(wamt>0)
            {
                rb_Cod.setClickable( false );
                rb_Cod.setVisibility( View.INVISIBLE );
                Usewalletfororder(getuser_id,String.valueOf(t));

                String amt = String.valueOf( t );



              //  attemptOrder();

            }
            else
            {
             Toast.makeText(ctx,"You don't have enough wallet amount.\n Please select another option",Toast.LENGTH_LONG).show();
            }



        }
       else if (rb_pay.isChecked()) {
       getAppSettingData(dd);
        }
        else if (rb_Cod.isChecked()) {

            //Toast.makeText(ctx,"rb_Cod",Toast.LENGTH_LONG).show();
           attemptOrder(getvalue,dd);
        }
        else {
            Toast.makeText(ctx, "Please Select One", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        ctx.unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();

        // register reciver
        ctx.registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }



    private BroadcastReceiver mCart = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             String type = intent.getStringExtra("type");

             if (type.contentEquals("update")) {
                 updateData();
             }
         }


    };

    private void updateData() {

        ((MainActivity) ctx).setCartCounter("" + db_cart.getCartCount());

    }


    public void getAppSettingData(final String dd)
    {
        loadingBar.show();
        String json_tag="json_app_tag";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_VERSTION_DATA, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    boolean sts=response.getBoolean("responce");

                    if(sts)
                    {
                        JSONObject object=response.getJSONObject("data");
                        merchantId=object.getString("merchant_id");
                        merchantkey=object.getString("merchant_key");

                        txnid =dd;
                        amount =total_amount;
                        phone =sessionManagement.getUserDetails().get(KEY_MOBILE);
                                prodname =getResources().getString(R.string.app_name);
                                firstname =sessionManagement.getUserDetails().get(KEY_NAME);
                                email ="beautymentor19@gmail.com";
                   startpay();
//                       startActivity(new Intent(ctx, PayUMoneyActivity.class));

                    }
                    else
                    {
                        Toast.makeText(ctx,""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
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
                    Toast.makeText(ctx,""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request,json_tag);
    }
    private void startpay() {
        Log.e(TAG, "startpay: "+amount+"\n"+txnid+"\n"+phone+"\n"+prodname+"\n"+firstname+"\n"+email );
        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnid)                     // Transaction ID
                .setPhone(phone)                   // User Phone number
                .setProductName(prodname)                   // Product Name or description
                .setFirstName(firstname)                              // User First name
                .setEmail(email)              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);
        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            Log.e(TAG, " error s "+e.toString());
        }

    }

    public void getHashkey(){
        ServiceWrapper service = new ServiceWrapper(null);
        Call<String> call = service.newHashCall(merchantkey, txnid, amount, prodname,
                firstname, email);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e(TAG, "hash res "+response.body());
                String merchantHash= response.body();
                if (merchantHash.isEmpty() || merchantHash.equals("")) {
                    Toast.makeText(ctx, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "hash empty");
                } else {
                    // mPaymentParams.setMerchantHash(merchantHash);
                    paymentParam.setMerchantHash(merchantHash);

                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, ctx, R.style.AppTheme2, false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "hash error "+ t.toString());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                    Log.e("taransactionsdsadasd", "" + transactionResponse.getTransactionDetails().toString());
                    attemptOrder(getvalue, txnid);
                } else {
                    //Failure Transaction
                    module.showToast(ctx, "Transaction Failed. Try again later");

                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.e(TAG, "tran " + payuResponse + "---" + merchantResponse);
            }
        }
    }
}

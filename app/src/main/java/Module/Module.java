package Module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.ProductVariantModel;
import beautymentor.in.MainActivity;
import beautymentor.in.R;
import util.DatabaseCartHandler;

public class Module {


    public static void setIntoCart(Activity activity,String attr_id,String product_id,String product_images,String cat_id,String details_product_name,String details_product_price,String details_product_desc,String details_product_rewards,String details_product_unit_price,String  details_product_unit,
                                   String details_product_increament,String details_product_stock,String deatils_atr_color,String deatils_atr_img,String details_product_title,String details_product_mrp,String details_product_attribute,String type,float qty)
    {
        DatabaseCartHandler db_cart=new DatabaseCartHandler(activity);
        HashMap<String,String> mapProduct=new HashMap<String, String>();
        mapProduct.put("product_id", product_id);
        mapProduct.put("attr_id", attr_id);
        mapProduct.put("product_image",product_images);
        mapProduct.put("cat_id",cat_id);
        mapProduct.put("product_name",details_product_name);
        mapProduct.put("price", details_product_price);
        mapProduct.put("product_description",details_product_desc);
        mapProduct.put("rewards", details_product_rewards);
        mapProduct.put("unit_price",details_product_unit_price );
        mapProduct.put("unit", details_product_unit);
        mapProduct.put("increment",details_product_increament);
        mapProduct.put("stock",details_product_stock);
        mapProduct.put("attr_color",deatils_atr_color);
        mapProduct.put("attr_img",deatils_atr_img);
        mapProduct.put("title",details_product_title);
        mapProduct.put("mrp",details_product_mrp);
        mapProduct.put("product_attribute",details_product_attribute);
        mapProduct.put("type",type);

        try {

            boolean tr = db_cart.setCart(mapProduct, qty);
            if (tr == true) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.setCartCounter("" + db_cart.getCartCount());

                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                Toast.makeText(activity, "Added to Cart" , Toast.LENGTH_LONG).show();
                int n = db_cart.getCartCount();
                updateintent(activity);
          //      txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));

            } else if (tr == false) {
                Toast.makeText(activity, "cart updated", Toast.LENGTH_LONG).show();
               // txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            // Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }



    }


    public static void setWithoutAttrIntoCart(Activity activity,String attr_id,String product_id,String product_images,String cat_id,String details_product_name,String details_product_price,String details_product_desc,String details_product_rewards,String details_product_unit_price,String  details_product_unit,
                                   String details_product_increament,String details_product_stock,String deatils_atr_color,String deatils_atr_img,String details_product_title,String details_product_mrp,String details_product_attribute,String type,float qty)
    {
        DatabaseCartHandler db_cart=new DatabaseCartHandler(activity);
        HashMap<String,String> mapProduct=new HashMap<String, String>();
        mapProduct.put("product_id", product_id);
        mapProduct.put("attr_id", attr_id);
        mapProduct.put("product_image",product_images);
        mapProduct.put("cat_id",cat_id);
        mapProduct.put("product_name",details_product_name);
        mapProduct.put("price", details_product_price);
        mapProduct.put("product_description",details_product_desc);
        mapProduct.put("rewards", details_product_rewards);
        mapProduct.put("unit_price",details_product_unit_price );
        mapProduct.put("unit", details_product_unit);
        mapProduct.put("increment",details_product_increament);
        mapProduct.put("stock",details_product_stock);
        mapProduct.put("attr_color",deatils_atr_color);
        mapProduct.put("attr_img",deatils_atr_img);
        mapProduct.put("title",details_product_title);
        mapProduct.put("mrp",details_product_mrp);
        mapProduct.put("product_attribute",details_product_attribute);
        mapProduct.put("type",type);

        try {

            boolean tr = db_cart.setWithoutAttrCart(mapProduct, qty);
            if (tr == true) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.setCartCounter("" + db_cart.getCartCount());

                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                Toast.makeText(activity, "Added to Cart" , Toast.LENGTH_LONG).show();
                int n = db_cart.getCartCount();
                updateintent(activity);
                //      txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));

            } else if (tr == false) {
                Toast.makeText(activity, "Cart Updated", Toast.LENGTH_LONG).show();
                // txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            // Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

    public static void updateintent(Activity activity) {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }

    public List<ProductVariantModel> getAttribute(String attribute)
    {
        ProductVariantModel model=new ProductVariantModel();
        List<ProductVariantModel> varientlist = new ArrayList<>();

        try {
            JSONArray jsonArr = new JSONArray(attribute);

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);

                if (jsonObj.has("id") && !jsonObj.isNull("id"))
                    model.setId(jsonObj.getString("id"));
                else
                    model.setId(jsonObj.getString(""));
                if (jsonObj.has("product_id") && !jsonObj.isNull("product_id"))
                    model.setProduct_id(jsonObj.getString("product_id"));
                else
                    model.setProduct_id("");

                if (jsonObj.has("attribute_name") && !jsonObj.isNull("attribute_name"))
                    model.setAttribute_name(jsonObj.getString("attribute_name"));
                else
                    model.setAttribute_name("");

                if (jsonObj.has("attribute_value") && !jsonObj.isNull("attribute_value"))
                    model.setAttribute_value(jsonObj.getString("attribute_value"));
                else
                    model.setAttribute_value("");

                if (jsonObj.has("attribute_mrp") && !jsonObj.isNull("attribute_mrp"))
                    model.setAttribute_mrp(jsonObj.getString("attribute_mrp"));
                else
                    model.setAttribute_mrp("");

                if (jsonObj.has("attribute_image") && !jsonObj.isNull("attribute_image"))
                    model.setAttribute_image(jsonObj.getString("attribute_image"));
                     else
                    model.setAttribute_image("");
                if (jsonObj.has("attribute_color") && !jsonObj.isNull("attribute_color"))
                    model.setAttribute_color(jsonObj.getString("attribute_color"));
                     else
                    model.setAttribute_color("");

                varientlist.add(model);
            }
    }
           catch (JSONException ex) {
            ex.printStackTrace();
        }
        return  varientlist ;

        }

    public static String VolleyErrorMessage(VolleyError error)
    {
        String str_error ="";
        if (error instanceof TimeoutError) {
            str_error="Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error="Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error="An Unknown error occur";
        }else if(error instanceof NoConnectionError){
            str_error="no Internet Connection";
        }

        return str_error;
    }

    public static void showVolleyError(Context ctx,VolleyError volleyError){
        showToast(ctx,VolleyErrorMessage(volleyError));
    }
    public static void showToast(Context ctx,String str)
    {
        if(str==null || str.isEmpty() || str.equalsIgnoreCase(null)){

        }else{
         Toast.makeText(ctx,""+str,Toast.LENGTH_LONG).show();
        }
    }

    public static String getFirstImage(String img_array)
    {
        String img_name="";
        try {
            JSONArray array=new JSONArray(img_array);
            img_name=array.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return img_name;
    }

    public static boolean checkNull(String str){
        if(str == null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return true;
        }else{
            return false;
        }
    }

    }


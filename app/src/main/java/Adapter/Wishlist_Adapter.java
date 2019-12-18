package Adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Fragment.Details_Fragment;
import Model.ProductVariantModel;
import Model.Product_model;
import Model.Wish_model;
import Module.Module;
import binplus.vijaylaxmi.R;
import util.DatabaseCartHandler;
import util.Session_management;
import util.WishlistHandler;

import static Config.BaseURL.KEY_ID;
import static android.content.Context.MODE_PRIVATE;

public class Wishlist_Adapter extends RecyclerView.Adapter<Wishlist_Adapter.WishHolder> {

    List<String> list_color=new ArrayList<>();
    List<String> list_images=new ArrayList<>();
    String user_id="";
    Session_management session_management;
    List<ProductVariantModel>  vlst=new ArrayList<>();
    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";
    String attribute_color="";
    String attribute_image="";

    Module module=new Module();
    ArrayList<ProductVariantModel> variantList;
    ArrayList<ProductVariantModel> attributeList;
    ProductVariantAdapter productVariantAdapter;
   ArrayList<HashMap<String, String>> list;
    private List<Wish_model> wishList;
    Activity activity;
    String Reward;
    int status=0;
    String id="";

    Double price ,reward ;
    SharedPreferences preferences;
    String language;
    float qty = 1;
   WishlistHandler db_wish ;
    DatabaseCartHandler db_cart;
    String product_id ;
    boolean isInCart=false;
  String cat_id,product_images,details_product_name,details_product_desc,details_product_inStock,details_product_attribute;
    String details_product_price,details_product_mrp,details_product_unit_value,details_product_unit_price,details_product_unit,details_product_rewards,details_product_increament,details_product_title;

    public Wishlist_Adapter(ArrayList<HashMap<String, String>> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        db_cart = new DatabaseCartHandler(activity);
    }



    @Override
    public WishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_wishlist, parent, false);
        return new Wishlist_Adapter.WishHolder( view );
    }

    @Override
    public void onBindViewHolder(final WishHolder holder, final int position) {
//        final Product_model mList = modelList.get(position);
        final HashMap<String, String> map = list.get(position);

        String img_array=map.get("product_image");
        String img_name = null;
        try {
            JSONArray array=new JSONArray(img_array);
            img_name=array.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(activity)
                .load( BaseURL.IMG_PRODUCT_URL + img_name)
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_icon);
        preferences = activity.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        holder.product_name.setText( map.get( "product_name" ));
        holder.txtdesc.setText( map.get( "product_description" ) );




//        boolean flag=db_cart.isCartIDInCart(id);
//        if(flag)
//        {
//            holder.icon_cart.setColorFilter( ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//        }

        product_id=map.get("product_id");
        product_images=map.get("product_image");
        cat_id=map.get("cat_id");
        details_product_name=map.get("product_name");
        details_product_price=map.get("price");
        details_product_mrp=map.get("mrp");
        details_product_unit_price=map.get("unit_price");
        details_product_unit_value=map.get("unit_value");
        details_product_unit=map.get("unit");
        details_product_desc=map.get("product_description");
         details_product_inStock=map.get("stock");
        details_product_attribute=map.get("product_attribute");
         details_product_increament=map.get("increment");
        details_product_title=map.get("title");
        details_product_rewards=map.get("rewards");
        String st_atr=map.get("product_attribute");



//        id=db_cart.getCartId(map.get("product_id"),"","");
//
//        if(id.equals("") || id.isEmpty())
//        {
//            holder.icon_cart.setVisibility(View.VISIBLE);
//            holder.cart_after.setVisibility(View.GONE);
//        }
//        else
//        {
//            isInCart=db_cart.isCartIDInCart(id);
//            if(isInCart)
//            {
//                holder.icon_cart.setVisibility(View.GONE);
//                holder.cart_after.setVisibility(View.VISIBLE);
//            }
//        }



        //  Toast.makeText(activity,""+map.get("product_id")+"\n cart "+id,Toast.LENGTH_LONG).show();
        if(st_atr.equals("[]"))
        {
            holder.product_price.setText(activity.getResources().getString(R.string.currency)+map.get("price"));
            holder.product_mrp.setText(activity.getResources().getString(R.string.currency)+map.get("mrp"));

            String p=String.valueOf(map.get("price"));
            String m=String.valueOf(map.get("mrp"));
            int discount=getDiscount(p,m);
            //Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
            holder.discount.setText(""+discount+"% OFF"+id);

            holder.txtrate.setText(map.get("unit_value")+" "+map.get("unit"));

        }
        else
        {
        vlst=module.getAttribute(st_atr);

        getAttrColor(vlst.get(0).getAttribute_color());
        getAttrImage(vlst.get(0).getAttribute_image());


            holder.product_price.setText(activity.getResources().getString(R.string.currency)+vlst.get(0).getAttribute_value());
            holder.product_mrp.setText(activity.getResources().getString(R.string.currency)+vlst.get(0).getAttribute_mrp());
            String p=String.valueOf(vlst.get(0).getAttribute_value());
            String m=String.valueOf(vlst.get(0).getAttribute_mrp());
            int discount=getDiscount(p,m);
            holder.txtrate.setText(vlst.get(0).getAttribute_name());

            //Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
            holder.discount.setText(""+discount+"% OFF");
        }

        holder.rel_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Details_Fragment details_fragment=new Details_Fragment();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle args = new Bundle();

//                args.putString("cat_id",map.get("cat_id"));
//                args.putString("product_id",map.get("product_id"));
//                args.putString("product_image",map.get("product_image"));
//                args.putString("product_name",map.get("product_name"));
//                args.putString("product_description",map.get("product_description"));
//                args.putString("stock",map.get("stock"));
//                args.putString("price",map.get("price"));
//                args.putString("mrp",map.get("mrp"));
//                args.putString("unit_price",map.get("unit_price"));
//                args.putString("unit_value",map.get("unit_value"));
//                args.putString("unit",map.get("unit"));
//                args.putString("product_attribute",map.get("product_attribute"));
//                args.putString("rewards",map.get("rewards"));
//                args.putString("increment",map.get("increment"));
//                args.putString("title",map.get("title"));

                args.putString("cat_id",map.get("cat_id"));
                args.putString("product_id", map.get("product_id"));
                args.putString("product_image", map.get("product_image"));
                args.putString("product_name",map.get("product_name"));
                args.putString("product_description",map.get("product_description"));
                args.putString("in_stock", map.get("in_stock"));
                args.putString("stock",map.get("stock"));
//                args.putString("product_size",modelList.get(position).getSize());
                // args.putString("product_color",modelList.get( position).getColor());
                args.putString("price",map.get("price"));
                args.putString("mrp", map.get("mrp"));
                args.putString("unit_price",map.get("price"));
                args.putString("unit_value", map.get("unit_value"));
                args.putString("unit",map.get("unit"));
                args.putString("product_attribute",map.get("product_attribute"));
                args.putString("rewards",map.get("rewards"));
                args.putString("increment", map.get("increment"));
                args.putString("title",map.get("title"));


                details_fragment.setArguments(args);


                FragmentManager fragmentManager=activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel,details_fragment)

                        .addToBackStack(null).commit();


            }
        });

        holder.icon_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.icon_cart.setVisibility(View.GONE);
                holder.cart_after.setVisibility(View.VISIBLE);
            //    holder.icon_cart.setColorFilter( ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                float qty = 1;
                String atr = String.valueOf(map.get("product_attribute"));
                if (atr.equals("[]")) {

                    String details_unt=details_product_unit_value+details_product_unit;
                    Module.setWithoutAttrIntoCart(activity,"0",map.get("product_id"),
                            map.get("product_image"),map.get("cat_id"),map.get("product_name"),
                            map.get("price"),map.get("product_description"),map.get("rewards"),
                            map.get("price"),details_unt,map.get("increment"),
                            map.get("stock"),"","",map.get("title"),map.get("mrp"),
                            map.get("product_attribute"),"p",qty);

                }
                else {

                 String img=getAttrImage(vlst.get(0).getAttribute_image());
                 String cl=getAttrColor(vlst.get(0).getAttribute_color());
//                    Toast.makeText(activity,""+img+"\n"+cl,Toast.LENGTH_LONG).show();

                    Module.setWithoutAttrIntoCart(activity,vlst.get(0).getId(),map.get("product_id"),
                            map.get("product_image"),map.get("cat_id"),map.get("product_name"),
                            vlst.get(0).getAttribute_value(),map.get("product_description"),map.get("rewards"),
                            vlst.get(0).getAttribute_value(),vlst.get(0).getAttribute_name(),map.get("increment"),
                            map.get("stock"),cl,img,map.get("title"),vlst.get(0).getAttribute_mrp(),
                            map.get("product_attribute"),"a",qty);


//                    Module.setIntoCart(activity,atr_id,product_id,product_images,cat_id,details_product_name,attribute_value,
//                            details_product_desc,details_product_rewards,attribute_value,attribute_name,details_product_increament,
//                            details_product_inStock,attribute_color,attribute_image,details_product_title,attribute_mrp,details_product_attribute,"a",qty);

                }

                updateCartintent();



            }
        });

        holder.cart_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //String cart_id=db_cart.getCartIDForWishlist(map.get("product_id"),"w");
                //Toast.makeText(activity,""+map.get("increment"),Toast.LENGTH_LONG).show();
//                if(cart_id.equals("") || cart_id.isEmpty())
//                {
//
//                }
//                else
//                {
//
//                    db_cart.removeItemFromCart(cart_id);
//                    updateCartintent();
//                }

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              //  holder.db_wish.clearWishtable(user_id);

                String us_id=session_management.getUserDetails().get(KEY_ID);
                holder.db_wish.removeItemFromWishtable(map.get("product_id"),us_id);
                list.remove(position);
                notifyDataSetChanged();

                // db_cart.getCartAll()
                updateintent();
            }
        });

    }

    private String getAttrImage(String attribute_image) {

        list_images.clear();
        String asd="";
        try
        {

            JSONArray array=new JSONArray(attribute_image);
            for(int i=0; i<array.length();i++)
            {
                list_images.add(array.getString(i).toString());
            }
            asd=list_images.get(0).toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return asd;
    }

    private String getAttrColor(String attribute_color) {

        String cl="";
        list_color.clear();
        try
        {
            JSONArray array=new JSONArray(attribute_color);
            for(int i=0; i<array.length();i++)
            {
             list_color.add(array.getString(i).toString());
            }
            cl=list_color.get(0).toString();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return cl;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WishHolder extends RecyclerView.ViewHolder {
        public WishlistHandler db_wish;
        TextView product_name ,product_price ,product_mrp ,unit_type ,discount;
        RelativeLayout varient , rel_wishlist ;
        ImageView iv_icon , delete ,icon_cart ,cart_after;
        CardView card_wishlist ;
        Button add ;
        private TextView dialog_unit_type,dialog_txtId,dialog_txtVar;
        TextView txtrate ,txtdesc;
        RelativeLayout rel_variant;
        DatabaseCartHandler db_cart ;
        ElegantNumberButton elegantNumberButton ;

        public WishHolder(View itemView) {
            super( itemView );
            product_name = (TextView)itemView.findViewById( R.id.product_name );
            product_price=(TextView)itemView.findViewById( R.id.product_prize );
            rel_wishlist=(RelativeLayout)itemView.findViewById( R.id.rel_wish );
            product_mrp=(TextView)itemView.findViewById( R.id.product_mrp );
            unit_type=(TextView)itemView.findViewById( R.id.unit_type );
            add=itemView.findViewById( R.id.btn_add );
            session_management=new Session_management(activity);
            iv_icon=(ImageView)itemView.findViewById( R.id.iv_icon );
            delete=(ImageView)itemView.findViewById( R.id.delete );
            cart_after=(ImageView)itemView.findViewById( R.id.cart_after );
            varient= itemView.findViewById( R.id.varient );
            txtrate=(TextView)itemView.findViewById(R.id.single_varient);
            db_cart=new DatabaseCartHandler(activity);
            rel_variant=(RelativeLayout)itemView.findViewById(R.id.rel_variant);
            discount=itemView.findViewById( R.id.dis );
            dialog_unit_type=(TextView)itemView.findViewById(R.id.unit_type);
            dialog_txtId=(TextView)itemView.findViewById(R.id.txtId);
            dialog_txtVar=(TextView)itemView.findViewById(R.id.txtVar);
            elegantNumberButton =(ElegantNumberButton)itemView.findViewById( R.id.elegantButton );
            txtdesc = (TextView)itemView.findViewById( R.id.txtDesc );
            icon_cart =(ImageView)itemView.findViewById( R.id.icon_cart );
            db_wish = new WishlistHandler( activity );

            attributeList=new ArrayList<>();
            variantList=new ArrayList<>();
            user_id=session_management.getUserDetails().get(KEY_ID);
        }
    }


    public int getDiscount(String price, String mrp)
    {
        double mrp_d=Double.parseDouble(mrp);
        double price_d=Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df=Math.round(per);
        int d=(int)df;
        return d;
    }

    public boolean checkAttributeStatus(String atr)
    {
        boolean sts=false;
        if(atr.equals("[]"))
        {
            sts=false;
        }
        else
        {
            sts=true;
        }
        return sts;
    }


    private void updateintent() {
        Intent updates = new Intent("Grocery_wish");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }

    private void updateCartintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }


}

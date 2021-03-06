package Fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.daimajia.slider.library.SliderLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.skyhope.showmoretextview.ShowMoreTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.AttrColorAdapter;
import Adapter.RelatedProductAdapter;
import Adapter.VarientsAdapter;
import Config.BaseURL;
import Model.ProductVariantModel;
import Model.Product_model;
import Model.RelatedProductModel;
import Module.Module;
import beautymentor.in.AppController;
import beautymentor.in.CustomSlider;
import beautymentor.in.LoginActivity;
import beautymentor.in.MainActivity;
import beautymentor.in.R;
import beautymentor.in.networkconnectivity.NoInternetConnection;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseCartHandler;
import util.Session_management;
import util.WishlistHandler;

import static Config.BaseURL.GET_PRODUCT_DETAIL_URL;
import static Config.BaseURL.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class Details_Fragment extends Fragment implements  RecyclerView.OnClickListener {
    public static  int col_position=-1;
    public static int position=-1;
    String atr_id="";
    String user_id;
    public static Button btn_adapter,btn_color;
    String atr_product_id,attribute_name,attribute_value,attribute_mrp,attribute_color,attribute_img;
    int flag=0;
    RelativeLayout rel_out;
    int stock;
    Context context;
    List<Product_model> models;
    VarientsAdapter varientsAdapter;
    List<ProductVariantModel> v_list,vlist;
    AttrColorAdapter colorAdapter;
    private static String TAG = Details_Fragment.class.getSimpleName();
    private RecyclerView rv_cat,rv_color,rv_weight,recyclerView ,varient_recycler;
   Dialog loadingBar;
    RelativeLayout rel_variant,rel_weight,rel_color;
    private List<RelatedProductModel> product_modelList = new ArrayList<>();
    private RelatedProductAdapter adapter_product;
    Button btn_add_to_cart,btn_add;
    DatabaseCartHandler db_cart;
    WishlistHandler db_wish ;
    TextView txtPer,txtTotal,txtName ,txtHsn,txtPrice,txtMrp,txtDesc,details_product_weight;
    public static ImageView wish_before ,wish_after,btn ;
    int status=0;
    String cat_id,product_id,product_images,details_product_name,details_product_desc,details_product_inStock,details_product_attribute;
    String details_product_price,details_product_stock,details_product_mrp,details_product_unit_value,details_product_unit,details_product_rewards,details_product_increament,details_product_title;
    List<String> image_list;
    public static ElegantNumberButton numberButton;
    Module module=new Module();
    private Session_management sessionManagement;
    private String details_product_unit_price;
    List<String> list_color=new ArrayList<>();
    List<String> list_images=new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    JSONObject var_respons=null;
   public static RelativeLayout rel_relative ;
   String qty_in_cart ;
   public static SliderLayout product_img_slider;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_details_, container, false);
         var_respons=new JSONObject();
        sessionManagement = new Session_management(getActivity());
        sessionManagement.cleardatetime();
        user_id=sessionManagement.getUserDetails().get(KEY_ID);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        product_img_slider = (SliderLayout) view.findViewById(R.id.product_img_slider);

        models= (List<Product_model>) getArguments().getSerializable("product_model");
        rv_cat = (RecyclerView) view.findViewById(R.id.top_selling_recycler);
        varient_recycler=(RecyclerView)view.findViewById( R.id.varient_recycler );
        rv_color=(RecyclerView)view.findViewById( R.id.rv_color);
        rv_weight=(RecyclerView)view.findViewById( R.id.rv_weight);
        rel_weight=(RelativeLayout) view.findViewById(R.id.rel_weight);
        rel_color=(RelativeLayout) view.findViewById(R.id.rel);
        details_product_weight=(TextView)view.findViewById(R.id.details_product_weight);
        btn_adapter=(Button) view.findViewById(R.id.btn_adapter);
        btn_color=(Button) view.findViewById(R.id.btn_color);
        rel_out =view.findViewById( R.id.rel_out );

        rel_relative = view.findViewById( R.id.rel_relative_product );
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_cat.setLayoutManager(linearLayoutManager1);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        varient_recycler.setLayoutManager( linearLayoutManager2 );
        db_wish = new WishlistHandler( getActivity() );
        db_cart=new DatabaseCartHandler(getActivity());
        v_list=new ArrayList<>();

        Bundle bundle=getArguments();

         vlist = new ArrayList<>();


        cat_id=bundle.getString("cat_id");
        product_id=bundle.getString("product_id");
        product_images=bundle.getString("product_image");
        details_product_name=bundle.getString("product_name");
        details_product_desc=bundle.getString("product_description");
        details_product_inStock=bundle.getString("in_stock");
        details_product_stock=bundle.getString("stock");
        details_product_attribute=bundle.getString("product_attribute");
        details_product_unit_price =bundle.getString("unit_price");
        details_product_price=bundle.getString("price");
        details_product_mrp=bundle.getString("mrp");
        details_product_unit_value=bundle.getString("unit_value");
        details_product_unit=bundle.getString("unit");
        details_product_rewards=bundle.getString("rewards");
        details_product_increament=bundle.getString("increment");
        details_product_title=bundle.getString("title");
        stock=Integer.parseInt(details_product_stock);

            if (ConnectivityReceiver.isConnected())
            {
                getProductDetail(product_id);
            }
            else
            {
                startActivity(new Intent(getActivity(), NoInternetConnection.class));
            }

    btn_add=(Button)view.findViewById(R.id.btn_add);
        btn_add_to_cart=(Button)view.findViewById(R.id.btn_f_Add_to_cart);
       // cardView=(CardView)view.findViewById(R.id.card_view2);
        txtPer=(TextView)view.findViewById(R.id.details_product_per);
        rel_variant=(RelativeLayout)view.findViewById(R.id.rel_variant);
        btn=(ImageView)view.findViewById(R.id.img_product);
        recyclerView=view.findViewById(R.id.recylerView);
            wish_after=(ImageView)view.findViewById(R.id.wish_after );
            wish_before = (ImageView)view.findViewById( R.id.wish_before );

        image_list=new ArrayList<>();

        txtName=(TextView)view.findViewById(R.id.details_product_name);
        txtHsn=(TextView)view.findViewById(R.id.details_product_hsn);
       txtDesc=(TextView) view.findViewById(R.id.details_product_description);
        txtPrice=(TextView)view.findViewById(R.id.details_product_price);
        txtMrp=(TextView)view.findViewById(R.id.details_product_mrp);
        txtMrp.setPaintFlags(txtMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(details_product_name);

        txtTotal=(TextView)view.findViewById(R.id.product_total);
        numberButton=(ElegantNumberButton)view.findViewById(R.id.product_qty);
        rel_out=view.findViewById( R.id.rel_out );


       txtDesc.setText(details_product_desc);
       txtName.setText(details_product_name);
        makeTextViewResizable(txtDesc, 3, "See More", true);
        makeRelatedProductRequest(cat_id);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(stock<1)
        {
            rel_out.setVisibility( View.VISIBLE );
        }
        else
        {
            numberButton.setRange(1,  stock+1 );
        }

        if(details_product_attribute.equals("[]"))
        {
            txtPrice.setText(getResources().getString(R.string.currency)+details_product_price);
            txtMrp.setText(getResources().getString(R.string.currency)+details_product_mrp);
            details_product_weight.setText(details_product_unit_value+details_product_unit);
            Double mrp=Double.parseDouble(details_product_mrp);
            final Double price=Double.parseDouble(details_product_price);
            Double discount = Double.valueOf(getDiscount(details_product_price,details_product_mrp));
            if (mrp > price)
            {
                txtMrp.setVisibility(View.VISIBLE);
                txtPer.setText(String.valueOf(Math.round(discount)+"%"+"off"));
            }
            else{
                txtPer.setVisibility(View.GONE);
                txtMrp.setVisibility(View.GONE);
            }

            String id=db_cart.getCartId(product_id,"","");
            if(id.isEmpty()|| id.equals(""))
            {

            }
            else
            {
                String qt=db_cart.getCartIdItemQty(id);
                btn_add.setVisibility(View.GONE);
                numberButton.setVisibility(View.VISIBLE);
                numberButton.setNumber(qt);
            }

        }
        else
        {
            details_product_weight.setVisibility(View.GONE);
           // rel_color.setVisibility(View.VISIBLE);
            rel_weight.setVisibility(View.VISIBLE);
            makeGetAttributeRequest(product_id);


            RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
            rv_weight.setLayoutManager(layoutManager1);
            varientsAdapter=new VarientsAdapter(getActivity(),vlist);
            rv_weight.setAdapter(varientsAdapter);

   if(flag==1)
   {
       rel_color.setVisibility(View.GONE);

   }
   else
   {
       layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
       rv_color.setLayoutManager(layoutManager);

       colorAdapter=new AttrColorAdapter(list_color,list_images,getActivity(),product_id,atr_id);
       rv_color.setAdapter(colorAdapter);
   }





        }


        btn_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ProductVariantModel model=vlist.get(position);

                atr_id=model.getId();
             //   Toast.makeText(getActivity(),""+atr_id, Toast.LENGTH_SHORT).show();

                atr_product_id=model.getProduct_id();
                attribute_mrp=model.getAttribute_mrp();
                attribute_value=model.getAttribute_value();
                attribute_name=model.getAttribute_name();

                txtPrice.setText(getResources().getString(R.string.currency)+model.getAttribute_value());
                txtMrp.setText(getResources().getString(R.string.currency)+model.getAttribute_mrp());
                Double mrp=Double.parseDouble(model.getAttribute_mrp());
                final Double price=Double.parseDouble(model.getAttribute_value());
                Double discount = Double.valueOf(getDiscount(model.getAttribute_value(),model.getAttribute_mrp()));
                if (mrp > price)
                {
                    txtPer.setVisibility(View.VISIBLE);
                    txtMrp.setVisibility(View.VISIBLE);
                    txtPer.setText(String.valueOf(Math.round(discount)+"%"+"off"));
                }
                else {
                    txtPer.setVisibility(View.GONE);
                    txtMrp.setVisibility(View.GONE);
                }


                try
                {

                    if(flag==1)
                    {
                        String qty="";
                        if(db_cart.isAttrInCart(product_id,atr_id))
                        {
                            String id=db_cart.getCartId(product_id,atr_id,"");
                            qty=db_cart.getCartIdItemQty(id);
                            //String id=db_cart.getCartId();
                          //  Toast.makeText(getActivity(),"id - "+id+"\n qty"+qty,Toast.LENGTH_SHORT).show();
                            numberButton.setVisibility(View.VISIBLE);
                            btn_add.setVisibility(View.GONE);
                            numberButton.setNumber(qty);

                        }
                        else
                        {

                            numberButton.setVisibility(View.GONE);
                            btn_add.setVisibility(View.VISIBLE);

                        }

                    }
                    else
                    {
                        String str_col=vlist.get(position).getAttribute_color();
                        if(str_col.isEmpty() || str_col.equals(null))
                        {
                            // Toast.makeText(getActivity(),"empty ",Toast.LENGTH_SHORT).show();
                            rel_color.setVisibility(View.GONE);
                            //rv_color.setVisibility(View.GONE);
                            colorAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            rel_color.setVisibility(View.VISIBLE);

                            list_color.clear();
                          //  rv_color.setVisibility(View.VISIBLE);
                            JSONArray col_array=new JSONArray(str_col);
                            for(int j=0; j<col_array.length();j++)
                            {
                                list_color.add(col_array.getString(j).toString());
                            }
                            colorAdapter.notifyDataSetChanged();
                            //Toast.makeText(getActivity(),""+col_array+"\n "+col_array.length(),Toast.LENGTH_LONG).show();
                        }

                        list_images.clear();
                        String str_img=vlist.get(position).getAttribute_image();
                        if(str_img.isEmpty() || str_img.equals(null))
                        {
                            // Toast.makeText(getActivity(),"empty ",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            JSONArray col_array=new JSONArray(str_img);
                            for(int j=0; j<col_array.length();j++)
                            {
                                list_images.add(col_array.getString(j).toString());
                            }
                            varientsAdapter.notifyDataSetChanged();
                        }

                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),"err_color :-  "+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        });

        btn_color.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        String qty="";
                if(db_cart.isAttrColInCart(product_id,atr_id,list_color.get(col_position)))
                {
                    String id=db_cart.getCartId(product_id,atr_id,list_color.get(col_position));
                    qty=db_cart.getCartIdItemQty(id);
                    //String id=db_cart.getCartId();
                   // Toast.makeText(getActivity(),"id - "+id+"\n qty"+qty,Toast.LENGTH_SHORT).show();
                    numberButton.setVisibility(View.VISIBLE);
                    btn_add.setVisibility(View.GONE);
                    numberButton.setNumber(qty);

                }
                else
                {

                    numberButton.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);

                }

    }
});
        if(sessionManagement.isLoggedIn())
        {
            if(db_wish.isInWishtable( product_id,user_id ))
            {
                wish_after.setVisibility( View.VISIBLE );
                wish_before.setVisibility( View.GONE );
            }
        }




        wish_before.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionManagement.isLoggedIn()) {



                    String tl = "";
                    String title = String.valueOf( details_product_title );
                    if (title.equals( "null" )) {
                        tl = "title";
                    } else {
                        tl = String.valueOf( details_product_title );
                    }

                    if (stock <1) {
                        Toast.makeText( getActivity(), "out of stock", Toast.LENGTH_LONG ).show();
                    } else {
                        wish_after.setVisibility( View.VISIBLE );
                        wish_before.setVisibility( View.INVISIBLE );
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", product_images );
                        mapProduct.put( "cat_id", cat_id );
                        mapProduct.put( "product_name", details_product_name );
                        mapProduct.put( "price", details_product_price );
                        mapProduct.put( "product_description", details_product_desc );
                        mapProduct.put( "rewards", details_product_rewards );
                        mapProduct.put( "in_stock", details_product_inStock );
                        mapProduct.put( "unit_value", details_product_unit_value );
                        mapProduct.put( "unit", details_product_unit );
                        mapProduct.put( "increment", details_product_increament );
                        mapProduct.put( "stock", details_product_stock );
                        mapProduct.put( "title", tl );
                        mapProduct.put( "mrp", details_product_mrp );
                        mapProduct.put( "product_attribute", details_product_attribute );
                        mapProduct.put( "user_id", user_id );

                        try {

                            boolean tr = db_wish.setwishTable( mapProduct );
                            if (tr == true) {
                                updateWishData();
                                Toast.makeText( getActivity(), "Added to Wishlist", Toast.LENGTH_SHORT ).show();

                            } else {
                                Toast.makeText( getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT ).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    }
                else
                    {
                        Intent intent = new Intent( getActivity(), LoginActivity.class );
                        getActivity().startActivity( intent );
                    }
                }

        } );

        wish_after.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wish_after.setVisibility( View.INVISIBLE );
                wish_before.setVisibility( View.VISIBLE );
                db_wish.removeItemFromWishtable(product_id,user_id);
                updateWishData();
                Toast.makeText(getActivity(), "Removed from Wishlist" , Toast.LENGTH_SHORT).show();
            }
        } );

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stock<1)
                {
                    Toast.makeText( getActivity(),"out of stock",Toast.LENGTH_LONG ).show();
                }
                else {

                    String tot1 = txtTotal.getText().toString().trim();

                    String tot_amount = tot1.substring( 1, tot1.length() );
                    float qty = 1;
                    String atr = String.valueOf( details_product_attribute );
                    if (atr.equals( "[]" )) {


                        String details_unt = details_product_unit_value + details_product_unit;
                        Module.setWithoutAttrIntoCart( getActivity(), "0", product_id, product_images, cat_id, details_product_name, details_product_price,
                                details_product_desc, details_product_rewards, details_product_price, details_unt, details_product_increament,
                                details_product_stock, "", "", details_product_title, details_product_mrp, details_product_attribute, "p", qty );
                        txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );
                        updateData();
                        btn_add.setVisibility( View.GONE );
                        numberButton.setVisibility( View.VISIBLE );
                        numberButton.setNumber( "1" );

//                    Toast.makeText(getActivity(),""+db_cart.getCartCount(),Toast.LENGTH_LONG).show();

                    } else {

                        if (flag > 0) {
                            if (position < 0) {
                                Toast.makeText( getActivity(), "Please select any weight", Toast.LENGTH_SHORT ).show();
                            } else {
                                Module.setIntoCart( getActivity(), atr_id, product_id, product_images, cat_id, details_product_name, attribute_value,
                                        details_product_desc, details_product_rewards, attribute_value, attribute_name, details_product_increament,
                                       details_product_stock, "", "", details_product_title, attribute_mrp, details_product_attribute, "a", qty );
                                txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );
                                updateData();
                                btn_add.setVisibility( View.GONE );
                                numberButton.setVisibility( View.VISIBLE );
                                numberButton.setNumber( "1" );


                            }
                        } else if (flag <= 0) {
                            if (position < 0) {
                                Toast.makeText( getActivity(), "Please select any weight", Toast.LENGTH_SHORT ).show();
                            }
                            else if (col_position < 0) {
                                Toast.makeText( getActivity(), "Please select any color", Toast.LENGTH_SHORT ).show();
                            } else {
                                String as = list_color.get( col_position );
                                String img = list_images.get( col_position );
//


                                Module.setIntoCart( getActivity(), atr_id, product_id, product_images, cat_id, details_product_name, attribute_value,
                                        details_product_desc, details_product_rewards, attribute_value, attribute_name, details_product_increament,
                                        details_product_stock, as, img, details_product_title, attribute_mrp, details_product_attribute, "c", qty );
                                txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );

                                updateData();
                                btn_add.setVisibility( View.GONE );
                                numberButton.setVisibility( View.VISIBLE );
                                numberButton.setNumber( "1" );

                            }
                        }

                    }

                }

            }
        });



        numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                int stck=Integer.parseInt( details_product_stock );
             if (newValue >= stck)
                {
                    Toast.makeText( getActivity(), "Only "+stock +" in stock" ,Toast.LENGTH_SHORT ).show();
                    numberButton.setNumber( String.valueOf(  stck) );
                }
             else {
                 if (newValue <= 0) {
                     //Toast.makeText( getActivity(), "Only "+stock +" in stock" ,Toast.LENGTH_SHORT ).show();
                     boolean st = checkAttributeStatus( details_product_attribute );
                     if (st == false) {
                         String id = db_cart.getCartId( product_id, "", "" );
                         db_cart.removeItemFromCart( id );
                     } else if (st == true) {
                         if (flag == 1) {
                             String id = db_cart.getCartId( product_id, atr_id, "" );
                             db_cart.removeItemFromCart( id );
                         } else {
                             String col = list_color.get( col_position );
                             String id = db_cart.getCartId( product_id, atr_id, col );
                             db_cart.removeItemFromCart( id );
                         }

                         //Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();

                         //db_cart.removeItemFromCart(at_id);
                     }

                     numberButton.setVisibility( View.GONE );
                     btn_add.setVisibility( View.VISIBLE );
                 } else {
                     //  Toast.makeText( getActivity(), "Only "+stock +" in stock" ,Toast.LENGTH_SHORT ).show();


                     float qty = Float.parseFloat( String.valueOf( newValue ) );


                     String atr = String.valueOf( details_product_attribute );
                     if (atr.equals( "[]" )) {
                         double pr = Double.parseDouble( details_product_price );
                         double amt = pr * qty;
                         HashMap<String, String> mapProduct = new HashMap<String, String>();
                         String unt = String.valueOf( details_product_unit_value + " " + details_product_unit );

                         Module.setWithoutAttrIntoCart( getActivity(), "0", product_id, product_images, cat_id, details_product_name, String.valueOf( amt ),
                                 details_product_desc, details_product_rewards, details_product_price, unt, details_product_increament,
                                 details_product_stock, "", "", details_product_title, details_product_mrp, details_product_attribute, "p", qty );
                         txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );


                     } else {
                         double pr = Double.parseDouble( attribute_value );
                         double amt = pr * qty;
                         if (flag == 1) {
                             Module.setIntoCart( getActivity(), atr_id, product_id, product_images, cat_id, details_product_name, String.valueOf( amt ),
                                     details_product_desc, details_product_rewards, attribute_value, attribute_name, details_product_increament,
                                     details_product_stock, "", "", details_product_title, attribute_mrp, details_product_attribute, "a", qty );
                         } else {
                             String col = list_color.get( col_position );
                             String img = list_images.get( col_position );
                             Module.setIntoCart( getActivity(), atr_id, product_id, product_images, cat_id, details_product_name, String.valueOf( amt ),
                                     details_product_desc, details_product_rewards, attribute_value, attribute_name, details_product_increament,
                                     details_product_stock, col, img, details_product_title, attribute_mrp, details_product_attribute, "c", qty );
                         }
                         //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();


                         txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );

                     }
                 }
                 txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
             }

            }
        });





        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText( getActivity(),""+details_product_stock,Toast.LENGTH_SHORT ).show();
                            if(db_cart.getCartCount()<1)
                {
                    Toast.makeText(getActivity(),"Your cart is empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Fragment fragment=new Cart_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fragment)
                            .addToBackStack(null).commit();
                }
            }
        });




        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
        getActivity().unregisterReceiver(mWish);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
        getActivity().registerReceiver(mWish, new IntentFilter("Grocery_wish"));
    }

    @Override
    public void onStart() {
        super.onStart();

            position=-1;
        col_position=-1;

        txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
//
//
//        if(details_product_attribute.equals("[]"))
//        {
        try {
            image_list.clear();
            JSONArray array = new JSONArray(product_images);
            //Toast.makeText(this,""+product_images,Toast.LENGTH_LONG).show();
            if (product_images.equals(null)) {
                Toast.makeText(getActivity(), "There is no image for this product", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i <= array.length() - 1; i++) {
                    image_list.add(array.get(i).toString());

                }

                for(int i=0; i<image_list.size();i++)
                {
                    CustomSlider textSliderView = new CustomSlider(getActivity());
                    // initialize a SliderLayout
                    textSliderView
                            .image(BaseURL.IMG_PRODUCT_URL +image_list.get(i).toString())
                            .setScaleType(CustomSlider.ScaleType.CenterInside);
                    product_img_slider.addSlider(textSliderView);
                }




                product_img_slider.setDuration(10000);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

//
//        }
//        else
//        {
//            List<ProductVariantModel> pv=module.getAttribute(details_product_attribute);
//
//           String v=pv.get(0).getAttribute_image();
//           String s=getAttrImage(v);
//
//            Glide.with(getActivity())
//                    .load(BaseURL.IMG_PRODUCT_URL +s)
//                    .fitCenter()
//                    .placeholder(R.drawable.icon)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .dontAnimate()
//                    .into(btn);
//        }

    }

    private String getAttrImage(String v) {

        String s="";
        try
        {
            List<String> list=new ArrayList<>();
            JSONArray array=new JSONArray(v);
            for(int i=0; i<array.length();i++)
            {
                list.add(array.getString(i).toString());
            }
            s=list.get(0).toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return s;
    }



    private List<String> makeGetProductRequest(String cat_id, String product_id, final ListView listView, final ProgressBar pg,final Dialog dialog) {
        final List list=new ArrayList();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);
        params.put("product_id", product_id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.PRODUCT_DETAILS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.d(TAG, response.toString());



                try {
                    String status = response.getString("responce");
                    //    Toast.makeText(Product_Frag_details.this,"asssssssssj"+response,Toast.LENGTH_SHORT).show();

                    //    Toast.makeText(Product_Frag_details.this,""+status.toString()+"\n ",Toast.LENGTH_SHORT).show();

                    if(status.equals("true")) {


                        JSONArray jsonArray = response.getJSONArray("data");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        // List list1=new ArrayList();

                        String sdf = jsonObject.getString("size");
                        String sdf1 = jsonObject.getString("color");

                        if (sdf.isEmpty()) {
                           // txtSize.setVisibility( View.GONE );
                            pg.setVisibility(View.GONE);
                            dialog.dismiss();
                           // Toast.makeText(getActivity(), "There are no other size", Toast.LENGTH_SHORT).show();
                        } else if (sdf.equals("null")) {
                          //  txtSize.setVisibility( View.GONE );
                            pg.setVisibility(View.GONE);
                            dialog.dismiss();
                           // Toast.makeText(getActivity(), "There are no other size", Toast.LENGTH_SHORT).show();
                        } else {

                            list.add("Select Size");
                            JSONArray array = new JSONArray(sdf);
                            for (int i = 0; i < array.length(); i++) {

                                list.add(array.get(i));
                            }
                            String str[] = new String[list.size()];
                            for (int l = 0; l < list.size(); l++) {
                                str[l] = list.get(l).toString();
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, str);

                            listView.setAdapter(arrayAdapter);

                            pg.setVisibility(View.GONE);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( getActivity(),""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        return list;
    }

    private List<String> makeGetProductColorRequest(String cat_id, String product_id, final ListView listView, final ProgressBar pg,final Dialog dialog) {
        final List list=new ArrayList();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);
        params.put("product_id", product_id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.PRODUCT_DETAILS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.d(TAG, response.toString());



                try {
                    String status = response.getString("responce");
                    //    Toast.makeText(Product_Frag_details.this,"asssssssssj"+response,Toast.LENGTH_SHORT).show();

                    //    Toast.makeText(Product_Frag_details.this,""+status.toString()+"\n ",Toast.LENGTH_SHORT).show();

                    if(status.equals("true")) {


                        JSONArray jsonArray = response.getJSONArray("data");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        // List list1=new ArrayList();

                        // String sdf = jsonObject.getString("size");
                        String sdf = jsonObject.getString("color");

                        if (sdf.isEmpty()) {
                            //txtColor.setVisibility( View.GONE );
                            pg.setVisibility(View.GONE);
                            dialog.dismiss();
                            //Toast.makeText(getActivity(), "There are no other color", Toast.LENGTH_SHORT).show();
                        } else if (sdf.equals("null")) {
                           // txtColor.setVisibility( View.GONE );
                            pg.setVisibility(View.GONE);
                            dialog.dismiss();
                          //  Toast.makeText(getActivity(), "There are no other color", Toast.LENGTH_SHORT).show();
                        } else {

                            list.add("Select color");
                            JSONArray array = new JSONArray(sdf);
                            for (int i = 0; i < array.length(); i++) {

                                list.add(array.get(i));
                            }
                            String str[] = new String[list.size()];
                            for (int l = 0; l < list.size(); l++) {
                                str[l] = list.get(l).toString();
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, str);

                            listView.setAdapter(arrayAdapter);

                            pg.setVisibility(View.GONE);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(getContext().getApplicationContext(),"Error"+error.getMessage(),Toast.LENGTH_SHORT).show();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( getActivity(),""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        // return list;
        return list;
    }


    private void makeGetAttributeRequest(String product_id)
    {

        loadingBar.show();
        String json_tag="json_attr_tag";
        HashMap<String,String> params=new HashMap<>();
        params.put("product_id",product_id);
       final List<ProductVariantModel>  lst=new ArrayList<>();
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_ATTR_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                v_list.clear();

                var_respons=response;

                Log.d("attrm",response.toString());
                loadingBar.dismiss();
                try
                {
                    JSONArray jsonArr=response.getJSONArray("product_attribute");

                    // Toast.makeText(getActivity(),""+jsonArr.length(),Toast.LENGTH_SHORT).show();

                    int len=jsonArr.length();

                    if(len<=0)
                    {
      //                  flag="1";
                    }
                    else
                    {
    //                    flag="2";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            ProductVariantModel model=new ProductVariantModel();


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


                            vlist.add(model);
                            varientsAdapter.notifyDataSetChanged();
                        }
                      // Toast.makeText(getActivity(),"asdasd"+lst.size(),Toast.LENGTH_SHORT).show();
                        list_color.clear();
                        atr_id=vlist.get(0).getId();
                        atr_product_id=vlist.get(0).getProduct_id();
                        attribute_mrp=vlist.get(0).getAttribute_mrp();
                        attribute_value=vlist.get(0).getAttribute_value();
                        attribute_name=vlist.get(0).getAttribute_name();
                        txtPrice.setText(getResources().getString(R.string.currency)+vlist.get(0).getAttribute_value());
                        txtMrp.setText(getResources().getString(R.string.currency)+vlist.get(0).getAttribute_mrp());
                        Double mrp=Double.parseDouble(vlist.get(0).getAttribute_mrp());
                        final Double price=Double.parseDouble(vlist.get(0).getAttribute_value());
                        Double discount = Double.valueOf(getDiscount(vlist.get(0).getAttribute_value(),vlist.get(0).getAttribute_mrp()));
                        if (mrp > price)
                        {

                            txtPer.setText(String.valueOf(Math.round(discount)+"%"+"off"));
                        }
                        else  txtPer.setVisibility(View.GONE);
                        String str_col=vlist.get(0).getAttribute_color();
                        if(str_col.isEmpty() || str_col.equals(null))
                        {
                            flag=1;
                            rel_color.setVisibility(View.GONE);

                           // Toast.makeText(getActivity(),"empty ",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            JSONArray col_array=new JSONArray(str_col);
                            for(int j=0; j<col_array.length();j++)
                            {
                                list_color.add(col_array.getString(j).toString());
                            }
                            //colorAdapter.notifyDataSetChanged();
                            attribute_color=list_color.get(0).toString();

                            //Toast.makeText(getActivity(),""+col_array+"\n "+col_array.length(),Toast.LENGTH_SHORT).show();
                        }

                        list_images.clear();
                        String str_img=vlist.get(0).getAttribute_image();
                        if(str_img.isEmpty() || str_img.equals(null))
                        {
                            //Toast.makeText(getActivity(),"empty ",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            JSONArray col_array=new JSONArray(str_img);
                            for(int j=0; j<col_array.length();j++)
                            {
                                list_images.add(col_array.getString(j).toString());
                            }
                            attribute_img=list_images.get(0).toString();

                            //Toast.makeText(getActivity(),""+col_array+"\n "+col_array.length(),Toast.LENGTH_SHORT).show();
                        }
                        colorAdapter.notifyDataSetChanged();
                    }



                        //JSONArray jsonArr = new JSONArray(attribute);




                }
                catch (Exception ex)
                {
                    Toast.makeText(getActivity(),"err2"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
              //  Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);

     }


    private void makeRelatedProductRequest(String cat_id) {
        loadingBar.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("rett" +
                        "", response.toString());

                try {

                    Boolean status = response.getBoolean("responce");

                    if (status) {
                        //        Toast.makeText(getActivity(),""+response.getString("data"),Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RelatedProductModel>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        loadingBar.dismiss();
                        if(product_modelList.size()>0){
                            product_modelList.remove(getRemovalIndex(product_modelList,product_id));
                        }

                        if(product_modelList.size()<=0)
                        {
                            rel_relative.setVisibility( View.GONE );
                        }
                        adapter_product = new RelatedProductAdapter( getActivity(),product_modelList,product_id);
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
//                       String i= adapter_product.getItemsCount().get("cnt");
//                        Toast.makeText(getActivity(), ""+i, Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {

                                 loadingBar.dismiss();

                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                            else if(product_modelList.size()<=0)
                            {
                                rel_relative.setVisibility( View.GONE );
                            }
                        }

                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
//                    if(e.getMessage().toString().equalsIgnoreCase("No Value for "))
                       e.printStackTrace();
//                    String ex=e.getMessage();




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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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


    private void makeGetLimiteRequest() {

        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_LIMITE_SETTING_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        Double total_amount = Double.parseDouble(db_cart.getTotalAmount());


                        try {
                            // Parsing json array response
                            // loop through each json object

                            boolean issmall = false;
                            boolean isbig = false;

                            // arraylist list variable for store data;
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                int value;

                                if (jsonObject.getString("id").equals("1")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount < value) {
                                        issmall = true;
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (jsonObject.getString("id").equals("2")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount > value) {
                                        isbig = true;
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            if (!issmall && !isbig) {
                                if (sessionManagement.isLoggedIn()) {
                                    Bundle args = new Bundle();
                                    Fragment fm = new Delivery_fragment();
                                    fm.setArguments(args);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                            .addToBackStack(null).commit();
                                } else {
                                    //Toast.makeText(getActivity(), "Please login or regiter.\ncontinue", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error3: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText( getActivity(),""+ msg,Toast.LENGTH_LONG ).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

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

        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());

    }
    private void updateWishData() {

        ((MainActivity) getActivity()).setWishCounter("" + db_wish.getWishtableCount(user_id));

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag( tv.getText() );
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener( this );
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd( 0 );
                    text = tv.getText().subSequence( 0, lineEndIndex - expandText.length() + 1 ) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd( maxLine - 1 );
                    text = tv.getText().subSequence( 0, lineEndIndex - expandText.length() + 1 ) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd( tv.getLayout().getLineCount() - 1 );
                    text = tv.getText().subSequence( 0, lineEndIndex ) + " " + expandText;
                }
                tv.setText( text );
                tv.setMovementMethod( LinkMovementMethod.getInstance() );
                tv.setText(
                        addClickablePartTextViewResizable( Html.fromHtml( tv.getText().toString() ), tv, lineEndIndex, expandText,
                                viewMore ), TextView.BufferType.SPANNABLE );
            }
        } );
    }
    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "See More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    public void onClick(View view) {

    }


    public String getImages(String im)
    {
        String img="";
        try {
            image_list.clear();
            JSONArray array = new JSONArray(im);
            //Toast.makeText(this,""+product_images,Toast.LENGTH_SHORT).show();

            for (int i = 0; i <= array.length() - 1; i++) {
                image_list.add(array.get(i).toString());

            }

     img=image_list.get(0).toString();

        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return img;
    }

    private BroadcastReceiver mWish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateWishData();
            }
        }
    };


    public int getRemovalIndex(List<RelatedProductModel> list,String p_id)
    {
        int inx=-1;
        for(int i=0; i<list.size();i++){
            RelatedProductModel model=list.get(i);
            if(model.getProduct_id().toString().equals(p_id)){
                inx=i;
                break;
            }
        }
        return inx;
    }

    public void getProductDetail(String productId)
    {
        loadingBar.show();
        final HashMap<String,String> params = new HashMap<String,String>();
        params.put("product_id",productId);

        final CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, GET_PRODUCT_DETAIL_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("productDetail",params+response.toString());
                    Boolean reps = response.getBoolean("responce");
                    if (reps) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            if (object.get("hsn_number").equals("")|| object.get("hsn_number")==null) {
                                txtHsn.setVisibility(View.GONE);
                            }
                            else
                            {
                                txtHsn.setText("HSN:"+object.getString("hsn_number"));
                                txtHsn.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();
                }

                loadingBar.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }
}


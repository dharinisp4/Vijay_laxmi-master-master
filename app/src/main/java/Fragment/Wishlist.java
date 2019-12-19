package Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.Wishlist_Adapter;
import binplus.vijaylaxmi.MainActivity;
import binplus.vijaylaxmi.R;
import util.DatabaseCartHandler;
import util.Session_management;
import util.WishlistHandler;

import static Config.BaseURL.KEY_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wishlist extends Fragment {
    private static String TAG = Shop_Now_fragment.class.getSimpleName();
    private Bundle savedInstanceState;
    private WishlistHandler db_wish;
    public static ImageView no_prod_image;
    private DatabaseCartHandler db_cart;
    public static RecyclerView rv_wishlist;
    String user_id="";
    Session_management session_management;
   Dialog loadingBar;

    public Wishlist() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_wishlist, container, false );
        setHasOptionsMenu( true );

        ((MainActivity) getActivity()).setTitle( getResources().getString( R.string.wishlist ) );

        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        no_prod_image = view.findViewById( R.id.no_prod_image );
        session_management=new Session_management(getActivity());
        user_id=session_management.getUserDetails().get(KEY_ID);
        rv_wishlist = view.findViewById( R.id.rv_wishlist );
        rv_wishlist.setLayoutManager( new LinearLayoutManager( getActivity() ) );

        //db = new DatabaseHandler(getActivity());
        db_wish = new WishlistHandler( getActivity() );
        db_cart=new DatabaseCartHandler(getActivity());

        ArrayList<HashMap<String, String>> map = db_wish.getWishtableAll(user_id);

//        Log.d("cart all ",""+db_cart.getCartAll());
        if(map.size()<=0)
        {
            if(map.size()<=0)
            {
                rv_wishlist.setVisibility(View.GONE);
                no_prod_image.setVisibility(View.VISIBLE);
            }
        }
        Wishlist_Adapter adapter = new Wishlist_Adapter( map,getActivity() );
        rv_wishlist.setAdapter( adapter );
        adapter.notifyDataSetChanged();
    updateData();


        return view;
    }



    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( getActivity() );
        alertDialog.setMessage( getResources().getString( R.string.sure_del ) );
        alertDialog.setNegativeButton( getResources().getString( R.string.cancle ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );
        alertDialog.setPositiveButton( getResources().getString( R.string.yes ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                db_wish.clearWishtable(user_id);
                ArrayList<HashMap<String, String>> map = db_wish.getWishtableAll(user_id);
                Wishlist_Adapter adapter = new Wishlist_Adapter(  map,getActivity() );
                rv_wishlist.setAdapter( adapter );
                adapter.notifyDataSetChanged();

                //updateData();

                dialogInterface.dismiss();
            }
        } );

        alertDialog.show();


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

    // broadcast reciver for receive data
    private BroadcastReceiver mWish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    private void updateData() {
        ((MainActivity) getActivity()).setWishCounter("" + db_wish.getWishtableCount(user_id));
    }
    private void updateCartData() {
        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());
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
}


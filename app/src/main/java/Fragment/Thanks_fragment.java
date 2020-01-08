package Fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Fragment;

import android.app.FragmentManager;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import beautymentor.in.MainActivity;
import beautymentor.in.My_Order_activity;
import beautymentor.in.R;
import util.DatabaseCartHandler;

import static android.content.Context.MODE_PRIVATE;



public class Thanks_fragment extends Fragment implements View.OnClickListener {

    TextView tv_info;
    RelativeLayout btn_home, btn_order;
Dialog loadingBar ;
    SharedPreferences preferences;
    DatabaseCartHandler db_cart ;

    String language;
    public Thanks_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_order_thanks, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.thank_you));
        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");

        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);

        db_cart = new DatabaseCartHandler( getActivity() );
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment fm = new Home_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        String data = getArguments().getString("msg");
     String dataarb=getArguments().getString("msg");
        tv_info = (TextView) view.findViewById(R.id.tv_thank_info);
        btn_home = (RelativeLayout) view.findViewById(R.id.btn_thank_home);
        btn_order = (RelativeLayout) view.findViewById(R.id.btn_track_order);

        if (language.contains("english")) {
            tv_info.setText(Html.fromHtml(data));
        }else {
            tv_info.setText(Html.fromHtml(dataarb));       }


        btn_home.setOnClickListener(this);
        btn_order.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_thank_home) {
            Fragment fm = new Home_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }
        if (id == R.id.btn_track_order) {
            Intent myIntent = new Intent(getActivity(), My_Order_activity.class);
            getActivity().startActivity(myIntent);
        }


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

}

package Adapter;

import android.app.Activity;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Fragment.Details_Fragment;
import Interface.RecyclerViewClickListener;
import de.hdodenhof.circleimageview.CircleImageView;
import beautymentor.in.R;
import util.DatabaseCartHandler;

import static Fragment.Details_Fragment.btn;

public class AttrColorAdapter extends RecyclerView.Adapter<AttrColorAdapter.ViewHolder> {
    List<String> list;
    List<String> list_img;
    Activity activity;
    String product_id;
    String atr_id="";
    int pos=-1;
  DatabaseCartHandler db_crt;
    private RecyclerViewClickListener mListener;

//    public AttrColorAdapter(List<String> list, Activity activity, RecyclerViewClickListener mListener) {
//        this.list = list;
//        this.activity = activity;
//        this.mListener = mListener;
//    }


    public AttrColorAdapter(List<String> list, List<String> list_img, Activity activity, String product_id, String atr_id) {
        this.list = list;
        this.list_img = list_img;
        this.activity = activity;
        this.product_id = product_id;
        this.atr_id = atr_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        db_crt=new DatabaseCartHandler(activity);
        View view= LayoutInflater.from(activity).inflate(R.layout.row_atr_col_layout,null);
        ViewHolder viewHolder=new ViewHolder(view , mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // holder.txt.setBackgroundColor(Color.parseColor(list.get(position).toString()));
    //  holder.txt.setVisibility(View.GONE);
        holder.img_color.setColorFilter(Color.parseColor(list.get(position).toString()));
        if(pos==position)
         holder.img_selected.setVisibility(View.VISIBLE);
        else
        {
                 holder.img_selected.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                //Toast.makeText(activity,"atr "+atr_id,Toast.LENGTH_LONG).show();
//
                btn.setVisibility(View.VISIBLE);
                Details_Fragment.product_img_slider.setVisibility(View.GONE);
                Glide.with(activity)
                        .load(BaseURL.IMG_PRODUCT_URL +list_img.get(position).toString())
                        .fitCenter()
                        .placeholder(R.drawable.icon)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(btn);

                Details_Fragment.col_position=position;

                pos=position;
                Details_Fragment.btn_color.performClick();
                notifyDataSetChanged();

            }
        });

    //   holder.txt.setText("a");


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img_color ;
       ImageView img_selected;
     TextView txt;
        private RecyclerViewClickListener mListener;


        public ViewHolder(View itemView ,RecyclerViewClickListener listener) {

            super(itemView);

            img_color=(CircleImageView) itemView.findViewById(R.id.img_color);
            txt=(TextView) itemView.findViewById(R.id.txt);
            img_selected=(ImageView)itemView.findViewById( R.id.color_selected );
            mListener = listener;
           // img_selected.setVisibility(View.VISIBLE);

           //itemView.setOnClickListener( this );
        }

        @Override
        public void onClick(View view) {
//            mListener.onClick(view, getAdapterPosition());
            int id = view.getId();
            int position = getAdapterPosition();
           if(id == R.id.img_color)
           {
               img_selected.setVisibility( View.VISIBLE );
           }

        }
    }
}

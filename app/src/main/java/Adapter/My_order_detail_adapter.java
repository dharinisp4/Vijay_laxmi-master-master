package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import Model.My_order_detail_model;
import beautymentor.in.R;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class My_order_detail_adapter extends RecyclerView.Adapter<My_order_detail_adapter.MyViewHolder> {

    private List<My_order_detail_model> modelList;
    private Context context;
    ArrayList<String> image_list;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_price, tv_qty,txt_color;
        public ImageView iv_img;
        public LinearLayout linear_color;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_order_Detail_title);
            tv_price = (TextView) view.findViewById(R.id.tv_order_Detail_price);
            tv_qty = (TextView) view.findViewById(R.id.tv_order_Detail_qty);
            txt_color = (TextView) view.findViewById(R.id.txt_color);
            iv_img = (ImageView) view.findViewById(R.id.iv_order_detail_img);
            linear_color = (LinearLayout) view.findViewById(R.id.linear_color);
            image_list=new ArrayList<>();

        }
    }

    public My_order_detail_adapter(List<My_order_detail_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_order_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_order_detail_rv, parent, false);

        context = parent.getContext();

        return new My_order_detail_adapter.MyViewHolder(itemView);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(My_order_detail_adapter.MyViewHolder holder, int position) {
        My_order_detail_model mList = modelList.get(position);


        try {
            image_list.clear();
            JSONArray array = new JSONArray(mList.getProduct_image());
            //Toast.makeText(this,""+product_images,Toast.LENGTH_LONG).show();

            if(mList.getAtr_img().equalsIgnoreCase("") || mList.getAtr_img().isEmpty())
            {
                if (mList.getProduct_image().equals(null)) {
                    Glide.with(context)
                            .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                            .centerCrop()
                            .placeholder(R.drawable.icon)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(holder.iv_img);
                } else {
                    for (int i = 0; i <= array.length() - 1; i++) {
                        image_list.add(array.get(i).toString());

                    }
                }
                Glide.with(context)
                        .load(BaseURL.IMG_PRODUCT_URL + image_list.get(0))
                        .centerCrop()
                        .placeholder(R.drawable.icon)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.iv_img);
            }
            else
            {
                Glide.with(context)
                        .load(BaseURL.IMG_PRODUCT_URL + mList.getAtr_img())
                        .centerCrop()
                        .placeholder(R.drawable.icon)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.iv_img);
            }


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        String p_color="";
        String p_name="";
       String name=mList.getProduct_name().toString();
        if(name.contains("#"))
        {
            String[] str=name.split("#");
            p_name=str[0].toString();
            p_color="#"+str[1].toString();
            holder.linear_color.setVisibility(View.VISIBLE);
            holder.txt_color.setBackgroundColor(Color.parseColor(p_color));

        }
        else
        {
            p_name=name;
        }

        holder.tv_title.setText(p_name);
        holder.tv_price.setText(context.getResources().getString(R.string.currency)+mList.getUnit_value()+"/"+mList.getUnit());
        holder.tv_qty.setText(mList.getQty());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
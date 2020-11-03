package Adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.DetailProductModel;
import beautymentor.in.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 03,November,2020
 */
public class ProductGstAdapter extends RecyclerView.Adapter<ProductGstAdapter.ViewHolder> {
    Activity activity;
    List<DetailProductModel> list;

    public ProductGstAdapter(Activity activity, List<DetailProductModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(activity).inflate(R.layout.row_gst_rv,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      DetailProductModel model=list.get(position);
      holder.tv_title.setText(model.getProduct_name());
      holder.tv_price.setText(activity.getResources().getString(R.string.currency)+model.getPrice());
      holder.tv_mrp.setText(activity.getResources().getString(R.string.currency)+model.getMrp());
      holder.tv_mrp.setPaintFlags(holder.tv_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int pp =Integer.parseInt(model.getPrice());
        int mm =Integer.parseInt(model.getMrp());
        if (mm>pp) {
            int discount = getDiscount(model.getPrice(), model.getMrp());
            holder.tv_dis.setText("" + Math.round(discount) + "% OFF");
        }
        else {
            holder.tv_mrp.setVisibility(View.GONE);
            holder.tv_dis.setVisibility(View.GONE);
        }
        holder.tv_gst.setText(model.getGst().toString()+ " % GST included");
        if(model.getGst().equals("0")){
            holder.tv_gst.setVisibility(View.GONE);
        }else{
            holder.tv_gst.setVisibility(View.VISIBLE);
        }
        Glide.with(activity)
                .load(model.getImg_url().toString())
                //  .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_price,tv_dis,tv_mrp,tv_gst;
        ImageView iv_icon;
        public ViewHolder(View v) {
            super(v);
            tv_title=v.findViewById(R.id.tv_title);
            tv_price=v.findViewById(R.id.tv_price);
            tv_dis=v.findViewById(R.id.tv_dis);
            tv_mrp=v.findViewById(R.id.tv_mrp);
            tv_gst=v.findViewById(R.id.tv_gst);
            iv_icon=v.findViewById(R.id.iv_icon);

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
}

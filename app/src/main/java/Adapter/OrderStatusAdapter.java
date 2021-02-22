package Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tcqq.timelineview.TimelineView;

import java.util.ArrayList;

import Model.OrderStatusModel;
import beautymentor.in.R;


public  class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>
{
    ArrayList<OrderStatusModel> stat_list;
    Context activity;

    public OrderStatusAdapter(ArrayList<OrderStatusModel> stat_list, Context activity) {
        this.stat_list = stat_list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_order_status,null);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name ="";
        switch (stat_list.get(position).getStatus())
        {
            case "0" :
                name = "Order Placed";
                break;
            case "1" :
                name = "Order Confirmed";
                break;
                case "2" :
                name = activity.getResources().getString(R.string.outfordeliverd);
                break;
                case "3" :
                name = "Order Cancelled";
                break;
                case "4" :
               if( stat_list.get(position).getTracking_status().equalsIgnoreCase("1"))
                {
                    name = "Order Delivered";
                }
                else
                {
                    name = "Expected Delivery";
                    stat_list.get(position).setUpdated_at(stat_list.get(position).getComment()+" "+"00:00:00");
                }
                break;
        }
        holder.tv_status.setText(name);
        if (stat_list.get(position).getUpdated_at()==null|| stat_list.get(position).getUpdated_at().equalsIgnoreCase("null")) {
            holder.tv_date.setText("");
        }
        else
        {
            String s[] = stat_list.get(position).getUpdated_at().split(" ");
            String d[] = s[0].split("-");

            holder.tv_date.setText(d[2]+"-"+d[1]+"-"+d[0]);
        }
        if (stat_list.get(position).isChecked())
        {
            holder.iv_order_status.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.color_1)));
            holder.v1.setBackgroundColor(activity.getResources().getColor(R.color.black));
            holder.v2.setBackgroundColor(activity.getResources().getColor(R.color.black));
            holder.timelineView.setMarkerColor(activity.getResources().getColor(R.color.color_1));
            holder.timelineView.getMarker().setColorFilter(activity.getResources().getColor(R.color.color_1), PorterDuff.Mode.DARKEN);
        }
        else
        {
            holder.iv_order_status.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.white)));
            holder.v1.setBackgroundColor(activity.getResources().getColor(R.color.gray));
            holder.v2.setBackgroundColor(activity.getResources().getColor(R.color.gray));
            holder.timelineView.setMarkerColor(activity.getResources().getColor(R.color.dark_gray));
        }

    }

    @Override
    public int getItemCount() {
        return stat_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_status ,tv_date ;
        ImageView iv_order_status;
        View v1 ,v2;
        TimelineView timelineView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_status = itemView.findViewById(R.id.tv_order_status);
            tv_date = itemView.findViewById(R.id.tv_order_date);
            timelineView = itemView.findViewById(R.id.order_timeline);
            v1 = itemView.findViewById(R.id.view1);
            v2 = itemView.findViewById(R.id.view2);
            iv_order_status = itemView.findViewById(R.id.iv_order_status);

        }
    }
}

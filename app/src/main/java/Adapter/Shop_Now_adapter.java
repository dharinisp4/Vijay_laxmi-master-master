package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import Model.ShopNow_model;
import beautymentor.in.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Shop_Now_adapter extends RecyclerView.Adapter<Shop_Now_adapter.MyViewHolder> {

    private List<ShopNow_model> modelList;
    private Context context;
    int count;
    String language;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.service_text);
            image = (ImageView) view.findViewById(R.id.service_image);
        }
    }

    public Shop_Now_adapter(List<ShopNow_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Shop_Now_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_all_catogaries, parent, false);

        context = parent.getContext();

        return new Shop_Now_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Shop_Now_adapter.MyViewHolder holder, int position) {
        ShopNow_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getImage())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.title.setText(mList.getTitle());
        }
        else {
            holder.title.setText(mList.getTitle());

        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}


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
import Model.BrandModel;
import beautymentor.in.R;

import static android.content.Context.MODE_PRIVATE;

public class TopBrandsAdapter extends RecyclerView.Adapter<TopBrandsAdapter.Holder> {
    private List<BrandModel> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;

    public TopBrandsAdapter(List<BrandModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.row_topbrands, parent, false);

        context = parent.getContext();

        return new TopBrandsAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        BrandModel mList = modelList.get(position);

        Glide.with(context)
                .load( BaseURL.IMG_CATEGORY_URL + mList.getBrand_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.brand_icon);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
//        if (language.contains("english")) {
//            holder.title.setText(mList.getTitle());
//        }
//        else {
//            holder.title.setText(mList.getTitle());
//
//        }

    }




    @Override
    public int getItemCount() {
        return  modelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView brand_icon ;
        TextView brand_name;

        public Holder(View itemView) {
            super( itemView );
            brand_icon = itemView.findViewById( R.id.brandlogo );
            brand_name=itemView.findViewById( R.id.brandname );

        }
    }
}

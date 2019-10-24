package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Fragment.Details_Fragment;
import Model.ProductVariantModel;
import binplus.vijaylaxmi.R;

import static Fragment.Details_Fragment.btn;

public class VarientsAdapter extends RecyclerView.Adapter<VarientsAdapter.ViewHolder> {
    Context context;
    List<ProductVariantModel> varientList ;

    ProductVariantModel list;
    int pos=-1;

    int row_index=-1;
    public VarientsAdapter(Context context, List<ProductVariantModel> varientList) {
        this.context = context;
        this.varientList = varientList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate( R.layout.row_weight_layout,parent,false );
    ViewHolder viewHolder = new ViewHolder( view );
    return  viewHolder ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ProductVariantModel model=varientList.get(position);
        holder.txt_weight.setText(model.getAttribute_name());

        if(pos==position)
            holder.rel_click.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
        else
            holder.rel_click.setCardBackgroundColor(Color.parseColor("#ffffff"));
        holder.rel_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pos=position;
                notifyDataSetChanged();
                Details_Fragment.position=position;
                Details_Fragment.btn_adapter.performClick();


            }
        });
    }

    @Override
    public int getItemCount() {
        return varientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      public CardView rel_click;
        TextView txt_weight ;

        public ViewHolder(View itemView) {
            super( itemView );
            rel_click=(CardView) itemView.findViewById(R.id.rel_click);
            txt_weight=(TextView) itemView.findViewById( R.id.txt_weight);
        }
    }

    public ProductVariantModel getData()
    {
       return list;
    }

}

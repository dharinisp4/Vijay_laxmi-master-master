package Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Fragment.Details_Fragment;
import Model.ProductVariantModel;
import beautymentor.in.R;

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

        if(pos==position) {
            holder.rel_click.setCardBackgroundColor( context.getResources().getColor( R.color.pink ) );
            holder.txt_weight.setTextColor( context.getResources().getColor( R.color.white ) );
        }
        else {
            holder.rel_click.setCardBackgroundColor( Color.parseColor( "#ffffff" ) );
            holder.txt_weight.setTextColor( Color.BLACK );
        }
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

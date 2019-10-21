package Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Fragment.Details_Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import trolley.tcc.R;

public class AttrColorAdapter extends RecyclerView.Adapter<AttrColorAdapter.ViewHolder> {
    List<String> list;
    Activity activity;

    public AttrColorAdapter(List<String> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(activity).inflate(R.layout.row_atr_col_layout,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // holder.txt.setBackgroundColor(Color.parseColor(list.get(position).toString()));
    //  holder.txt.setVisibility(View.GONE);
        holder.img_color.setColorFilter(Color.parseColor(list.get(position).toString()));
//        holder.img_color.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.img_selected.setVisibility( View.VISIBLE );
//            }
//        } );
       holder.txt.setText("a");


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img_color ;
        ImageView img_selected;
     TextView txt;
        public ViewHolder(View itemView) {

            super(itemView);

            img_color=(CircleImageView) itemView.findViewById(R.id.img_color);
            txt=(TextView) itemView.findViewById(R.id.txt);
            img_selected=(ImageView)itemView.findViewById( R.id.color_selected );
            img_color.setOnClickListener( this );
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
           if(id == R.id.img_color)
           {
               img_selected.setVisibility( View.VISIBLE );
           }
        }
    }
}

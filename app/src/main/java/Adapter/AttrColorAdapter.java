package Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Interface.RecyclerViewClickListener;
import de.hdodenhof.circleimageview.CircleImageView;
import binplus.vijaylaxmi.R;

public class AttrColorAdapter extends RecyclerView.Adapter<AttrColorAdapter.ViewHolder> {
    List<String> list;
    Activity activity;
    private RecyclerViewClickListener mListener;

//    public AttrColorAdapter(List<String> list, Activity activity, RecyclerViewClickListener mListener) {
//        this.list = list;
//        this.activity = activity;
//        this.mListener = mListener;
//    }
        public AttrColorAdapter(List<String> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(activity).inflate(R.layout.row_atr_col_layout,null);
        ViewHolder viewHolder=new ViewHolder(view , mListener);
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
        private RecyclerViewClickListener mListener;


        public ViewHolder(View itemView ,RecyclerViewClickListener listener) {

            super(itemView);

            img_color=(CircleImageView) itemView.findViewById(R.id.img_color);
            txt=(TextView) itemView.findViewById(R.id.txt);
            img_selected=(ImageView)itemView.findViewById( R.id.color_selected );
            mListener = listener;
           itemView.setOnClickListener( this );
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

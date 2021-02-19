package Adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Config.BaseURL;
import Model.VideoModel;
import beautymentor.in.R;
import beautymentor.in.YoutubeActivity;

import static Config.BaseURL.YOUTUBE_API_KEY;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
    Activity activity;
    ArrayList<VideoModel> list;
    private final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
    String v_id="";

    public VideosAdapter(Activity activity, ArrayList<VideoModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_videos,null);
        ViewHolder holder = new ViewHolder(view);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final VideoModel model = list.get(position);
        String url = model.getVid_url();
         v_id = getVideoId(url);
        String v_url = url.substring(17,url.length());
        String img_url = BaseURL.IMG_VIDEOS_URL+model.getVid_img();
           setThumbnail(holder.tv_youtube,v_id);
//         setThumbnail(holder.tv_youtube,img_url);
         holder.tv_desc.setText(model.getDescription());
         holder.tv_title.setText(model.getTitle());
        holder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, YoutubeActivity.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("desc",model.getDescription());
                intent.putExtra("v_link",v_id);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_delete;
        Button btn_edit,btn_status;
        RelativeLayout rel_main;
        TextView tv_title,tv_desc;
        YouTubeThumbnailView tv_youtube;

        public ViewHolder(@NonNull View v) {
            super(v);
//            iv_delete=(ImageView)v.findViewById(R.id.iv_delete);
//            btn_edit=(Button)v.findViewById(R.id.btn_edit);
//            btn_status=(Button)v.findViewById(R.id.btn_status);
            rel_main=(RelativeLayout)v.findViewById(R.id.rel_main);
            tv_title=v.findViewById(R.id.tv_title);
            tv_desc=v.findViewById(R.id.tv_desc);
            tv_youtube = v.findViewById(R.id.tb_youtube);

        }
    }
    public void setThumbnail(YouTubeThumbnailView tb_youtube , final String link)
    {
        tb_youtube.initialize(YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
                youTubeThumbnailLoader.setVideo(link);

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0){
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

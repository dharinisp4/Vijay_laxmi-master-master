package beautymentor.in;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import beautymentor.in.networkconnectivity.NetworkConnection;
import beautymentor.in.networkconnectivity.NoInternetConnection;


import static Config.BaseURL.YOUTUBE_API_KEY;


public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayer YPlayer;
    String v_link  ,link ="";
    TextView tv_title,tv_desc;
    private static final int RECOVERY_DIALOG_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_desc=(TextView)findViewById(R.id.tv_desc);
        v_link = getIntent().getStringExtra("v_link");

        Log.e("v_link",v_link+ "\n" +link);
        tv_title.setText(getIntent().getStringExtra("title"));
        tv_desc.setText(getIntent().getStringExtra("desc"));
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        if(NetworkConnection.connectionChecking(YoutubeActivity.this))
        {
            youTubeView.initialize(YOUTUBE_API_KEY, this);
        }
        else
        {
         Intent intent = new Intent(YoutubeActivity.this, NoInternetConnection.class);
         startActivity(intent);
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        YPlayer = youTubePlayer;
        /*
         * Now that this variable YPlayer is global you can access it
         * throughout the activity, and perform all the player actions like
         * play, pause and seeking to a position by code.
         */
        if (!b) {
            YPlayer.cueVideo(v_link);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);

        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeView);
    }
}

package Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import beautymentor.in.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

ProgressDialog loadingBar ;
    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadingBar=new ProgressDialog(getActivity());
        loadingBar.setContentView( R.layout.progressbar);
        loadingBar.setCanceledOnTouchOutside(false);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

}

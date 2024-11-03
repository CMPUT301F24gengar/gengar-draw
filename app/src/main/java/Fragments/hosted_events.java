package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;

/**
 * "Hosted Events" fragment hander
 */
public class hosted_events extends Fragment {
    //activity views
    //data
    public hosted_events(){
        //required empty constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_events, container, false);//TODO use recycler view to fill listview correctly
    }
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }
    // replace current fragment with Joined Events fragment
    private void openJoinedEventsFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new my_events()).commit();
        } else {
            // Handle error
        }
    }
}

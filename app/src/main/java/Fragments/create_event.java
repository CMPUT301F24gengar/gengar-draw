package Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import Classes.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link create_event#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class create_event extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //data
    private UserProfile user;
    private ImageView eventImage;
    private EditText titleEditText, registrationOpensEditText, registrationDeadlineEditText;
    private EditText eventStartsEditText, maxWinnersEditText, maxEntrantsEditText;
    private EditText detailsEditText;
    private CheckBox checkboxCheckBox;


    public create_event() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_event.
     */
    // TODO: Rename and change types and number of parameters
    public static create_event newInstance(String param1, String param2) {
        create_event fragment = new create_event();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_create_event, container, false);
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        return view;
    }
}
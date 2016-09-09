package org.wcodes.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wcodes.android.R;
import org.wcodes.core.Dictionary;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoredFragment extends Fragment {

    static org.wcodes.core.Dictionary dictionary;

    public StoredFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment StoredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoredFragment newInstance(Dictionary dictionary) {
        StoredFragment fragment = new StoredFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        StoredFragment.dictionary = dictionary;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the button_phonenumber for this fragment
        return inflater.inflate(R.layout.fragment_stored, container, false);
    }

}

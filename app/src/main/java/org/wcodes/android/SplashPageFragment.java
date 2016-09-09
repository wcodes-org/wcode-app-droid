package org.wcodes.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashPageFragment extends Fragment implements  ViewPager.OnPageChangeListener {

    private static final String ARG_INDEX = "id";

    private int mArgIndex;

//    private OnFragmentInteractionListener mListener;

    public SplashPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id
     * @return A new instance of fragment SplashPageFragment.
     */

    public static SplashPageFragment newInstance(int id) {
        SplashPageFragment fragment = new SplashPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArgIndex = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImageView rootView = (ImageView) inflater.inflate(
                R.layout.fragment_splash_page, container, false);
        int idDrawable;
        switch (mArgIndex) {
            case 0: idDrawable = R.drawable.comics_1; break;
            case 1: idDrawable = R.drawable.comics_2; break;
            case 2: idDrawable = R.drawable.comics_3; break;
            case 3: idDrawable = R.drawable.comics_4; break;
            default: idDrawable = 0;
        }
        rootView.setImageResource(idDrawable);

        return rootView;
    }

    @Override
    public void onPageSelected(int iPage) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

// TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

}

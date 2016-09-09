package org.wcodes.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import org.wcodes.core.Code;
import org.wcodes.core.Data;
import org.wcodes.core.Dictionary;
import org.wcodes.core.Header;

import java.math.BigInteger;

public class DecodeFragment extends Fragment {

//    private static final String ARG_PARAM1 = "param1";
//    AlphaTree alphaTree;

    static org.wcodes.core.Dictionary dictionary;

    GridView mGridView;
    CodeType codeType;

    EditText input;
    EditText output;

    public DecodeFragment() {
    }

    public static DecodeFragment newInstance(Dictionary dictionary) {
        DecodeFragment fragment = new DecodeFragment();
        DecodeFragment.dictionary = dictionary;
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decode, container, false);
        codeType = new CodeType(getContext(), this);
        mGridView = (GridView) view.findViewById(R.id.gridLayout);
        mGridView.setAdapter(codeType.getTypeAdapter());

        input = (EditText) view.findViewById(R.id.editTextA);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setOnEditorActionListener(mInputDoneListener);
        output = (EditText) view.findViewById(R.id.editTextB);

        return view;
    }

    public void changeMode(DataObjectType dataObjectType) {
        switch (dataObjectType) {
            case AUTO_DOT:
                Answers.getInstance().logCustom(new CustomEvent("Auto mode"));
                break;
            case PHONE_NUMBER_DOT:
                input.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                Answers.getInstance().logCustom(new CustomEvent("Phone number mode"));
                break;
            case EMAIL_DOT:
                Answers.getInstance().logCustom(new CustomEvent("email mode"));
                break;
            case LOCATION_DOT:
                Answers.getInstance().logCustom(new CustomEvent("location mode"));
                break;
            case TIME_DOT:
                Answers.getInstance().logCustom(new CustomEvent("Date mode"));
                break;
            case DATE_DOT:
                break;
            case URL_DOT:
                Answers.getInstance().logCustom(new CustomEvent("URL mode"));
                break;
            case TEXT_DOT:
                Answers.getInstance().logCustom(new CustomEvent("Text mode"));
                break;
        }
    }

    TextView.OnEditorActionListener mInputDoneListener = ((v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            convert(codeType.getMode());
        }
        return false;
    });

    private void convert(DataObjectType dataObjectType) {
        Header header = new Header();
        Code code = new Code(dictionary, header, false);
        Data data = new Data(header, false, 8);
        byte[] outData;
        String message = input.getText().toString();
        String output;
        switch (dataObjectType) {
            case AUTO_DOT:
                output = "";
                break;
            case TEXT_DOT:
                output = "";
                break;
            case PHONE_NUMBER_DOT:
                outData = code.out(data, message);
                BigInteger bgI = new BigInteger(outData);
                output = bgI.toString();
                break;
            default:
                output = "";
        }
        this.output.setText(output);
    }

}

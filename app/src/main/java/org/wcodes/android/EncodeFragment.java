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

public class EncodeFragment extends Fragment {

//    private static final String ARG_DICTIONARY = "dictionary";

    static org.wcodes.core.Dictionary dictionary;
//    AlphaTree alphaTree;

    GridView mGridView;
    CodeType codeType;
//    TypeAdapter mTypeAdapter;

    EditText input;
    EditText output;

    public EncodeFragment() {
//        EncodeFragment.dictionary = (Dictionary)this.getArguments().get(ARG_DICTIONARY);
    }

    public static EncodeFragment newInstance(Dictionary dictionary) {
        EncodeFragment fragment = new EncodeFragment();
        EncodeFragment.dictionary = dictionary;
//        Bundle args = new Bundle();
//        args.putParcelable(ARG_DICTIONARY, dictionary);
//        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            dictionary = getArguments().getString(ARG_DICTIONARY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encode, container, false);
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
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
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
        byte[] inData;
        switch (dataObjectType) {
            case AUTO_DOT:
                inData = input.getText().toString().getBytes();
                break;
            case TEXT_DOT:
                inData = input.getText().toString().getBytes();
                break;
            case PHONE_NUMBER_DOT:
                //inData = new byte[4];
                //Package.packInt(inData, Integer.parseInt(input.getText().toString()));
                BigInteger bgI = new BigInteger(input.getText().toString());
                inData = bgI.toByteArray();

                break;
            default:
                inData = null;
        }
        String message = code.in(data, inData, 0);
        output.setText(message);
    }

}

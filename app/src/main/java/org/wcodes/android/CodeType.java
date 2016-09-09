package org.wcodes.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

/**
 * Created by Ujjwal on 9/8/2016.
 */
public class CodeType {

    DataObjectType dataObjectType;
    ToggleButton currentModeButton;
    Object mCodeFragment;

    private DataObjectType[] daoArray = {
            DataObjectType.AUTO_DOT,
            DataObjectType.PHONE_NUMBER_DOT,
            DataObjectType.EMAIL_DOT,
            DataObjectType.LOCATION_DOT,
            DataObjectType.TIME_DOT,
            DataObjectType.DATE_DOT,
            DataObjectType.URL_DOT,
            DataObjectType.TEXT_DOT };

    private class TypeAdapter extends BaseAdapter {

        private View.OnClickListener mOnButtonClick;
        private Context mContext;

        TypeAdapter(Context context) {
            mContext = context;
        }

        public void setOnButtonClickListener() {
            mOnButtonClick = view -> {
                ToggleButton button = (ToggleButton) view;
                dataObjectType = (DataObjectType) button.getTag();
                if(mCodeFragment.getClass() == EncodeFragment.class)
                    ((EncodeFragment) mCodeFragment).changeMode(dataObjectType);
                else
                    ((DecodeFragment) mCodeFragment).changeMode(dataObjectType);
                //mode = dataObjectType;
                clearModeButton(button);
            };
        }

        @Override
        public int getCount() {
            return DataObjectType.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ToggleButton button;
            DataObjectType dataObjectType = daoArray[i];
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (dataObjectType) {
                case AUTO_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_auto, null);
                    break;
                case PHONE_NUMBER_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_phonenumber, null);
                    break;
                case EMAIL_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_email, null);
                    break;
                case LOCATION_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_location, null);
                    break;
                case TIME_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_time, null);
                    break;
                case DATE_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_date, null);
                    break;
                case URL_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_url, null);
                    break;
                default:
                case TEXT_DOT:
                    button = (ToggleButton) inflater.inflate(R.layout.button_text, null);
                    break;
            }
            button.setOnClickListener(mOnButtonClick);
            button.setTag(dataObjectType);
            return button;
        }
    }

    TypeAdapter mTypeAdapter;

    public CodeType(Context context, Object fragment) {
        mTypeAdapter = new TypeAdapter(context);
        mCodeFragment = fragment;
    }

    public TypeAdapter getTypeAdapter() {
        mTypeAdapter.setOnButtonClickListener();
        return mTypeAdapter;
    }

    public DataObjectType getMode() {
        return dataObjectType;
    }

    private void clearModeButton(ToggleButton newModeButton) {
        newModeButton.setChecked(true);
        if (currentModeButton != null && currentModeButton != newModeButton)
            currentModeButton.setChecked(false);
        currentModeButton = newModeButton;
    }

}


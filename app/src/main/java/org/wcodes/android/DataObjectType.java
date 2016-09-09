package org.wcodes.android;

/**
 * Created by Ujjwal on 8/30/2016.
 */
public enum DataObjectType {
    AUTO_DOT("Auto"),
    PHONE_NUMBER_DOT("Phone number"),
    EMAIL_DOT("Email"),
    LOCATION_DOT("Location"),
    TIME_DOT("Time"),
    DATE_DOT("Date"),
    URL_DOT("URL"),
    TEXT_DOT("Text");

    private final String mLabel;
    static final int mNEncodeOptions = DataObjectType.values().length;

    DataObjectType(String label) {
        mLabel = label;
    }

    public String label() {
        return mLabel;
    }

    public static int length() {
        return mNEncodeOptions;
    }

}

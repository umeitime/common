package com.umeitime.common.helper;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract {

    public static final String CONTENT_AUTHORITY = "com.umeitime.android.userinfoprovider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_UMEITIME= "umeitime";
    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_UMEITIME).build();
        public static final String TABLE_NAME = "userinfo";
        public static final String COLUMN_UID = "uid";
        public static final String COLUMN_NAME = "name";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
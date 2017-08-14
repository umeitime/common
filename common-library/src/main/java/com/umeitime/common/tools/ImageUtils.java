package com.umeitime.common.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;
        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);
        String pre1 = "file:///sdcard" + File.separator;
        String pre2 = "file:///mnt/sdcard" + File.separator;
        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = {"_data"};
        Cursor cursor = context.managedQuery(uri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow("_data");
            if ((cursor.getCount() > 0) && (cursor.moveToFirst())) {
                imagePath = cursor.getString(column_index);
            }
        }
        return imagePath;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getUriFromPath(Context context, String path) {
        Uri mUri = Uri.parse("content://media/external/images/media");
        Uri mImageUri = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA}, MediaStore.Images.Media.DATA + " like ? ",
                new String[]{"%/sujian/%"}, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATA));
                System.out.println(data);
                if (path.equals(data)) {
                    int ringtoneID = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.MediaColumns._ID));
                    mImageUri = Uri.withAppendedPath(mUri, ""
                            + ringtoneID);
                    break;
                }
            }
        }
        return mImageUri == null ? "" : mImageUri.toString();
    }
}

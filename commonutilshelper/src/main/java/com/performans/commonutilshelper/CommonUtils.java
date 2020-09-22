package com.performans.commonutilshelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.exifinterface.media.ExifInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    private static final String DATE_STRING_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static int getAppVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getOsVersion() {
        double release = Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)", "$1"));
        String codeName = "Unsupported";
        if (release >= 4.1 && release < 4.4) codeName = "Jelly Bean";
        else if (release < 5) codeName = "Kit Kat";
        else if (release < 6) codeName = "Lollipop";
        else if (release < 7) codeName = "Marshmallow";
        else if (release < 8) codeName = "Nougat";
        else if (release < 9) codeName = "Oreo";
        return codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String encodeBase64(String text) throws UnsupportedEncodingException {
        if (text != null && !text.equals("")) {
            byte[] data = text.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            return base64.replace("\n", "");
        } else {
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String decodeBase64(String base64) throws UnsupportedEncodingException {
        if (base64 != null && !base64.equals("")) {
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            return new String(data, StandardCharsets.UTF_8);
        } else {
            return "";
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNullOrWhitespace(String s) {
        return s == null || isWhitespace(s);

    }

    public static boolean isWhitespace(String s) {
        try {
            int length = s.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(s.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static ProgressDialog showProgressDialog(Activity activity) {
        return showProgressDialog(activity, activity.getResources().getString(R.string.str_loading));
    }

    public static ProgressDialog showProgressDialog(Activity activity, String text) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(text);
        progressDialog.setProgressStyle(android.R.style.Theme_Holo_Dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        return progressDialog;
    }

    public static ProgressDialog showProgressDialogWithProgress(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getResources().getString(R.string.str_loading));
        progressDialog.setProgressStyle(android.R.style.Theme_Holo_Dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.show();
        return progressDialog;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidJSON(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static void scrollToUp(final NestedScrollView scroll) {
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scroll.fullScroll(View.FOCUS_UP);
    }

    public static void scrollToDown(final NestedScrollView scroll) {
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scroll.fullScroll(View.FOCUS_DOWN);
    }

    public static int boolToInt(boolean val) {
        return val ? 1 : 0;
    }

    public static boolean intToBool(int v) {
        return v == 1;
    }

    public static double getBatteryPercentage(Context context) {
        try {
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);
            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
            float batteryPct = level / (float) scale;
            return batteryPct;
        } catch (Exception e) {
            return -1;
        }
    }

    public static int getBatteryStatus(Context context) {
        try {
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);
            return batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        String[] places = {"/sbin/", "/system/bin/", "/system/xbin/",
                "/data/local/xbin/", "/data/local/bin/",
                "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
        for (String where : places) {
            if (new File(where + binaryName).exists()) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static boolean isRooted() {
        return findBinary("su");
    }

    public static String encodeUrl(String in) {
        try {
            return URLEncoder.encode(in, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "encodeUrl: " + e.getMessage(), e);
            return "";
        }
    }


    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void viewOnMap(Context context, String address) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=" + encodeUrl(address)));
        context.startActivity(intent);
    }

    public static void sendSMS(Context context, String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[\\D]", "");
        if (isPhoneNumberValid(phoneNumber)) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber)));
        } else {
            Toast.makeText(context, R.string.str_invalid_phone_number, Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendEmail(Context context, String email) {
        if (isEmailValid(email)) {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse("mailto:" + email));
                context.startActivity(emailIntent);
            } catch (Exception e) {
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
                    context.startActivity(Intent.createChooser(emailIntent, "Eposta Gönder"));
                } catch (Exception ee) {
                    Toast.makeText(context, R.string.str_cant_send_email, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, R.string.str_invalid_email, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openWebSite(Context context, String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        context.startActivity(webIntent);
    }

    public static void dialPhone(Context context, String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[\\D]", "");
        if (isPhoneNumberValid(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.str_invalid_phone_number, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isPhoneNumberValid(String phoneNo) {
        try {
            phoneNo = phoneNo.trim();

            if (phoneNo.equalsIgnoreCase("")) {
                return false;
            }
            if (phoneNo.length() < 6 || phoneNo.length() > 13) {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (width_tmp / 2 >= requiredSize && height_tmp / 2 >= requiredSize) {
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    public static String generateFileName() {
        return generateFileName("jpg");
    }

    public static String generateFileName(String fileExt) {
        return generateFileName(fileExt, "");
    }

    public static String generateFileName(String fileExt, String preFix) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        return preFix + formatter.format(now) + "." + fileExt;
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Context context,
                                               Uri selectedImage) throws IOException {

        if (selectedImage != null && selectedImage.getScheme() != null && selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c != null && !c.isClosed() && c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            assert selectedImage != null;
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //Timber.d("orientation: %s", orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyBoard(Activity mActivity) {
        try {
            View view = mActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager keyboard = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNow() {
        return getNow(Calendar.getInstance().getTime(), DATE_STRING_FORMAT);
    }

    public static String getNow(String format) {
        return getNow(Calendar.getInstance().getTime(), format);
    }

    public static String getNow(Date date) {
        return getNow(date, DATE_STRING_FORMAT);
    }

    public static String getNow(long timeInMillis) {
        return getNow(new Date(timeInMillis), DATE_STRING_FORMAT);
    }

    public static String getNow(Date date, String format) {
        if (date == null) date = Calendar.getInstance().getTime();
        if (isNullOrWhitespace(format)) format = DATE_STRING_FORMAT;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static long getLongFromStringTime(String timeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = sdf.parse(timeString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    public static String randomString(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static boolean isVersionHigher(String baseVersion, String testVersion) {
        System.out.println("versionToComparable( baseVersion ) =" + versionToComparable(baseVersion));
        System.out.println("versionToComparable( testVersion ) =" + versionToComparable(testVersion) + " is this higher ?");
        return versionToComparable(testVersion).compareTo(versionToComparable(baseVersion)) >= 0;
    }

    private static String versionToComparable(String version) {
//        System.out.println("version - " + version);
        String versionNum = version;
        int at = version.indexOf('-');
        if (at >= 0)
            versionNum = version.substring(0, at);

        String[] numAr = versionNum.split("\\.");
        String versionFormatted = "0";
        for (String tmp : numAr) {
            versionFormatted += String.format("%4s", tmp).replace(' ', '0');
        }
        while (versionFormatted.length() < 12)  // pad out to aaaa.bbbb.cccc
        {
            versionFormatted += "0000";
        }
//        System.out.println( "converted min version =" + versionFormatted + "=   : " + versionNum );
        return versionFormatted + getVersionModifier(version, at);
    }

    private static String getVersionModifier(String version, int at) {
//        System.out.println("version - " + version );
        String[] wordModsAr = {"-SNAPSHOT", "-ALPHA", "-BETA", "-RC", "-RELEASE"};

        if (at < 0)
            return "." + wordModsAr.length + "00";   // make nothing = RELEASE level

        int i = 1;
        for (String word : wordModsAr) {
            if ((at = version.toUpperCase().indexOf(word)) > 0)
                return "." + i + getSecondVersionModifier(version.substring(at + word.length()));
            i++;
        }

        return ".000";
    }

    private static String getSecondVersionModifier(String version) {
        System.out.println("second modifier =" + version + "=");
        Matcher m = Pattern.compile("(.*?)(\\d+).*").matcher(version);
//        if ( m.matches() )
//            System.out.println( "match ? =" + m.matches() + "=   m.group(1) =" + m.group(1) + "=   m.group(2) =" + m.group(2) + "=   m.group(3) =" + (m.groupCount() >= 3 ? m.group(3) : "x") );
//        else
//            System.out.println( "No match" );
        return m.matches() ? String.format("%2s", m.group(2)).replace(' ', '0') : "00";
    }

    public static List<String> convertToList(JSONArray jsonArray) {
        ArrayList<String> listdata = new ArrayList<String>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                listdata.add(jsonArray.optString(i));
            }
        }
        return listdata;
    }

    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }
}

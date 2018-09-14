package com.lotview.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lotview.app.BuildConfig;
import com.lotview.app.R;
import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.interfaces.DialogClickListener;
import com.lotview.app.model.bean.LoginResponseBean;
import com.lotview.app.model.location.LocationTrackBean;
import com.lotview.app.netcom.Keys;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.views.activity.home.HomeActivity;
import com.lotview.app.views.base.BaseActivity;
import com.lotview.app.views.custom_view.CustomProgressDialog;
import com.lotview.app.views.fragment.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.nlopez.smartlocation.SmartLocation;

import static android.content.Context.LOCATION_SERVICE;


public class Utils {

    private static final String TAG = "Utils";
    public static final String INPUT_DATE_TIME_FORMATE = "yyyy-MM-dd HH:mm:ss";
    public static final String OUTPUT_DATE_TIME_FORMATE = "dd-MM-yyyy HH:mm:a";
    public static final String OUTPUT_TIME_FORMATE = "HH:mm:a";
    public static final String INPUT_TIME_FORMATE = "HH:mm:ss";
    public static final String OUTPUT_HOUR_TIME_FORMATE = "HH:mm";

    private static SmartLocation.LocationControl location_control;

    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showSnackBar(ViewDataBinding binding, String message) {

//    public static void showSnackBar(ViewDataBinding binding, String message, boolean istoAction, String ActionText, String actionMessage) {
//        Snackbar snackbar = null;
//        View snackBarView = snackbar.getView();
//        snackBarView.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.app_blue));
//
//        if (istoAction) {
//            snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG)
//                    .setAction(ActionText, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Snackbar snackbar1 = Snackbar.make(binding.getRoot(), actionMessage, Snackbar.LENGTH_SHORT);
//                            snackbar1.show();
//                        }
//                    });
//        } else {
//            snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
//        }
//
//        snackbar.show();

        Snackbar snackbar;
        snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.app_blue));
//        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(R.color.white);
        snackbar.show();
    }

    public static void showSnackBar(Context context, View view, String message) {

        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.app_blue));
//        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(R.color.white);
        snackbar.show();
    }


    public static boolean isValideEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
    }

    public static String getRealPathFromURIAll(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);

        int idx;
        if (contentUri.getPath().startsWith("/external/image") || contentUri.getPath().startsWith("/internal/image")) {
            idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        } else if (contentUri.getPath().startsWith("/external/video") || contentUri.getPath().startsWith("/internal/video")) {
            idx = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
        } else if (contentUri.getPath().startsWith("/external/audio") || contentUri.getPath().startsWith("/internal/audio")) {
            idx = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        } else {
            return contentUri.getPath();
        }
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(idx);
        }
        return null;
    }

//    public static void shareString(String subject, String message, Context mContext) {
//        try {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
//            sendIntent.setType("text/plain");
//            mContext.startActivity(sendIntent);
//        } catch (Exception e) {
//            showToast(mContext, mContext.getString(R.string.activity_not_found));
//        }
//
//    }


    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {//DocumentsContract.isDocumentUri(context.getApplicationContext(), uri))
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static void openKeyPad(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void showAlert(Context context, String title, String message,
                                 String positiveText,
                                 String negativeText, final int requestCode,
                                 final DialogClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        alertDialog.setCancelable(false);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if (!positiveText.equalsIgnoreCase(""))
            alertDialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickListener.onDialogClick(which, requestCode);
                }
            });

        if (!negativeText.equalsIgnoreCase(""))
            alertDialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickListener.onDialogClick(which, requestCode);
                }
            });

        alertDialog.show();
    }


    public static boolean checkPermissions(Activity activity, String... permission) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permission.length; i++) {
                    if (ActivityCompat.checkSelfPermission(activity, permission[i])
                            != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }


    public static boolean onRequestPermissionsResult(String[] permissions, int[] grantResults) {
        try {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public static void setupUI(final AppCompatActivity activity, View view) {

        try {
            // Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(activity);
                        return false;
                    }
                });
            }

            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setupUI(activity, innerView);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(AppCompatActivity activity, EditText editText) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        editText.getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showKeyboard(AppCompatActivity activity) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showKeyboard(AppCompatActivity activity, EditText editText) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void writeData(Context context, String data, String fileName) {
        FileOutputStream fout = null;
        try {
            //getting output stream
            fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            //writng data
            fout.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                //closing the output stream
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String readData(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static void shareFile(Context context, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                Utils.showToast(context, "file_not_found_message");
                return;
            }
            Uri uri = Uri.fromFile(file);

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);

            if (file.exists()) {

                String subject = "";

                intentShareFile.setType("application/pdf");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sent from VestDavit:Android");

                context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }

        } catch (ActivityNotFoundException ex) {
            Utils.showToast(context, "application_not_found");
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


//    public static void showSelectImageDialog(final AppCompatActivity activity) {
//        try {
//            android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(activity);
//
//            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.dialog_list_item);
//            arrayAdapter.add("Camera");
//            arrayAdapter.add("Gallery");
//
//            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String strName = arrayAdapter.getItem(which);
//
//                    if (strName.equalsIgnoreCase("Camera"))
//                        Utils.openImageCamera(activity);
//                    else
//                        Utils.openImageGallery(activity);
//                }
//            });
//            builderSingle.show();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

//    public static void openImageCamera(AppCompatActivity activity) {
//        try {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            activity.startActivityForResult(cameraIntent, ConstantsLib.SELECT_IMAGE_CAMERA);
//        } catch (ActivityNotFoundException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

//    public static void openImageGallery(AppCompatActivity activity) {
//        try {
//            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            activity.startActivityForResult(galleryIntent, ConstantsLib.SELECT_IMAGE_GALLERY);
//        } catch (ActivityNotFoundException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public static void openDatePicker(Context context, final View view) {
        try {
            Calendar newCalendar = Calendar.getInstance();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);

                    if (view instanceof TextView)
                        ((TextView) view).setText(dateFormatter.format(newDate.getTime()));
                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showHidePassword(Context context, EditText editText) {
        TransformationMethod transformationMethod = editText.getTransformationMethod();

        if (transformationMethod instanceof PasswordTransformationMethod) {
//            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_remove_red_eye1), null);
            editText.setTransformationMethod(null);
        } else {
//            editText.setCompoundDrawablesWithIntrinsicBounds(null, null,  context.getResources().getDrawable(R.drawable.ic_remove_red_eye), null);
            editText.setTransformationMethod(new PasswordTransformationMethod());
        }
    }


    public static String getBase64Image(Bitmap bitmap) {
        String bas64Image = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            bas64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return bas64Image;
    }

    public static String getFormattedDate(String dateString) {
        String formattedDate = null;
        try {

            String inputPattern = "dd.MM.yyyy";
            String outputPattern = "dd-MM-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = inputFormat.parse(dateString);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static void pickVideoFromGallery(AppCompatActivity context, int requestCode) {

        try {

            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            context.startActivityForResult(Intent.createChooser(intent, "Select Vine"), requestCode);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void printHashKey(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    BuildConfig.APPLICATION_ID,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                LogUtils.v("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
    }

    public static String getFormattedDate(Date date) {
        String returnDate = "";

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            returnDate = sdf1.format(date.getTime());
            String myTime = Utils.getCurrentTime();
//            returnDate = Utils.formattedDateFromString("yyyy-MM-dd HH:mm a", "yyyy-MM-dd HH:mm:ss", s_date, myTime, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnDate;
    }


    public static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        LogUtils.e(TAG, "density==" + density);
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public static boolean isValidUrl(String url) {
        try {
            return Patterns.WEB_URL.matcher(url).matches();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    //    public static ProgressDialog progressDialog;
    public static CustomProgressDialog progressDialog;

    public static CustomProgressDialog showProgressDialog(Context context, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
//        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog = new CustomProgressDialog(context);

        try {
            progressDialog.setCancelable(false);
            progressDialog.show(message);
            progressDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return progressDialog;
    }

    public static void hideProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void replaceFragment(BaseActivity activity, Fragment fragment) {
        try {
            if (activity instanceof HomeActivity)
                ((HomeActivity) activity).setMyActionBar(fragment);

            manageBackStack(activity, fragment.getClass().getSimpleName());
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (!(fragment.getClass().getSimpleName().equalsIgnoreCase(HomeFragment.class.getSimpleName())
                    && fragmentManager.getBackStackEntryCount() == 0)) {
//                previousFragment = fragmentManager.findFragmentById(R.id.home_layout_container);
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }

            fragmentTransaction.add(R.id.home_layout_container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void manageBackStack(BaseActivity activity, String fragmentName) {
        try {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            for (int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); i++) {
                if (fragmentName.equalsIgnoreCase(fragmentManager.getBackStackEntryAt(i).getName())) {
                    fragmentManager.popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static String saveBitmapOnSdCard(Context context, Bitmap bitmap) {

        String imagePath = "";

        try {

            String root = Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name);
            File myDir = new File(root);
            if (!myDir.exists())
                myDir.mkdirs();

            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(myDir, fileName);

            try {

                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

                imagePath = root + "/" + fileName;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return imagePath;
    }

    public static String saveBitmapOnSdCache(Context context, Bitmap bitmapImage) {

        String filePath = "";
        try {
            ContextWrapper cw = new ContextWrapper(context);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, "tempImg.jpg");
            filePath = mypath.getAbsolutePath();

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return filePath;
    }


    /**
     * dialog width 75% of screen width
     *
     * @param context
     * @return width
     */
    public static int getDialogWidth(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int dialogWidth = width * 90 / 100;
        return dialogWidth;
    }

    /**
     * dialog width 60% of screen height
     *
     * @param context
     * @return height
     */
    public static int getDialogHeight(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int dialogHeight = height * 60 / 100;
        return dialogHeight;
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }


    public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate) {

        if (inputFormat.equals("")) { // if inputFormat = "", set a default input format.
            inputFormat = "yyyy-MM-dd hh:mm:ss";
        }

        if (outputFormat.equals("")) {
            outputFormat = "EEEE d 'de' MMMM 'del' yyyy"; // if inputFormat = "", set a default output format.
        }
        Date parsed;
        String outputDate = "";

        try {

            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
//            df_input.setTimeZone(TimeZone.getTimeZone("utc"));
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);
//            df_output.setTimeZone(TimeZone.getDefault());
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            LogUtils.v("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }
        return outputDate;

    }


    public static String validateTime(String startTime, String endTime) {

        String startT = "N/A";
        String endT = "N/A";

        if (startTime == null || startTime.equals("null") || startTime.equals("")) {
            startT = "N/A";
        } else {

            String[] timeArr = startTime.split(":");
            String h = timeArr[0];
            String m = timeArr[1];
            if (Integer.valueOf(h) > 12) {
                int h1 = Integer.valueOf(h) - 12;
                startT = h1 + ":" + m + " pm";
            } else {
                startT = h + ":" + m + " am";
            }
        }


        if (endTime == null || endTime.equals("null") || endTime.equals("")) {
            endT = "N/A";
        } else {

            String[] timeArr = endTime.split(":");
            String h = timeArr[0];
            String m = timeArr[1];
            if (Integer.valueOf(h) > 12) {
                int h1 = Integer.valueOf(h) - 12;
                endT = h1 + ":" + m + " pm";
            } else {
                endT = h + ":" + m + " am";
            }
        }

        return startT + " to " + endT;
    }

    public static String getDateJson(List<String> dateList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < dateList.size(); i++) {

                jsonArray.put(dateList.get(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonArray.toString();
    }

    public static String getDateJson(String date) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        date = jsonArray.toString();
        date = date.replace("/", "");
        return date;
    }


    public static String getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateValue = inFormat.format(date);
        return dateValue;
    }

   /* public static String getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm:ss");
        String timeValue = inFormat.format(date);
        return timeValue;
    }*/

    public static String validateIntValue(Integer value) {

        if (value == null)
            return "0";

        return value + "";

    }

    public static String validateStringValue(String value) {

        String returntValue = "";

        if (value == null || value.equals("") || value.equals("null"))
            return "";

        returntValue = value;

        return returntValue;
    }


    public static String validateValue(String value) {

        if (value == null || value.equals("") || value.equals("null"))
            return "N/A";

        return value;
    }


    public static int validateInt(Integer value) {

        if (value == null) {
            return 0;
        }
        return value;
    }

    public static String getWeekName(String id) {

        switch (id) {

            case "1":
                return "Sunday";
            case "2":
                return "Monday";
            case "3":
                return "Tuesday";
            case "4":
                return "Wednesday";
            case "5":
                return "Thursday";
            case "6":
                return "Friday";
            case "7":
                return "Saturday";

        }

        return "";
    }


    public static String validateString(String value) {

        String respose = "";

        if (value == null || value.equals("null") || value.equals(""))
            return respose;
        else
            return value;

    }


    public static boolean isStringsEmpty(String str1, String str2) {

        if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2)) {
            return true;
        }
        return false;
    }

    public static boolean isStringsEmpty(String str1) {

        if (TextUtils.isEmpty(str1)) {
            return true;
        }
        return false;
    }

    //UTC conversion
    public static String getTime(Calendar calendar_time, String myFormat, boolean convertUtc) {

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        if (convertUtc) {
//            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return sdf.format(calendar_time.getTime());
    }


    public static boolean isGpsEnable(Context context) {
        LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled;
        // Check if enabled and if not send user to the GPS settings
//        if (!enabled) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }
    }

    public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate, String inputTime, boolean toUTC) {

        String outputDate = "";
        try {
            Date parsed;

            String dateTime = inputDate + " " + inputTime;

//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
//            df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

            if (toUTC) {
                df_input.setTimeZone(TimeZone.getDefault());
                df_output.setTimeZone(TimeZone.getTimeZone("UTC"));
            } else {
                df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
                df_output.setTimeZone(TimeZone.getDefault());
            }


            try {
                parsed = df_input.parse(dateTime);
                outputDate = df_output.format(parsed);

                String[] dateTimeArr = outputDate.split(" ");
                String date = dateTimeArr[0].trim();
                String time = dateTimeArr[1].trim();

                outputDate = date;

            } catch (Exception e) {
                LogUtils.v("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputDate;
    }

    public static String formattedTimeFromString(String inputFormat, String outputFormat, String inputDate, String inputTime, boolean toUTC) {
        String outputTime = "";
        try {
            Date parsed;
            String outputDate = "";

            String dateTime = inputDate + " " + inputTime;

//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
//            df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());
//            df_output.setTimeZone(TimeZone.getDefault());

            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

            if (toUTC) {
                df_input.setTimeZone(TimeZone.getDefault());
                df_output.setTimeZone(TimeZone.getTimeZone("UTC"));
            } else {
                df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
                df_output.setTimeZone(TimeZone.getDefault());
            }

            try {
                parsed = df_input.parse(dateTime);
                outputDate = df_output.format(parsed);

                String[] dateTimeArr = outputDate.split(" ");
                String date = dateTimeArr[0].trim();
                String time = dateTimeArr[1].trim();

                if (dateTimeArr.length == 3)
                    time = time + " " + dateTimeArr[2].trim();

                outputTime = time;

            } catch (Exception e) {
                LogUtils.v("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputTime;
    }


    /**
     * new methods
     *
     * @param inputFormat
     * @param outputFormat
     * @param inputDate
     * @param inputTime
     * @return
     */
    public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate, String inputTime) {

        String outputDate = "";
        try {

            Date parsed;

            String dateTime = inputDate + " " + inputTime;

//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
//            df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);

//            if (toUTC) {
//                df_input.setTimeZone(TimeZone.getDefault());
//                df_output.setTimeZone(TimeZone.getTimeZone("UTC"));
//            } else {
//                df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
//                df_output.setTimeZone(TimeZone.getDefault());
//            }

            if (inputDate == null || inputDate.equals("") || inputDate.equals("0000-00-00")) {
                return "";
            }

            try {
                parsed = df_input.parse(dateTime);
                outputDate = df_output.format(parsed);

                String[] dateTimeArr = outputDate.split(" ");
                String date = dateTimeArr[0].trim();
                String time = dateTimeArr[1].trim();

                outputDate = date;

            } catch (Exception e) {
                LogUtils.v("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputDate;
    }

    public static String formattedTimeFromString(String inputFormat, String outputFormat, String inputDate, String inputTime) {
        String outputTime = "";
        try {
            Date parsed;
            String outputDate = "";

            String dateTime = inputDate + " " + inputTime;

//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
//            df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());
//            df_output.setTimeZone(TimeZone.getDefault());

//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);


//            if (toUTC) {
//                df_input.setTimeZone(TimeZone.getDefault());
//                df_output.setTimeZone(TimeZone.getTimeZone("UTC"));
//            } else {
//                df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
//                df_output.setTimeZone(TimeZone.getDefault());
//            }


            try {
                parsed = df_input.parse(dateTime);
                outputDate = df_output.format(parsed);

                String[] dateTimeArr = outputDate.split(" ");
                String date = dateTimeArr[0].trim();
                String time = dateTimeArr[1].trim();

                if (dateTimeArr.length == 3)
                    time = time + " " + dateTimeArr[2].trim();

                outputTime = time;

            } catch (Exception e) {
                LogUtils.v("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputTime;
    }

    public static String getTypedFormattedNumber(String mobileNo) {

        StringBuilder stringBuilder = new StringBuilder(mobileNo);
        try {

            if (stringBuilder.length() == 5) {
                stringBuilder.trimToSize();
                stringBuilder.insert(4, " ");
            }
            if (stringBuilder.length() == 9) {
                stringBuilder.trimToSize();
                stringBuilder.insert(8, " ");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String fromJson(String bookingDate) {

        String date = "";

        try {
            JSONArray jsonArray = new JSONArray(bookingDate);
            date = (String) jsonArray.get(0);
        } catch (JSONException e) {
            e.printStackTrace();
            date = "";
        }
        return date;

    }

    public static String getFormattedNumber(String mobileNo) {
        StringBuilder stringBuilder = new StringBuilder(mobileNo);
        try {
            for (int i = 0; i < mobileNo.length(); i++) {
                if (i == 4 || i == 8) {
                    stringBuilder.insert(i, " ");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String formateAmount(String value) {

        if (value.equals("")) {
            value = "0";
        }
        String amount = String.format("%.02f", Float.valueOf(value));

        return amount;
    }

    public static String validateEventDate(String eventDate) {

        String date = "test";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate;

        try {

            startDate = df.parse(eventDate);
            String newDateString = format.format(startDate);
            System.out.println("date >" + newDateString);
            date = newDateString;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String validateDateWithTime(String eventDate) {

        String date = "test";

        if (eventDate == null || eventDate.equalsIgnoreCase("") || eventDate.equalsIgnoreCase("null"))
            return "";

        String[] arr = eventDate.split(" ");
        date = arr[0];

        return date;
    }

    public static String validateStringToValue(String value) {

        if (value == null || value.equals("") || value.equals("null")) {
            return "";
        }
        return value;

    }

    public static String validateInteger(Integer value) {

        if (value == null) {
            return "0";
        }

        return value + "";
    }

    public static String validateTimeWithDate(String eventDate) {

        String date = "test";

        if (eventDate == null || eventDate.equalsIgnoreCase("") || eventDate.equalsIgnoreCase("null"))
            return "";

        String[] arr = eventDate.split(" ");

        date = arr[1];

        return date;
    }


    public static String validateSingleTime(String startTime) {

        String startT = "";

        if (startTime == null || startTime.equals("null") || startTime.equals("")) {
            startT = "";
        } else {

            String[] timeArr = startTime.split(":");
            String h = timeArr[0];
            String m = timeArr[1];
            if (Integer.valueOf(h) > 12) {
                int h1 = Integer.valueOf(h) - 12;
                startT = h1 + ":" + m + " PM";
            } else {
                startT = h + ":" + m + " AM";
            }
        }

        return startT;
    }

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return getFormattedDate(calendar.getTime());
    }

    public static String getDaysAgoDate(int previousDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -previousDays);
        return getFormattedDate(calendar.getTime());
    }

/*
*
* --------------------------------chat data---------------------------------------------
*
* */

    public static String changeDateFormat(String inputFormat, String outputFormat, String inputDate) {
        String outputDate = "";
        try {
            Date parsed;

            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

            try {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            } catch (Exception e) {
                LogUtils.v("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputDate;
    }


    public static void showLog(String TAG, String mes) {
        LogUtils.v(TAG, mes);
    }

    public static String sendMessageDate() {
        Date myDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(myDate);
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAsString = outputFmt.format(time);
        return dateAsString;
    }

    public static String getToadyAndYesterdayFoChat(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = simpleDateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR)
                == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormatter.parse(date);
// Get time from date
            SimpleDateFormat timeFormatter1 = new SimpleDateFormat("dd MMM yyyy");
            String displayValue = timeFormatter1.format(date1);
            return displayValue;
        }
    }


    public static long getCurrentTimeInMiliSecondUTC(String date) {
        long time = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date dateTime = format.parse(date);
            time = dateTime.getTime();
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


    public static long getCurrentTimeInMiliSecond(String date) {
        long time = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime = format.parse(date);
            time = dateTime.getTime();
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        Date date = new Date();
        return sdf.format(date);
    }


    public static String getDateForChat(String dateFormat) {
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormatter.parse(dateFormat);
            // Get time from date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            String displayValue = timeFormatter.format(date);
            return displayValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getCurrentUTC() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return outputFmt.format(time);
    }


    public static String getCurrentUTCTimeStampDate() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        outputFmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return outputFmt.format(time);
    }

    public static String getCurrentTimeStampDate() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return outputFmt.format(time);
    }


    public static String convertIntoLocalTime(String datetime) {
        String datetimeLocale = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date value = formatter.parse(datetime);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            datetimeLocale = dateFormatter.format(value);
            return datetimeLocale;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datetimeLocale;
    }


    public static String convertMiliSecondIntoCurrentDateAndTime(String miliSecond) {
        String dateAndTime = "";
        try {
            long time = Long.parseLong(miliSecond);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date resultdate = new Date(time);
            String dateFormat = sdf.format(resultdate);
            dateFormat = convertIntoLocalTime(dateFormat);
            dateAndTime = String.valueOf(getCurrentTimeInMiliSecond(dateFormat));
            return dateAndTime;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateAndTime;
    }


    public static void openPlayStore(Context context) {
        try {
            final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //----------------State


    private static String getStateAbbervation(String id) {

        String abbervation = "";

        switch (id) {

            case "266":
                abbervation = "NSW";
                break;

            case "269":
                abbervation = "Qld";
                break;

            case "270":
                abbervation = "SA";
                break;

            case "271":
                abbervation = "Tas";
                break;

            case "273":
                abbervation = "Vic";
                break;

            case "275":
                abbervation = "WA";
                break;

            case "4122":
                abbervation = "ACT";
                break;

            case "4123":
                abbervation = "NT";
                break;

        }

        return abbervation;

    }

    public static void updateAppDialog(Context context, String title, String message,
                                       String positiveText,
                                       String negativeText, final int requestCode, boolean isForceUpdate,
                                       final DialogClickListener onClickListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setCancelable(false);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (!positiveText.equalsIgnoreCase(""))
            alertDialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickListener.onDialogClick(which, requestCode);
                }
            });

        if (!negativeText.equalsIgnoreCase(""))
            alertDialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickListener.onDialogClick(which, requestCode);
                }
            });

        alertDialog.show();
    }


    public static float toPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static double getRadian(float x1, float y1, float x2, float y2) {
        float width = x2 - x1;
        float height = y1 - y2;
        return Math.atan(Math.abs(height) / Math.abs(width));
    }


    /**
     * add common params in below method
     *
     * @return
     */
    public static String getApiKey() {
        return Keys.API_KEY;
    }

    public static String getDeviceID() {
        String device_id = Settings.Secure.getString(KeyKeepApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_id;
    }

    public static String getToken() {
        /**
         * dummy token for now
         */
        String token = "adfasdfasdfaf";

        return token;
    }

    public static String getDeviceType() {
        return Keys.TYPE_ANDROID;
    }

    public static LoginResponseBean.Result getUserDetail() {
        LoginResponseBean.Result bean = null;
        String data = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getUserDetail();
        Gson gson = new Gson();
        try {
            bean = gson.fromJson(data, LoginResponseBean.Result.class);
        } catch (Exception ex) {
        }
        return bean;
    }

    public static String getAssetType(int assetType) {
        String type = "N/A";
        switch (assetType) {

            case 1:
                type = "New";
                break;
            case 2:
                type = "Used";
                break;

            case 3:
                type = "Service";
                break;

        }
        return type;
    }

    public static LocationTrackBean getLocationDetail(Context context) {

        LocationTrackBean locationTrackBean = new LocationTrackBean();
        locationTrackBean.setEmployeeId(Integer.valueOf(AppSharedPrefs.getInstance(context).getEmployeeID()));
        locationTrackBean.setEmployeeLatitue(Double.valueOf(AppSharedPrefs.getLatitude()));
        locationTrackBean.setEmployeeLongitude(Double.valueOf(AppSharedPrefs.getLongitude()));
        locationTrackBean.setEmployeeSpeed(Float.valueOf(AppSharedPrefs.getSpeed()));
        locationTrackBean.setEmployeeTimeStampLocal(getCurrentTimeStampDate());
        locationTrackBean.setEmployeeTimeStampLocalUTC(getCurrentUTC());

        return locationTrackBean;
    }


}

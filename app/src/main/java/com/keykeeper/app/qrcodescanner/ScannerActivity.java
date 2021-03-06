package com.keykeeper.app.qrcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.keykeeper.app.R;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.LogUtils;
import com.keykeeper.app.utils.Utils;

public class ScannerActivity extends AppCompatActivity {

    private static final String IS_MANUAL_QR = "manual";
    private final int CUSTOMIZED_REQUEST_CODE = 113;
    static Activity activity;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        CaptureCodeActivity.setTitle(getIntent().getStringExtra("title"));
        new IntentIntegrator(this).setCaptureActivity(CaptureCodeActivity.class).initiateScan();

    }


    public static void finishWithManual(String tag_number) {

        Intent mIntent = new Intent();
        mIntent.putExtra("sdsadsad", tag_number);
        mIntent.putExtra(IS_MANUAL_QR, false);
        activity.setResult(Activity.RESULT_OK, mIntent);
        Toast.makeText(activity, tag_number, Toast.LENGTH_SHORT).show();
        activity.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!Connectivity.isConnected()) {
            Utils.hideProgressDialog();
            Utils.showSnackBar(this, text, getString(R.string.internet_connection));
            finish();
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        if (result.getContents() != null) {
            String resultString = result.getContents();
            LogUtils.d("", "Got scan result from user loaded image :" + resultString);
            Intent mIntent = new Intent();
            mIntent.putExtra(AppUtils.SCAN_SUCCESS, resultString);
            setResult(Activity.RESULT_OK, mIntent);
            finish();
        } else {
            Intent mIntent = new Intent();
            mIntent.putExtra(AppUtils.SCAN_FAIL, "Something went wrong");
            setResult(Activity.RESULT_CANCELED, mIntent);
            finish();
        }
    }


}

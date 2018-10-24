package com.keykeeper.app.qrcodescanner;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.keykeeper.app.R;
import com.keykeeper.app.views.custom_view.CustomActionBar;

/**
 * Sample Activity extending from ActionBarActivity to display a Toolbar.
 */
public class CaptureCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private final int CUSTOMIZED_REQUEST_CODE = 113;
    public static String title = "";
    boolean isFlashOn;
    private CustomActionBar customActionBar;

    public static void setTitle(String title) {
        CaptureCodeActivity.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.capture_code_activity);
        customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(title, true, true,false, false, false, this);
        customActionBar.setRightIcon(R.drawable.flashlight);
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.left_iv) {
            finish();
        }
        if (view.getId() == R.id.right_iv){
            if (!isFlashOn) {
                barcodeScannerView.setTorchOn();
                isFlashOn = true;
                customActionBar.setRightIcon(R.drawable.flashlight);
            } else {
                barcodeScannerView.setTorchOff();
                isFlashOn = false;
                customActionBar.setRightIcon(R.drawable.flashlight);
            }
        }
    }

}

package com.lotview.app.qrcodescanner.decode;

import com.google.zxing.Result;

public interface DecodeImageCallback {

    void decodeSucceed(Result result);

    void decodeFail(int type, String reason);
}
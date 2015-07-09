
package com.example.yamaguchi.arduinotest.usb;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 *
 */
interface OnShortToastMessageListener extends OnReactToMessageListener {

}

/**
 *
 */
public class ReactToMessageUtil {
    public OnReactToMessageListener listener;
    public Activity mActivity;

    public ReactToMessageUtil(Activity activity) {
        mActivity = activity;
    }

    public void runOnUiThread() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener == null) {
                    return;
                }

                listener.updateUi(mActivity);
            }
        });
    }

    public OnShortToastMessageListener mOnShortToastMessageListener = new OnShortToastMessageListener() {

        @Override
        public void updateUi(Context context) {
            Toast.makeText(context, "Message from Arduino", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     *
     */
    public static int composeInt(byte hi, byte lo) {
        return ((hi & 0xff) << 8) + (lo & 0xff);
    }

    public static OnReactToMessageListener creareReactToMessageListener(final Runnable runnable) {
        return new OnReactToMessageListener() {
            @Override
            public void updateUi(Context context) {
                runnable.run();
            }
        };
    }
}

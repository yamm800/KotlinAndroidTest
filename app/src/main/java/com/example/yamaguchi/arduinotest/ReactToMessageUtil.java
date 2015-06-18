
package com.example.yamaguchi.arduinotest;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Arduino���炫�����b�Z�[�W���������郊�X�i�[ UI�̍X�V���������̒��ɓ��ReactToMessageUtil��runOnUiThread()���Ă�
 * 
 * @author tomotaka
 */
interface OnReactToMessageListener {
    public void updateUi(Context context);
}

interface OnShortToastMessageListener extends OnReactToMessageListener {
}

/**
 * UsbConnectionProtocol�ɏ]��, Arduino���炫�����b�Z�[�W����������N���X
 * 
 * @see UsbConnectionProtocol
 * @author tomotaka
 */
public class ReactToMessageUtil {
    public OnReactToMessageListener listener;
    public Activity mActivity;

    public ReactToMessageUtil(Activity activity) {
        mActivity = activity;
    }

    // UI�X���b�h�Ŏ��s
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
}

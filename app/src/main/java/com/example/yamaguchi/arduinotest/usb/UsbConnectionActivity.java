
package com.example.yamaguchi.arduinotest.usb;

import android.os.Bundle;

import com.example.yamaguchi.arduinotest.common.BaseActivity;

/**
 *
 */
public class UsbConnectionActivity extends BaseActivity {
    public interface ISubmitMessage {
        public void submitMessage();

        public void submitMessage(byte[] msg);

        public void submitMessage(int msg);
    }

    public interface IReceiveMessage {
        public void receiveMessage(byte[] msg);

        public void receiveMessage(int msg);
    }

    protected UsbConnection mUsbConnection;
    public ReactToMessageUtil mReactUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactUtil = new ReactToMessageUtil(this);
        initConnection();
    }

    protected void setView() {

    }

    private void initConnection() {
        mUsbConnection = new UsbConnection(this);
        mUsbConnection.registerReceiver(this);
    }

    protected void setConnectionListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsbConnection.openAccessroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUsbConnection.closeAccessory();
    }

    @Override
    protected void onDestroy() {
        mUsbConnection.unregisterReceiver(this);
        super.onDestroy();
    }

    protected byte[] createMessage(int msg) {
        return null;
    }

    public void reactToMessage(byte[] message) {

    }
}

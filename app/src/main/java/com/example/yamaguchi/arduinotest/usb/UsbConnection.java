
package com.example.yamaguchi.arduinotest.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public class UsbConnection {

    private OnOpenAccesoryListener mOnOpenAccessoryListener;

    public interface OnOpenAccesoryListener {
        public void onOpenAccessory();
    }

    public void setOnOpenAccessoryListener(OnOpenAccesoryListener listener) {
        mOnOpenAccessoryListener = listener;
    }


    private OnWillCloseAccesoryListerner mOnWillCloseAccessoryListener;

    public interface OnWillCloseAccesoryListerner {
        public void onWillCloseAccessory();
    }

    public void setOnWillCloseAccesoryListerner(OnWillCloseAccesoryListerner listener) {
        mOnWillCloseAccessoryListener = listener;
    }


    private OnClosedAccesoryListerner mOnClosedAccessoryListener;

    public interface OnClosedAccesoryListerner {
        public void onClosedAccessory();
    }

    public void setOnClosedAccessoryListerner(OnClosedAccesoryListerner listener) {
        mOnClosedAccessoryListener = listener;
    }

    private static final String TAG = "UsbConnection";

    private final String mActionUsbPermissionString;

    private final UsbManager mUsbManager;
    private final PendingIntent mPermissionIntent;
    private boolean mPermissionRequestPending;

    private UsbAccessory mAccessory;
    private ParcelFileDescriptor mFileDescriptor;
    private OutputStream mOutputStream;
    private FileInputStream mInputStream;

    public UsbConnection(Context context) {
        mActionUsbPermissionString = "aoabook.sample.accessory.action.USB_PERMISSION";
        setUsbReceiver();

        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//        mUsbManager = UsbManager.getInstance(context);

        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(
                mActionUsbPermissionString), 0);
    }

    private BroadcastReceiver mUsbReceiver;

    private void setUsbReceiver() {
        mUsbReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                if (mActionUsbPermissionString.equals(action)) {
                    synchronized (this) {
                        mAccessory = manager.getAccessoryList()[0];
//                        mAccessory = UsbManager.getAccessory(intent);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            openStreamAccessory(mAccessory);
                        } else {
                            Log.d(TAG, "permission denied for accessory = " + mAccessory);
                        }
                        mPermissionRequestPending = false;
                    }
                } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
//                    UsbAccessory accessory = manager.getAccessoryList()[0];
//////                    UsbAccessory accessory = UsbManager.getAccessory(intent);
//                    if ((accessory != null) && accessory.equals(mAccessory)) {
//                        closeAccessory();
//                    }
                    if (mAccessory != null) {
                        closeAccessory();
                    }
                }
            }
        };
    }

    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(mActionUsbPermissionString);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        context.registerReceiver(mUsbReceiver, filter);
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(mUsbReceiver);
    }

    public void openAccessroy() {
        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        mAccessory = (accessories == null ? null : accessories[0]);
        if (mAccessory != null) {
            if (mUsbManager.hasPermission(mAccessory)) {
                openStreamAccessory(mAccessory);
            } else {
                synchronized (mUsbManager) {
                    if (!mPermissionRequestPending) {
                        mUsbManager.requestPermission(mAccessory, mPermissionIntent);
                        mPermissionRequestPending = true;
                    }
                }
            }
        } else {
            Log.d(TAG, "Accessory is null");
        }
    }

    private void openStreamAccessory(UsbAccessory accessory) {
        mFileDescriptor = mUsbManager.openAccessory(accessory);
        if (mFileDescriptor != null) {
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mOutputStream = new FileOutputStream(fd);
            mInputStream = new FileInputStream(fd);

            if (mOnOpenAccessoryListener != null) {
                mOnOpenAccessoryListener.onOpenAccessory();
            }

        } else {
            Log.d(TAG, "Failed to open the accessoty");
        }
    }

    public void closeAccessory() {

        try {
            if (mFileDescriptor != null) {
                if (mOnWillCloseAccessoryListener != null) {
                    mOnWillCloseAccessoryListener.onWillCloseAccessory();
                }

                mFileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mInputStream = null;
            mOutputStream = null;
            mFileDescriptor = null;
            mAccessory = null;

            if (mOnClosedAccessoryListener != null) {
                mOnClosedAccessoryListener.onClosedAccessory();
            }
        }
    }

    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    public FileInputStream getInputStream() {
        return mInputStream;
    }
}

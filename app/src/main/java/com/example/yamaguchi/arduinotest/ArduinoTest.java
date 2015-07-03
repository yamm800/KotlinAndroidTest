
package com.example.yamaguchi.arduinotest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.yamaguchi.arduinotest.UsbConnection.OnClosedAccesoryListerner;
import com.example.yamaguchi.arduinotest.UsbConnection.OnOpenAccesoryListener;
import com.example.yamaguchi.arduinotest.UsbConnection.OnWillCloseAccesoryListerner;
import com.example.yamaguchi.arduinotest.UsbConnectionActivity.IReceiveMessage;
import com.example.yamaguchi.arduinotest.UsbConnectionActivity.ISubmitMessage;

import java.io.IOException;
import java.io.InputStream;

public class ArduinoTest extends UsbConnectionActivity implements ISubmitMessage, IReceiveMessage {

    private TextView mStatusText;
    private Button mButton;

    private int mValue;
    private boolean mThreadRunning = false;

    static MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mMediaPlayer.prepare();
        } catch (Exception e) {

        }

        setContentView(R.layout.activity_analog_in);
        setView();
        setConnectionListener();
    }

    @Override
    protected void setView() {
        mStatusText = (TextView) findViewById(R.id.text_status);
        // mButton = (Button) findViewById(R.id.button1);

        // mButton.setOnTouchListener(new View.OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // OutputStream outputStream = mUsbConnection.getOutputStream();
        // if (outputStream == null) {
        // Log.d("", "outputStream is null");
        // return false;
        // }
        //
        // byte[] cmd = new byte[1];
        // try {
        // switch (event.getAction()) {
        // case MotionEvent.ACTION_DOWN:
        // cmd[0] = 1;
        // outputStream.write(cmd);
        // return true;
        //
        // case MotionEvent.ACTION_UP:
        // cmd[0] = 0;
        // outputStream.write(cmd);
        // return true;
        //
        // default:
        // return false;
        // }
        //
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // return false;
        // }
        // });
    }

    @Override
    protected void setConnectionListener() {
        mUsbConnection.setOnOpenAccessoryListener(new OnOpenAccesoryListener() {

            @Override
            public void onOpenAccessory() {
                // USB�ڑ�����ɌĂ΂��B���ł�while���[�v���񂵂Ă���
                new Thread(new MyRunnable()).start();
            }
        });
        mUsbConnection.setOnWillCloseAccesoryListerner(new OnWillCloseAccesoryListerner() {

            @Override
            public void onWillCloseAccessory() {
                mThreadRunning = false;
            }
        });
        mUsbConnection.setOnClosedAccessoryListerner(new OnClosedAccesoryListerner() {

            @Override
            public void onClosedAccessory() {
                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                }
            }
        });
    }

    @Override
    public void submitMessage() {

    }

    @Override
    public void submitMessage(byte[] msg) {

    }

    @Override
    public void submitMessage(int msg) {

    }

    @Override
    public void receiveMessage(byte[] msg) {

    }

    @Override
    public void receiveMessage(int msg) {

    }

    @Override
    public void reactToMessage(byte[] message) {
        mValue = ReactToMessageUtil.composeInt(message[0], message[1]);

        mReactUtil.listener = react1Listener;

        // UI�X���b�h�Ŏ��s
        if (mReactUtil.listener != null) {
            mReactUtil.runOnUiThread();
        }
    }

    private void readByte(InputStream input) {
        byte[] buffer = new byte[2];
        try {
            // ��̓X�g���[���̓ǂݍ���
            input.read(buffer);
            // UI�����̓��C���X���b�h�ōs��
            reactToMessage(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            mThreadRunning = true;
            InputStream inputStream = mUsbConnection.getInputStream();
            if (inputStream == null) {
                Log.d("", "inputStream is null");
                return;
            }

            // ���y�Đ�
            mMediaPlayer = MediaPlayer.create(ArduinoTest.this,
                    R.raw.girl_of_white_tiger_field);
            mMediaPlayer.start();

            while (mThreadRunning) {
                // ���̒���arduino����̐M���̓ǂݍ��݁AUI�Ȃǂ̍X�V�����Ă���
                readByte(inputStream);
            }
        }
    }

    private final OnReactToMessageListener react1Listener = ReactToMessageUtil.creareReactToMessageListener(new Runnable() {
        @Override
        public void run() {
            mStatusText.setText(String.valueOf(mValue));
            float volume = (float) mValue / 1023;
            mMediaPlayer.setVolume(volume, volume);
        }
    });
}

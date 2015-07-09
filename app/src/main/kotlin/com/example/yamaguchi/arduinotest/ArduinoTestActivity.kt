package com.example.yamaguchi.arduinotest

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.yamaguchi.arduinotest.usb.ReactToMessageUtil

import com.example.yamaguchi.arduinotest.usb.UsbConnection.OnClosedAccesoryListerner
import com.example.yamaguchi.arduinotest.usb.UsbConnection.OnOpenAccesoryListener
import com.example.yamaguchi.arduinotest.usb.UsbConnection.OnWillCloseAccesoryListerner
import com.example.yamaguchi.arduinotest.usb.UsbConnectionActivity
import com.example.yamaguchi.arduinotest.usb.UsbConnectionActivity.IReceiveMessage
import com.example.yamaguchi.arduinotest.usb.UsbConnectionActivity.ISubmitMessage

import java.io.IOException
import java.io.InputStream

public class ArduinoTestActivity : UsbConnectionActivity(), ISubmitMessage, IReceiveMessage {

    private var mStatusText: TextView? = null
    private val mButton: Button? = null

    private var mValue: Int = 0
    private var mThreadRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super<UsbConnectionActivity>.onCreate(savedInstanceState)

        try {
            mMediaPlayer!!.prepare()
        } catch (e: Exception) {
        }


        setContentView(R.layout.activity_analog_in)
        setView()
        setConnectionListener()
    }

    override fun setView() {
        mStatusText = findViewById(R.id.text_status) as TextView
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

    override fun setConnectionListener() {
        mUsbConnection.setOnOpenAccessoryListener(object : OnOpenAccesoryListener {

            override fun onOpenAccessory() {
                Thread(MyRunnable()).start()
            }
        })
        mUsbConnection.setOnWillCloseAccesoryListerner(object : OnWillCloseAccesoryListerner {

            override fun onWillCloseAccessory() {
                mThreadRunning = false
            }
        })
        mUsbConnection.setOnClosedAccessoryListerner(object : OnClosedAccesoryListerner {

            override fun onClosedAccessory() {
                if (mMediaPlayer != null) {
                    mMediaPlayer!!.stop()
                }
            }
        })
    }

    override fun submitMessage() {
    }

    override fun submitMessage(msg: ByteArray) {
    }

    override fun submitMessage(msg: Int) {
    }

    override fun receiveMessage(msg: ByteArray) {
    }

    override fun receiveMessage(msg: Int) {
    }

    override fun reactToMessage(message: ByteArray) {
        mValue = ReactToMessageUtil.composeInt(message[0], message[1])

        mReactUtil.listener = react1Listener

        if (mReactUtil.listener != null) {
            mReactUtil.runOnUiThread()
        }
    }

    private fun readByte(input: InputStream) {
        val buffer = ByteArray(2)
        try {
            input.read(buffer)
            reactToMessage(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    inner class MyRunnable : Runnable {

        override fun run() {
            mThreadRunning = true
            val inputStream = mUsbConnection.getInputStream()
            if (inputStream == null) {
                Log.d("", "inputStream is null")
                return
            }

            mMediaPlayer = MediaPlayer.create(this@ArduinoTestActivity, R.raw.girl_of_white_tiger_field)
            mMediaPlayer!!.start()

            while (mThreadRunning) {
                readByte(inputStream)
            }
        }
    }

    private val react1Listener = ReactToMessageUtil.creareReactToMessageListener(object : Runnable {
        override fun run() {
            mStatusText!!.setText("" + mValue)
            val volume = mValue.toFloat() / 1023
            mMediaPlayer!!.setVolume(volume, volume)
        }
    })

    companion object {

        var mMediaPlayer: MediaPlayer? = null
    }
}

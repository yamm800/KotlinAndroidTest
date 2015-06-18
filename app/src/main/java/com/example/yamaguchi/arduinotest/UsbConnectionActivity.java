
package com.example.yamaguchi.arduinotest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Arduino�Ƃ̐ڑ����s��Activity�̊��N���X IUsbConnection���������ׂ�
 * 
 * @author tomotaka
 */
public class UsbConnectionActivity extends Activity {
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
    ReactToMessageUtil mReactUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactUtil = new ReactToMessageUtil(this);
        initConnection();
    }

    /**
     * View��id�Ŏ擾�����菉����s���A���X�i�[�⑮����ݒ肷��
     */
    protected void setView() {

    }

    /**
     * UsbConnection���쐬���A���W�X�^�[��o�^
     */
    private void initConnection() {
        mUsbConnection = new UsbConnection(this);
        mUsbConnection.registerReceiver(this);
    }

    /**
     * �R�l�N�V�����̃��X�i�[���܂Ƃ߂Đݒ肷��
     */
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

    /**
     * arduino�ɑ��M���郁�b�Z�[�W�̍쐬
     * 
     * @param msg
     * @return
     */
    protected byte[] createMessage(int msg) {
        return null;
    }

    /**
     * �ǂݍ��񂾃��b�Z�[�W�ɍ��킹��util�̃��X�i�[��I��
     * 
     * @param message
     */
    public void reactToMessage(byte[] message) {

    }
}

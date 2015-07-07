
package com.example.yamaguchi.arduinotest.usb;

/**
 * Arduino�s�̒ʐM�v���g�R�����߂�N���X
 * 
 * @author tomotaka
 */
public class UsbConnectionProtocol {

    // Arduino�ւ̃��b�Z�[�W
    public enum MSG_TO_ARDUINO {
        LED_ON((byte) 1),
        LED_OFF((byte) 0),
        NONE((byte) -1);

        private final byte mMessageByte;

        MSG_TO_ARDUINO(byte messageByte) {
            mMessageByte = messageByte;
        }

        public byte getByte() {
            return mMessageByte;
        }
    };

    // Arduino����̃��b�Z�[�W
    public enum MSG_FROM_ARDUINO {
        SHOW_TOAST((byte) 1),
        NONE((byte) -1);

        private final byte mMessageByte;

        MSG_FROM_ARDUINO(byte messageByte) {
            mMessageByte = messageByte;
        }

        public Byte getByte() {
            return mMessageByte;
        }
    };
}

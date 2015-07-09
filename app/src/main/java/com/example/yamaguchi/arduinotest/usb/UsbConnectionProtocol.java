
package com.example.yamaguchi.arduinotest.usb;

/**
 */
public class UsbConnectionProtocol {

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

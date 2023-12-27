package com.holtek.usb_cdc;

import android.content.SharedPreferences;

import com.holtek.util.SocketLogger;

/**
 * Created by holtek on 2016/7/16.
 */
public class SerialSettings {
    public static final String BAUDRATE = "Baudrate";
    public static final String BYTEORDER = "ByteOrder";
    public static final int BYTEORDER_BIG_ENDIAN = 2;
    public static final int BYTEORDER_LITTLE_ENDIAN = 1;
    public static final String CHARSET = "charset";
    public static final String CHARSET_IBM437 = "IBM-437";
    public static final String CHARSET_UNKNOWN = "Unknown";
    public static final String DATABITS = "DataBits";
    public static final int DEFAULT_RX_CRLF = 3;
    //public static final int DEFAULT_SETTING_BAUDRATE = 38400;
    public static final int DEFAULT_SETTING_BAUDRATE = 19200;
    public static final int DEFAULT_SETTING_BYTEORDER = 1;
    public static final int DEFAULT_SETTING_DATABITS = 8;
    public static final String DEFAULT_SETTING_HANDSHAKING = "None";
    public static final String DEFAULT_SETTING_PARITY = "None";
    //public static final int DEFAULT_SETTING_STOPBITS = 1;
    public static final int DEFAULT_SETTING_STOPBITS = 0;
    public static final int DEFAULT_TX_CRLF = 3;
    public static final String ENABLE_FILE_LOGGING = "enableFileLogging";
    public static final String HANDSHAKING = "HandShaking";
    public static final String IS_CUSTOM_BAUDRATE = "IsCustomBaudrate";
    public static final String PARITY = "Parity";
    public static final String RX_CRLF = "rxCRLF";
    public static final String SETTING_PREFERENCE_NAME = "cdc_settings";
    public static final String STOPBITS = "StopBits";
    public static final String TX_CRLF = "txCRLF";

    //public int baudRate = 38400;
    public int baudRate = 19200;
    public String charset = null;
    public int dataBits = 8;
    public boolean enableFileLogging = false;
    public String handShaking = "None";
    public boolean isCustomBaudrate = false;
    public String parity = "None";
    public int rxCRLF = 3;
    //public float stopBits = 1.0F;
    public float stopBits = 0.0F;
    public int txCRLF = 3;

    //加载保存的数据
    public void loadPreferences(SharedPreferences paramSharedPreferences)
    {
        isCustomBaudrate = paramSharedPreferences.getBoolean(IS_CUSTOM_BAUDRATE, false);
        SocketLogger.debug("IsCustomBaudrate:" + this.isCustomBaudrate);
        baudRate = paramSharedPreferences.getInt(BAUDRATE, DEFAULT_SETTING_BAUDRATE);
        SocketLogger.debug("Baudrate:" + this.baudRate);
        dataBits = paramSharedPreferences.getInt(DATABITS, DEFAULT_SETTING_DATABITS);
        SocketLogger.debug("DataBits:" + this.dataBits);
        parity = paramSharedPreferences.getString(PARITY, DEFAULT_SETTING_PARITY);
        SocketLogger.debug("Parity:" + this.parity);
        stopBits = paramSharedPreferences.getFloat(STOPBITS, DEFAULT_SETTING_STOPBITS);
        SocketLogger.debug("StopBits:" + this.stopBits);
        handShaking = paramSharedPreferences.getString(HANDSHAKING, DEFAULT_SETTING_HANDSHAKING);
        SocketLogger.debug("HandShaking:" + this.handShaking);
        enableFileLogging = paramSharedPreferences.getBoolean(ENABLE_FILE_LOGGING, false);
        SocketLogger.debug("enableFileLogging:" + this.enableFileLogging);
        charset = paramSharedPreferences.getString(CHARSET, "");
        SocketLogger.debug("charset:" + this.charset);
        rxCRLF = paramSharedPreferences.getInt(RX_CRLF, DEFAULT_RX_CRLF);
        System.out.println("rxCRLF:" + this.rxCRLF);
        txCRLF = paramSharedPreferences.getInt(TX_CRLF, DEFAULT_TX_CRLF);
        System.out.println("txCRLF:" + this.txCRLF);
    }

    //保存数据到本地
    public void savePreference(SharedPreferences paramSharedPreferences)
    {
        SharedPreferences.Editor localEditor = paramSharedPreferences.edit();
        localEditor.putBoolean(IS_CUSTOM_BAUDRATE, isCustomBaudrate);
        localEditor.putInt(BAUDRATE, baudRate);
        localEditor.putInt(DATABITS, dataBits);
        localEditor.putString(PARITY, parity);
        localEditor.putFloat(STOPBITS, stopBits);
        localEditor.putString(HANDSHAKING, handShaking);
        localEditor.putBoolean(ENABLE_FILE_LOGGING, enableFileLogging);
        localEditor.putString(CHARSET, charset);
        localEditor.putInt(RX_CRLF, rxCRLF);
        localEditor.putInt(TX_CRLF, txCRLF);
        localEditor.commit();
    }

    public String toString()
    {
        return "SerialSettings [isCustomBaudrate=" + isCustomBaudrate
                + ", baudRate=" + baudRate
                + ", dataBits=" + dataBits
                + ", parity=" + parity
                + ", stopBits=" + stopBits
                + ", handShaking=" + handShaking
                + ", enableFileLogging=" + enableFileLogging
                + ", charset=" + charset + "]";
    }
}

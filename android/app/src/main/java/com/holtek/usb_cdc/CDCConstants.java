package com.holtek.usb_cdc;

/**
 * Created by holtek on 2016/7/15.
 */
public class CDCConstants {

    public static final int CDC_READ_DATA_HANDLENUM = 5;
    public static final int CDC_READ_MODEMSTATUS_HANDLENUM = 6;

    public static final int HT_VENDOR_ID = 0x04D9;
    public static final int HT_PRODUCT_ID = 0xB564;

    public static final int HID_INTERFACE_CLASSTYPE = 3;
    public static final int HID_INTERFACE_SUBCLASSTYPE = 0;
    public static final int HID_INTERFACE_PROTOCOL = 0;

    public static final int GET_CLASS_REQUESTTYPE = 0xA1;
    public static final int SET_CLASS_REQUESTTYPE = 0x21;

    //cdc class request
    public static final int GET_REPORT = 0x01;
    public static final int SET_REPORT = 0x09;

    public static final int INPUT_REPORT   = 0x01;
    public static final int OUTPUT_REPORT  = 0x02;
    public static final int FEATURE_REPORT = 0x03;

}

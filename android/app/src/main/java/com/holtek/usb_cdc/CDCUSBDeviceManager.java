package com.holtek.usb_cdc;

import com.holtek.util.*;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

/**
 * Created by holtek on 2016/7/15.
 */
public class CDCUSBDeviceManager extends AbstractUSBDeviceManager{
    int baudRate;
    UsbEndpoint inEndPoint = null;
    int InterfaceNo = 0;
    UsbEndpoint outEndPoint = null;
    public byte[] portSetting = new byte[8];
    Context context = null;


    public CDCUSBDeviceManager(Context paramContext, Handler paramHandler, UsbManager paramUsbManager)
    {
        this.usbManager = paramUsbManager;
        this.handler = paramHandler;
        this.context = paramContext;
        //fReceiveData = false;
    }


    private boolean checkEndPoints(UsbInterface paramUsbInterface)
    {
        this.inEndPoint = null;
        this.outEndPoint = null;
        for(int i=0; i<paramUsbInterface.getEndpointCount(); i++){
            UsbEndpoint localUsbEndpoint = paramUsbInterface.getEndpoint(i);
            if(localUsbEndpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
                if (localUsbEndpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                    this.inEndPoint = localUsbEndpoint;
                } else if (localUsbEndpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                    this.outEndPoint = localUsbEndpoint;
                }
            }
        }
        if(this.inEndPoint == null || this.outEndPoint == null){
            return false;
        }else{
            return true;
        }
    }
/*
    //返回CDC控制接口号
    private int findCDCControlInterface(UsbDevice paramUsbDevice)
    {
        for (int i = 0; i < paramUsbDevice.getInterfaceCount(); i++)
        {
            if (paramUsbDevice.getInterface(i).getInterfaceClass() == CDCConstants.CDC_CONTROL_CLASSTYPE) {
                return i;
            }
        }
        return -1;
    }

    //返回CDC数据接口
    private UsbInterface findCDCDataInterface(UsbDevice paramUsbDevice)
    {
        UsbInterface localUsbInterface = null;
        for (int i = 0; i < paramUsbDevice.getInterfaceCount(); i++)
        {
            localUsbInterface = paramUsbDevice.getInterface(i);
            if(localUsbInterface.getInterfaceClass() == CDCConstants.CDC_DATA_CLASSTYPE){
                break;
            }
        }
        return localUsbInterface;
    }

    //是否同时声明了控制和数据接口
    public static boolean isExistCDCControlAndDtaInterface(UsbDevice paramUsbDevice)
    {
        boolean isExitControl = false;
        boolean isExitData = false;
        UsbInterface localUsbInterface ;
        for (int k = 0;k < paramUsbDevice.getInterfaceCount(); k++)
        {
            localUsbInterface = paramUsbDevice.getInterface(k);
            if (localUsbInterface.getInterfaceClass() == CDCConstants.CDC_CONTROL_CLASSTYPE) {
                isExitControl = true;
            }
            if (localUsbInterface.getInterfaceClass() == CDCConstants.CDC_DATA_CLASSTYPE) {
                isExitData = true;
            }
        }
        return isExitControl&&isExitData;
    }

    private static int makeModemHandshakingStatus(boolean paramBoolean1, boolean paramBoolean2)
    {
        System.out.println("makeModemHandshakingStatus");
        byte b = 0;
        if (paramBoolean1) {
            b = (byte)1;
        }
        if (paramBoolean2) {
            b = (byte)(b | 0x2);
        }
       // System.out.println("SET MHS : " + PrintUtil.bytesToBINString(new byte[] { b, 0 }));
        return DecodeUtil.decodeInt2(b, (byte)0);
    }

*/
    public byte[] MakeLineControl(byte[] param)
            throws USBTerminalException
    {
        //report id
        //portSetting[0] = 0;
        //cmd code
        portSetting[0] = 0;
        //baud rate
        for(int i=0; i<param.length && i+1<portSetting.length; i++){
            portSetting[i+1] = param[i];
        }
        return portSetting;
    }


    public void close()
    {
        System.out.println("Closing ....");
        if (this.intf != null) {
            this.connection.releaseInterface(this.intf);
        }
        this.connection.close();
        this.intf = null;
        this.usbDevice = null;
    }

    public int getBaudrate()
    {
        return this.baudRate;
    }


    //返回HID接口
    public UsbInterface findHIDInterface(UsbDevice paramUsbDevice)
    {
        UsbInterface localUsbInterface = null;
        for (int i = 0; i < paramUsbDevice.getInterfaceCount(); i++)
        {
            localUsbInterface = paramUsbDevice.getInterface(i);
            if(localUsbInterface.getInterfaceClass() == CDCConstants.HID_INTERFACE_CLASSTYPE){

                InterfaceNo = i;
                return localUsbInterface;
            }
        }
        return null;
    }

    public boolean initDevice(Context paramContext, UsbManager paramUsbManager, UsbDevice paramUsbDevice, SerialSettings paramSerialSettings)
            throws USBTerminalException
    {
        System.out.println("initDevice");
        this.usbDevice = paramUsbDevice;
        if (paramUsbDevice == null) {
            return false;
        }
        this.intf = findHIDInterface(paramUsbDevice);   //only one interface
        if (this.intf == null)
        {
            System.out.println("USB HID Interface no find.");
            sendMessageToHandler(2);
            return false;
        }
        this.connection = paramUsbManager.openDevice(paramUsbDevice);
        if (this.connection == null)
        {
            System.out.println("USB Connection is null.");
            sendMessageToHandler(2);
            return false;
        }
        if (!this.connection.claimInterface(this.intf, true))
        {
            SocketLogger.debug("Exclusive interface access failed!");
            return false;
        }
        if(!checkEndPoints(this.intf)){
            return false;
        }
        printRawDescriptors(this.connection.getRawDescriptors(), this.connection.getSerial());
        //GetSerialSetting()
        SocketLogger.debug("Initialized");
        return true;
    }

    //放在线程中，不确定线程被打断时执行到了哪一步，所以可能会分多次读
    public byte[] recv()
    {
        if (connection == null) {
            return null;
        }
        int i = connection.bulkTransfer(inEndPoint, recvBuffer,recvBuffer.length, 100);
        if (i < 0)
        {
            SocketLogger.debug("sensor timeout");
            return null;
        }
        Log.d("USBDeviceManager","Received bytes: " + i);

        /*byte[] arrayOfByte = new byte[i];
        System.arraycopy(recvBuffer, 0, arrayOfByte, 0, i);*/

        int len = recvBuffer[0];
        byte[] arrayOfByte = new byte[len];
        System.arraycopy(recvBuffer, 1, arrayOfByte, 0, len);

        // Alvin test
        /*Log.d("USBDeviceManager","len: " + len);
        String tmpStr = "";
        for (int j=0; j<len; j++) {
            tmpStr += String.format("%02x,", arrayOfByte[j]);
        }
        Log.d("USBDeviceManager","buffer: " + tmpStr);*/

        return arrayOfByte;
    }

    public void send(String paramString)
    {
        if (this.connection == null) {
            return;
        }
        SocketLogger.debug("Send> " + paramString);
        byte[] arrayOfByte = paramString.getBytes();

        this.connection.bulkTransfer(this.outEndPoint, arrayOfByte, arrayOfByte.length, 0);
    }

    public void send(byte[] paramArrayOfByte)
    {
        if (this.connection == null) {
            return;
        }

        byte[] SendBuff = new byte[outEndPoint.getMaxPacketSize()];
        //SendBuff[0] = 0;
        for(int i=0; i<paramArrayOfByte.length; i++){
            SendBuff[i] = paramArrayOfByte[i];
        }
        int sendLen = this.connection.bulkTransfer(this.outEndPoint, SendBuff, SendBuff.length, 1000);
        Log.d("USBDeviceManager", "Send length: "+String.valueOf(sendLen));

        // Alvin test
        /*Log.d("USBDeviceManager", "paramArrayOfByte length: "+String.valueOf(paramArrayOfByte.length));
        String tmpStr = "";
        for (int j=0; j<paramArrayOfByte.length; j++) {
            tmpStr += String.format("%02x,", paramArrayOfByte[j]);
        }
        Log.d("USBDeviceManager","buffer: " + tmpStr);*/
    }

    //传送paramInt长度的数据
    public void send(byte[] paramArrayOfByte, int paramInt)
    {
        if (this.connection == null) {
            return;
        }
       // SocketLogger.debug("Send> " + PrintUtil.bytesToHEXString(paramArrayOfByte));
        this.connection.bulkTransfer(this.outEndPoint, paramArrayOfByte, paramInt, 0);
    }

    //set feature
    public boolean SetFeatureReport()
    {
        //report id
        //this.portSetting[0] = 0;
        //cmd code
        this.portSetting[0] = 1;
        int value = (CDCConstants.FEATURE_REPORT<<8)+0;  //low byte: report id
        if (this.connection.controlTransfer(CDCConstants.SET_CLASS_REQUESTTYPE, CDCConstants.SET_REPORT, value,
                this.InterfaceNo, portSetting, portSetting.length, 100) < 0)
        {
            Log.d("USBDeviceManager", "Set feature Fail");
            return false;
        }
        return true;
    }

    //get feature
    public boolean GetFeatureReport()
    {
        //report id
        //this.portSetting[0] = 0;
        //cmd code
        this.portSetting[0] = 0;
        int value = (CDCConstants.FEATURE_REPORT<<8)+0;  //low byte: report id
        if (this.connection == null || this.connection.controlTransfer(CDCConstants.GET_CLASS_REQUESTTYPE, CDCConstants.GET_REPORT, value,
                this.InterfaceNo, portSetting, portSetting.length, 100) < 0)
        {
            Log.d("USBDeviceManager", "get feature Fail");
            return false;
        }
        return true;
    }


}

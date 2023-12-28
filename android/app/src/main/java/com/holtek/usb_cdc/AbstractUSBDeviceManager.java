
/**
 * Created by holtek on 2016/7/15.
 */

package com.holtek.usb_cdc;
import com.holtek.util.*;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public abstract class AbstractUSBDeviceManager
{
    //protected byte[] blankBytes = new byte[0];
    protected UsbDeviceConnection connection = null;
    protected Handler handler = null;
    protected UsbInterface intf = null;
    protected byte[] recvBuffer = new byte[32];
    //protected byte[] sendBuffer = new byte[1024];
    protected UsbDevice usbDevice = null;
    protected UsbManager usbManager = null;

    public abstract void close();

    public String getDescriptions(UsbDevice paramUsbDevice)
    {
        StringBuilder localStringBuilder1 = new StringBuilder();
        this.usbDevice = paramUsbDevice;
        if(this.usbDevice == null){
            return "USB device null";
        }
        if (this.connection == null) {
            this.connection = this.usbManager.openDevice(paramUsbDevice);
        }
        //获取device级别的信息
        localStringBuilder1.append("Device ID: ").append(this.usbDevice.getDeviceId()).append("\n");
        localStringBuilder1.append("Device Name: ").append(UsbDevice.getDeviceName(this.usbDevice.getDeviceId())).append("\n");
        localStringBuilder1.append("Device Class: ").append(getUSBClassDescription(this.usbDevice.getDeviceClass())).append("\n");
        localStringBuilder1.append("Device Sub Class: ").append(getUSBClassDescription(this.usbDevice.getDeviceSubclass())).append("\n");
        localStringBuilder1.append("Device Protocol: ").append(this.usbDevice.getDeviceProtocol()).append("\n");
        localStringBuilder1.append("Vendor ID: ").append(this.usbDevice.getVendorId()).append("\n");
        localStringBuilder1.append("Product ID: ").append(this.usbDevice.getProductId()).append("\n");
        if (this.connection != null)
        {
            //Returns the raw USB descriptors for the device
            byte[] arrayOfByte = this.connection.getRawDescriptors();
            if ((arrayOfByte != null) && (arrayOfByte.length > 13))
            {
                localStringBuilder1.append("USB Version: ").append(arrayOfByte[2]).append(".").append(arrayOfByte[3]).append("\n");
                localStringBuilder1.append("Product Version in BCD: ").append(arrayOfByte[12]).append(".").append(arrayOfByte[13]).append("\n");
                localStringBuilder1.append("Serial: ").append(this.connection.getSerial()).append("\n");
            }
        }
        localStringBuilder1.append("Interface Count: ").append(this.usbDevice.getInterfaceCount()).append("\n\n");
        //获取interface级别的信息
        UsbInterface localUsbInterface;
        for (int i = 0;i < this.usbDevice.getInterfaceCount(); i++)
        {
            localUsbInterface = this.usbDevice.getInterface(i);
            localStringBuilder1.append("-----------------------------------------------\n");
            localStringBuilder1.append("Interface ID: ").append(localUsbInterface.getId()).append("\n");
            localStringBuilder1.append("Interface Class: ").append(getUSBClassDescription(localUsbInterface.getInterfaceClass())).append("\n");
            localStringBuilder1.append("Interface Sub Class: ").append(getUSBClassDescription(localUsbInterface.getInterfaceSubclass())).append("\n");
            localStringBuilder1.append("Interface Protocol: ").append(localUsbInterface.getInterfaceProtocol()).append("\n");
            localStringBuilder1.append("Interface Endpoint Count: ").append(localUsbInterface.getEndpointCount()).append("\n\n");
            for (int j = 0;j < localUsbInterface.getEndpointCount();j++) {
                //获取端点信息
                localStringBuilder1.append("======================================\n");
                UsbEndpoint localUsbEndpoint = localUsbInterface.getEndpoint(j);
                localStringBuilder1.append("Endpoint Number: ").append(localUsbEndpoint.getEndpointNumber()).append("\n");
                localStringBuilder1.append("Endpoint Address: ").append(localUsbEndpoint.getAddress()).append("\n");
                localStringBuilder1.append("Endpoint Direction: ").append((localUsbEndpoint.getDirection()== UsbConstants.USB_DIR_IN?"in":"out")).append("\n");
                localStringBuilder1.append("Endpoint Interval: ").append(localUsbEndpoint.getInterval()).append("\n");
                localStringBuilder1.append("Endpoint Max Packet Size: ").append(localUsbEndpoint.getMaxPacketSize()).append("\n");
                localStringBuilder1.append("Endpoint Type: ").append(getEndPointTypeName(localUsbEndpoint.getType())).append("\n");
            }
        }
        if (paramUsbDevice.getVendorId() == CDCConstants.HT_VENDOR_ID)
        {
            //interface遍历结束
            localStringBuilder1.append("\n+++++++++++++++++++++++++++++++++++++++\n");
            localStringBuilder1.append("Holtek Communication Properties: ").append(this.usbDevice.getInterfaceCount()).append("\n");
         //   localStringBuilder1.append(getProperties());
        }
        return localStringBuilder1.toString();
    }

    //获取端点类型的描述
    protected String getEndPointTypeName(int paramInt)
    {
        switch (paramInt)
        {
            default:
                return "Unknown";
            case UsbConstants.USB_ENDPOINT_XFER_CONTROL:
                return "Control endpoint type (endpoint zero)";
            case UsbConstants.USB_ENDPOINT_XFER_INT:
                return "Interrupt endpoint type";
            case UsbConstants.USB_ENDPOINT_XFER_ISOC:
                return "Isochronous endpoint type (currently not supported)";
            case UsbConstants.USB_ENDPOINT_XFER_BULK:
                return "Bulk endpoint type";
        }
    }

    //厂商自定义请求
    public String getProperties()
    {
        if (!isConnected()) {
            return "(Not connected!)";
        }
        StringBuilder localStringBuilder = new StringBuilder();
        byte[] arrayOfByte = new byte['p'];
        //controlTransfer(int requestType, int request, int value, int index, byte[] buffer, int length, int timeout)
        this.connection.controlTransfer(193, 15, 0, 0, arrayOfByte, arrayOfByte.length, 0);

        ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte, 4, 56);
        localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer localIntBuffer = localByteBuffer.asIntBuffer();
        localStringBuilder.append("ServiceMask: " ).append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("Reserved: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("MaxTxQueue: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("MaxRxQueue: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("MaxBaudrate: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("ProvSubType: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("ProvCapability: \n").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("SettableParams: \n").append(localIntBuffer.get()).append("\n");
    //    localStringBuilder.append("ProvSubType: " + CP210XPropertyCodes.getProbSubType(localIntBuffer.get())).append("\n");
    //    localStringBuilder.append("ProvCapability: \n" + CP210XPropertyCodes.getProvCapability(localIntBuffer.get())).append("\n");
    //    localStringBuilder.append("SettableParams: \n" + CP210XPropertyCodes.getSettableParams(localIntBuffer.get())).append("\n");
        localStringBuilder.append("SettableBaud: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("SettableData: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("CurrentTxQueue: ").append(localIntBuffer.get()).append("\n");
        localStringBuilder.append("CurrentRxQueue: ").append(localIntBuffer.get()).append("\n");
        return localStringBuilder.toString();
    }

    protected USBDeviceDescriptors getRawDdescriptors(byte[] paramArrayOfByte, String paramString)
    {
        USBDeviceDescriptors localUSBDeviceDescriptors = new USBDeviceDescriptors();
        localUSBDeviceDescriptors.setDescriptorLength(paramArrayOfByte[0]);
        localUSBDeviceDescriptors.setDescriptorType(paramArrayOfByte[1]);
      //  localUSBDeviceDescriptors.setUsbVersion(DecodeUtil.decodeInt2(paramArrayOfByte[2], paramArrayOfByte[3]));
        localUSBDeviceDescriptors.setDeviceClass(paramArrayOfByte[4]);
        localUSBDeviceDescriptors.setDeviceSubClass(paramArrayOfByte[5]);
        localUSBDeviceDescriptors.setDeviceProtocol(paramArrayOfByte[6]);
        localUSBDeviceDescriptors.setMaxPacketSize(paramArrayOfByte[7]);
     //   localUSBDeviceDescriptors.setVendorID(DecodeUtil.decodeInt2(paramArrayOfByte[8], paramArrayOfByte[9]));
     //   localUSBDeviceDescriptors.setProductID(DecodeUtil.decodeInt2(paramArrayOfByte[10], paramArrayOfByte[11]));
     //   localUSBDeviceDescriptors.setProductVersionInBCD(DecodeUtil.decodeInt2(paramArrayOfByte[12], paramArrayOfByte[13]));
        localUSBDeviceDescriptors.setManufacturer(paramArrayOfByte[14]);
        localUSBDeviceDescriptors.setProductDescriptorIndex(paramArrayOfByte[15]);
        localUSBDeviceDescriptors.setSerialStringDescriptorIndex(paramArrayOfByte[16]);
        localUSBDeviceDescriptors.setSerialString(paramString);
        localUSBDeviceDescriptors.setNumberOfConfiguration(paramArrayOfByte[17]);
        return localUSBDeviceDescriptors;
    }

    public Handler getUIHandler()
    {
        return this.handler;
    }

    public String getUSBClassDescription(int paramInt)
    {
        switch (paramInt)
        {
            default:
                return "Unknown";
            case 0:
                return "Unspecified";
            case 1:
                return "Audio";
            case 2:
                return "Communications and CDC Control";
            case 3:
                return "HID (Human Interface Device)";
            case 5:
                return "Physical";
            case 6:
                return "Image";
            case 7:
                return "Printer";
            case 8:
                return "Mass Storage";
            case 9:
                return "Hub";
            case 10:
                return "CDC-Data";
            case 11:
                return "Smart Card";
            case 13:
                return "Content Security";
            case 14:
                return "Video";
            case 15:
                return "Personal Healthcare";
            case 220:
                return "Diagnostic Device";
            case 224:
                return "Wireless Controller";
            case 239:
                return "Miscellaneous";
            case 254:
                return "Application Specific";
            case 255:
                return "Vendor Specific";
        }
    }

    public abstract boolean initDevice(Context paramContext, UsbManager paramUsbManager, UsbDevice paramUsbDevice, SerialSettings paramSerialSettings)
            throws USBTerminalException;

    public boolean isConnected()
    {
        return this.connection != null;
    }

    protected void print(UsbDevice paramUsbDevice)
    {
        SocketLogger.debug("Device ID: " + paramUsbDevice.getDeviceId());
        SocketLogger.debug("Device Name: " + UsbDevice.getDeviceName(paramUsbDevice.getDeviceId()));
        SocketLogger.debug("Device Class: " + getUSBClassDescription(paramUsbDevice.getDeviceClass()));
        SocketLogger.debug("Device Sub Class: " + getUSBClassDescription(paramUsbDevice.getDeviceSubclass()));
        SocketLogger.debug("Device Protocol: " + paramUsbDevice.getDeviceProtocol());
        SocketLogger.debug("Vendor ID: " + paramUsbDevice.getVendorId());
        SocketLogger.debug("Product ID: " + paramUsbDevice.getProductId());
        SocketLogger.debug("Interface Count: " + paramUsbDevice.getInterfaceCount());
        UsbInterface localUsbInterface;

        for (int i = 0;i < paramUsbDevice.getInterfaceCount(); i++)
        {
            localUsbInterface = paramUsbDevice.getInterface(i);
            SocketLogger.debug("-----------------------------------------------");
            SocketLogger.debug("Interface ID: " + localUsbInterface.getId());
            SocketLogger.debug("Interface Class: " + getUSBClassDescription(localUsbInterface.getInterfaceClass()));
            SocketLogger.debug("Interface Sub Class: " + getUSBClassDescription(localUsbInterface.getInterfaceSubclass()));
            SocketLogger.debug("Interface Protocol: " + localUsbInterface.getInterfaceProtocol());
            SocketLogger.debug("Interface Endpoint Count: " + localUsbInterface.getEndpointCount());
            for (int j=0;j < localUsbInterface.getEndpointCount();j++) {
                SocketLogger.debug("===============================================");
                UsbEndpoint localUsbEndpoint = localUsbInterface.getEndpoint(j);
                SocketLogger.debug("Endpoint Number: " + localUsbEndpoint.getEndpointNumber());
                SocketLogger.debug("Endpoint Address: " + localUsbEndpoint.getAddress());
                SocketLogger.debug("Endpoint Direction: "+(localUsbEndpoint.getDirection()== UsbConstants.USB_DIR_IN?"in":"out"));
                SocketLogger.debug("Endpoint Interval: " + localUsbEndpoint.getInterval());
                SocketLogger.debug("Endpoint Max Packet Size: " + localUsbEndpoint.getMaxPacketSize());
                SocketLogger.debug("Endpoint Type: " + getEndPointTypeName(localUsbEndpoint.getType()));
            }
        }
    }

    protected void printRawDescriptors(byte[] paramArrayOfByte, String paramString)
    {
        SocketLogger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        SocketLogger.debug("Raw Descriptors:");
        SocketLogger.debug("Descriptor length in bytes: " + paramArrayOfByte[0]);
        SocketLogger.debug("Descriptor type: " + paramArrayOfByte[1]);
        String  logStr = "USB Version: "+DecodeUtil.decodeInt2(paramArrayOfByte[2], paramArrayOfByte[3]);
        SocketLogger.debug(logStr);
//        byte[] arrayOfByte1 = new byte[2];
//        arrayOfByte1[0] = paramArrayOfByte[3];
//        arrayOfByte1[1] = paramArrayOfByte[2];
//        SocketLogger.debug(PrintUtil.bytesToHEXString(arrayOfByte1));
        SocketLogger.debug("Device Class: " + getUSBClassDescription(paramArrayOfByte[4]));
        SocketLogger.debug("Device Sub Class: " + getUSBClassDescription(paramArrayOfByte[5]));
        SocketLogger.debug("Device Protocol: " + paramArrayOfByte[6]);
        SocketLogger.debug("Max Packet Size: " + paramArrayOfByte[7]);
        logStr = "Vendor ID: "+DecodeUtil.decodeInt2(paramArrayOfByte[8], paramArrayOfByte[9]);
        SocketLogger.debug(logStr);
    /*    byte[] arrayOfByte2 = new byte[2];
        arrayOfByte2[0] = paramArrayOfByte[9];
        arrayOfByte2[1] = paramArrayOfByte[8];
        SocketLogger.debug(PrintUtil.bytesToHEXString(arrayOfByte2));*/
        logStr = "Product ID: "+DecodeUtil.decodeInt2(paramArrayOfByte[10], paramArrayOfByte[11]);
        SocketLogger.debug(logStr);
//        byte[] arrayOfByte3 = new byte[2];
//        arrayOfByte3[0] = paramArrayOfByte[11];
//        arrayOfByte3[1] = paramArrayOfByte[10];
//    //    SocketLogger.debug(PrintUtil.bytesToHEXString(arrayOfByte3));
        logStr = "Product Version in BCD: "+DecodeUtil.decodeInt2(paramArrayOfByte[12], paramArrayOfByte[13]);
        SocketLogger.debug(logStr);
//        byte[] arrayOfByte4 = new byte[2];
//        arrayOfByte4[0] = paramArrayOfByte[13];
//        arrayOfByte4[1] = paramArrayOfByte[12];
    //    SocketLogger.debug(PrintUtil.bytesToHEXString(arrayOfByte4));
        SocketLogger.debug("Manufacturer: " + paramArrayOfByte[14]);
        SocketLogger.debug("Product Descriptor Index: " + paramArrayOfByte[15]);
        SocketLogger.debug("Serial String Descriptor Index: " + paramArrayOfByte[16]);
        SocketLogger.debug("Serial String: " + paramString);
        SocketLogger.debug("Number Of Configuration: " + paramArrayOfByte[17]);
    }

    public abstract byte[] recv();

    public abstract void send(byte[] paramArrayOfByte);

    protected void sendMessageToHandler(int paramInt)
    {
        Message localMessage = this.handler.obtainMessage();
        localMessage.what = paramInt;
        this.handler.sendMessage(localMessage);
    }

}
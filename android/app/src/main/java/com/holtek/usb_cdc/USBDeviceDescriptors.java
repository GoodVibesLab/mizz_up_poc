package com.holtek.usb_cdc;

import android.hardware.usb.UsbConstants;

/**
 * Created by holtek on 2016/7/15.
 */
public class USBDeviceDescriptors {
    int descriptorLength;
    int descriptorType;
    int deviceClass;
    int deviceProtocol;
    int deviceSubClass;
    int manufacturer;
    int maxPacketSize;
    int numberOfConfiguration;
    int productDescriptorIndex;
    int productID;
    int productVersionInBCD;
    String serialString;
    int serialStringDescriptorIndex;
    int usbVersion;
    int vendorID;

    public static String getEndPointTypeName(int paramInt)
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

    public static String getUSBClassDescription(int paramInt)
    {
        switch (paramInt) {
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
            case 0xff:
                return "Vendor Specific";
        }
    }

    public int getDescriptorLength()
    {
        return this.descriptorLength;
    }

    public int getDescriptorType()
    {
        return this.descriptorType;
    }

    public int getDeviceClass()
    {
        return this.deviceClass;
    }

    public int getDeviceProtocol()
    {
        return this.deviceProtocol;
    }

    public int getDeviceSubClass()
    {
        return this.deviceSubClass;
    }

    public int getManufacturer()
    {
        return this.manufacturer;
    }

    public int getMaxPacketSize()
    {
        return this.maxPacketSize;
    }

    public int getNumberOfConfiguration()
    {
        return this.numberOfConfiguration;
    }

    public int getProductDescriptorIndex()
    {
        return this.productDescriptorIndex;
    }

    public int getProductID()
    {
        return this.productID;
    }

    public int getProductVersionInBCD()
    {
        return this.productVersionInBCD;
    }

    public String getSerialString()
    {
        return this.serialString;
    }

    public int getSerialStringDescriptorIndex()
    {
        return this.serialStringDescriptorIndex;
    }

    public int getUsbVersion()
    {
        return this.usbVersion;
    }

    public int getVendorID()
    {
        return this.vendorID;
    }

    public void setDescriptorLength(int paramInt)
    {
        this.descriptorLength = paramInt;
    }

    public void setDescriptorType(int paramInt)
    {
        this.descriptorType = paramInt;
    }

    public void setDeviceClass(int paramInt)
    {
        this.deviceClass = paramInt;
    }

    public void setDeviceProtocol(int paramInt)
    {
        this.deviceProtocol = paramInt;
    }

    public void setDeviceSubClass(int paramInt)
    {
        this.deviceSubClass = paramInt;
    }

    public void setManufacturer(int paramInt)
    {
        this.manufacturer = paramInt;
    }

    public void setMaxPacketSize(int paramInt)
    {
        this.maxPacketSize = paramInt;
    }

    public void setNumberOfConfiguration(int paramInt)
    {
        this.numberOfConfiguration = paramInt;
    }

    public void setProductDescriptorIndex(int paramInt)
    {
        this.productDescriptorIndex = paramInt;
    }

    public void setProductID(int paramInt)
    {
        this.productID = paramInt;
    }

    public void setProductVersionInBCD(int paramInt)
    {
        this.productVersionInBCD = paramInt;
    }

    public void setSerialString(String paramString)
    {
        this.serialString = paramString;
    }

    public void setSerialStringDescriptorIndex(int paramInt)
    {
        this.serialStringDescriptorIndex = paramInt;
    }

    public void setUsbVersion(int paramInt)
    {
        this.usbVersion = paramInt;
    }

    public void setVendorID(int paramInt)
    {
        this.vendorID = paramInt;
    }

    public String toString()
    {
        StringBuilder localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>").append("\n");
        localStringBuilder1.append("Raw Descriptors:").append("\n");
        localStringBuilder1.append("Descriptor length in bytes: ").append(this.descriptorLength).append("\n");
        localStringBuilder1.append("Descriptor type: ").append(this.descriptorType).append("\n");

        localStringBuilder1.append("USB Version: ").append(this.usbVersion).append(", ");
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.valueOf(this.usbVersion);
        localStringBuilder1.append(String.format("%04x", arrayOfObject1)).append("\n");

        localStringBuilder1.append("Device Class: ").append(getUSBClassDescription(this.deviceClass)).append("\n");
        localStringBuilder1.append("Device Sub Class: ").append(getUSBClassDescription(this.deviceSubClass)).append("\n");
        localStringBuilder1.append("Device Protocol: ").append(this.deviceProtocol).append("\n");
        localStringBuilder1.append("Max Packet Size: ").append(this.maxPacketSize).append("\n");

        localStringBuilder1.append("Vendor ID: ").append(this.vendorID).append(", ");
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Integer.valueOf(this.vendorID);
        localStringBuilder1.append(String.format("%04x", arrayOfObject2)).append("\n");

        localStringBuilder1.append("Product ID: ").append(this.productID).append(", ");
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = Integer.valueOf(this.productID);
        localStringBuilder1.append(String.format("%04x", arrayOfObject3)).append("\n");

        localStringBuilder1.append("Product Version in BCD: ").append(this.productVersionInBCD).append(", ");
        Object[] arrayOfObject4 = new Object[1];
        arrayOfObject4[0] = Integer.valueOf(this.productVersionInBCD);
        localStringBuilder1.append(String.format("%04x", arrayOfObject4)).append("\n");

        localStringBuilder1.append("Manufacturer: ").append(this.manufacturer).append("\n");
        localStringBuilder1.append("Product Descriptor Index: ").append(this.productDescriptorIndex).append("\n");
        localStringBuilder1.append("Serial String Descriptor Index: ").append(this.serialStringDescriptorIndex).append("\n");
        localStringBuilder1.append("Serial String: ").append(this.serialString).append("\n");
        localStringBuilder1.append("Number Of Configuration: ").append(this.numberOfConfiguration).append("\n");
        return localStringBuilder1.toString();
    }
}

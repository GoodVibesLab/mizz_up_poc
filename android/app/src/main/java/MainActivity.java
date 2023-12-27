package com.example.mizz_up_poc; // Replace with your package name

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import com.holtek.usb_cdc.CDCConstants;
import com.holtek.usb_cdc.CDCUSBDeviceManager;
import com.holtek.usb_cdc.DataReceiveThread;
import com.holtek.usb_cdc.SerialSettings;
import com.holtek.usb_cdc.USBTerminalException;
import com.holtek.util.SocketLogger;

import java.text.SimpleDateFormat;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "fr.mizzup.healthformankind/channel";

    CDCUSBDeviceManager usbDeviceManager = null;
    static final String USB_REQUEST_PERMISSION_ACTION = "com.holtek.usb_cdc.USB_PERMISSION";
    static final int SETCOM_REQUEST = 1;
    private UsbManager usbManager = null;

    private SerialSettings serialSettings = new SerialSettings();

    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("connect")) {
                                usbManager = ((UsbManager)getSystemService(Context.USB_SERVICE));
                                usbDeviceManager = new CDCUSBDeviceManager(this, handler, usbManager);
                                try {
                                    usbDeviceManager.initDevice(this, usbManager, null, this.serialSettings);
                                    result.success("connected");
                                } catch (USBTerminalException e) {
                                    result.error("USBTerminalException", e.getMessage(), null);
                                }
                            }else if(call.method.equals("send")){
                                try{
                                    usbDeviceManager.send(call.argument("data"));
                                    result.success("sent");
                                }catch (Exception e){
                                    result.error("Exception", e.getMessage(), null);
                           }
                        }
                );
    }

    public final Handler handler = new Handler() {
        public void handleMessage(Message message) {

        }
    };
}

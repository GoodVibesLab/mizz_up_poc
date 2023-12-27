package com.holtek.usb_cdc;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.holtek.util.FileLogger;
import com.holtek.util.SocketLogger;

/**
 * Created by holtek on 2016/7/15.
 */
public class DataReceiveThread extends Thread {
    CDCUSBDeviceManager deviceManager = null;
    FileLogger fileLogger = null;
    private Handler handler = null;
    private static Object lock=new Object();

    private DataReceiveThread() {}

    public DataReceiveThread(Context paramContext, CDCUSBDeviceManager paramDefaultUSBDeviceManager, boolean paramBoolean)
    {
        this.deviceManager = paramDefaultUSBDeviceManager;
        this.handler = paramDefaultUSBDeviceManager.getUIHandler();
        if (paramBoolean) {
            this.fileLogger = new FileLogger(paramContext);
        }
    }

    public void run()
    {
        byte[] arrayOfByte = new byte[1024];
        byte lastIndex = 1;  //第0个放收到的数据长度
        //如果连续几次都能读到数据，则认为这几笔数据为同一笔
        boolean fSamePackage = false;
        Message localMessage;
        SocketLogger.debug("start thread");
        while (true) {
            boolean bool = Thread.currentThread().isInterrupted();
            if (bool) {
                return;
            }
            byte[] newBytes = deviceManager.recv();
            if (newBytes != null) {
                if (this.fileLogger != null) {
                    this.fileLogger.log(newBytes);
                }
                fSamePackage = true;
                //合并新数组到上一次收到的数组
                for(int i=lastIndex,j=0; j<newBytes.length; i++,j++){
                    arrayOfByte[i] = newBytes[j];
                }
                lastIndex += newBytes.length;
            }else {
                if(fSamePackage == true){
                    localMessage = handler.obtainMessage();
                    localMessage.what = CDCConstants.CDC_READ_DATA_HANDLENUM;
                    arrayOfByte[0] = lastIndex;
                    localMessage.obj = arrayOfByte;
                    handler.sendMessage(localMessage);
                }
                lastIndex = 1;
                fSamePackage = false;
            }
            try {
                Thread.sleep(5L);
            } catch (InterruptedException localInterruptedException) {
                return;
            } finally {
                SocketLogger.debug("DataReceiveThread Thread is dead..");
                if (this.fileLogger != null) {
                    this.fileLogger.close();
                }
            }
        }
    }
}

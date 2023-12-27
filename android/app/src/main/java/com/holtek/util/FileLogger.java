package com.holtek.util;

/**
 * Created by holtek on 2016/7/16.
 */

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileLogger {
    Context context;
    File logFile;
    String logFileName;
    FileOutputStream out;

    private FileLogger() {}

    public FileLogger(Context paramContext)
    {
        if (Environment.getExternalStorageState().equals("mounted"))
        {
            this.context = paramContext;
            this.logFileName = (DateUtil.now2StrInFileName() + ".log");
            File localFile = new File(Environment.getExternalStorageDirectory(), "USBTerminal");
            localFile.mkdirs();
            this.logFile = new File(localFile, this.logFileName);
        }
        try
        {
            this.out = new FileOutputStream(this.logFile);
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
            localFileNotFoundException.printStackTrace();
        }
    }

    public void close()
    {
        if (this.out == null) {
            return;
        }
        try
        {
            this.out.close();
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }

    public void log(byte[] paramArrayOfByte)
    {
        if (this.out == null) {
            return;
        }
        try
        {
            this.out.write(paramArrayOfByte);
            this.out.flush();
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }
}

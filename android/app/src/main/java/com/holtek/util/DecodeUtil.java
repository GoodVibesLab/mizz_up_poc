package com.holtek.util;

import java.io.PrintStream;

/**
 * Created by holtek on 2016/7/16.
 */
public class DecodeUtil {
    public static byte decodeBit(byte paramByte, int paramInt)
    {
        byte b = (byte)(1 << paramInt);
        PrintStream localPrintStream = System.out;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(paramInt);
        arrayOfObject[1] = Short.valueOf(b);
        localPrintStream.format("decodeBit bitIndex = %d, Mask = %x", arrayOfObject);
        return (byte)((paramByte | b) >> paramInt);
    }

    public static int decodeInt2(byte paramByte1, byte paramByte2)
    {
        return 0x0 | 0xFF00 & paramByte2 << 8 | paramByte1 & 0xFF;
    }

    public static long decodeInt4(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
    {
        return 0L | 0xFF000000 & paramByte4 << 24 | 0xFF0000 & paramByte3 << 16 | 0xFF00 & paramByte2 << 8 | paramByte1 & 0xFF;
    }

    public static String decodeString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
        for (int i = paramInt1;; i++)
        {
            int j = 0;
            if (i >= paramInt2) {}
            for (;;)
            {
                return new String(paramArrayOfByte, paramInt1, j);
               /* if (paramArrayOfByte[i] != 0) {
                    break;
                }
                j = 1 + (i - paramInt1);*/
            }
        }
    }
}

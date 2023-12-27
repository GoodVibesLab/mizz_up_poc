package com.holtek.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by holtek on 2016/7/16.
 */
public class SocketLogger  extends OutputStream
{
    static boolean ENABLE_LOGGING = false;
    public static boolean isConnected = false;
    private static ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue();

    static
    {
        if (ENABLE_LOGGING)
        {
            PrintStream localPrintStream = new PrintStream(new SocketLogger());
            System.setOut(localPrintStream);
            System.setErr(localPrintStream);
        }
    }

    public SocketLogger()
    {
        if (ENABLE_LOGGING) {
            new Thread()
            {
                /* Error */
                public void run()
                {
                    // Byte code:
                    //   0: aconst_null
                    //   1: astore_1
                    //   2: aconst_null
                    //   3: astore_2
                    //   4: new 22	java/net/Socket
                    //   7: dup
                    //   8: ldc 24
                    //   10: sipush 9002
                    //   13: invokespecial 27	java/net/Socket:<init>	(Ljava/lang/String;I)V
                    //   16: astore_3
                    //   17: aload_3
                    //   18: invokevirtual 31	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
                    //   21: astore 10
                    //   23: new 33	java/io/BufferedWriter
                    //   26: dup
                    //   27: new 35	java/io/OutputStreamWriter
                    //   30: dup
                    //   31: aload 10
                    //   33: invokespecial 38	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;)V
                    //   36: invokespecial 41	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
                    //   39: astore 11
                    //   41: iconst_1
                    //   42: putstatic 45	com/oneman/freeusbtools/util/SocketLogger:isConnected	Z
                    //   45: invokestatic 49	com/oneman/freeusbtools/util/SocketLogger:access$0	()Ljava/util/concurrent/ConcurrentLinkedQueue;
                    //   48: invokevirtual 55	java/util/concurrent/ConcurrentLinkedQueue:poll	()Ljava/lang/Object;
                    //   51: astore 12
                    //   53: aload 12
                    //   55: instanceof 57
                    //   58: ifeq +64 -> 122
                    //   61: aload 10
                    //   63: aload 12
                    //   65: checkcast 57	java/lang/Integer
                    //   68: invokevirtual 61	java/lang/Integer:intValue	()I
                    //   71: invokevirtual 67	java/io/OutputStream:write	(I)V
                    //   74: aload 10
                    //   76: invokevirtual 70	java/io/OutputStream:flush	()V
                    //   79: goto -34 -> 45
                    //   82: astore 14
                    //   84: aload 14
                    //   86: invokevirtual 73	java/net/UnknownHostException:printStackTrace	()V
                    //   89: goto -44 -> 45
                    //   92: astore 4
                    //   94: aload 11
                    //   96: astore_2
                    //   97: aload_3
                    //   98: astore_1
                    //   99: aload 4
                    //   101: invokevirtual 73	java/net/UnknownHostException:printStackTrace	()V
                    //   104: ldc 75
                    //   106: invokestatic 79	com/oneman/freeusbtools/util/SocketLogger:debug	(Ljava/lang/String;)V
                    //   109: aload_1
                    //   110: ifnull +11 -> 121
                    //   113: aload_2
                    //   114: invokevirtual 82	java/io/BufferedWriter:close	()V
                    //   117: aload_1
                    //   118: invokevirtual 83	java/net/Socket:close	()V
                    //   121: return
                    //   122: aload 12
                    //   124: instanceof 85
                    //   127: ifeq +69 -> 196
                    //   130: aload 10
                    //   132: aload 12
                    //   134: checkcast 85	[B
                    //   137: invokevirtual 88	java/io/OutputStream:write	([B)V
                    //   140: aload 10
                    //   142: invokevirtual 70	java/io/OutputStream:flush	()V
                    //   145: goto -100 -> 45
                    //   148: astore 13
                    //   150: aload 13
                    //   152: invokevirtual 89	java/io/IOException:printStackTrace	()V
                    //   155: goto -110 -> 45
                    //   158: astore 8
                    //   160: aload 11
                    //   162: astore_2
                    //   163: aload_3
                    //   164: astore_1
                    //   165: aload 8
                    //   167: invokevirtual 89	java/io/IOException:printStackTrace	()V
                    //   170: ldc 75
                    //   172: invokestatic 79	com/oneman/freeusbtools/util/SocketLogger:debug	(Ljava/lang/String;)V
                    //   175: aload_1
                    //   176: ifnull -55 -> 121
                    //   179: aload_2
                    //   180: invokevirtual 82	java/io/BufferedWriter:close	()V
                    //   183: aload_1
                    //   184: invokevirtual 83	java/net/Socket:close	()V
                    //   187: return
                    //   188: astore 9
                    //   190: aload 9
                    //   192: invokevirtual 89	java/io/IOException:printStackTrace	()V
                    //   195: return
                    //   196: aload 12
                    //   198: instanceof 91
                    //   201: ifeq -156 -> 45
                    //   204: aload 11
                    //   206: aload 12
                    //   208: checkcast 91	java/lang/String
                    //   211: invokevirtual 93	java/io/BufferedWriter:write	(Ljava/lang/String;)V
                    //   214: aload 11
                    //   216: invokevirtual 94	java/io/BufferedWriter:flush	()V
                    //   219: goto -174 -> 45
                    //   222: astore 5
                    //   224: aload 11
                    //   226: astore_2
                    //   227: aload_3
                    //   228: astore_1
                    //   229: ldc 75
                    //   231: invokestatic 79	com/oneman/freeusbtools/util/SocketLogger:debug	(Ljava/lang/String;)V
                    //   234: aload_1
                    //   235: ifnull +11 -> 246
                    //   238: aload_2
                    //   239: invokevirtual 82	java/io/BufferedWriter:close	()V
                    //   242: aload_1
                    //   243: invokevirtual 83	java/net/Socket:close	()V
                    //   246: aload 5
                    //   248: athrow
                    //   249: astore 7
                    //   251: aload 7
                    //   253: invokevirtual 89	java/io/IOException:printStackTrace	()V
                    //   256: return
                    //   257: astore 6
                    //   259: aload 6
                    //   261: invokevirtual 89	java/io/IOException:printStackTrace	()V
                    //   264: goto -18 -> 246
                    //   267: astore 5
                    //   269: goto -40 -> 229
                    //   272: astore 5
                    //   274: aload_3
                    //   275: astore_1
                    //   276: aconst_null
                    //   277: astore_2
                    //   278: goto -49 -> 229
                    //   281: astore 8
                    //   283: aconst_null
                    //   284: astore_2
                    //   285: aconst_null
                    //   286: astore_1
                    //   287: goto -122 -> 165
                    //   290: astore 8
                    //   292: aload_3
                    //   293: astore_1
                    //   294: aconst_null
                    //   295: astore_2
                    //   296: goto -131 -> 165
                    //   299: astore 4
                    //   301: aconst_null
                    //   302: astore_2
                    //   303: aconst_null
                    //   304: astore_1
                    //   305: goto -206 -> 99
                    //   308: astore 4
                    //   310: aload_3
                    //   311: astore_1
                    //   312: aconst_null
                    //   313: astore_2
                    //   314: goto -215 -> 99
                    // Local variable table:
                    //   start	length	slot	name	signature
                    //   0	317	0	this	1
                    //   1	311	1	localObject1	Object
                    //   3	311	2	localObject2	Object
                    //   16	295	3	localSocket	java.net.Socket
                    //   92	8	4	localUnknownHostException1	java.net.UnknownHostException
                    //   299	1	4	localUnknownHostException2	java.net.UnknownHostException
                    //   308	1	4	localUnknownHostException3	java.net.UnknownHostException
                    //   222	25	5	localObject3	Object
                    //   267	1	5	localObject4	Object
                    //   272	1	5	localObject5	Object
                    //   257	3	6	localIOException1	java.io.IOException
                    //   249	3	7	localIOException2	java.io.IOException
                    //   158	8	8	localIOException3	java.io.IOException
                    //   281	1	8	localIOException4	java.io.IOException
                    //   290	1	8	localIOException5	java.io.IOException
                    //   188	3	9	localIOException6	java.io.IOException
                    //   21	120	10	localOutputStream	OutputStream
                    //   39	186	11	localBufferedWriter	java.io.BufferedWriter
                    //   51	156	12	localObject6	Object
                    //   148	3	13	localIOException7	java.io.IOException
                    //   82	3	14	localUnknownHostException4	java.net.UnknownHostException
                    // Exception table:
                    //   from	to	target	type
                    //   53	79	82	java/net/UnknownHostException
                    //   122	145	82	java/net/UnknownHostException
                    //   196	219	82	java/net/UnknownHostException
                    //   41	45	92	java/net/UnknownHostException
                    //   45	53	92	java/net/UnknownHostException
                    //   84	89	92	java/net/UnknownHostException
                    //   150	155	92	java/net/UnknownHostException
                    //   53	79	148	java/io/IOException
                    //   122	145	148	java/io/IOException
                    //   196	219	148	java/io/IOException
                    //   41	45	158	java/io/IOException
                    //   45	53	158	java/io/IOException
                    //   84	89	158	java/io/IOException
                    //   150	155	158	java/io/IOException
                    //   179	187	188	java/io/IOException
                    //   41	45	222	finally
                    //   45	53	222	finally
                    //   53	79	222	finally
                    //   84	89	222	finally
                    //   122	145	222	finally
                    //   150	155	222	finally
                    //   196	219	222	finally
                    //   113	121	249	java/io/IOException
                    //   238	246	257	java/io/IOException
                    //   4	17	267	finally
                    //   99	104	267	finally
                    //   165	170	267	finally
                    //   17	41	272	finally
                    //   4	17	281	java/io/IOException
                    //   17	41	290	java/io/IOException
                    //   4	17	299	java/net/UnknownHostException
                    //   17	41	308	java/net/UnknownHostException
                }
            }.start();
        }
    }

    public static void debug(String paramString)
    {
        if ((!ENABLE_LOGGING) || (!isConnected)) {
            return;
        }
        queue.offer(paramString + "\n");
    }

    public void write(int paramInt)
    {
        if ((!ENABLE_LOGGING) || (!isConnected)) {
            return;
        }
        queue.offer(Integer.valueOf(paramInt));
    }

    public void write(byte[] paramArrayOfByte)
    {
        if ((!ENABLE_LOGGING) || (!isConnected)) {
            return;
        }
        queue.offer(paramArrayOfByte);
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
        if ((!ENABLE_LOGGING) || (!isConnected)) {
            return;
        }
        byte[] arrayOfByte = new byte[paramInt2];
        System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
        queue.offer(arrayOfByte);
    }
}


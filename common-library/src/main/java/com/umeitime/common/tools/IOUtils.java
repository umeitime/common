package com.umeitime.common.tools;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename com.umeitime.common.tools
 * @desc: io操作
 */

public class IOUtils {
    /**
     * Close closeable object
     * 关闭可以关闭的对象
     *
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LogUtils.d("IOUtils",e.toString());
            }
        }
    }

}

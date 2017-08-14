package com.umeitime.common.tools;

import android.content.Context;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename com.umeitime.common.tools
 * @desc: 序列化和反序列化操作类
 */

public class SerializableUtils {
    /**
     * 序列化数据
     *
     * @param context
     * @param fileName
     * @param obj
     * @throws IOException
     */
    public static void serializeData(Context context, String fileName, Object obj) throws IOException {
        if (!(obj instanceof Serializable) && !(obj instanceof Externalizable)) {
            throw new InvalidClassException("Object must be serialized!");
        }
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream ostream = new ObjectOutputStream(fos);
        ostream.writeObject(obj);
        ostream.flush();
        ostream.close();
        fos.close();
    }

    /**
     * 反序列化数据
     *
     * @param context
     * @param fileName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Object deserializeData(Context context, String fileName) throws ClassNotFoundException, IOException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream s = new ObjectInputStream(fis);
        Object obj = s.readObject();
        s.close();
        fis.close();
        return obj;
    }

}

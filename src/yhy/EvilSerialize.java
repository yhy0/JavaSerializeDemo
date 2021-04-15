package yhy;

import java.io.*;

/**
 * @author yhy
 * @date 2021/4/4 23:36
 * @github https://github.com/yhy0
 */


public class EvilSerialize {
    public static void main(String[] args) throws Exception {
//        EvilClass evilObj = new EvilClass();
//        evilObj.setName("calc");
        // mac
//        EvilClass evilObj = new EvilClass("Calculator");
        Evil evilObj = new Evil("Calculator");

        // 序列化为字节数组
        byte[] bytes = serializeToBytes(evilObj);
        // 反序列化
        Evil o = (Evil)deserializeFromBytes(bytes);
        System.out.println(o);
    }

    public static byte[] serializeToBytes(final Object obj) throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        objOut.flush();
        objOut.close();
        return out.toByteArray();
    }

    public static Object deserializeFromBytes(final byte[] serialized) throws Exception {
        final ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        final ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }
}

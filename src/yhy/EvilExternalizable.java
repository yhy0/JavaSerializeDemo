package yhy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author yhy
 * @date 2021/4/5 00:29
 * @github https://github.com/yhy0
 */

public class EvilExternalizable {


//    public static void main(String[] args) throws Exception {
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ExPerson.txt"));
//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ExPerson.txt"));
//
//            oos.writeObject(new Evil("yhy"));
//            Evil ep = (Evil) ois.readObject();
//            System.out.println(ep);
//
//    }
//
//    public static byte[] serializeToBytes(final Object obj) throws Exception {
//        final ByteArrayOutputStream out = new ByteArrayOutputStream();
//        final ObjectOutputStream objOut = new ObjectOutputStream(out);
//        objOut.writeObject(obj);
//        objOut.flush();
//        objOut.close();
//        return out.toByteArray();
//    }
//
//    public static Object deserializeFromBytes(final byte[] serialized) throws Exception {
//        final ByteArrayInputStream in = new ByteArrayInputStream(serialized);
//        final ObjectInputStream objIn = new ObjectInputStream(in);
//        return objIn.readObject();
//    }
//
}

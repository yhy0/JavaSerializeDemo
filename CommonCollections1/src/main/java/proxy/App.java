package proxy;

/**
 * @author yhy
 * @date 2021/6/11 08:32
 * @github https://github.com/yhy0
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        InvocationHandler handler = new ExampleInvocationHandler(new HashMap());
        // Proxy.newProxyInstance 的第一个参数是ClassLoader，我们用默认的即可;第二个参数是我们需要 代理的对象集合;第三个参数是一个实现了InvocationHandler接口的对象，里面包含了具体代理的逻辑。

        Map proxyMap = (Map) Proxy.newProxyInstance(
                Map.class.getClassLoader(),
                new Class[] {Map.class},
                handler
        );

        proxyMap.put("hello", "world");
        String result = (String) proxyMap.get("hello");
        System.out.println(result);
    }
}

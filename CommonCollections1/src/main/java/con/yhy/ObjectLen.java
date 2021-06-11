package con.yhy;

import org.apache.commons.collections.Transformer;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * @author yhy
 * @date 2021/6/10 19:01
 * @github https://github.com/yhy0
 */

// 当map 中的 key 变动时，输出一句话
interface Test {
    void test();
    void test1();
}
public class ObjectLen {
    public static void main(String[] args) {
        Class[] var = Documented.class.getInterfaces();
        System.out.println(var.length);
        System.out.println(var.toString());

    }
}

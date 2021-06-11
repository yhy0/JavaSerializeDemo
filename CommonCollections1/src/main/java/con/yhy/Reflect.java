package con.yhy;

import java.lang.reflect.Method;

/**
 * @author yhy
 * @date 2021/6/8 22:00
 * @github https://github.com/yhy0
 */

public class Reflect {
    public static void main(String[] args) throws Exception {
        Object input = Runtime.getRuntime();    // 第一个数组元素的执行返回结果
        Class cls = input.getClass();
        Method method = cls.getMethod("exec", new Class[]{String.class});
        // 反射执行命令
        method.invoke(input, new Object[] {"/System/Applications/Calculator.app/Contents/MacOS/Calculator"});
    }

}

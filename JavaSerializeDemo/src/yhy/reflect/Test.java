package yhy.reflect;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author yhy
 * @date 2021/5/2 21:08
 * @github https://github.com/yhy0
 */

public class Test {
    public static void main(String[] args) throws Exception {
        //正常实例化、调用
        User user = new User();
        user.setName("yhy");
        System.out.println("正：" + user.getName());

        //利用反射机制

        // new
        Class clz = Class.forName("yhy.reflect.User");
        Object object = clz.newInstance();

        // setName("yhy")
        Method method = clz.getMethod("setName", String.class);
        method.invoke(object, "yhy");

        // getName()
        Method name = clz.getDeclaredMethod("getName",null);
        Object o1 = name.invoke(object, null);
        System.out.println("反：" + o1);


        // 通过 Constructor 对象创建类对象可以选择特定构造方法
        Constructor constructor = clz.getConstructor(String.class);
        Object object1 = constructor.newInstance("yhy1");
        // getName()
        Method name1 = clz.getDeclaredMethod("getName",null);
        Object o11 = name1.invoke(object1, null);
        System.out.println("反1：" + o11);

        Field[] fields = clz.getFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }


        Field[] fields1 = clz.getDeclaredFields();
        for (Field field : fields1) {
            System.out.println(field.getName());
        }


        // 通过反射调用命令执行
        //获取对象
        Class cls = Class.forName("java.lang.Runtime");
        //实例化对象
        Object ob = cls.getMethod("getRuntime",null).invoke(null,null);
        // 反射调用执行命令， Mac
        cls.getMethod("exec", String.class).invoke(ob,"open -a Calculator");

        // cls.getMethod("exec", String.class).invoke(ob,"calc");
    }
}


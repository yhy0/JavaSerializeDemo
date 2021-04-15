package yhy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author yhy
 * @date 2021/4/5 00:24
 * @github https://github.com/yhy0
 */

public class Evil implements Externalizable {
    String name;

    // 实现了Externalizable这个接口需要提供无参构造，在反序列化时会检测
    public Evil() {
        System.out.println(this.getClass() + "的EvilClass()无参构造方法被调用!!!!!!");
    }

    public Evil(String name) {
        System.out.println(this.getClass() + "的EvilClass(String name)构造方法被调用!!!!!!");
        this.name = name;
    }
    public String getName() {
        System.out.println(this.getClass() + "的getName被调用!!!!!!");
        return name;
    }

    public void setName(String name) {
        System.out.println(this.getClass() + "的setName被调用!!!!!!");
        this.name = name;
    }

    @Override
    public String toString() {
        System.out.println(this.getClass() + "的toString()被调用!!!!!!");
        return "EvilClass{" +
                "name='" + getName() + '\'' +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("evil writeExternal...");
        out.writeObject(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("evil readExternal...");
        name = (String) in.readObject();
        Runtime.getRuntime().exec(new String[]{"open", "-a", name});
    }

}

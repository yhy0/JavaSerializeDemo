package yhy;

/**
 * @author yhy
 * @date 2021/4/4 21:46
 * @github https://github.com/yhy0
 */

import java.io.Serializable;

// 要实现序列化的类
public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAAA() {
        return "AAAAAAAAAAAAAAAAAAAAAA";
    }
}

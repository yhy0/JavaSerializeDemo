package yhy.reflect;

/**
 * @author yhy
 * @date 2021/5/2 21:08
 * @github https://github.com/yhy0
 */

public class User {
    private String name;
    public int age;

    public User(){}

    public User(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

package com.chen.autocases;
public class ClassObject {
    String name;
    int age;
    static String hobby ="eat";
    private String drink = "water";
    public ClassObject(String name,int age) {
        this.name=name;
        this.age=age;
    }
    public String  getstring() {
        return this.name;
    }
    public static void main (String [] args) {
        ClassObject ClassObject =new ClassObject("zs",21);
    }
}
class UseClassObject extends ClassObject{
    String user;
    public UseClassObject(String user,String name,int age) {
        super(name,age);
        this.user=user;
    }

    public String getstring() {
        return this.user;
    }
    public static void main(String [] args) {
        UseClassObject test = new UseClassObject("admin","test",22);
        ClassObject ClassObject =new ClassObject("zs",21);
        System.out.println(test.age);  //子类实例访问实例变量
        System.out.println(ClassObject.name);  // 父类实例访问实例变量
        System.out.println(ClassObject.hobby); // 父类访问类变量
        System.out.println(UseClassObject.hobby); //子类访问类变量
    }
}

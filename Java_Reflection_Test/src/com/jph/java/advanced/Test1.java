package com.jph.java.advanced;

import java.lang.reflect.Constructor;

import com.jph.test.Child;

/**
 * 反射 Class 以及构造对象
 * issue:
 * 1.父类获取子类的方法和属性
 * 2.被依赖包调用依赖包中类的熟悉和方法
 * 
 * @author Penn
 */
public class Test1 {
	public static String string="haha";
	public static void main(String[] args) {
//		classForName();
		Child child=new Child();
		child.call("dd");
	}

	/**
	 * 通过 Class 对象构造目标类型的对象
	 */
	private static void classForName() {
		try {
			// 获取 Class 对象
			Class<?> clz = Class.forName("com.jph.study.Person");
			// 通过 Class 对象获取 Constructor，Student 的构造函数有一个字符串参数
			// 因此这里需要传递参数的类型 ( Student 类见后面的代码 )
			Constructor<?> constructor = clz.getConstructor(String.class);
			// 通过 Constructor 来创建 Student 对象
			Object obj = constructor.newInstance("小明");
			Person p = (Person) obj;
			System.out.println(" obj :  " + p.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

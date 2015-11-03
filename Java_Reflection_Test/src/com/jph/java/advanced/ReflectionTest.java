package com.jph.java.advanced;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {
	public static void main(String args[]) throws Exception {
		System.out.println("-----");
		//获取类的构造方法
//		test1();
		//通过反射获取类的实例
//		test2();
		//获取和修改类中的属性
//		test3();
		//获取并调用类中的方法 
//		test4();
		//通过反射动态创建和访问数组  
		test5();
	}

	/**
	 * 通过反射动态创建和访问数组  
	 */
	private static void test5() throws Exception{
		Class<?>componentType=Class.forName("java.lang.String");
		Object array=Array.newInstance(componentType, 10);
		Array.set(array, 5, "通过反射动态创建和访问数组 ");
		System.out.println(Array.get(array, 5));
	}

	/**
	 * 获取并调用类中的方法 
	 */
	private static void test4() throws Exception{
		System.out.println("调用非静态的方法");
		Person person=new Person();
		Class<?>cls=person.getClass();
		Method method=cls.getMethod("setAge", new Class[]{int.class});
		method.invoke(person, 18);
		Method method2=cls.getMethod("getAge", new Class[]{});
		Object object=method2.invoke(person, new Object[0]);
		System.out.println(object.toString());
		
		System.out.println("调用静态的方法");
		Class<?> cls2=Class.forName("java.lang.String");
		Method method3=cls2.getMethod("valueOf", new Class[]{long.class});
		Object object2=method3.invoke(null, 123);
		System.out.println(object2.toString());
	}

	/**
	 *获取和修改类中的属性 
	 */
	private static void test3() throws Exception{
		Person person=new Person();
		Class<?>cls=person.getClass();
		Field field=cls.getDeclaredField("age");
		field.setAccessible(true);
		field.set(person,18);
		System.out.println(field.get(person));
		
		System.out.println("修改类的静态字段");
		Field field2=cls.getDeclaredField("height");
		field2.setAccessible(true);
		field2.set(null, 180);
		System.out.println(field2.get(null));
		
		
		System.out.println("获取该类以及继承的所有公有属性");
		Field[]fields=cls.getFields();
		for (int i = 0; i < fields.length; i++) {
			System.out.println("总共："+fields.length+"当前第"+(i+1)+"个 "+fields[i].toString());
		}
		
		System.out.println("获取该类的所有属性");
		Field[]fields2=cls.getDeclaredFields();
		for (int i = 0; i < fields2.length; i++) {
			System.out.println("总共："+fields2.length+"当前第"+(i+1)+"个 "+fields2[i].toString());
		}
		
	}

	/**
	 * 通过反射获取类的实例
	 * @throws Exception
	 */
	private static void test2()throws Exception {
		Class<?> cls=Class.forName("java.lang.String");
		Constructor<?>defaultConstructor=cls.getConstructor();
		defaultConstructor.newInstance();
		System.out.println(cls.toString());
		
		Class<?>[]parameterTypes2=new Class[]{char[].class,boolean.class};
		System.out.println("2.通过参数获取类的指定构造方法： ");
		Constructor<?>constructor2=cls.getDeclaredConstructor(parameterTypes2);
		System.out.println("constructor2:"+constructor2.toString());
//		constructor2.setAccessible(true);
		char[] chars={'a',' ','s','t','u','d','e','n','t'};
		Object object=constructor2.newInstance(chars,false);
		System.out.println(object.toString());
	}

	/**
	 * 获取类的构造方法
	 * @throws Exception 
	 */
	private static void test1() throws Exception {
		Class<?> cls=Class.forName("java.lang.String");
		
		Class<?>[]parameterTypes=new Class[]{String.class};
		System.out.println("1.通过参数获取类的指定公共构造方法： ");
		Constructor<?>constructor=cls.getConstructor(parameterTypes);
		System.out.println("constructor:"+constructor.toString());
		
		Class<?>[]parameterTypes2=new Class[]{char[].class,boolean.class};
		System.out.println("2.通过参数获取类的指定构造方法： ");
		Constructor<?>constructor2=cls.getDeclaredConstructor(parameterTypes2);
		System.out.println("constructor2:"+constructor2.toString());
		constructor2.setAccessible(true);
		char[] chars={'a',' ','s','t','u','d','e','n','t'};
		Object object=constructor2.newInstance(chars,false);
		System.out.println(object.toString());
		
		System.out.println("3.获取所有的公共构造方法");
		Constructor<?>[]allPubConstructors=cls.getConstructors();
		for (int i = 0; i < allPubConstructors.length; i++) {
			System.out.println("总共："+allPubConstructors.length+"当前第"+(i+1)+"个 "+allPubConstructors[i].toString());
		}
		
		System.out.println("4.获取所有本类定义的构造方法");
		Constructor<?>[]allOwnConstructors=cls.getDeclaredConstructors();
		for (int i = 0; i < allOwnConstructors.length; i++) {
			System.out.println("总共："+allOwnConstructors.length+"当前第"+(i+1)+"个 "+allOwnConstructors[i].toString());
		}
		
		Object object2=constructor.newInstance("获取类的构造方法");
		System.out.println("object="+object2);
		
		System.out.println("5.获取类的默认构造方法");
		Constructor<?>defaultConstructor=cls.getConstructor();
		defaultConstructor.newInstance();
		System.out.println(cls.toString()); 
	}
	
}

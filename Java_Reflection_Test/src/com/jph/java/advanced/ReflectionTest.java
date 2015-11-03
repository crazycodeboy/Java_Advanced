package com.jph.java.advanced;

import java.lang.reflect.Constructor;

public class ReflectionTest {
	public static void main(String args[]) throws Exception {
		System.out.println("-----");
		//获取类的构造方法
//		test1();
		//通过反射获取类的实例
//		test2();
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

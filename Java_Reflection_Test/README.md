# Java反射研究和应用 #

原理和作用   
访问和修改类的静态字段和方法  
构造类的对象  
调用对象的方法和修改对象的属性  
安全性和反射  
反射的性能问题  
反射与动态代理




###概述
Java的反射机制是Java语言动态性的一种体现。反射机制是通过反射API来实现的，它允许程序在运行过程中取得任何一个已知名称的类的内部信息，包括其中的构造方法、声明的属性和定义的方法等。这不得不说是一个很强大的能力。   
正如英文单词reflection的含义一样，使用反射API的时候就好像在看一个Java类在水中的倒影一样。知道了Java类的内部结构之后，就可以与它进行交互，包括创建新的对象和调用对象中的方法等。这种交互方式与直接在源代码中使用的效果是相同的，但是又额外提供了运行时刻的灵活性。使用反射的一个最大的弊端是性能比较差。相同的操作，用反射API所需的时间大概比直接的使用要慢一两到三个数量级。不过现在的JVM实现中，反射操作的性能已经有了很大的提升。
  
###基本用法
说到反射的用法不得不提java.lang.Class类，通过该类的对象就的方法可以获取到该类中的构造方法、域和方法。对应的方法分别是**getConstructor**、**getField**和**getMethod**。这三个方法还有相应的**getDeclaredXXX**版本，区别在于**getDeclaredXXX**版本的方法会获取该类自身所声明的所有属性包括public, protected, default (package), 和 private，但不会获取继承下来的属性。   
Java反射API位于java.lang.reflect包中。主要包括以下几类：
**Constructor类**：用来描述一个类的构造方法。
**Field类**：用来描述一个类的成员变量。
**Method类**：用来描述一个类的方法。
**Modifer类**：用来描述类内各元素的修饰符。
**Array**：用来对数组进行操作。
Constructor、Field和Method这三个类分别表示类中的构造方法、属性和方法。
###获取类的构造方法
java.lang.Class提供了4种获取类构造方法的反射调用：
- `Constructor getConstructor(Class[] params)` -获得使用特殊的参数类型的公共构造函数
- `Constructor[] getConstructors()` -获得类的所有公共构造函数
- `Constructor getDeclaredConstructor(Class[] params)` -获得使用特定参数类型的构造函数(不包含继承的构造方法)
- `Constructor[] getDeclaredConstructors()` -获得类的所有构造函数(不包含继承的构造方法)   
   
调用这些方法会返回一个或多个 java.lang.reflect.Constructor 对象。Constructor 类定义了newInstance 方法，它采用一组对象作为其唯一的参数，然后返回新创建的原始类实例。该组对象是用于构造函数调用的参数值。
**第一步：获取类的Class对象**  
方式一：通过将目标类装入到虚拟机的方式获得该类的Class对象  
```java  
Class<?> cls=Class.forName("java.lang.String");	  
```  
此种方式通常会用在无法直接引用目标类的情况。
方式二：通过类或类的对象获取类的Class对象  
```java
Class<?> cls=String.class;
或
String str="";
Class<?> cls=str.getClass()
Class<?> cls=Class.forName("java.lang.String");
```  
**第二步：根据需要调用Class对象的4种获取构造方法的一种，获取到类的构造方法**
####1.通过参数获取类的指定公共构造方法  
```java 
	
Class<?>[]parameterTypes=new Class[]{String.class};
System.out.println("1.通过参数获取类的指定公共构造方法： ");
Constructor<?>constructor=cls.getConstructor(parameterTypes);
System.out.println("constructor:"+constructor.toString());
```  
####2.通过参数获取类的指定构造方法    
```java
Class<?> cls=Class.forName("java.lang.String");	
Class<?>[]parameterTypes2=new Class[]{char[].class,boolean.class};
System.out.println("2.通过参数获取类的指定构造方法： ");
Constructor<?>constructor2=cls.getDeclaredConstructor(parameterTypes2);
System.out.println("constructor2:"+constructor2.toString());
```  
####3.获取所有的公共构造方法  
```java
System.out.println("3.获取所有的公共构造方法");
Constructor<?>[]allPubConstructors=cls.getConstructors();
for (int i = 0; i < allPubConstructors.length; i++) {
	System.out.println("总共："+allPubConstructors.length+"当前第"+(i+1)+"个 "+allPubConstructors[i].toString());
}
```
####4.获取所有本类定义的构造方法
```java
System.out.println("4.获取所有本类定义的构造方法");
Constructor<?>[]allOwnConstructors=cls.getDeclaredConstructors();
for (int i = 0; i < allOwnConstructors.length; i++) {
	System.out.println("总共："+allOwnConstructors.length+"当前第"+(i+1)+"个 "+allOwnConstructors[i].toString());
}
```
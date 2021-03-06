# Java反射研究和实践 #

###概述
Java的反射机制是Java语言动态性的一种体现。反射机制是通过反射API来实现的，它允许程序在运行过程中取得任何一个已知名称的类的内部信息，包括其中的构造方法、声明的字段和定义的方法等。这不得不说是一个很强大的能力。   
正如英文单词reflection的含义一样，使用反射API的时候就好像在看一个Java类在水中的倒影一样。知道了Java类的内部结构之后，就可以与它进行交互，包括创建新的对象和调用对象中的方法等。这种交互方式与直接在源代码中使用的效果是相同的，但是又额外提供了运行时刻的灵活性。使用反射的一个最大的弊端是性能比较差。相同的操作，用反射API所需的时间大概比直接的使用要慢一两到三个数量级。不过现在的JVM实现中，反射操作的性能已经有了很大的提升。
  
###基本用法
说到反射的用法不得不提java.lang.Class类，通过该类的对象就的方法可以获取到该类中的构造方法、域和方法。对应的方法分别是**getConstructor**、**getField**和**getMethod**。这三个方法还有相应的**getDeclaredXXX**版本，区别在于**getDeclaredXXX**版本的方法会获取该类自身所声明的所有字段包括public, protected, default (package), 和 private，但不会获取继承下来的字段。     
Java反射API位于java.lang.reflect包中。主要包括以下几类：  
**Constructor类**：用来描述一个类的构造方法。  
**Field类**：用来描述一个类的成员变量。  
**Method类**：用来描述一个类的方法。  
**Modifer类**：用来描述类内各元素的修饰符。  
**Array**：用来对数组进行操作。      
Constructor、Field和Method这三个类分别表示类中的构造方法、字段和方法。  
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
####5.获取类的默认构造方法
```java
System.out.println("5.获取类的默认构造方法");
Constructor<?>defaultConstructor=Person.class.getConstructor();
defaultConstructor.newInstance();
System.out.println(cls.toString()); 
```
###通过反射获取类的实例  
上文中已经通过反射获取到类的构造方法也就是Constructor对象。Constructor对象提供了`public T newInstance(Object ... initargs)`方法，该方法返回类的实例，其中initargs作为调用类的构造方法的参数数组，反射API会根据数组中元素的类型和个数选择调用类的相应构造方法。
```java
Class<?>[]parameterTypes2=new Class[]{char[].class,boolean.class};
System.out.println("2.通过参数获取类的指定构造方法： ");
Constructor<?>constructor2=cls.getDeclaredConstructor(parameterTypes2);
System.out.println("constructor2:"+constructor2.toString());
constructor2.setAccessible(true);
char[] chars={'a',' ','s','t','u','d','e','n','t'};
Object object=constructor2.newInstance(chars,false);
System.out.println(object.toString());
```  
上面代码中`constructor2.setAccessible(true)`的意思是告诉虚拟机，当调用该构造方法时，不用进行访问权限的控制，也就是给反射开个绿灯。相应地Method，Field以及Constructor类都直接或间接的继承了AccessibleObject类，该类提供了将反射的对象标记为在使用时取消默认 Java 语言访问控制检查的能力。所以说在使用Method，Field以及Constructor类的对象时，如果该对象所表示的方法或字段是非public的，那么在访问它之前需要调用` public void setAccessible(boolean flag)`方法将该对象的访问控制权限取消，否则java的异常处理机制会告诉你` can not access a member of class`。  
###通过反射获取和修改类中的字段
和获取类的构造方法相似的是Class类同样也提供了获取类中字段的4种调用：  
`Field getField(String name)` -- 获得命名的公共字段  
`Field[] getFields() `-- 获得类的所有公共字段  
`Field getDeclaredField(String name)` -- 获得类声明的命名的字段  
`Field[] getDeclaredFields()`     -- 获得类声明的所有字段  
    同样getField与getFields可以获得当前类中以及继承的public字段，getDeclaredField与getDeclaredFields则可以获得当前类中声明的所有字段，但不能获取继承的字段。  
java.lang.reflect.Field表示类中的字段，该类提供了getXXX 和 setXXX 方法，以及get 和 set 方法来获取和设置该字段的值。大家可以根据实际的字段类型自行选择一种适当的方法。
先看一下一会要用到的Person类，该类是一个简单的JavaBean。  
```java
public class Person {
	private static int height;
	private int age;
	private String name;
/******其他代码省略******/
```
####1.获取类的字段
**获取指定字段：**
```java
Person person=new Person();
Class<?>cls=person.getClass();
Field field=cls.getDeclaredField("age");
field.set(person,18);
System.out.println(field.toString());
```  
**获取类的所有字段**
- 获取该类以及继承的所有公有字段  
    
```java
System.out.println("获取该类以及继承的所有公有字段");
Field[]fields=cls.getFields();
for (int i = 0; i < fields.length; i++) {
	System.out.println("总共："+fields.length+"当前第"+(i+1)+"个 "+fields[i].toString());
}
```  
- 获取该类的所有字段  

```java
System.out.println("获取该类的所有字段");  
Field[]fields2=cls.getDeclaredFields();
for (int i = 0; i < fields2.length; i++) {
	System.out.println("总共："+fields2.length+"当前第"+(i+1)+"个 "+fields2[i].toString());
}
```    
        
####2.修改字段
**修非静态字段**  
非静态字段和方法属于类的对象所有，所以要修改类的非静态字段需要一个该类的对象。  
    
```java
Person person=new Person();
Class<?>cls=person.getClass();
Field field=cls.getDeclaredField("age");
field.setAccessible(true);
field.set(person,18);
System.out.println(field.get(person));
```  
需要指出的是如果字段是非public的，需要在访问该字段前取消该字段的访问权限控制`field.setAccessible(true)`，那么对于一个final字段是否可以通过反射修改它的值呢，答案是肯定的，前提同样是在访问该字段前取消该字段的访问权限控制。但如果该字段既被final修饰又被static修饰，那么是无法修改的。   
**修改静态字段**  
和修改非静态字段相比，修改类的静态字段就要轻松的多了，因为静态字段属于类所有，所以在修改静态字段的时候就不需要再传递一个该类的对象了。  
```java
System.out.println("修改类的静态字段");
Field field2=cls.getDeclaredField("height");
field2.setAccessible(true);
field2.set(null, 180);
System.out.println(field2.get(null));
```
###通过反射获取并调用类中的方法  
Class类提供了以下4种方式来获取类中的方法：  
`Method getMethod(String name, Class[] params)` -- 使用特定的参数类型，获得命名的公共方法  
`Method[] getMethods()` -- 获得类的所有公共方法  
`Method getDeclaredMethod(String name, Class[] params)` -- 使用特写的参数类型，获得类声明的命名的方法  
`Method[] getDeclaredMethods()` -- 获得类声明的所有方法  
与获取类中字段类似，前两个方法用于获取类中以及继承的所有公共方法 。后两个方法用于获取当前类中（不包含继承）所有的方法。以上4种方法返回的是Method对象，Method类中提供了`public Object invoke(Object obj, Object... args)`通过该方法我们可以调用任何一个类的任何一个方法。invoke 方法使用两个参数，为调用提供类实例和参数值数组。  
####调用非静态的方法
```java
Person person=new Person();
Class<?>cls=person.getClass();
Method method=cls.getMethod("setAge", new Class[]{int.class});
method.invoke(person, 18);
Method method2=cls.getMethod("getAge", new Class[]{});
Object object=method2.invoke(person, new Object[0]);
System.out.println(object.toString());
```  
####调用静态方法
```java
System.out.println("调用静态的方法");
Class<?> cls2=Class.forName("java.lang.String");
Method method3=cls2.getMethod("valueOf", new Class[]{long.class});
Object object2=method3.invoke(null, 123);
System.out.println(object2.toString());
```  
非静态方法属于类方法，所以调用的时候可以将invoke的第一参数省去。  
###通过反射动态创建和访问数组  
java.lang.reflect.Array 类提供的静态方法的集合。该类中的方法使您能够创建新数组，获得数组对象的长度，读和写数组对象的索引值。 
```java
Class<?>componentType=Class.forName("java.lang.String");
Object array=Array.newInstance(componentType, 10);
Array.set(array, 5, "通过反射动态创建和访问数组 ");
System.out.println(Array.get(array, 5));
```   
上述代码中`Array.newInstance(componentType, 10);`表示创建一个componentType类型大小为10的数组。`Array.set(array, 5, "通过反射动态创建和访问数组 ");`表示将数组第6个元素修改为"通过反射动态创建和访问数组 "，`Array.get(array, 5)`表示访问数组中第6个元素的值。  
###反射的应用   
  
####Java反射与动态代理     
[Java反射与动态代理](https://github.com/crazycodeboy/Java_Advanced/tree/master/Java_Proxy_Test "Java反射与动态代理")

####反射在orm框架上的使用  
待续...


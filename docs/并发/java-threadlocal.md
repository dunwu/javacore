---
title: ThreadLocal
date: 2018/01/22
categories:
- javase
tags:
- javase
- concurrent
---
# ThreadLocal

> 本文版本：JDK8

## 对ThreadLocal的理解

ThreadLocal，很多地方叫做线程本地变量，也有些地方叫做线程本地存储，其实意思差不多。可能很多朋友都知道ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。

示例 - 非线程安全示例

```javaopenConnection
class ConnectionManager {
     
    private static Connection connect = null;
     
    public static Connection openConnection() {
        if(connect == null){
            connect = DriverManager.getConnection();
        }
        return connect;
    }
     
    public static void closeConnection() {
        if(connect!=null)
            connect.close();
    }
}

```

假设有这样一个数据库链接管理类，这段代码在单线程中使用是没有任何问题的，但是如果在多线程中使用呢？很显然，在多线程中使用会存在线程安全问题：第一，这里面的2个方法都没有进行同步，很可能在openConnection方法中会多次创建connect；第二，由于connect是共享变量，那么必然在调用connect的地方需要使用到同步来保障线程安全，因为很可能一个线程在使用connect进行数据库操作，而另外一个线程调用closeConnection关闭链接。

所以出于线程安全的考虑，必须将这段代码的两个方法进行同步处理，并且在调用connect的地方需要进行同步处理。

这样将会大大影响程序执行效率，因为一个线程在使用connect进行数据库操作的时候，其他线程只有等待。

那么大家来仔细分析一下这个问题，这地方到底需不需要将connect变量进行共享？事实上，是不需要的。假如每个线程中都有一个connect变量，各个线程之间对connect变量的访问实际上是没有依赖关系的，即一个线程不需要关心其他线程是否对这个connect进行了修改的。

到这里，可能会有朋友想到，既然不需要在线程之间共享这个变量，可以直接这样处理，在每个需要使用数据库连接的方法中具体使用时才创建数据库链接，然后在方法调用完毕再释放这个连接。比如下面这样：

```java
class ConnectionManager {
     
    private  Connection connect = null;
     
    public Connection openConnection() {
        if(connect == null){
            connect = DriverManager.getConnection();
        }
        return connect;
    }
     
    public void closeConnection() {
        if(connect!=null)
            connect.close();
    }
}
 
 
class Dao{
    public void insert() {
        ConnectionManager connectionManager = new ConnectionManager();
        Connection connection = connectionManager.openConnection();
         
        //使用connection进行操作
         
        connectionManager.closeConnection();
    }
}
```

这样处理确实也没有任何问题，由于每次都是在方法内部创建的连接，那么线程之间自然不存在线程安全问题。但是这样会有一个致命的影响：导致服务器压力非常大，并且严重影响程序执行性能。由于在方法中需要频繁地开启和关闭数据库连接，这样不仅严重影响程序执行效率，还可能导致服务器压力巨大。

那么这种情况下使用ThreadLocal是再适合不过的了，因为ThreadLocal在每个线程中对该变量会创建一个副本，即每个线程内部都会有一个该变量，且在线程内部任何地方都可以使用，线程之间互不影响，这样一来就不存在线程安全问题，也不会严重影响程序执行性能。

但是要注意，虽然ThreadLocal能够解决上面说的问题，但是由于在每个线程中都创建了副本，所以要考虑它对资源的消耗，比如内存的占用会比不使用ThreadLocal要大。

## 深入解析ThreadLocal类

ThreadLocal 的主要方法：

```java
public class ThreadLocal<T> {
    public T get() {}
	public void remove() {}
	public void set(T value) {}
	public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {}
}

```

* get()方法是用来获取ThreadLocal在当前线程中保存的变量副本。
* set()用来设置当前线程中变量的副本。
* remove()用来移除当前线程中变量的副本。
* initialValue()是一个protected方法，一般是用来在使用时进行重写的，它是一个延迟加载方法，下面会详细说明。

### get() 源码实现

**get 源码**

```java
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}

```
1. 取得当前线程。
2. 通过getMap() 方法获取 ThreadLocalMap。
3. 成功，返回 value；失败，返回 setInitialValue()。

**ThreadLocalMap 源码**

ThreadLocalMap 是 ThreadLocal 的一个内部类。

ThreadLocalMap 的 Entry 继承了 WeakReference，并且使用 ThreadLocal 作为键值。

### setInitialValue 源码实现

```java
private T setInitialValue() {
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
    return value;
}

```

如果 map 不为空，就设置键值对；为空，再创建 Map，看一下 createMap 的实现：

```java
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

### ThreadLocal 源码小结

至此，可能大部分朋友已经明白了ThreadLocal是如何为每个线程创建变量的副本的：

1. 在每个线程Thread内部有一个ThreadLocal.ThreadLocalMap类型的成员变量threadLocals，这个threadLocals就是用来存储实际的变量副本的，键值为当前ThreadLocal变量，value为变量副本（即T类型的变量）。
2. 在Thread里面，threadLocals为空，当通过ThreadLocal变量调用get()方法或者set()方法，就会对Thread类中的threadLocals进行初始化，并且以当前ThreadLocal变量为键值，以ThreadLocal要保存的副本变量为value，存到threadLocals。
3. 在当前线程里面，如果要使用副本变量，就可以通过get方法在threadLocals里面查找。

## ThreadLocal的应用场景

ThreadLocal 最常见的应用场景为用于解决数据库连接、Session 管理等问题。

示例 - 数据库连接

```java
private static ThreadLocal<Connection> connectionHolder
= new ThreadLocal<Connection>() {
public Connection initialValue() {
    return DriverManager.getConnection(DB_URL);
}
};
 
public static Connection getConnection() {
return connectionHolder.get();
}
```

示例 - Session 管理

```java
private static final ThreadLocal threadSession = new ThreadLocal();
 
public static Session getSession() throws InfrastructureException {
    Session s = (Session) threadSession.get();
    try {
        if (s == null) {
            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
    } catch (HibernateException ex) {
        throw new InfrastructureException(ex);
    }
    return s;
}
```
# Java 容器之 Stream

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

<!-- TOC depthFrom:2 depthTo:3 -->

<!-- /TOC -->

## Stream 简介

在 Java8 中，`Collection` 新增了两个流方法，分别是 `stream()` 和 `parallelStream()`。

`Stream` 相当于高级版的 `Iterator`，他可以通过 Lambda 表达式对集合进行各种非常便利、高效的聚合操作（Aggregate Operation），或者大批量数据操作 (Bulk Data Operation)。

## Stream 操作分类

官方将 Stream 中的操作分为两大类：中间操作（Intermediate operations）和终结操作（Terminal operations）。

中间操作又可以分为无状态（Stateless）与有状态（Stateful）操作，前者是指元素的处理不受之前元素的影响，后者是指该操作只有拿到所有元素之后才能继续下去。

终结操作又可以分为短路（Short-circuiting）与非短路（Unshort-circuiting）操作，前者是指遇到某些符合条件的元素就可以得到最终结果，后者是指必须处理完所有元素才能得到最终结果。

## Stream 源码实现

![](https://raw.githubusercontent.com/dunwu/images/dev/snap/20201205174140.jpg)

`BaseStream` 和 `Stream` 是最顶层的接口类。`BaseStream` 主要定义了流的基本接口方法，例如，spliterator、isParallel 等；`Stream` 则定义了一些流的常用操作方法，例如，map、filter 等。

`Sink` 接口是定义每个 `Stream` 操作之间关系的协议，他包含 `begin()`、`end()`、`cancellationRequested()`、`accpt()` 四个方法。`ReferencePipeline` 最终会将整个 `Stream` 流操作组装成一个调用链，而这条调用链上的各个 `Stream` 操作的上下关系就是通过 `Sink` 接口协议来定义实现的。

`ReferencePipeline` 是一个结构类，他通过定义内部类组装了各种操作流。他定义了 `Head`、`StatelessOp`、`StatefulOp` 三个内部类，实现了 `BaseStream` 与 `Stream` 的接口方法。Head 类主要用来定义数据源操作，在初次调用 names.stream() 方法时，会加载 Head 对象，此时为加载数据源操作；接着加载的是中间操作，分别为无状态中间操作 StatelessOp 对象和有状态操作 StatefulOp 对象，此时的 Stage 并没有执行，而是通过 AbstractPipeline 生成了一个中间操作 Stage 链表；当我们调用终结操作时，会生成一个最终的 Stage，通过这个 Stage 触发之前的中间操作，从最后一个 Stage 开始，递归产生一个 Sink 链。

## Stream 并行处理

Stream 处理数据的方式有两种，串行处理和并行处理。

## 4. 参考资料

- [Java 编程思想（第 4 版）](https://item.jd.com/10058164.html)

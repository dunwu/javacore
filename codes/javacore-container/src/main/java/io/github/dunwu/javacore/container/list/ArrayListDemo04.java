package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.List;

public class ArrayListDemo04 {

    public static void main(String[] args) {
        List<String> allList = null;
        allList = new ArrayList<String>();    // 指定操作的泛型为String
        allList.add("Hello");    // 此方法由Collection接口而来
        allList.add(0, "World");    // 在第一个位置上添加新的内容
        allList.add("MLDN");    // 向Collection中加入内容
        allList.add("www.mldn.cn");
        String[] str = allList.toArray(new String[] {});    // 指定好类型
        System.out.print("指定数组类型：");
        for (int i = 0; i < str.length; i++) {
            System.out.print(str[i] + "、");
        }
        System.out.print("\n返回对象数组：");
        Object[] obj = allList.toArray();    // 返回Object类型
        for (int i = 0; i < obj.length; i++) {
            String temp = (String) obj[i];    // 进行向下转型
            System.out.print(temp + "、");
        }
    }

}

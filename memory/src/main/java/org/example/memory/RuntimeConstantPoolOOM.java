package org.example.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xms10M -Xmx10m -XX:+HeapDumpOnOutOfMemoryError
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {

        /*List<String> list = new ArrayList<>();

        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }*/

        /*String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);*/

        String str1 = "hello";
        String str = new String("hello");
        String str2 = "hel" + "lo";
        System.out.println(str == str1);
        System.out.println(str == str2);
        System.out.println(str1 == str2);

    }
}

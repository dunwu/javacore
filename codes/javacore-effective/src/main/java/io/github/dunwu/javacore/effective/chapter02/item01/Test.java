// Simple test program for service provider framework
package io.github.dunwu.javacore.effective.chapter02.item01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    private static Provider DEFAULT_PROVIDER = new Provider() {
        @Override
        public Service newService() {
            return new Service() {
                @Override
                public String toString() {
                    return "Default service";
                }
            };
        }
    };

    private static Provider COMP_PROVIDER = new Provider() {
        @Override
        public Service newService() {
            return new Service() {
                @Override
                public String toString() {
                    return "Complementary service";
                }
            };
        }
    };

    private static Provider ARMED_PROVIDER = new Provider() {
        @Override
        public Service newService() {
            return new Service() {
                @Override
                public String toString() {
                    return "Armed service";
                }
            };
        }
    };

    public static void main(String[] args) {
        // Providers would execute these lines
        Services.registerDefaultProvider(DEFAULT_PROVIDER);
        Services.registerProvider("comp", COMP_PROVIDER);
        Services.registerProvider("armed", ARMED_PROVIDER);

        // Clients would execute these lines
        Service s1 = Services.newInstance();
        Service s2 = Services.newInstance("comp");
        Service s3 = Services.newInstance("armed");
        System.out.printf("%s, %s, %s%n", s1, s2, s3);

        List<String> a = new ArrayList<String>();
        a.add("1");
        a.add("2");
        a.add("3");

        List<String> b = new ArrayList<String>();
        b.add("10");
        b.add("20");
        b.add("30");

        Map<String, List<String>> m = new HashMap<String, List<String>>();
        m.put("a", a);
        m.put("b", b);

        for (Map.Entry entry : m.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }
    }

}

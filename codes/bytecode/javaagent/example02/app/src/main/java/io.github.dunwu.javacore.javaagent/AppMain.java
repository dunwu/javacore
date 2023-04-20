package io.github.dunwu.javacore.javaagent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class AppMain {

    public static void main(String[] args) {
        System.out.println("APP 启动！！！");
        for (VirtualMachineDescriptor vmd : VirtualMachine.list()) {
            // 指定的VM才可以被代理
            if (true) {
                System.out.println("该VM为指定代理的VM");
                System.out.println(vmd.displayName());
                try {
                    VirtualMachine vm = VirtualMachine.attach(vmd.id());
                    vm.loadAgent("D:/Codes/zp/ztutorial/zp-java/javacore/codes/javaagent/example02/agent/target/javacore-javaagent-agent2-1.0.1.jar=hello");
                    vm.detach();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        AppInit.init();
    }

}

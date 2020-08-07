package io.github.dunwu.javacore.jvm.memory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Linux Test Cli: nohup java -verbose:gc -Xms256M -Xmx512M
 * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
 * -XX:+PrintGCDateStamps -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError
 * -Dcom.sun.management.jmxremote=true
 * -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
 * -Djava.rmi.server.hostname=${ip} -Dcom.sun.management.jmxremote.port=18888
 * -Xdebug -Xnoagent -Djava.compiler=NONE
 * -Xrunjdwp:transport=dt_socket,address=28888,server=y,suspend=n
 * -classpath "target/javacore-jvm-1.0.1.jar:target/lib/*" io.github.dunwu.javacore.jvm.memory.DeadloopDemo
 * >> output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-07
 */
public class DeadloopDemo {

    public static void main(String[] args) throws InterruptedException {
        //启动10个线程
        IntStream.rangeClosed(1, 10).mapToObj(i -> new Thread(() -> {
            while (true) {
                //每一个线程都是一个死循环，休眠10秒，打印10M数据
                String payload = IntStream.rangeClosed(1, 10000000)
                    .mapToObj(__ -> "a")
                    .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(payload.length());
            }
        })).forEach(Thread::start);
        TimeUnit.HOURS.sleep(1);
    }

}

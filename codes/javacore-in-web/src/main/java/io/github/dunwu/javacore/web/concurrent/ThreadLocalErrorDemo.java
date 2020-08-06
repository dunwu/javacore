package io.github.dunwu.javacore.web.concurrent;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-31
 */
@RestController
@RequestMapping("threadlocal")
public class ThreadLocalErrorDemo {

    private ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    @GetMapping("wrong")
    public Map<String, String> wrong(@RequestParam("id") Integer userId) {
        //设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        //设置用户信息到ThreadLocal
        currentUser.set(userId);
        //设置用户信息之后再查询一次ThreadLocal中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        //汇总输出两次查询结果
        Map<String, String> result = new HashMap<>();
        result.put("before", before);
        result.put("after", after);
        return result;
    }

    @GetMapping("right")
    public Map<String, String> right(@RequestParam("id") Integer userId) {
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        currentUser.set(userId);
        try {
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            Map<String, String> result = new HashMap<>();
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            //在finally代码块中删除ThreadLocal中的数据，确保数据不串
            currentUser.remove();
        }
    }

}

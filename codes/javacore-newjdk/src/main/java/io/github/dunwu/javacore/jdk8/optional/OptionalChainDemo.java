package io.github.dunwu.javacore.jdk8.optional;

import java.util.Optional;

/**
 * Java 8 Optional 链式操作与最佳实践示例。
 * <p>
 * Optional 真正的威力在于函数式链式调用，彻底替代层层嵌套的判空：
 * <ul>
 * <li>{@code map}：值存在则转换，结果为 Optional&lt;U&gt;</li>
 * <li>{@code flatMap}：转换函数本身返回 Optional 时使用，避免 Optional 嵌套</li>
 * <li>{@code filter}：按条件保留值，不满足则变为 empty</li>
 * </ul>
 */
public class OptionalChainDemo {

    /**
     * 用户：姓名、地址（可能为 null）
     */
    static class User {

        private final String name;

        private final Address address;

        User(String name, Address address) {
            this.name = name;
            this.address = address;
        }

        String getName() {
            return name;
        }

        Address getAddress() {
            return address;
        }

    }

    static class Address {

        private final String city;

        Address(String city) {
            this.city = city;
        }

        String getCity() {
            return city;
        }

    }

    /**
     * 示例 1：map 链式调用替代层层判空，任何一环为 null 都会短路为 empty
     */
    public static void mapChain() {
        User user = new User("张三", new Address("杭州"));
        User noAddressUser = new User("李四", null);

        // 传统写法：层层判空
        // if (user != null && user.getAddress() != null) { city = user.getAddress().getCity(); }
        String city = Optional.ofNullable(user)
            .map(User::getAddress)
            .map(Address::getCity)
            .orElse("未知城市");
        System.out.println("map 链式取城市: " + city);
        System.out.println("地址为 null 时: " + Optional.ofNullable(noAddressUser)
            .map(User::getAddress)
            .map(Address::getCity)
            .orElse("未知城市"));
    }

    /**
     * 示例 2：flatMap，转换函数本身返回 Optional 时使用，避免 Optional 嵌套
     */
    public static void flatMapChain() {
        User user = new User("张三", new Address("杭州"));
        Optional<String> cityByFlatMap = Optional.ofNullable(user)
            .flatMap(u -> Optional.ofNullable(u.getAddress()))
            .flatMap(addr -> Optional.ofNullable(addr.getCity()));
        System.out.println("flatMap: " + cityByFlatMap.orElse("未知城市"));
    }

    /**
     * 示例 3：filter，按条件保留值，不满足则变为 empty
     */
    public static void filterDemo() {
        Optional<Integer> age = Optional.of(25);
        System.out.println("filter >= 18: " + age.filter(a -> a >= 18).orElse(-1));
        System.out.println("filter > 30: " + age.filter(a -> a > 30).orElse(-1));
    }

    /**
     * 示例 4：最佳实践，Optional 只做方法返回值，表达"可能无结果"
     */
    public static void returnValueBestPractice() {
        System.out.println("findUser 存在: " + findUser("张三").map(User::getName).orElse("-"));
        System.out.println("findUser 不存在: " + findUser("王五").map(User::getName).orElse("-"));
    }
    
    public static void main(String[] args) {
        mapChain();
        flatMapChain();
        filterDemo();
        returnValueBestPractice();
    }

    private static Optional<User> findUser(String name) {
        if ("张三".equals(name)) {
            return Optional.of(new User(name, null));
        }
        return Optional.empty();
    }

}
// Output:
// map 链式取城市: 杭州
// 地址为 null 时: 未知城市
// flatMap: 杭州
// filter >= 18: 25
// filter > 30: -1
// findUser 存在: 张三
// findUser 不存在: -

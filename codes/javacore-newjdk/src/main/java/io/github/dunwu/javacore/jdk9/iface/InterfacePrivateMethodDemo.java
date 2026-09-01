package io.github.dunwu.javacore.jdk9.iface;

/**
 * Java 9 接口私有方法示例。
 * <p>
 * Java 8 引入了接口的默认方法（default）和静态方法（static），但当多个默认方法存在重复逻辑时，
 * 无法在接口内部抽取公共方法（只能是 public）。Java 9 允许接口定义：
 * <ul>
 * <li>私有实例方法：供默认方法复用公共逻辑</li>
 * <li>私有静态方法：供静态方法和默认方法复用公共逻辑</li>
 * </ul>
 */
public class InterfacePrivateMethodDemo {

    /**
     * 示例 1：私有实例方法——默认方法 logInfo / logError 复用私有 log 方法
     */
    public static void privateInstanceMethodDemo() {
        Logger consoleLogger = new ConsoleLogger();
        consoleLogger.logInfo("服务启动成功");
        consoleLogger.logError("连接超时");
    }

    /**
     * 示例 2：私有静态方法——静态方法 printHeader 复用私有静态 decorate 方法
     */
    public static void privateStaticMethodDemo() {
        Logger.printHeader();
    }

    public static void main(String[] args) {
        privateInstanceMethodDemo();
        privateStaticMethodDemo();
    }

    interface Logger {

        String PREFIX = "[LOG]";

        /**
         * 默认方法：打印 info 日志
         */
        default void logInfo(String message) {
            log("INFO", message);
        }

        /**
         * 默认方法：打印 error 日志
         */
        default void logError(String message) {
            log("ERROR", message);
        }

        /**
         * 私有实例方法：抽取默认方法中的公共逻辑，接口外部不可见、不可调用
         */
        private void log(String level, String message) {
            System.out.println(PREFIX + " [" + level + "] " + decorate(message));
        }

        /**
         * 静态方法
         */
        static void printHeader() {
            System.out.println(decorate("日志系统已初始化"));
        }

        /**
         * 私有静态方法：供静态方法和实例方法共同复用
         */
        private static String decorate(String message) {
            return ">>> " + message + " <<<";
        }

    }

    static class ConsoleLogger implements Logger {

    }

}
// Output:
// [LOG] [INFO] >>> 服务启动成功 <<<
// [LOG] [ERROR] >>> 连接超时 <<<
// >>> 日志系统已初始化 <<<

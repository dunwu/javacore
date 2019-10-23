package io.github.dunwu.javacore.control;

/**
 * 中断语句示例
 *
 * @author Zhang Peng
 * @see BreakDemo
 * @see ContinueDemo
 * @see ReturnDemo
 */
public class BreakDemo {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			if (i == 3) {
				break;
			}
			System.out.println(i);
		}
		System.out.println("示例结束");
	}

};
// output:
// 0
// 1
// 2
// 示例结束

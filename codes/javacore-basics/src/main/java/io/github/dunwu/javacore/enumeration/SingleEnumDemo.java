package io.github.dunwu.javacore.enumeration;

/**
 * 利用枚举实现单例
 *
 * @author Zhang Peng
 * @since 2019-03-19
 */
public class SingleEnumDemo {

	public static void main(String[] args) {
		SingleEn.INSTANCE.setName("zp");
		System.out.println(SingleEn.INSTANCE.getName());
	}

	public enum SingleEn {

		INSTANCE;

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}

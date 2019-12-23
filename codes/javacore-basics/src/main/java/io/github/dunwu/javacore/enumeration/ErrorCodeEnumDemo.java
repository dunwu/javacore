package io.github.dunwu.javacore.enumeration;

/**
 * @author Zhang Peng
 * @since 2019-03-19
 */
public class ErrorCodeEnumDemo {

	public static void main(String[] args) {
		System.out.println(ErrorCodeEn.toStringAll());
		for (ErrorCodeEn s : ErrorCodeEn.values()) {
			System.out.println(s);
		}
	}

	enum ErrorCodeEn {

		OK(0, "成功"), ERROR_A(100, "错误A"), ERROR_B(200, "错误B");

		private int code;

		private String msg;

		ErrorCodeEn(int number, String msg) {
			this.code = number;
			this.msg = msg;
		}

		public static String toStringAll() {
			StringBuilder sb = new StringBuilder();
			sb.append("ErrorCodeEn All Elements: [");
			for (ErrorCodeEn code : ErrorCodeEn.values()) {
				sb.append(code.getCode()).append(", ");
			}
			sb.append("]");
			return sb.toString();
		}

		public int getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}

		@Override
		public String toString() {
			return "ErrorCodeEn{" + "code=" + code + ", msg='" + msg + '\'' + '}';
		}
	}

}
// Output:
// ErrorCodeEn All Elements: [0, 100, 200, ]
// ErrorCodeEn{code=0, msg='成功'}
// ErrorCodeEn{code=100, msg='错误A'}
// ErrorCodeEn{code=200, msg='错误B'}

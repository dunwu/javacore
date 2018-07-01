package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class HelloClient{
	public static void main(String[] args) throws Exception {	// 所有异常抛出
		Socket client = null ;	// 表示客 户端
		client = new Socket("localhost",8888) ;
		BufferedReader buf = null ;	// 一次性接收完成
		buf = new BufferedReader(new InputStreamReader(client.getInputStream())) ;
		String str = buf.readLine() ;
		System.out.println("服务器端输出内容：" + str) ;
		buf.close() ;
		client.close() ;
	}
};

package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer{
	public static void main(String[] args) throws Exception {	// 所有异常抛出
		ServerSocket server = null ;		// 定义ServerSocket类
		Socket client = null ;	// 表示客 户端
		BufferedReader buf = null ;	// 接收输入流
		PrintStream out = null ;		// 打印流输出最方便
		server = new ServerSocket(8888) ;	// 服务器在8888端口上监听
		boolean f = true ;	// 定义个标记位
		while(f){
			System.out.println("服务器运行，等待客户端连接。") ;
			client = server.accept() ;		// 得到连接，程序进入到阻塞状态
			out = new PrintStream(client.getOutputStream()) ;
			// 准备接收客户端的输入信息
			buf = new BufferedReader(new InputStreamReader(client.getInputStream())) ;
			boolean flag = true ;	// 标志位，表示可以一直接收并回应信息
			while(flag){
				String str = buf.readLine() ;		// 接收客户端发送的内容
				if(str==null||"".equals(str)){	// 表示没有内容
					flag = false ;	// 退出循环
				}else{
					if("bye".equals(str)){	// 如果输入的内容为bye表示结束
						flag = false ;
					}else{
						out.println("ECHO : " + str) ;	// 回应信息
					}
				}
			}
			client.close() ;
		}
		server.close() ;
	}
};

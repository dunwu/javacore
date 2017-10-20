package io.github.dunwu.javase.operator;

public class TypeChangeDemo{
	public static void main(String args[]){
		char ch = 'a' ;
		short a = -2 ;
		int b = 3 ;
		float f = 5.3f ;
		double d = 6.28 ;
		System.out.print("(ch / a) - (d / f) - (a + b) = ") ;
		System.out.println((ch / a) - (d / f) - (a + b)) ;
	}
};

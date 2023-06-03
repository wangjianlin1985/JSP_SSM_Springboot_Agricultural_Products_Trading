package com.chengxusheji.controller;

import org.mindrot.jbcrypt.BCrypt;

public class TestJbcrypt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String password = "123";//$2a$10$Q3p5vhW0vZO3ihn8bFJThOnb6hiTaT32f8b/9RYP6MhH2HdSDizEy
		String pwt = BCrypt.hashpw(password, BCrypt.gensalt());  //加密
		//注：每次加密后的值都是不一样的
		boolean pswFlag = BCrypt.checkpw(password,"$2a$10$Q3p5vhW0vZO3ihn8bFJThOnb6hiTaT32f8b/9RYP6MhH2HdSDizEy");//解密
		System.out.println(pwt+"===="+pswFlag);
	}

}

package com.sit.pibics.oauth;

import util.tranp.TranpUtil;

public class TestPasswordEncrypt {

	public static void main(String[] args) {
		try {
			String d = TranpUtil.doBefore("Fl,k4k");
			System.out.println("password : " + d);
			System.out.println("---------------------");
			
			String db = TranpUtil.doBefore("Evoa2018");
			System.out.println("database : " + db);
			System.out.println("---------------------");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

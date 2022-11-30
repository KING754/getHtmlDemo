package com.wejias;

import com.wejias.getHtml.GetIndexHtml;
import com.wejias.getHtml.Save2Execl;

/**
 * Copyright © www.wejias.com/微信公众号:迦识
 * @author 戈寒
 *
 */
public class MainClass {
	public static void main(String[] args) throws InterruptedException {
		String a1 = "./chromedriver.exe";
		String a2 = "./execl/";
		
//		String a1 = "i:/chromedriver.exe";
//		String a2 = "i:/execl/";
		
		GetIndexHtml.getData(a1);
		Save2Execl.saveExecl(a2);
		
		System.out.println("已成功保存,3秒后,程序自动退出!!!");
		for (int i = 3; i >= 0; i--) {
			System.out.println(i);
			Thread.sleep(1000);
		}
		System.out.println("程序退出!!!");
		System.exit(0);
	}
}

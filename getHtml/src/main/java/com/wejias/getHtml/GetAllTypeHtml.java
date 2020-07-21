package com.wejias.getHtml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class GetAllTypeHtml implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent(
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
	public static Set<Cookie> cookies;
	
	public static List<String> needGetCat = new ArrayList<String>();
	private static String prefix = "<a onclick=\"javascript:kmjump('26097','','";
	private static String endFix = ",'');\" href=\"javascript:void(0)\">";

	public Site getSite() {
		if(cookies != null && !cookies.isEmpty()) {
			for (Cookie cookie : cookies) {
				site.addCookie(cookie.getName().toString(), cookie.getValue().toString());
			}
		}
		
		return site;
	}

	public void process(Page page) {
		Html html = page.getHtml();
		Selectable allTypeList = html.xpath("//a[contains(@onclick, 'javascript:kmjump')]");
		for (Selectable typeNode : allTypeList.nodes()) {
			String aStr = typeNode.get();
			if(aStr.startsWith(prefix)) {
				int endIndex = aStr.indexOf(endFix);
				needGetCat.add(aStr.substring(prefix.length(), endIndex-1));
			}
		}
	}

	public static void getCookie(String path) {
		System.setProperty("webdriver.chrome.driver", path); // 注册驱动
		WebDriver driver = new ChromeDriver();

		driver.get("url?currentPage=1");// 打开网址
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 获取cookie信息
		cookies = driver.manage().getCookies();
		driver.close();
	}
	
	public static void getData(String chromedriverPath) {
		GetAllTypeHtml.getCookie(chromedriverPath);
		Spider.create(new GetAllTypeHtml()).addUrl("url?currentPage=1").thread(1)
		.run();
		
		if(needGetCat == null || needGetCat.isEmpty()) {
			System.out.println("获取分类信息有错误1!!!");
			System.exit(0);
		}
		List<String> needDelSecondGetCat = new ArrayList<String>();
		
		for (String type : needGetCat) {
			if(isSecondType(type)) {
				needDelSecondGetCat.add(type);
			}
		}
		
		for (Iterator<String> iterator = needDelSecondGetCat.iterator(); iterator.hasNext();) {
			String secondTypeName = (String) iterator.next();
			if(!isHaveThirdType(secondTypeName)) {
				iterator.remove();
			}	
		}
		
		for (String delSecondName : needDelSecondGetCat) {
			for (Iterator<String> iterator = needGetCat.iterator(); iterator.hasNext();) {
				String typeName = (String) iterator.next();
				if(typeName.equals(delSecondName)) {
					iterator.remove();
				}
			}
		}
		
		System.out.println("------------将要拉取的分类总数: "+needGetCat.size()+"-------------具体见下----------");
		for (String name : needGetCat) {
			System.out.println(name);
		}
	}
	
	public static boolean isSecondType(String typeName) {
		String[] arr = mySplit(typeName, ".");
		return arr.length == 2;
	}
	
	public static boolean isHaveThirdType(String secondTypeName) {
		int secondTypeNameLen = secondTypeName.length();
		for (String type : needGetCat) {
			if(type.startsWith(secondTypeName) && type.length() > secondTypeNameLen) {
				return true;
			}
		}
		return false;
	}
	
	public static final String ESCAPE = "\\";
	public static String[] mySplit(String str,String split){
		if(str == null || "".equals(str)){
			return null;
		}
		
		if(str.indexOf(split) > -1){
			try{
				return str.split(ESCAPE+split);
			}catch(Exception e){
				return null;
			}
		}else{
			return new String[]{str};
		}
	}
}

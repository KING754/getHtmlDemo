package com.wejias.getHtml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Cookie;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;


/**
 * Copyright © www.wejias.com/微信公众号:迦识
 * @author 戈寒
 *
 */
public class GetIndexHtml implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent(
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");

	public Site getSite() {
		// 将获取到的cookie信息添加到webmagic中
		if(GetAllTypeHtml.cookies != null && !GetAllTypeHtml.cookies.isEmpty()) {
			for (Cookie cookie : GetAllTypeHtml.cookies) {
				site.addCookie(cookie.getName().toString(), cookie.getValue().toString());
			}
		}
		

		return site;
	}

	public static Map<String,List<String>> goodsNameMap = new LinkedHashMap<String,List<String>>();
	public static Map<String,List<String>> priceMap = new LinkedHashMap<String,List<String>>();
	
	public static boolean isLastPage = false;
	public static String maxPage = "1";
	public void process(Page page) {
		Selectable url = page.getUrl();
		String type = getPageType(url.get());
		if(type == null || "".equals(type)) {
			System.out.println("页址有错!!! URL=["+url.get()+"]");
			return;
		}
		
		Html html = page.getHtml();
		Selectable goodsNameList = html.xpath("//a[contains(@href, 'goodsdetails.action?sptid=')]/h5/text()");
		if(goodsNameList.nodes() == null || goodsNameList.nodes().isEmpty()) {
			System.out.println("当前没有数据!!! URL=["+url.get()+"]");
			System.out.println(html);
			isLastPage = true;
			return;
		}
		List<Selectable> nodes = goodsNameList.nodes();
		List<String> goodsNames = goodsNameMap.get(type);
		if(goodsNames == null) {
			goodsNames = new ArrayList<String>();
		}
		
		for (Selectable node : nodes) {
			goodsNames.add(node.get());
			System.out.println(node.get());
		}
		
		goodsNameMap.put(type, goodsNames);

		Selectable priceList = html.xpath("//p[@class='pull-left']/text()");
		List<Selectable> priceNodes = priceList.nodes();
		List<String> prices = priceMap.get(type);
		if(prices == null) {
			prices = new ArrayList<String>();
		}
		
		for (Selectable priceNode : priceNodes) {
			String spanValue = priceNode.get();
			if (spanValue.indexOf("￥") >= 0) {
				prices.add(priceNode.get());
				System.out.println(priceNode.get());
			}
		}
		priceMap.put(type, prices);
		String pageNo = getPageNo(url.get());
		if(maxPage.equals("1")) {
			maxPage = getMaxPage(html);
		}
		if(pageNo.equals(maxPage)) {
			isLastPage = true;
		}
	}
	
	public static String getPageType(String url) {
		String para = "&km=";
		int startIndex = url.indexOf(para)+para.length();
		int endIndex = url.indexOf("&", startIndex);
		String type = url.substring(startIndex,endIndex);
		return type;
	}
	
	public static String getPageNo(String url) {
		String para = "&currentPage=";
		int startIndex = url.indexOf(para)+para.length();
		String type = url.substring(startIndex);
		return type;
	}
	
	public static String getMaxPage(Html html) {
		Selectable goodsNameList = html.xpath("//a[contains(@onclick, 'goPage')]");
		String maxPage = "1";
		for (Selectable node : goodsNameList.nodes()) {
			String str = node.get();
			String para = "goPage(";
			int startIndex = str.indexOf(para)+para.length();
			int endIndex = str.indexOf(")", startIndex);
			maxPage = str.substring(startIndex,endIndex);
		}
		return maxPage;
	}

	
	public static void getData(String chromedriverPath) throws InterruptedException {
		GetAllTypeHtml.getData(chromedriverPath);
		if(GetAllTypeHtml.needGetCat == null || GetAllTypeHtml.needGetCat.isEmpty()) {
			System.out.println("获取分类信息有错误2!!!");
			System.exit(0);
		}
		
		System.out.println("开始访问页面。。。。");
		for (String typeName : GetAllTypeHtml.needGetCat) {
			System.out.println("-----------"+typeName+"----------------");
			for (int i = 1; i <= 100; i++) {
				Spider.create(new GetIndexHtml()).addUrl("url?gsdm=26097&pp=&km="+typeName+"&network=&tykhgsdm=&currentPage=" + i).thread(1).run();
				if(isLastPage) {
					break;
				}
			}
			isLastPage = false;
			maxPage = "1";
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		GetIndexHtml.getData("I:/chromedriver.exe");
	}
}

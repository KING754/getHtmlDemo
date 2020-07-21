package com.wejias.getHtml;

public class DataPojo {
	private String firstName;
	private String secondName;
	private String thirdName;
	private String goodsName;
	private String price;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}

	public DataPojo(String goodsName, String price,String typeName) {
		this.goodsName = goodsName;
		this.price = price;
		
		String[] nameArr = GetAllTypeHtml.mySplit(typeName, ".");
		int len = nameArr.length;
		switch (len) {
			case 1:
				firstName = nameArr[0];
				break;
			case 2:
				firstName = nameArr[0];
				secondName = nameArr[1];
				break;
			case 3:
				firstName = nameArr[0];
				secondName = nameArr[1];
				thirdName = nameArr[2];
				break;
		}
	}

	public DataPojo() {
	}
	
	public static void main(String[] args) {
//		int x = 2;
//		switch (x) {
//			case 1:Sys
//			case 2:System.out.println(2);
//			case 3:System.out.println(3);
//		}
	}

}

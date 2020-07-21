package com.wejias.getHtml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Copyright © www.wejias.com/微信公众号:程序员伪架师
 * @author 师哥
 *
 */
public class Save2Execl {
	public static void saveExecl(String saveDir) {
		if(GetIndexHtml.goodsNameMap.isEmpty()) {
			System.out.println("商品名称类别数量为0");
		}
		if(GetIndexHtml.priceMap.isEmpty()) {
			System.out.println("商品价格类别数量为0");
		}
		
		if(GetIndexHtml.goodsNameMap.size() != GetIndexHtml.priceMap.size()) {
			System.out.println("商品数量与价格数量不一致");
			System.out.println("商品类别数量: "+GetIndexHtml.goodsNameMap.size());
			System.out.println("价格类别数量: "+GetIndexHtml.priceMap.size());
		}
		System.out.println("一共有显示了:"+GetIndexHtml.priceMap.size()+" 类件商品");
		Save2Execl.writeData2Execl(saveDir);
	}
	
	private static List<DataPojo> map2List() {
		List<DataPojo> dataList = new ArrayList<DataPojo>();
		for (String keyStr : GetIndexHtml.goodsNameMap.keySet()) {
			List<String> nameList = GetIndexHtml.goodsNameMap.get(keyStr);
			List<String> priceList = GetIndexHtml.priceMap.get(keyStr);
			
			for (int i = 0; i < nameList.size(); i++) {
				String name = nameList.get(i);
				String price = priceList.get(i);
				
				DataPojo data = new DataPojo(name, price,keyStr);
				dataList.add(data);
			}
		}
		return dataList;
	}
	
	private static void writeData2Execl(String saveDir) {
		List<DataPojo> dataList =map2List();
		System.out.println("一共有 "+dataList.size()+"  件商品");
		
		
		File file = new File(saveDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		 // 写入数据到工作簿对象内
        Workbook workbook = ExcelWriter.exportData(dataList);

        // 以文件的形式输出工作簿对象
        FileOutputStream fileOut = null;
        try {
            String exportFilePath = saveDir+"/"+Save2Execl.getDateStr()+".xlsx";
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            System.out.println("已存好文件："+exportFilePath);
        } catch (Exception e) {
            System.out.println("输出Excel时发生错误，错误原因：" + e.getMessage());
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
            	 System.out.println("关闭输出流时发生错误，错误原因：" + e.getMessage());
            }
        }
	}
	
	public static String getDateStr() {
		Date date = new Date();
		return String.format("%tF", date);
	}
	
	public static void main(String[] args) {
		Save2Execl.getDateStr();
	}
}

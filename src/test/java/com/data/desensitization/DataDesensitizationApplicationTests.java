package com.data.desensitization;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.data.desensitization.controller.DbController;
import com.data.desensitization.service.Desensitization;
import com.data.desensitization.service.Utils;
import com.data.desensitization.service.datastructure.BPlusTree;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataDesensitizationApplicationTests {

	public static Utils utils = new Utils();
	public static Desensitization desensitize = new Desensitization();
	public DbController dbs;
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void TestCalculateNums() {
		String sql = "explain select count(*) from privateinfo";
		System.out.println(db.getTotalNums(sql));
	}
	
	@Test
	public void TestidCardMapReplace() {
		//String[] codes = new String[4000];
		// initialize the address code
		//utils.initAddressCode(codes);
		String str = desensitize.idCardMapReplace("510311199603191739");
		System.out.println(str);
		/*try {
		utils.UCTToCST("19960319");
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		// create a key S to encode the true address code
		//String newAddrCode = codes[encodeAddrCode(idCard.substring(0, 5))];
	}
	
	@Test
	public void TestAddressMask() {
		System.out.println(desensitize.addressMask("北京市海淀区科学院南路6号中科院计算所"));
	}
	
	@Test
	public void TestAddressMapReplace() {
		String str = desensitize.addressMapReplace("北京市海淀区科学院南路6号中科院计算所", 1);
		System.out.println(str);
	}
	
	@Test
	public void TestNameMapReplace() {
		System.out.println(desensitize.nameMapReplace("卢杰杰"));
	}
	
	@Test
	public void TestgetLngAndLat() {
		System.out.println(utils.parseAddress("北京市朝阳区"));
		//System.out.println(utils.parseAddress("内蒙古自治区兴安盟科尔沁右翼前旗"));
		System.out.println(utils.parseAddress("北京市海淀区科学院南路6号中科院计算所"));
	}
	
	@Test
	public void TestOrderInsertAndSearch() {
		int size = 101;
		int order = 4;
		String[] nameDB = new String[101];
		String[] names = new String[101];
		Double[] proportion = new Double[101]; 
		utils.getNameDatabase(nameDB,"surname");
		DecimalFormat df = new DecimalFormat("0.000000");
		for (int i = 0; i < nameDB.length; i++) {
			if (nameDB[i] != null) {
				names[i] = nameDB[i].split(" ")[0];
				proportion[i] = Double.valueOf(df.format(Double.valueOf(nameDB[i].split(" ")[1])));
			}
		}
		
		BPlusTree<Double, String> tree = new BPlusTree<Double, String>(order);
		System.out.println("\nTest order search " + size + " datas, of order:" + order);
		
		System.out.println("Begin order insert...");
		for (int i = 0; i < size; i++) {
			if (proportion[i] != null && names[i] != null) {
				tree.insert(proportion[i], names[i]);
			}
		}
		System.out.println("Begin order search...");
		long current = System.currentTimeMillis();
		for (int j = 0; j < size; j++) {
			double randomNumber = Math.random();
			randomNumber = Double.valueOf(df.format(Double.valueOf(randomNumber)));
			if (tree.get(randomNumber) == null) {
				System.err.println("得不到数据:" + j);
				break;
			}else {
				System.out.println(randomNumber + "," + tree.get(randomNumber));
				//tree.get(randomNumber);
			}
		}
		long duration = System.currentTimeMillis() - current;
		System.out.println("time elpsed for duration: " + duration);
	}
	private DbController db = new DbController();
	@Test
	public void testDbConnector() {
		System.out.println(db.getData("privateinfo"));
	}
}

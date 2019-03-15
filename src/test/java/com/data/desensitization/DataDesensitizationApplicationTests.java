package com.data.desensitization;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.data.desensitization.service.Desensitization;
import com.data.desensitization.service.Utils;
import com.data.desensitization.service.datastructure.BPlusTree;
import com.data.desensitization.service.encryption.RSAGeneratorKey;
import com.data.desensitization.service.encryption.RSAUtil;
import com.data.desensitization.service.encryption.RsaKeyPair;
import com.data.desensitization.service.encryption.SymmetricEncoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataDesensitizationApplicationTests {

	public static Utils utils = new Utils();
	public static Desensitization desensitize = new Desensitization();
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testGeneratorKey(){
        RsaKeyPair keyPair = RSAGeneratorKey.generatorKey(6);
        System.out.println("n的值是:"+keyPair.getPublicKey().getN());
        System.out.println("公钥b:"+keyPair.getPublicKey().getB());
        System.out.println("私钥a:"+keyPair.getPrivateKey().getA());
    }
	
	@Test
    public void testRSA() throws UnsupportedEncodingException{
        //生成KeyPair
        RsaKeyPair keyPair = RSAGeneratorKey.generatorKey(1024);
        // 元数据
        String source = new String("510311");

        System.out.println("加密前:"+source);
        //使用公钥加密 
        String cryptdata = RSAUtil.encrypt(source, keyPair.getPublicKey(),"UTF-8");
        System.out.println("加密后:"+cryptdata);
        //使用私钥解密
        try {
            String result = RSAUtil.decrypt(cryptdata, keyPair.getPrivateKey(),"UTF-8");
            System.out.println("解密后:"+result);
            System.out.println(result.equals(source));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }   
    }
	
	@Test
	public void testAES() {
		SymmetricEncoder se=new SymmetricEncoder();
        Scanner scanner=new Scanner(System.in);
        /*
         * 加密
         */
        String encodeRules="AES";
        String content = "510311";
        System.out.println("根据输入的规则"+encodeRules+"加密后的密文是:"+se.AESEncode(encodeRules, content));
       
        /*
         * 解密
         */
        /*System.out.println("使用AES对称解密，请输入加密的规则：(须与加密相同)");
         encodeRules="AES";
        System.out.println("请输入要解密的内容（密文）:");
         content = scanner.next();
        System.out.println("根据输入的规则"+encodeRules+"解密后的明文是:"+se.AESDncode(encodeRules, content));*/
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
	public void TestAddressMapReplace() {
		String str = desensitize.addressMapReplace("北京市海淀区科学院南路6号中科院计算所", 1);
		System.out.println(str);
	}
	
	@Test
	public void TestNameMapReplace() {
		System.out.println(desensitize.nameMapReplace("卢杰杰"));
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

	 
}

package edu.cc.gatech.cs6675.project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TermFrequency {
	
	//--------------------------Word Frequency----------------------------
	static HashMap<String, Integer> noise = new HashMap<String, Integer>();
	public static void initializeNoise() {
//		noise.put("嘻嘻", 1);
//		noise.put("爱你", 1);
//		noise.put("自己", 1);
//		noise.put("哈哈", 1);
//		noise.put("分享", 1);
		noise.put("在这里", 1);
//		noise.put("今天", 1);
//		noise.put("就是", 1);
//		noise.put("的人", 1);
//		noise.put("可以", 1);
//		noise.put("图片", 1);
//		noise.put("我们", 1);
//		noise.put("真的", 1);
//		noise.put("什么", 1);
//		noise.put("还是", 1);
//		noise.put("没有", 1);
//		noise.put("抓狂", 1);
//		noise.put("一个", 1);
		noise.put("不知道", 1);
		noise.put("是不是", 1);
		noise.put("一个人", 1);
		noise.put("越来越", 1);
		noise.put("第一次", 1);
		noise.put("让我们", 1);
		noise.put("亲爱的", 1);
		noise.put("为什么", 1);
		noise.put("哈哈哈", 1);
		noise.put("haha", 1);
		noise.put("哈哈哈哈", 1);
		noise.put("真的很", 1);
		noise.put("怎么办", 1);
	}
	
	public static ArrayList<String> getWord (StringBuffer input) {
		initializeNoise();
		ArrayList<String> result = new ArrayList<String>();
		
		Analyzer anal=new IKAnalyzer(true);		
		StringReader reader=new StringReader(input.toString());
		//分词
		TokenStream ts=anal.tokenStream("", reader);
		CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
		//遍历分词数据
		try {
			while(ts.incrementToken()){
				if(term.toString().length() > 2 && !noise.containsKey(term.toString())){
					result.add(term.toString());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		
		return result;
	}
	
	//------------------------------Read Date and Do Cleaning--------------------------
	public static StringBuffer readContent(String rawString) {
		StringBuffer content = new StringBuffer("");
		String[] lines = rawString.split("\n");
		for(String line: lines) {
			String c = getContent(line);
			content.append(c);
		}
 
		return content;

	}
	
//	public static StringBuffer readContent(String filename) {
//		StringBuffer content = new StringBuffer("");
//        FileInputStream fis;
//		try {
//			fis = new FileInputStream(filename);
//	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8"); 
//	        BufferedReader br = new BufferedReader(isr); 
//	        String line = null; 
//	        while ((line = br.readLine()) != null) { 
//	        	String c = getContent(line);
//	            content.append(c); 
//	            //content.append("\r\n"); // 补上换行符 
//	        }
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
//		return content;
//
//	}
	
	public static String getContent(String line) {
		String[] temp = line.split("\"");
		String[] noAdd = temp[3].split("http:");
		String result = noAdd[0];
		for(int i = 1; i < noAdd.length - 1; i ++) {
			int index = noAdd[i].indexOf(' ');
			String withoutAdd = noAdd[i].substring(index);
			result += withoutAdd;
		}
		result = result.replace(" ", "");
		return result;
	}
	
	public static String[] doTermFrequencyCount(String filename) {
		
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		
		StringBuffer result = readContent(filename);
		ArrayList<String> words = getWord(result);
		for(String word: words) {
			if(wordMap.containsKey(word)) {
				int count = wordMap.get(word);
				wordMap.put(word, count + 1);
			}else{
				wordMap.put(word, 1);
			}			
		}
		
		String[] top10 = new String[10];
		int[] maxCounts = new int[10];
		int count = 0;
		int min = -1;
		int minIndex = -1;
		
		Iterator iter = wordMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			int val = (Integer)entry.getValue();
			
			if(count < 10) {
				top10[count] = key;
				maxCounts[count] = val;
				count ++;
				if(min == -1) {
					min = val;
					minIndex = count - 1;
				} else if(val < min) {
					min = val;
					minIndex = count - 1;
				}
			}else {
				if(val > min) {
					top10[minIndex] = key;
					maxCounts[minIndex] = val;
					min = maxCounts[0];
					minIndex = 0;
					for(int i = 0; i < 10; i ++) {
						int c = maxCounts[i];
						if(c < min) {
							min = c;
							minIndex = i;
						}
					}
				}
			}
		}
		
		for(int i = 0; i < 9; i++) {
			for(int j = i+1; j < 10; j++) {
				if(maxCounts[i] < maxCounts[j]) {
					int temp = maxCounts[i];
					maxCounts[i] = maxCounts[j];
					maxCounts[j] = temp;
					
					String tempString = top10[i];
					top10[i] = top10[j];
					top10[j] = tempString;
				}
			}
		}
		
		int top = maxCounts[0];
		if(top == 0) {
			return top10;
		}
		int color = -1;
		int division = -1;
		if(top > 10 ) {
			color = 0;
		}else {
			color = 1;
		}
		
		int i = 0;
		for(int j = 0; j < 10; j++){
			if(top10[j] != null){
				i ++;
				if(color == 0) {
					if(maxCounts[j] > (top / (3-color) * 2)) {
						top10[j] = "<div style='color:red'>" + i + ". " + top10[j] + "  " + maxCounts[j] + "</div>";
					}else if(maxCounts[i] > (top / (3-color))) {
						top10[j] = "<div style='color:orange'>" + i + ". " + top10[j] + "  " + maxCounts[j] + "</div>";
					}else {
						top10[j] = "<div style='color:yellow'>" + i + ". " + top10[j] + "  " + maxCounts[j] + "</div>";
					}
				}else if(color == 1) {
					if(maxCounts[j] > (top / (3-color))) {
						top10[j] = "<div style='color:orange'>" + i + ". " + top10[j] + "  " + maxCounts[j] + "</div>";
					}else {
						top10[j] = "<div style='color:yellow'>" + i + ". " + top10[j] + "  " + maxCounts[j] + "</div>";
					}
				}
				
			}
		}
		
		return top10;
		
//		String[] results = new String[10];
//		for(int i = 0; i < 10; i ++) {
//			results[i] = top10[i] + "|" + maxCounts[i];
//		}
//		
//		
//		return results;
	}
	
	public static void main(String[] args) {
//		String[] top1 = TermFrequency.doTermFrequencyCount("C:\\Users\\mqy\\Desktop\\test.txt");
		String[] top1 = TermFrequency.doTermFrequencyCount(args[0]);
//		String[] top1 = TermFrequency.doTermFrequencyCount("{\"text\" : \"#同性婚姻法#   彩虹旗飘扬 http://t.cn/8sfN56Z 我在:http://t.cn/8sIMzA5\"}\n{\"text\" : \"#同性婚姻法#   彩虹旗飘扬http://t.cn/8sfN56Z 我在:http://t.cn/8sIMzA5\"}\n{\"text\" : \"#同性婚姻法#   彩虹旗飘扬 http://t.cn/8sfN56Z 我在:http://t.cn/8sIMzA5\"}");
		String result = "Top 10 Hot Labels\n";
		for(String s:top1) {
			if(s != null) {
			result = result + s + "\n";
			}
		}
		

		System.out.print(result);
	}
}

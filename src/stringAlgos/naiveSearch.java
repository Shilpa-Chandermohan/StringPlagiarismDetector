package stringAlgos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class naiveSearch {
	private String string1;
	private String string2;
	private int timeCount;
	public naiveSearch(String s1, String s2){
		if(s1.length() > s2.length()){
			string1 = s2;
			string2 = s1;			
		} else {
			string1 = s1;
			string2 = s2;
		}
		timeCount = 0;
	}

	public naiveSearch() {
		// TODO Auto-generated constructor stub
	}

	private int naiveSrc() {
		int str1_len = string1.length();
		int str2_len = string2.length();
		int count=0,loopVar_i,loopVar_j;
		int diff = str2_len - str1_len;

		timeCount = 0;
		for (loopVar_i = 0; loopVar_i <= diff; loopVar_i++){
			for (loopVar_j = 0; loopVar_j < str1_len; loopVar_j++){
				timeCount++;
				if (string2.charAt(loopVar_i + loopVar_j) != string1.charAt(loopVar_j)){
					count = (count > loopVar_j ? count : loopVar_j);
					break;
				}
			}
			if (loopVar_j == str1_len){
				System.out.println("Substring found in genuine string at : " + (loopVar_i+1));
				count = str1_len;
				loopVar_i = loopVar_i + count;
			}
		}
		if (count != str1_len){
			System.out.println("Partial Substring match length : " + count);
		}
		return count;
	}

	public void naiveInitiate(){
		int time = 0;
		int totalmaxLen= 0,totalStrLen= 0;
		String fn = "",fnPer = "";
		try {
			final File folder = new File("src/evaluation");
			File plagiarisedFile = new File( "src/plagiarised/plagiarised.txt");
			
			/*final File folder = new File("src/genuineExp");
			File plagiarisedFile = new File( "src/plagExp/plag.txt");*/
			
			
			String genuineFilePath,genuineLine,plagiarisedLine;
			int tot=0,row=0,fileNum=0;
			long startTime,endTime   ;
			for (final File genuineFileList : folder.listFiles()) {
				time=0;tot=0;
				totalmaxLen=0;totalStrLen=0;
				row=0;
				fileNum++;
				fn = "E:/kmp_" + fileNum + ".xls";
				File file = new File(fn);
				fnPer = "E:/kmpPercentage_" + fileNum + ".xls";
				File kmpPercentage = new File(fnPer);
				HSSFWorkbook workbook = null;
				HSSFSheet sheet = null;
				HSSFWorkbook workbookPer = null;
				HSSFSheet sheetPer = null;
				if(file.exists()){
					System.out.println("yesssssssss");
					try {
						workbook = new HSSFWorkbook(new FileInputStream(file));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sheet = workbook.getSheet("kmp");
				} else {
					System.out.println("noooooooooooooo");
					workbook = new HSSFWorkbook();
					sheet = workbook.createSheet("kmp");
				}
				FileOutputStream out = new FileOutputStream(fn);
				if(kmpPercentage.exists()){
					System.out.println("yesssssssss");
					try {
						workbookPer = new HSSFWorkbook(new FileInputStream(kmpPercentage));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sheetPer = workbookPer.getSheet("kmpPer");
				} else {
					System.out.println("noooooooooooooo");
					workbookPer = new HSSFWorkbook();
					sheetPer = workbookPer.createSheet("kmpPer");
				}
				FileOutputStream outPer = new FileOutputStream(fnPer);
				
				genuineFilePath = genuineFileList.getPath();
				System.out.println("file= "+genuineFilePath);
				//srcLineIndex=1;
				File genuineFile = new File(genuineFilePath);
				try {
					Scanner genuineScanner = new Scanner(genuineFile);
					genuineScanner.useDelimiter("\\n|\\.");
//					genuineScanner.useDelimiter(" ");
					//looping through genuine file
					genuineLine="";
					while (genuineScanner.hasNext()) {
						Scanner plagiarisedScanner = new Scanner(plagiarisedFile);
						plagiarisedScanner.useDelimiter("\\n|\\.");
//						plagiarisedScanner.useDelimiter(" ");
						genuineLine = genuineScanner.next();
						System.out.println("-------------------------------------------------------------------------");
						System.out.println("genuine= "+genuineLine);
						//looping through plagiarised file
						plagiarisedLine="";
						startTime = System.currentTimeMillis();
						while (plagiarisedScanner.hasNext()) {
							plagiarisedLine = plagiarisedScanner.next();						
							System.out.println("-------------------------------------------------------------------------");
							System.out.println("plagia= "+plagiarisedLine);
							naiveSearch naiveObj = new naiveSearch(genuineLine,plagiarisedLine);
							int per = naiveObj.naiveSrc();
							//int a =  plagiarisedLine.length();
							System.out.println("Percentage match = " + ((float)per/naiveObj.string1.length())*100 + "%");
							System.out.println("Time taken = " + naiveObj.timeCount);
							time += naiveObj.timeCount;
							totalmaxLen += per;
							totalStrLen += naiveObj.string1.length();
						}
						endTime   = System.currentTimeMillis();
						tot += (endTime - startTime);
						Row kmp = null;
						if(sheet.getRow(row) == null){
							kmp = sheet.createRow(row);
						} else {
							kmp = sheet.getRow(row);
						}
						//kmp.createCell(8).setCellValue(tot);
						kmp.createCell(6).setCellValue(row);
						kmp.createCell(7).setCellValue(time);
						if(sheetPer.getRow(row) == null){
							kmp = sheetPer.createRow(row);
						} else {
							kmp = sheetPer.getRow(row);
						}
						kmp.createCell(6).setCellValue(totalStrLen);
						kmp.createCell(7).setCellValue(totalmaxLen);
						row++;
						plagiarisedScanner.close();
					}
					genuineScanner.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					workbook.write(out);
					out.close();
					workbook.close();
					workbookPer.write(outPer);
					outPer.close();
					workbookPer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Time taken for naive for the whole file = " + time);
				System.out.println("------------eof-----------------------");			
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		naiveSearch obj = new naiveSearch();
		obj.naiveInitiate();
	}

}

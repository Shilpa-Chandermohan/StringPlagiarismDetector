package stringAlgos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.stream.FileImageInputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

public class KMP {
	private String string1;
	private String string2;
	private int string_1_Len;
	private int string_2_Len;
	private int timeCount;

	public KMP(String s1, String s2){
		if(s1.length() > s2.length()){
			string1 = s1;
			string2 = s2;
		} else {
			string1 = s2;
			string2 = s1;
		}
		string_1_Len = string1.length();
		string_2_Len = string2.length();
		timeCount = 0;
	}

	public KMP() {
	}

	public int[] preProcessPattern() { //for genuine text
		int loopVar_i = 0;
		int loopVar_j = -1;
		int[] lps = new int[string_1_Len + 1];

		lps[loopVar_i] = loopVar_j;
		while (loopVar_i < string_1_Len) {
			while (loopVar_j >= 0 && string1.charAt(loopVar_i) != string1.charAt(loopVar_j)) {
				loopVar_j = lps[loopVar_j];
				//timeCount++;
			}
			loopVar_i++;
			loopVar_j++;
			lps[loopVar_i] = loopVar_j;
		}

		return lps;
	}

	public int searchKMP() {
		int[] lps = preProcessPattern();
		int i = 0, j = 0,maxSubstringLength=0;
		while (i < string_2_Len) {
			while (j >= 0 && string2.charAt(i) != string1.charAt(j)) {
				j = lps[j];
				timeCount++;  
			}
			i++;
			j++;
			maxSubstringLength = Math.max(maxSubstringLength, j);
			if (j == string_1_Len) {
				System.out.println("FOUND SUBSTRING AT i " + i + " and index:"
						+ (i - string_1_Len));
				System.out.println("Setting j from " + j + " to " + lps[j]);
				j = lps[j];
			}
		}
		return maxSubstringLength;
	}

	public void kmpInitiate(){
		int timeCount = 0;
		int totalmaxLen= 0,totalStrLen= 0;
		
		try {
			final File folder = new File("src/evaluation");
			File plagiarisedFile = new File( "src/plagiarised/plagiarised.txt");
			
			/*final File folder = new File("src/genuineExp");
			File plagiarisedFile = new File( "src/plagExp/plag.txt");*/
			
			
			String genuineFilePath,genuineLine,plagiarisedLine;
			int row=0,fileNum=0,tot=0;
			String fn = "",fnPer = "";
			long startTime,endTime   ;
			for (final File genuineFileList : folder.listFiles()) {
				timeCount=0;
				tot=0;
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
				System.out.println("-------------------------------------------------------------------------");
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
						if(genuineLine.length() > 0){
							System.out.println("Genuine paragraph = " + genuineLine);
							System.out.println("Genuine paragraph len = " + genuineLine.length());
							System.out.println("-------------------------------------------------------------------------");
							//looping through plagiarised file
							plagiarisedLine="";
							int maxLen=0;
							startTime = System.currentTimeMillis();
							while (plagiarisedScanner.hasNext()) {
								plagiarisedLine = plagiarisedScanner.next();
								System.out.println("Plagiarised paragraph = " + plagiarisedLine);
								KMP kmpObj = new KMP(genuineLine,plagiarisedLine);
								maxLen = kmpObj.searchKMP();
								System.out.println("maxLen = " + maxLen);
								System.out.println("Percentage match = " + ((float)maxLen/kmpObj.string_2_Len)*100 + "%");
								System.out.println("Time taken = " + kmpObj.timeCount);
								System.out.println("-------------------------------------------------------------------------");
								timeCount += kmpObj.timeCount;
								totalmaxLen += maxLen;
								totalStrLen += kmpObj.string_2_Len;
							}
							endTime   = System.currentTimeMillis();
							tot += (endTime - startTime);
							Row kmp = null;
							if(sheet.getRow(row) == null){
								kmp = sheet.createRow(row);
							} else {
								kmp = sheet.getRow(row);
							}
							//kmp.createCell(5).setCellValue(tot);
							kmp.createCell(3).setCellValue(row);
							kmp.createCell(4).setCellValue(timeCount);
							if(sheetPer.getRow(row) == null){
								kmp = sheetPer.createRow(row);
							} else {
								kmp = sheetPer.getRow(row);
							}
							kmp.createCell(3).setCellValue(totalStrLen);
							kmp.createCell(4).setCellValue(totalmaxLen);
							row++;
							plagiarisedScanner.close();
						}

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
				System.out.println("Time taken for kmp for the whole file = " + timeCount);
				System.out.println("------------eof-----------------------");			
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		KMP stm = new KMP();
		stm.kmpInitiate();
	}

}

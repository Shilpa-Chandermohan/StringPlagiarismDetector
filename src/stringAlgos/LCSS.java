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

public class LCSS {
	private String string1;
	private String string2;
	private String lcssStringVal;
	private int string_1_Len;
	private int string_2_Len;
	private int[][] lcssMatrix;

	public LCSS(){
		//System.out.println("LCSS constructor invoked!");
	}
	public LCSS(String s1, String s2){
		//System.out.println("LCSS parameterized constructor invoked!");
		if(s1.length() > s2.length()){
			string1 = s2;
			string2 = s1;
		} else {
			string1 = s1;
			string2 = s2;
		}
		string_1_Len = string1.length();
		string_2_Len = string2.length();
		lcssMatrix = new int[string_1_Len + 1][string_2_Len + 1];
		lcssStringVal = "";
		for(int i = 0; i < string_1_Len; i++)  // lcssMatrix gets initialized to 0 for all entries
			for(int j = 0; j < string_2_Len; j++){
				lcssMatrix[i][j] = 0;
			}
	}

	public int max(int matrix1,int matrix2){
		if(matrix1 > matrix2){
			return matrix1;
		} else {
			return matrix2;
		}
	}

	public int lcssLength(){
		int i,j;
		for (i=0; i<=string_1_Len; i++){
			for (j=0; j<=string_2_Len; j++){
				if (i == 0 || j == 0)
					lcssMatrix[i][j] = 0;
				else if (string1.charAt(i-1) == string2.charAt(j-1))
					lcssMatrix[i][j] = lcssMatrix[i-1][j-1] + 1;
				else
					lcssMatrix[i][j] = max(lcssMatrix[i-1][j], lcssMatrix[i][j-1]);
			}
		}
		return lcssMatrix[string_1_Len][string_2_Len];
	}

	public void lcssStringVal(int lcssVal){
		if(lcssMatrix[string_1_Len][string_2_Len] != 0){
			for(int loopVarI=string_1_Len;loopVarI>0 && lcssStringVal.length() < lcssVal;){
				for(int loopVarJ=string_2_Len;loopVarJ>0&& lcssStringVal.length() < lcssVal;){
					if(lcssMatrix[loopVarI][loopVarJ] == lcssMatrix[loopVarI][loopVarJ-1]){
						loopVarJ--;
					} else if(lcssMatrix[loopVarI][loopVarJ] == lcssMatrix[loopVarI-1][loopVarJ]){
						loopVarI--;
					} else {
						lcssStringVal = string1.charAt(loopVarI-1)+lcssStringVal;
						loopVarJ--;
						loopVarI--;
					}
				}
			}
			System.out.println("lcss string = " + lcssStringVal);
		}
	}

	public void lcssInitiate(){
		int totalmaxLen= 0,totalStrLen= 0;
		String fn = "",fnPer = "";
		try {
			final File folder = new File("src/evaluation");
			File plagiarisedFile = new File( "src/plagiarised/plagiarised.txt");
			
			/*final File folder = new File("src/genuineExp");
			File plagiarisedFile = new File( "src/plagExp/plag.txt");*/
			
			
			String genuineFilePath,genuineLine,plagiarisedLine;
			int tot=0,lcssVal=0,lcssPercentage=0,lineNumber=1,time=0,row=0,fileNum=0;
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
				System.out.println("-------------------------------------------------------------------------");
				//srcLineIndex=1;
				File genuineFile = new File(genuineFilePath);
				try {
					Scanner genuineScanner = new Scanner(genuineFile);
					
					genuineScanner.useDelimiter("\\n|\\.");
//					genuineScanner.useDelimiter(" ");
					
					//looping through genuine file
					genuineLine="";
					//while (genuineScanner.hasNextLine()) {
					while (genuineScanner.hasNext()) {
						Scanner plagiarisedScanner = new Scanner(plagiarisedFile);
						
						plagiarisedScanner.useDelimiter("\\n|\\.");
//						plagiarisedScanner.useDelimiter(" ");
						
						//genuineLine = genuineScanner.nextLine();
						genuineLine = genuineScanner.next();
						
						System.out.println("Genuine paragraph = " + genuineLine);
						System.out.println("-------------------------------------------------------------------------");
						//looping through plagiarised file
						plagiarisedLine="";
						lineNumber=1;
						startTime = System.currentTimeMillis();
						//while (plagiarisedScanner.hasNextLine()) {
						while (plagiarisedScanner.hasNext()) {
							//plagiarisedLine = plagiarisedScanner.nextLine();
							plagiarisedLine = plagiarisedScanner.next();
							
							LCSS lcssObj = new LCSS(genuineLine,plagiarisedLine);
							time = time + (genuineLine.length()*plagiarisedLine.length());
							lcssVal = lcssObj.lcssLength();
							lcssPercentage = computePercentage(lcssVal,lcssObj);
							if(lcssVal > 0 && lcssPercentage > 80){
								System.out.println("Paragraph number = " + lineNumber);
								System.out.println("Plagiarised paragraph = " + plagiarisedLine);
								System.out.println("lcss Val = " + lcssVal + " ,Time taken = " + (genuineLine.length()*plagiarisedLine.length()) + ", Percentage matched = " + lcssPercentage + "%");
								lcssObj.lcssStringVal(lcssVal);
								System.out.println("-------------------------------------------------------------------------");
								
								
							}
							lineNumber++;
							totalmaxLen += lcssVal;
							totalStrLen += lcssObj.string_1_Len;
						}
						endTime   = System.currentTimeMillis();
						tot += (endTime - startTime);
						Row kmp = null;
						if(sheet.getRow(row) == null){
							kmp = sheet.createRow(row);
						} else {
							kmp = sheet.getRow(row);
						}
						//kmp.createCell(2).setCellValue(tot);
						kmp.createCell(0).setCellValue(row);
						kmp.createCell(1).setCellValue(time);
						if(sheetPer.getRow(row) == null){
							kmp = sheetPer.createRow(row);
						} else {
							kmp = sheetPer.getRow(row);
						}
						kmp.createCell(0).setCellValue(totalStrLen);
						kmp.createCell(1).setCellValue(totalmaxLen);
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
					e.printStackTrace();
				}
				System.out.println("Time taken for lcss for the whole file = " + time);
				System.out.println("------------eof-----------------------");			
			}
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public int computePercentage(int lcssLength,LCSS lcssObj){
		if(lcssObj.string_1_Len != 0){
			return ((lcssLength/lcssObj.string_1_Len)*100);
		} else {
			return 0;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LCSS lcssObjInvoke = new LCSS();
		lcssObjInvoke.lcssInitiate();

	}

}

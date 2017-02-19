package stringAlgos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class boyerMoore {
	private String string1;
	private String string2;
	private int string_1_Len;
	private int string_2_Len;
	private int timeCount,shift=0;
	private int [] badArr = new int[256];

	public boyerMoore(String s1, String s2) {
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
		for(int loopVar=0;loopVar<256;loopVar++){
			badArr[loopVar] = -1;
		}
		for (int i = 0; i < string2.length(); i++) {
			badArr[string2.charAt(i)] = i; // the shift values for  the characters in the text that does not occur in the pattern
			//preprocessingTimeCount++;
			timeCount++;

		}
		System.out.println("lengthhhhhhhhhhhhhhhhhhh = " + badArr.length);
	}

	public boyerMoore() {
	}

	private void bmInitiate() {
		int timeCount = 0;
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
				timeCount=0;tot=0;
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
					//genuineScanner.useDelimiter(" ");
					//looping through genuine file
					genuineLine="";
					while (genuineScanner.hasNext()) {
						Scanner plagiarisedScanner = new Scanner(plagiarisedFile);
						plagiarisedScanner.useDelimiter("\\n|\\.");
						//plagiarisedScanner.useDelimiter(" ");
						genuineLine = genuineScanner.next();
						if(genuineLine.length() > 0){
							System.out.println("Genuine paragraph = " + genuineLine);
							System.out.println("Genuine paragraph len = " + genuineLine.length());
							System.out.println("-------------------------------------------------------------------------");
							//looping through plagiarised file
							plagiarisedLine="";
							startTime = System.currentTimeMillis();
							while (plagiarisedScanner.hasNext()) {
								plagiarisedLine = plagiarisedScanner.next();
								System.out.println("Plagiarised length = " + plagiarisedLine.length());
								System.out.println("Plagiarised paragraph = " + plagiarisedLine);

								boyerMoore bmObj = new boyerMoore(genuineLine,plagiarisedLine);
								int shiftVal = bmObj.searchBM();

								//System.out.println("Percentage match = " + ((float)bmObj.shift/bmObj.string_2_Len)*100 + "%");
								System.out.println("Time taken = " + bmObj.timeCount);
								System.out.println("-------------------------------------------------------------------------");
								timeCount += bmObj.timeCount;
								totalmaxLen += (shiftVal >= 0 ? bmObj.string_2_Len : 0);
								totalStrLen += bmObj.string_2_Len;

							}
							endTime   = System.currentTimeMillis();
							tot += (endTime - startTime);
							Row kmp = null;
							if(sheet.getRow(row) == null){
								kmp = sheet.createRow(row);
							} else {
								kmp = sheet.getRow(row);
							}
							//kmp.createCell(11).setCellValue(tot);
							kmp.createCell(9).setCellValue(row);
							kmp.createCell(10).setCellValue(timeCount);
							if(sheetPer.getRow(row) == null){
								kmp = sheetPer.createRow(row);
							} else {
								kmp = sheetPer.getRow(row);
							}
							kmp.createCell(9).setCellValue(totalStrLen);
							kmp.createCell(10).setCellValue(totalmaxLen);
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
	private int searchBM() {
		shift = 0;
		while(shift <= (string_1_Len - string_2_Len)){
			int j = string_2_Len - 1;
			while(j >= 0 && string2.charAt(j) == string1.charAt(shift+j)){
				j--;
				timeCount++;
			}
			if (j < 0)
			{
				System.out.println("pattern occurs at shift = " + shift);
				shift += (shift + string_2_Len < string_1_Len)? string_2_Len - badArr[string1.charAt(shift + string_2_Len)] : 1;
				return shift;
			}

			else
				shift += Math.max(1, j - badArr[string1.charAt(shift + j)]);
		}
		System.out.println("Noooooooooooooooooooooooooooooo = " + shift);
		return -1;
	}
	public static void main(String[] args) {
		boyerMoore bm = new boyerMoore();
		bm.bmInitiate();
	}

}

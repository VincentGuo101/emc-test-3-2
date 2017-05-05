/**
 * This program tests concurrence based on 5 files on a file system.
 * 
 * 10 write threads, each of them randomly picks up one file to write something random. 
 *  
 * 2 read threads, in the meanwhile, each of them randomly picks up one of the files to read and verify whether the content is just as what the last thread writes to it.
 * 
 * All the 5 files are written and verified exactly 2 times.
 * 
 * @author vincent.guo
 */

package com.vincentguo.emctest3_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestConcurrence {
	/**
	 * This class tests concurrence based on 5 files on a file system by using
	 * 10 write threads and 2 read threads.
	 */
	public static File file1;
	public static File file2;
	public static File file3;
	public static File file4;
	public static File file5;

	public static String writtenContentFile1 = "";
	public static String writtenContentFile2 = "";
	public static String writtenContentFile3 = "";
	public static String writtenContentFile4 = "";
	public static String writtenContentFile5 = "";

	public static String readContentFile1 = "";
	public static String readContentFile2 = "";
	public static String readContentFile3 = "";
	public static String readContentFile4 = "";
	public static String readContentFile5 = "";

	public static String testFileName1 = "";
	public static String testFileName2 = "";

	public static String verBase = "1122334455"; // For making each file be
													// written twice.
	private static ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

	public static void prepareFiles() {
		/*
		 * This method checks whether the 5 files exist. If not exist, then
		 * create new files.
		 */
		try {
			file1 = new File("C:\\Users\\Vincent.Guo\\Documents\\file1.txt");
			if (!file1.exists())
				file1.createNewFile();
			System.out.println("file1.txt is prepared.");

			file2 = new File("C:\\Users\\Vincent.Guo\\Documents\\file2.txt");
			if (!file2.exists())
				file2.createNewFile();
			System.out.println("file2.txt is prepared.");

			file3 = new File("C:\\Users\\Vincent.Guo\\Documents\\file3.txt");
			if (!file3.exists())
				file3.createNewFile();
			System.out.println("file3.txt is prepared.");

			file4 = new File("C:\\Users\\Vincent.Guo\\Documents\\file4.txt");
			if (!file4.exists())
				file4.createNewFile();
			System.out.println("file4.txt is prepared.");

			file5 = new File("C:\\Users\\Vincent.Guo\\Documents\\file5.txt");
			if (!file5.exists())
				file5.createNewFile();
			System.out.println("file5.txt is prepared.");
		} catch (IOException e) {
			System.out.println("Failed to create files.");
			e.printStackTrace();
		}
	}

	public static void writeFile() {
		/*
		 * This method writes 30 random chars into picked file.
		 */
		rw.writeLock().lock(); // Write lock is switched on.
		File pf = pickedFile();
		String s = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz !@#$%^&*()_+~";
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < 30; i++) {
			int n = random.nextInt(s.length());
			stringBuffer.append(s.charAt(n));
		}

		try {
			FileWriter fw = new FileWriter(pf);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(stringBuffer.toString());
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Failed to write in file.");
			e.printStackTrace();
		}

		switch (pf.getName()) {
		case "file1.txt":
			writtenContentFile1 = stringBuffer.toString();
			break;
		case "file2.txt":
			writtenContentFile2 = stringBuffer.toString();
			break;
		case "file3.txt":
			writtenContentFile3 = stringBuffer.toString();
			break;
		case "file4.txt":
			writtenContentFile4 = stringBuffer.toString();
			break;
		case "file5.txt":
			writtenContentFile5 = stringBuffer.toString();
		}

		System.out.println(pf.getName() + " is written.");
		rw.writeLock().unlock(); // Write lock is switched off.
	}

	public static void readFile() {
		/*
		 * This method reads content from picked file.
		 */
		rw.readLock().lock(); // Read lock is switched on.
		File pf = pickedFile();
		String readContent = "";

		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(pf));
			BufferedReader br = new BufferedReader(read);
			try {
				readContent = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read from file.");
			e.printStackTrace();
		}

		switch (pf.getName()) {
		case "file1.txt":
			if (testFileName1.equals(""))
				testFileName1 = "file1.txt";
			else
				testFileName2 = "file1.txt";
			readContentFile1 = readContent;
			break;
		case "file2.txt":
			if (testFileName1.equals(""))
				testFileName1 = "file2.txt";
			else
				testFileName2 = "file2.txt";
			readContentFile2 = readContent;
			break;
		case "file3.txt":
			if (testFileName1.equals(""))
				testFileName1 = "file3.txt";
			else
				testFileName2 = "file3.txt";
			readContentFile3 = readContent;
			break;
		case "file4.txt":
			if (testFileName1.equals(""))
				testFileName1 = "file4.txt";
			else
				testFileName2 = "file4.txt";
			readContentFile4 = readContent;
			break;
		case "file5.txt":
			if (testFileName1.equals(""))
				testFileName1 = "file5.txt";
			else
				testFileName2 = "file5.txt";
			readContentFile5 = readContent;
		}

		System.out.println(pf.getName() + " is read.");
		rw.readLock().unlock(); // Read lock is switched off.
	}

	public static File pickedFile() {
		/*
		 * This method picks a random file from 5 files.
		 */
		File targetFile = file1;
		Random random = new Random();
		int n = random.nextInt(verBase.length());
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(verBase.charAt(n));
		String c = stringBuffer.toString();

		switch (c) {
		case "1":
			targetFile = file1;
			break;
		case "2":
			targetFile = file2;
			break;
		case "3":
			targetFile = file3;
			break;
		case "4":
			targetFile = file4;
			break;
		case "5":
			targetFile = file5;
		}

		// Reform verBase that stands writing files.
		verBase = verBase.substring(0, n) + verBase.substring(n + 1, verBase.length());

		// Reform verBase that stands for reading files.
		if (verBase.equals(""))
			verBase = "12345";
		return targetFile;
	}

	public static void showTestResult() {
		/*
		 * This method shows two test result in format.
		 */
		switch (testFileName1) {
		case "file1.txt":
			System.out.println("");
			System.out.println("File Name: file1.txt");
			System.out.println("Expected Content: " + writtenContentFile1);
			System.out.println("Actual Content: " + readContentFile1);
			if (writtenContentFile1.equals(readContentFile1))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file2.txt":
			System.out.println("");
			System.out.println("File Name: file2.txt");
			System.out.println("Expected Content: " + writtenContentFile2);
			System.out.println("Actual Content: " + readContentFile2);
			if (writtenContentFile2.equals(readContentFile2))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file3.txt":
			System.out.println("");
			System.out.println("File Name: file3.txt");
			System.out.println("Expected Content: " + writtenContentFile3);
			System.out.println("Actual Content: " + readContentFile3);
			if (writtenContentFile3.equals(readContentFile3))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file4.txt":
			System.out.println("");
			System.out.println("File Name: file4.txt");
			System.out.println("Expected Content: " + writtenContentFile4);
			System.out.println("Actual Content: " + readContentFile4);
			if (writtenContentFile4.equals(readContentFile4))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file5.txt":
			System.out.println("");
			System.out.println("File Name: file5.txt");
			System.out.println("Expected Content: " + writtenContentFile5);
			System.out.println("Actual Content: " + readContentFile5);
			if (writtenContentFile5.equals(readContentFile5))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
		}

		switch (testFileName2) {
		case "file1.txt":
			System.out.println("");
			System.out.println("File Name: file1.txt");
			System.out.println("Expected Content: " + writtenContentFile1);
			System.out.println("Actual Content: " + readContentFile1);
			if (writtenContentFile1.equals(readContentFile1))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file2.txt":
			System.out.println("");
			System.out.println("File Name: file2.txt");
			System.out.println("Expected Content: " + writtenContentFile2);
			System.out.println("Actual Content: " + readContentFile2);
			if (writtenContentFile2.equals(readContentFile2))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file3.txt":
			System.out.println("");
			System.out.println("File Name: file3.txt");
			System.out.println("Expected Content: " + writtenContentFile3);
			System.out.println("Actual Content: " + readContentFile3);
			if (writtenContentFile3.equals(readContentFile3))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file4.txt":
			System.out.println("");
			System.out.println("File Name: file4.txt");
			System.out.println("Expected Content: " + writtenContentFile4);
			System.out.println("Actual Content: " + readContentFile4);
			if (writtenContentFile4.equals(readContentFile4))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
			break;
		case "file5.txt":
			System.out.println("");
			System.out.println("File Name: file5.txt");
			System.out.println("Expected Content: " + writtenContentFile5);
			System.out.println("Actual Content: " + readContentFile5);
			if (writtenContentFile5.equals(readContentFile5))
				System.out.println("Test Result: Pass");
			else
				System.out.println("Test Result: Fail");
		}

	}

	public static void main(String[] args) {
		/*
		 * This is the entry of program.
		 */

		// Make 5 files prepared.
		prepareFiles();

		// Create and run 10 write threads.
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				public void run() {
					writeFile();
				}
			}).start();

		}

		// Create and run 2 read threads.
		for (int j = 0; j < 2; j++) {
			new Thread(new Runnable() {
				public void run() {
					readFile();
				}
			}).start();

		}

		// Wait for 5 seconds then show test result.
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showTestResult();
	}

}

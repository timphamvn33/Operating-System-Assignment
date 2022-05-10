
// CS4345, Spring 2022, Assignment 2, Thuong Pham
// This main submission includes the Extra Credits incorporation
// Please include the reading text file in the same folder with this program 
// The latest version for the assignment 2(resubmit on 03/09/2022). 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class a2pham {
	static List<String> sortedList = new ArrayList<String>();
	public static void main (String []args) throws IOException {
// check if the args[0] is not typed
if (0 < args.length) {
   File file = new File(args[0]);
} else {
   System.err.println("Invalid arguments count:" + args.length);
   
}
		while(args[0].length()<1) {
			System.out.println("please input the right file name");
		}
		File file = new File(args[0]);
		// write the sorted numbers output the output file that you created in the command line
		String outFile = args[0].substring(0, args[0].indexOf("."))+ "-sorted.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
		// count how many rows in the file 
		int numLine = 0;
		Scanner sc = new Scanner (file) ;
		while(sc.hasNextLine()) {
			sc.nextLine();
			numLine++;
		}
		HashMap<Integer, List<Integer>> data = readFile(file);
		List <Thread> tS = new ArrayList<Thread>();// List to store the threads 
		
		// run the number of threads base on how many rows 
		
		// Create the threads 
		for(int i=1; i<data.size()+1; i++) {
			int lineNum = i;
			SortNum sortNum = new SortNum(data.get(i),out, lineNum, sortedList);
			Thread t = new Thread(sortNum);
			tS.add(t);
			
		}
		//  start the threads 
		for(int i=0; i<numLine; i++) {
			tS.get(i).start();
		}
		
			// join the threads 
		for(int i=0; i<numLine; i++) {
			try {
				tS.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		------------------------------------------------------------------------Extra Credit----------------------------------------
			// write the output file 
		for(int key:data.keySet()) {
			
			for(String line:  sortedList) {
				
				if(key==Integer.parseInt(line.split(" ")[1])) {
					try {
						out.write(line);
						out.flush();
						
					} catch (IOException e) {
					
						e.printStackTrace();
					}
					
				}
			}
		
		}
		out.write("Row major sorting complete");
		out.flush();
			
	}
//	read the file 
	public static HashMap<Integer, List<Integer>> readFile(File file) throws FileNotFoundException {
		Scanner scanner;
		
			scanner = new Scanner(file);
			 HashMap<Integer, List<Integer>> data = new  HashMap<Integer, List<Integer>>();
			// call the variable count to count the number of row. Base on that variable I can specify the row to read and sort
			int count=1;
			// create a list to store the numbers and sort it 

			while(scanner.hasNextLine()) {
					
					String line = scanner.nextLine();
					List <Integer> numbers = new ArrayList<Integer>();
						String[] nums = line.split(" ");
						
						for(int i=0; i<nums.length; i++) {
							numbers.add(Integer.parseInt(nums[i]));
							
						}
						
						data.put(count, numbers);
						count++;

		  }
			return data;
			
		}
}

// class to sort the numbers at the specific row 

	class SortNum implements Runnable {
		List<Integer>numbers;
		// writing the output file 
		BufferedWriter out;
		int lineNum;
		List<String> sortedList;

		 

		public SortNum(List<Integer>numbers, BufferedWriter out, int lineNum, List<String> sortedList) {
			this.numbers=numbers;
			this.out=out;
			this.lineNum = lineNum;
			this.sortedList= sortedList;
		}

		@Override
		public void run() {
			String strs="";
			// sort the numbers in ascending order
			Collections.sort(numbers);
			
			strs+="Thread "+ Integer.toString(lineNum)+ " Start: ";
			// loop the sorted numbers loop and add them to the output string strs 
			for(int n : numbers) {
				strs+=Integer.toString(n) + " ";
			}
			strs+=" Thread "+ lineNum+ " Ends.\n" ;
			System.out.println(strs);
			sortedList.add(strs);

		}

	}
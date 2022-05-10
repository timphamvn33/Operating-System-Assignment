//Thuong Manh Pham, CS4345, Spring2022, Assignment 3.
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class Scheduling_Pham {
//	List<Integer> processId = new ArrayList<Integer>();
//	List<Integer> priority = new ArrayList<Integer>();
//	List<Integer>burstLength = new ArrayList<Integer>();
	public static void main(String [] args) {
			String display= "Process ID | "+ "Priority |"+ " Burst-length\n";
			String upDateDisplay= "---------Updated Snapshot---------\nProcess ID | "+ "Priority |"+ " Burst-length\n";
			List<Integer> iDs = processId(0, 10);
			Collections.sort(iDs);
			List<Integer> priority =  randomNumber(0, 10);
			List<Integer>burstLength = randomNumber(20, 100);
			for(int i=0; i<iDs.size(); i++) {
				display+= iDs.get(i)+ "\t\t"+ priority.get(i)+ "\t\t"+ burstLength.get(i)+"\n";
			}
			System.out.println(display);
			// update snapshot after user input
			Scanner sc = new Scanner(System.in);
			
			List<Integer> userInput = inputUser(iDs, sc);
			int userId= userInput.get(0);
			int userPriority = userInput.get(1);
			int userburstLength = userInput.get(2);
			iDs.add(userId);
			Collections.sort(iDs);
			int pos = iDs.indexOf(userId);
			priority.add(pos,userPriority );
			burstLength.add(pos,userburstLength );
			
			for(int i=0; i<iDs.size(); i++) {
				upDateDisplay+= iDs.get(i)+ "\t\t"+ priority.get(i)+ "\t\t"+ burstLength.get(i)+"\n";
			}
			System.out.println(upDateDisplay);
			
			// ---------------requirement 4---------------------
			// --------------Shortest Job First-----------------
			List<Integer>burstLengthClone = new ArrayList<Integer>(burstLength); // copy another burstLength list
			String head = "Scheduling algorithm | Process ID | Priority | Burst-length | Total waiting time\r\n";
			String sJFString = head;
			List<List<Integer>> btMap = sJF(burstLengthClone);
			// sort the burst and priority
			Collections.sort(burstLengthClone);
			List<Integer> newIDs = getIds_Priority(iDs,btMap,burstLengthClone); 
			List<Integer> waitingTime = sJFWaitingTime(burstLengthClone);
			sJFString+= "Non-preemptive SJF\n";
			List<Integer>sortByProcess= sortByProcessID(waitingTime, newIDs, iDs); // sort the waiting time by id order
			for(int i=0; i<newIDs.size(); i++) {
				sJFString+="\t\t\t"+iDs.get(i)+ "\t\t"+ priority.get(i)+ "\t\t"+ burstLength.get(i)+ "\t\t"+  sortByProcess.get(i)+"\n";
			}
			System.out.println(sJFString);
			double average = averageWaitingTime(waitingTime);
			System.out.printf("\nAverage waiting time: %.2f\n", average);
			// -----------------priority-------------------
			// priority 
			List<Integer>priorityClone = new ArrayList<Integer>(priority); // copy another priority list
			
			String priorityString = head;
			List<List<Integer>> btpMap = sJF(priorityClone);
			Collections.sort(priorityClone);
//			List<Integer> newIDPs = getIds_Priority(iDs,btpMap,priorityClone); 
			List<Integer> newBurst= getIds_Priority(burstLength,btpMap,priorityClone);
			List<Integer> newIDsp= getIds_Priority(iDs,btpMap,priorityClone);
			List<Integer> waitingTimeP = sJFWaitingTime(newBurst);
			priorityString+= "Non-preemptive priority\n";
		

			List<Integer>sortByProcessP= sortByProcessID(waitingTimeP, newIDsp, iDs); // sort the waiting time by id order
			for(int i=0; i<iDs.size(); i++) {
				priorityString+="\t\t\t"+iDs.get(i)+ "\t\t"+ priority.get(i) + "\t\t"+ burstLength.get(i)+"\t\t"+ sortByProcessP.get(i)+"\n";
			}
			
			System.out.println(priorityString);
			
			double averageP = averageWaitingTime(waitingTimeP);
			System.out.printf("\nAverage waiting time: %.2f\n", averageP);
		
			// -----------------round robin--------------------
			String roundString = head+"Round Robin With quantum=25\n";
			
			int quantum =25;
			List<Integer>roundID = roundRobin(burstLength, iDs,priority, quantum, "ID");
			List<Integer>roundBurst = roundRobin(burstLength, iDs, priority,quantum, "burst");
			List<Integer>roundpriority = roundRobin(burstLength, iDs,priority, quantum, "priority");
			List<Integer>waitTimeRR= waitingTimeRR(roundBurst, quantum);
			for(int i=0; i<roundID.size(); i++) {
				roundString+="\t\t\t"+roundID.get(i)+ "\t\t"+ roundpriority.get(i) + "\t\t"+ roundBurst.get(i)+"\t\t"+ waitTimeRR.get(i)+"\n";
			}
			System.out.println(roundString);
			double averageRR = averageWaitingTime(waitTimeRR);
			System.out.printf("\nAverage waiting time: %.2f\n", averageRR);
			String finalString = sortAverageWaiting(average, " Non-preemptive SJF", averageP, "Non-preemptive priority", averageRR, "\tRound Robin");
			System.out.println(finalString);
		}
// list of non-duplicated id numbers
	public static List<Integer> processId(int min, int max) {
		List<Integer> idTemps = new ArrayList<Integer>();
		Random random = new Random();
		int count=0;
		
		int num = random.nextInt(max-min) +min;
		idTemps.add(num);
		
		while(count<4) {
			
			int num1 = random.nextInt(max-min)+min;
				for(int i=0; i<idTemps.size(); i++) {
					
						if(idTemps.get(i)==num1) {
							
							i=idTemps.size();
						}
						else if(i==idTemps.size()-1) {
							idTemps.add(num1);
							count++;
						}
				}
			
			}

		return idTemps;
	
	}
	// list of random numbers for priority and burst-length
	public static List<Integer> randomNumber(int min, int max){
		List<Integer> idTemps = new ArrayList<Integer>();
		Random random = new Random();
		for(int i=0; i<5; i++) {
			int num = random.nextInt(max-min) +min;
			idTemps.add(num);
		}
		return idTemps;
		
	}
	public static List<Integer> inputUser(List<Integer>ids, Scanner sc){
		List<Integer> idTemps = new ArrayList<Integer>();
			System.out.println(" Enter the valid id in range[0, 10]: ");
			isRealNumber(sc);
			int num1=sc.nextInt();
			num1=ilegal(num1, sc, 0, 10);
			int num2, num3;
//			asking the id
			boolean notGo=true; 
			// check if the input number is out of range 
			// check whether the input already exist 
			while(notGo) {
				int countDub=0;
			
				for(int i=0; i<ids.size(); i++) {
					if(ids.get(i)==num1) {
						countDub++;
						
					}
				}
				if(countDub==1) {
					System.out.println(" The value is already exist ");
					isRealNumber(sc);
					num1=sc.nextInt();
					num1=ilegal(num1, sc, 0, 10);
					
				}
				else {
					notGo=false;
					
				}
			}
			idTemps.add(num1);
			
//			asking the priority
			System.out.println(" Enter the valid priority in range[0, 10]: ");
			isRealNumber(sc);
			num2 =sc.nextInt();
			num2=ilegal(num2, sc, 0, 10);
			idTemps.add(num2);
//			asking the burst length
			System.out.println(" Enter the burst-lenghth in range [20, 100]: ");
			isRealNumber(sc);
			num3 =sc.nextInt();
			num3=ilegal(num3, sc, 20, 100);
			idTemps.add(num3);
		return idTemps;
	}
	
	// check if the number in range 0-10 
	public static int ilegal(int num, Scanner sc, int min, int max ) {
			
			while(num<min || num> max)
			{
				System.out.println("please Enter the valid number");
				num=sc.nextInt();
				
			}

		return num;
	}
	// check for any non-numbers 
	public static void isRealNumber(Scanner sc) {
		
		while(!sc.hasNextInt()) {
			System.out.println("please Enter the valid number");
			sc.next();

		}
		
	}
	
	// -----Non-preemptive SJF algorithm-----
	// -----Non-preemptive priority algorithm-----
	public static List<List<Integer>> sJF(List<Integer> burstAndPriority){
		List<List<Integer>> bTMap = new ArrayList<List<Integer>>();
		for(int i=0; i<burstAndPriority.size(); i++) {
			List<Integer> inside = new  ArrayList<Integer>();
			inside.add(burstAndPriority.get(i));
			inside.add(i);
			bTMap.add(inside);
		}
			
		
		return bTMap;
		
	}
	// get the list sort of ID and priority base on the burst-length
	public static List<Integer> getIds_Priority(List<Integer> lists, List<List<Integer>>bTPMap,  List<Integer> burstOrPrioritySorted ){
		
		List<Integer> IdsOrPriority = new ArrayList<Integer>();
		 
		for( int i=0; i<burstOrPrioritySorted.size(); i++) {
			int countDub = 0; // count duplicate
			for(int j=0; j<bTPMap.size(); j++) {
				
				
				if(burstOrPrioritySorted.get(i)== bTPMap.get(j).get(0)) {
					countDub++; // check any for any duplicate value 
					if(countDub>=2) {
						i++; 

					}
					else {}
					int pos = bTPMap.get(j).get(1);
					IdsOrPriority .add(lists.get(pos));
				}
			}

		}
		return IdsOrPriority ;
	}

	// waiting time 
	public static List<Integer>sJFWaitingTime(List<Integer>burstOrPrioritySorted){
		List<Integer> waitingTime = new ArrayList<Integer>();
		int fistWait = 0;
		waitingTime.add(fistWait);
		for(int i=0; i<burstOrPrioritySorted.size()-1; i++) {
			fistWait+=burstOrPrioritySorted.get(i);
			waitingTime.add(fistWait);
		}
		return  waitingTime;
	}
	// average waiting time
	public static double averageWaitingTime(List<Integer> waitingTime ) {
		int average =0;
		for(int i=0; i<waitingTime.size()-1; i++) {
			average+=waitingTime.get(i);
			
			
		}
		return average/waitingTime.size();
	}
	// sort the processes in the order base on their ID
	
	public static List<Integer>sortByProcessID(List<Integer>waitingTime, List<Integer>iDS, List<Integer>iDSorted){
		List<Integer>returnList = new ArrayList<Integer>(); // output list
		HashMap<Integer, Integer> waiting = new HashMap<Integer, Integer>();
		
		
		for(int i=0; i<waitingTime.size(); i++) {
			// put to hashmap 
			waiting.put(i, waitingTime.get(i));
		}
		
		for(int j=0; j<iDSorted.size(); j++) {
			for(int t=0; t<iDS.size(); t++) {
				if(iDSorted.get(j)==iDS.get(t)) {
						returnList.add(waiting.get(t));
				}
			}
		}
		return returnList;
	}
	// Round-Robin 
		public static List<Integer>roundRobin(List<Integer>burstTime, List<Integer>processID, List<Integer>prio, int quantum, String choose){
			List<Integer>returnList = new ArrayList<Integer>(); // output list
			List<Integer>grantID = new ArrayList<Integer>(); // list of process id
			List<Integer>gratPriority = new ArrayList<Integer>(); // list of priority
			List<Integer>grantBurst = new ArrayList<Integer>(); // list of burstLength
			
			HashMap<Integer, Integer> iDS = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> bursts = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> priorities = new HashMap<Integer, Integer>();
			
			for(int i=0; i<processID.size(); i++) {
				// put to hashmap 
				iDS.put(i, processID.get(i));
				priorities.put(i, prio.get(i));
				bursts.put(i, burstTime.get(i));
				priorities.put(i, priorities.get(i));
				
				// add to the list 
				grantID.add(iDS.get(i));
				grantBurst.add(bursts.get(i));
				gratPriority.add(priorities.get(i));
			}

			boolean go=true;
			while(go) {
				int point=0;
				
				int firstNum=0;
				int pos=0;
				while(point<bursts.size()) {
					
					int countZero=0;
					if(quantum < bursts.get(point)) {
						firstNum = bursts.get(point)-quantum;
					}
					else {
//						firstNum=0;
						countZero++;
						
					}
					if(countZero==1) {
						pos++;
					}
					if(countZero<1) {
						bursts.replace(point, firstNum);
						grantID.add(iDS.get(point));
						gratPriority.add(priorities.get(point));
						grantBurst.add(bursts.get(point));
						
					}

					point++;
				}
				if(pos==bursts.size()) {
					go=false;
				}
			}

			// condition to bring which list can be returned
			if(choose.equals("ID")) {
				returnList= grantID;	
			}
			else if(choose.equals("burst")){
				returnList= grantBurst;
			}
			else if(choose.equals("priority")) {
				returnList= gratPriority;
			}
			return returnList;

		}
		// waiting time after apply Round Robin algorithm 
		public static List<Integer>waitingTimeRR(List<Integer>burstList, int quantum){
			List<Integer> waitingTimeRR = new ArrayList<Integer>();
			int waitTime = 0;
			waitingTimeRR.add(waitTime);
			for(int i=0; i<burstList.size()-1; i++ ) {
				if(burstList.get(i)<=quantum) {
					waitTime += burstList.get(i);
				}
				else {
					waitTime+=quantum;
				}
				
				waitingTimeRR.add(waitTime);
			}
			return waitingTimeRR;
		}
		// sort average waiting time of all algorithm 
		public static String sortAverageWaiting(double num1, String one, double num2, String two, double num3, String three){
			String outPut="Scheduling Algorithm\t|\t Average waiting time\n";
			List<Double> averages = new ArrayList<Double>();
			averages.add(num1);
			averages.add(num2);
			averages.add(num3);
			List<String> averagesName = new ArrayList<String>();
			averagesName.add(one);
			averagesName.add(two);
			averagesName.add(three);
			HashMap<Integer, String> result = new HashMap<Integer, String>();
			HashMap<Integer, Double> result1 = new HashMap<Integer, Double>();
			for(int n=0; n<averages.size(); n++) {
				result.put(n, averagesName.get(n));
				result1.put(n, averages.get(n));
			}
			Collections.sort(averages);
			
			for(int i=0; i<averages.size(); i++) {
				int countDub =0;
				for(int j=0; j<result1.size(); j++) {
					
					if(averages.get(i)==result1.get(j)){
						countDub++;
						Double key=result1.get(j);
						if(countDub>1) {
							j=result1.size();
							result1.remove(j);
						}
						outPut+=result.get(j) +"\t\t\t"+key+"\n"; 

					}
				}
				
			}
			return outPut;
		}
}
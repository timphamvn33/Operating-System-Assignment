// CS4345, Spring/2022, Assigment4, Thuong Pham & Maoting Zeng
import java.util.Random;
public class Synchronization_Pham_Maoting{ 
	public static void main(String args[]) { 
		CarControl carControl = new CarControl(); // creating the shared resource
		new RightCar(carControl); //right bound thread begins	
		new LeftCar(carControl); //left bound thread begins
	} 
}
// create the synchronized 
 class CarControl {
	 Random random = new Random();
	 int oddNum,evenNum ;
	boolean flag = false;
	// synchronized for left bound cars 
	synchronized void leftCarControl(int evenNum) throws InterruptedException {
		boolean exit =false;
		if(!flag) {
			try { 
				wait();
				Thread.sleep(random.nextInt(2000));
				System.out.println("Left-bound Car "+ evenNum + " is in the tunnel "); 
				exit =true;
				
				
			} catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			} 
		}
		if(!exit) {
			Thread.sleep(random.nextInt(1000));
			System.out.println("Left-bound Car "+ evenNum + " is in the tunnel ");
		}
		flag = true;
		this.evenNum=evenNum;
		Thread.sleep(random.nextInt(500));
		System.out.println("Left-bound Car "+ evenNum + " is exiting the tunnel");
		notify(); 	
	} 
	// synchronized for right bound cars
	synchronized void rightCarControl(int oddNum) throws InterruptedException { 
		boolean exit =false;
		if(flag){
			try { 
				wait();
				Thread.sleep(random.nextInt(500));
				System.out.println("Right-bound Car "+ oddNum + " is in the tunnel");
				exit =true;
			} catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			}	
		}
		if(!exit) {
			Thread.sleep(random.nextInt(1000));
			System.out.println("Right-bound Car "+ oddNum + " is in the tunnel ");
		}
		flag = false;
		this.oddNum=oddNum;
		Thread.sleep(random.nextInt(2000));
		System.out.println("Right-bound Car "+ oddNum + " is exiting the tunnel");
		notify(); 
	} 
}
//THREADS
class  RightCar implements Runnable { // thread class for right bound cars
	Random random = new Random();
	boolean go=true;
	CarControl carControl;
	int oddNum = 1;
	RightCar(CarControl carControl) { 
		this.carControl = carControl; 
		new Thread(this).start(); 
	} 
	public void run() { 
		while(go){
		      try {  	
		    	Thread.sleep(random.nextInt(1000));
		    	System.out.println("Right-bound Car "+ oddNum+" wants to enter the tunnel");
				carControl.rightCarControl(oddNum);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		      oddNum+=2; // making odd number
		} 
	} 
}
class LeftCar implements Runnable { // thread class for left bound cars
	Random random = new Random();
	boolean go=true;
	CarControl carControl;
	int evenNum = 0;
	LeftCar(CarControl carControl) { 
		this.carControl = carControl; 
		new Thread(this).start(); 
	}
	public void run() { 
		while(go){
			try {
				Thread.sleep(random.nextInt(500));
				System.out.println("Left-bound Car "+ evenNum+" wants to enter the tunnel");
				carControl.leftCarControl(evenNum);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			evenNum+=2; // making even number
		} 
	} 
}
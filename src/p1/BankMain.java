package p1;
import java.sql.*;
import java.util.Scanner;
public class BankMain extends BankDBHandler{
	
	 public static void main(String[] args) {
		int ch = 0;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("1. Create Account");
			System.out.println("2. Login Account");
			System.out.println("5. Exit");
			ch = Integer.parseInt(sc.nextLine());
			if(ch == 1) {
//				Create account
				System.out.println("Enter Unique username: ");
				String cname = sc.nextLine();
				System.out.println("Enter passcode (4 digit): ");
				int password = Integer.parseInt(sc.nextLine());
				System.out.println("Enter the balance: ");
				int balance = Integer.parseInt(sc.nextLine());
				if(BankDBHandler.createBankAccount(cname, password, balance)) {
					System.out.println("Account Created Successfully");
				}
				else {
					System.out.println("Error in creating account.");
				}
				
			}
			else if(ch == 2){
//			Login account
				System.out.println("Enter Unique username: ");
				String cname = sc.nextLine();
				System.out.println("Enter passcode (4 digit): ");
				int password = Integer.parseInt(sc.nextLine());
				if(BankDBHandler.loginBankAccount(cname, password)) {
					System.out.println("Login SuccessFull");
				}
				else {
					System.out.println("Login failed!");
				}
			}
			else if(ch != 5){
				System.out.println("Enter valid choice.");
			}
		}
		while(ch != 5);
	}
}
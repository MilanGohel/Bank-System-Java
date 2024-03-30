package p1;
import java.sql.*;
import java.util.Scanner;
public class BankDBHandler {
	static Scanner sc = new Scanner(System.in);
	static Connection con = DBConnection.getConnection();
	protected static boolean createBankAccount(String cname, int password, int balance) {
		try {

			if(cname == "" || password == 0 || balance == 0) {
				System.out.println("All fields are required");
				return false;
				
			}
			
			PreparedStatement ps = con.prepareStatement("insert into customer (cname,balance, password) value(?,?,?);");
					ps.setString(1, cname);
			ps.setInt(2, password);
			ps.setInt(3, balance);
			if(ps.executeUpdate() == 1) {
				return true;
			}
		}
		catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("Username already exists");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	protected static boolean loginBankAccount(String cname, int password) {
		
		try {
			if(cname == "" || password == 0) {
				System.out.println("All fields are required!");
				return false;
			}
			
			PreparedStatement ps = con.prepareStatement("Select * from customer where cname = ? and password = ?");
			ps.setString(1, cname);
			ps.setInt(2, password);
			
				
			ResultSet rs = ps.executeQuery();
//			if rs.next then user found
			
			if(rs.next()) {
				int ch; 
				int amount = 0;
				int senderAccount = rs.getInt(1);
				do {
					System.out.println("1. Transfer money: ");
					System.out.println("2. View Balance: ");
					System.out.println("5. Log Out");
					
					ch = Integer.parseInt(sc.nextLine());
					
					if(ch == 1) {
//						Transfer money
						System.out.println("Enter the Account No. Of Receiver: ");
						int receiverAccount = Integer.parseInt(sc.nextLine());
						System.out.println("Enter the amount: ");
						amount = Integer.parseInt(sc.nextLine());
						if(transferMoney(senderAccount, receiverAccount, amount)) {
							System.out.println("Transfer successfull");
						}
						else {
							System.out.println("Transfer has failed!");
						}
						
					}
					else if(ch == 2) {
//						view balance
						if(!BankDBHandler.getBalance(senderAccount)) {
							System.out.println("Error in balance");
						}
					}
					else if(ch != 5){
						System.out.println("Enter valid choice");
					}
				}while(ch != 5);
			}
			else {
				System.out.println("Invalid Credentials!");
				return false;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected static boolean getBalance(int acc) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM customer where ac_no = ?");
			ps.setInt(1, acc);
			ResultSet rs = ps.executeQuery();
			System.out.println("Account No."+ "\t"+"Name " + "\t"+ "Balance") ;
			while(rs.next()) {
				
				System.out.println(rs.getInt(1)+ "\t\t"+rs.getString(2) + "\t"+ rs.getInt("balance")) ;
				
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	protected static boolean transferMoney(int sender, int receiver, int amount) throws SQLException{
		if( amount == 0) {
			System.out.println("Enter valid amount");
			return false;
		}
		
		try {
			con.setAutoCommit(false);
			PreparedStatement send_ps = con.prepareStatement("SELECT * FROM customer where ac_no = ?");
			send_ps.setInt(1, sender);
			PreparedStatement rec_ps = con.prepareStatement("SELECT * FROM customer where ac_no = ?");
			rec_ps.setInt(1, receiver);
			
			ResultSet send_rs = send_ps.executeQuery();
		
			ResultSet rec_rs = rec_ps.executeQuery();
			
			
			if(send_rs.next() && rec_rs.next()) {
				if (send_rs.getInt("balance") < amount) {
                    System.out.println(
                        "Insufficient Balance!");
                    return false;
                }
			}
			else {
				System.out.println("Invalid receiver id");
				return false;
			}
			
			con.setSavepoint();
//			debit the amount
			
			
			PreparedStatement updateAmount = con.prepareStatement("UPDATE customer set balance = balance - ? where ac_no = ?");
			updateAmount.setInt(1, amount);
			updateAmount.setInt(2, sender);
			int cnt = updateAmount.executeUpdate();
		
			if(cnt == 1) {
				System.out.println("Amount is debited from the account!!");
			}
			else {
				return false;
			}
			con.setSavepoint();
			
		
//		credit amount
			
			PreparedStatement updateCredit = con.prepareStatement("UPDATE customer set balance = balance + ? where ac_no = ?");
			updateCredit.setInt(1, amount);
			updateCredit.setInt(2, receiver);
			cnt = updateCredit.executeUpdate();
		
			if(cnt == 1) {
				System.out.println("Amount is credited to receiver's account!1");
			}
			else {
				return false;
			}
			con.commit();
			return true;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}

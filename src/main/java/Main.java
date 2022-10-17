import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	public static Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);

		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/second_assignment", "root", "root");
		
		boolean flag =  true;

		while(flag) {
			
			
			System.out.println("Enter Your Username!!");

			String username = sc.next();

			System.out.println("Enter Your Password");

			String password = sc.next();

			PreparedStatement pstmt = conn.prepareStatement("select * from user where id = ? and password = ?");
			
			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
				LOGGER.info(username+ " Logged-In Successfully");
				
				pstmt = conn.prepareStatement("select * from product");
				
				rs = pstmt.executeQuery();
				System.out.println("Products Available in the warehouse Right Now..");
				while(rs.next()) {
					
					System.out.println(rs.getInt(1)+"   "+rs.getString(2)+"   "+rs.getString(3)+"    "+rs.getInt(4));
				}
				
				System.out.println("Initialize Checkout Process to Buy? Then Press 1 or 0 to Terminate");
				
				int checkoutInput = sc.nextInt();
				
				switch (checkoutInput) {
				case 1:
					System.out.println("Enter the product-Id to purchase the product");
					
					int productId = sc.nextInt();
					
					pstmt = conn.prepareStatement("select * from product where id = ?");
					
					pstmt.setInt(1, productId);
					
					rs = pstmt.executeQuery();
					
					rs.next();
					
					int stock = rs.getInt(4);
					
					if(stock < 10) {
						LOGGER.warn("Stock is gone below which is "+(stock-1));
					}
					
					pstmt = conn.prepareStatement("update product set unit_count = ? where id = ?");
					
					pstmt.setInt(1, stock-1);
					pstmt.setInt(2, productId);
					
					pstmt.execute();
					
					break;
					
				case 0:
					flag = false;
					break;
				}
			}
			else {
				LOGGER.info("Log-In failed by username as "+username);
			}
		}
		
		

	}
}


//Do an assignmnent for E-Commerce Application

//If any user is logged-in, I should get the username logged in the log file
//I will let the customer do shopping and if the stock goes down by a certain number, I should log the message that low-stock...

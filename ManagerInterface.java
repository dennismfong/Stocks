import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ManagerInterface {
	public ManagerInterface() {
		private int aid;
		private float balance; //market balance
		private String username;
	}

	public boolean login(String username, String password) {
		boolean success = true;
		// select * from Customer where Customer.username = username and Customer.password = password;
		// select * from MarketAccount where MarketAccount.aid in (select aid from Account where Account.username = username);
		this.aid = aid;
		this.balance = balance;
		this.username = username;
		return success;
	}

	public static void main(String[] args) {
		ManagerInterface managerifc = new ManagerInterface();

		boolean loggedIn = false;
		System.out.println("Welcome to the Trader Interface of StarsRUs");
		while (!loggedIn) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Please enter in your username");
				String username = reader.readLine();
				System.out.println("Please enter in your password");
				String password = reader.readLine();

				loggedIn = managerifc.login(username, password);
				if (!loggedIn) {
					System.out.println("Sorry, that user was not found, please try again");
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
	public static void generateMonthlyStatement(String username){
		//select cname, email from Customer where Customer.username = username;
		//select * from Transaction t, where t.aid IN( SELECT aid FROM Account a WHERE a.username = username;
		System.out.println("MONTHLY STATEMENT FOR: "+/*cname*/+" ("+/*email*/+")\n");
		for(Transaction t : transactionlist){
			System.out.println(t);
		}
	}

	public static void generateCustomerReport(String username){
		//select * from Account a where a.username = username and a.aid IN( select aid from StockAccount sa where sa.aid = a.aid) join 
		//select * from Account a where a.username = username and a.aid IN( select aid from MarketAccount ma where ma.aid = a.aid) join 
		System.out.println("CUSTOMER REPORT FOR: "+username+"\n");
		for(Account a : accountlist)
			System.out.println(a)
	}

	public static void addInterest(){
		//UPDATE
    //Account A,
    //MarketAccount MA
    //SET
    //A.balance = A.balance * MA.interest
    //WHERE
    //A.aid = MA.aid;

    System.out.println("INTEREST ADDED TO MARKET ACCOUNTS");
	}

	public static void deleteTransaction(){
		//DELETE FROM Transactions
		System.out.println("ALL TRANSACTIONS DELETED");
	}
}
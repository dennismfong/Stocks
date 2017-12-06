import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ManagerInterface {
	public ManagerInterface() {
		private User user;
	}

	public boolean login(String username, String password) {
		Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);

	  Statement statement = connection.createStatement();

	  String query = "select * from Customer where Customer.username = "+username+" and Customer.password = +"password;
	  ResultSet resultSet = statement.executeQuery(query);

	  if(resultSet.next())
			boolean success = true;
		else{
			return false;
		}
		this.user.setUsername(resultSet.getString(6));
		this.user.setName(resultSet.getString(1));
		
		String query = "select balance from MarketAccount where MarketAccount.aid in (select aid from Account where Account.username = "+username+")";
		ResultSet resultSet = statement.executeQuery(query);

		this.user.setBalance(resultSet.getFLoat(1));
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
	public void generateMonthlyStatement(String username){
		Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);

	  Statement statement = connection.createStatement();

	  String query = "select cname, email from Customer where Customer.username = "+username;
	  ResultSet resultSet = statement.executeQuery(query);
	  String cname = resultSet.getString(1); 
	  String email = resultSet.getString(2);
		
		String query = "select * from Transaction t, where t.aid IN( SELECT aid FROM Account a WHERE a.username = "+username+")";
	  ResultSet resultSet = connection.executeQuery(query);
	  
	  System.out.println("MONTHLY STATEMENT FOR: "+cname+" ("+email+")\n");
		while(resultSet.next()){
			System.out.println(resultSet.getString(1)+"|"+resultSet.getString(2)+"|"+resultSet.getDate(3)+"|"+resultSet.getString(4));
		}
		statement.close();
		connection.close();
	}

	public void generateCustomerReport(String username){
		Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);
	  Statement statement = connection.createStatement();

	  ResultSet resultSet = statement.executeQuery(query);
	 
		String query = "select * from Account a where a.username = "+username+" and a.aid IN( select aid from StockAccount sa where sa.aid = a.aid) join select * from Account a where a.username = "+username+" and a.aid IN( select aid from MarketAccount ma where ma.aid = a.aid)";
		System.out.println("CUSTOMER REPORT FOR: "+username+"\n");
		while(resultSet.next()){
			System.out.println(resultSet.getString(1)+"|"+resultSet.getInt(2)+"|"+resultSet.getString(3));
		}
		statement.close();
		connection.close();
	}

	public void addInterest(){
		//UPDATE Account A, MarketAccount MA
    //SET
    //A.balance = A.balance * MA.interest
    //WHERE
    //A.aid = MA.aid;
		Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);
	  Statement statement = connection.createStatement();
	  
	  String query = "select balance, interest from Account a JOIN MarketAccount ma ON ma.aid = a.aid"; 
	  ResultSet resultSet = statement.executeQuery(query);
	 	
	 	while(resultSet.next()){
	 		resultSet.updateFloat(1, resultSet.getFloat(1)*=resultSet.getFloat(2));
	 	}
		statement.close();
		connection.close();

    System.out.println("INTEREST ADDED TO MARKET ACCOUNTS");
	}

	public void deleteTransaction(){
		Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);
	  Statement statement = connection.createStatement();
	  
	  String query = "delete from Transaction";
	  ResultSet resultSet = statement.executeQuery(query);
		
		String query = "delete from MarketTransaction";
	  ResultSet resultSet = statement.executeQuery(query);
		
		String query = "delete from StockTransaction";
	  ResultSet resultSet = statement.executeQuery(query);

		statement.close();
		connection.close();

		System.out.println("ALL TRANSACTIONS DELETED");
	}
}
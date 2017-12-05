import java.io.InputStreamReader;
import java.io.BufferedReader;

public class TraderInterface {

	private String name;
	private int aid;
	private float balance; //market balance
	private String username;
	
	public TraderInterface() {
	}

	public void displayGreeting() {
		System.out.println("Welcome to the Trader Interface of StarsRUs");
		System.out.println("Issue commands using the number key associated with your request");
	}

	public boolean login(String username, String password) {
		boolean success = true;
		// Add in code to check if user is in the customer table
		this.name = "Test User"; // Update it with the cname column from query
		return success;
	}

	public boolean registerUser() {
		boolean registered = true; // Set to true for debugging purposes
		System.out.println("Welcome to the register portal of StarsRUs");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter in your desired username");
			String username = br.readLine();
//			Only works on CSIL and not IntelliJ
//			Console console = System.console();
//			String password = new String(console.readPassword("Enter in your desired password"));
			System.out.println("Enter in your desired password");
			String password = br.readLine();

			// Add code to create a new row in the Customer table

			return registered;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String[] args) {
		TraderInterface traderifc = new TraderInterface();

		boolean loggedIn = false;
		boolean exit = false;

		traderifc.displayGreeting();
		while (!exit) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Are you a registered user?");
				System.out.println("1.	yes");
				System.out.println("2.	no");
				System.out.println("3.	exit");

				String answer = reader.readLine();
				if ("2".equals(answer)) {
					if (!traderifc.registerUser()) {
						continue;
					}
				}
				else if ("3".equals(answer)) {
					break;
				}

				while (!loggedIn) {
					System.out.println("Please enter in your username or type register to make a new account");
					String username = reader.readLine();
					if ("register".equals(username.toLowerCase())) {
						break;
					}
					System.out.println("Please enter in your password");
					String password = reader.readLine();

					loggedIn = traderifc.login(username, password);
					if (!loggedIn) {
						System.out.println("Sorry, that user was not found, please try again");
					} else {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Start of application logic
			System.out.println("Welcome " + traderifc.name + " to your portal!");
			System.out.println("Issue commands using the number key associated with your request");

			System.out.println("\n\n"
							+ "\n1.     Deposit"
							+ "\n2.     Withdrawal"
							+ "\n3.     Buy"
							+ "\n4.     Sell"
							+ "\n5.     Get market account balance"
							+ "\n6.     Get stock account transaction history"
							+ "\n7.     Get current price of a stock and actor profile"
							+ "\n8.     Get specific movie information "
							+ "\n9.     Get top movies information"
							+ "\n10.    Get reviews for a movie"
							+ "\n\n"
			);
		}
	}

	public static void showBalance(){
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(HOST, USER, PWD);
		Statement statement = connection.createStatement();
		
		String query = "select * from Account WHERE Account.username = "+this.username+" AND Account.aid IN (select aid from MarketAccount)";
		ResultSet resultSet = statement.executeQuery(query);	
		System.out.println("CURRENT BALANCE: "+resultSet.getInt(1));
	}

	public static void depositBalance(int money){
		//UPDATE MarketAccount SET Balance = Balance + money WHERE aid = this.aid";
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(HOST, USER, PWD);
		Statement statement = connection.createStatement();
		
		String query = "select * from Account WHERE Account.username = "+this.username+" AND Account.aid IN (select aid from MarketAccount)";
		ResultSet resultSet = statement.executeQuery(query);
		resultSet.absolute(1);
		resultSet.updateInt(2, resultSet.getInt(1)+money);

		String query = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, "Deposit", date, this.aid)";
		ResultSet resultSet = statement.executeQuery(query);

		String query = "INSERT INTO MarketTransaction(tid, amount) VALUES (####, "+money+")";
		ResultSet resultSet = statement.executeQuery(query);

		statement.close();
		connection.close();
    System.out.println("DEPOSITED "+ money+ " DOLLARS.");
	}

	public static void withdrawBalance(int money){
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(HOST, USER, PWD);
		Statement statement = connection.createStatement();
		
		String query = "select * from Account WHERE Account.username = "+this.username+" AND Account.aid IN (select aid from MarketAccount)";
		ResultSet resultSet = statement.executeQuery(query);
		
		if(balance < money)
			System.out.println("CANNOT WITHDRAW MONEY: INSUFFICIENT BALANCE");
		else{

			resultSet.absolute(1);
			resultSet.updateInt(2, resultSet.getInt(1)-money);

			String query = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, "Withdrawal", date, this.aid)";
			ResultSet resultSet = statement.executeQuery(query);

			String query = "INSERT INTO MarketTransaction(tid, amount) VALUES (####, "+money+")";
			ResultSet resultSet = statement.executeQuery(query);

			//UPDATE MarketAccount SET Balance = Balance - money WHERe aid = this.aid;
	
			//INSERT INTO Transaction(tid, type, date, aid) 
			//VALUES (####, "Withdrawal", date, this.aid)
	
			//INSERT INTO MarketTransaction(tid, amount) 
			//VALUES (####, money);
			System.out.println("WITHDREW "+ balance + " DOLLARS.");
		}
		statement.close();
    connection.close();
	}
	public static void listStockDetails(String stock){
	  Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);

	  Statement statement = connection.createStatement();

	  String query = "select currentPrice from Stock where Stock.symbol = "+stock;
	  ResultSet resultSet = statement.executeQuery(query);
	  float currentPrice = resultSet.getFloat(1);
		System.out.println("CURRENT PRICE FOR STOCK "+stock+": "+currentPrice);
		
		//select * from ActorDirector where ActorDirector.symbol = stock;
		String query = "select * from ActorDirector where ActorDirector.symbol = "+stock;
		ResultSet resultSet = statement.executeQuery(query);
		System.out.println("STOCK DETAILS");
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int numColumns = rsmd.getColumnCount();
		for (int i = 1; i <= numColumns; i++) {
			System.out.println(rsmd.getColumnName(i) + " " + resultSet.getString(i));
			System.out.print(", ");
		}
	  statement.close();
	  connection.close();
	}

	public static void buyStock(String key, int amount){
		Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);

	  Statement statement = connection.createStatement();

	  String query = "select currentPrice, numStocks from Stock where Stock.key = "+key;
	  ResultSet resultSet = statement.executeQuery(query);
	  float currentPrice = resultSet.getFloat(1);
		//select currentPrice, numStocks from Stock where Stock.key = key;
		if(amount > resultSet.getInt(2)){
			System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH STOCKS REMAINING");
		}
		else if(this.balance < amount*resultSet.getFloat(1)+20){
			System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH MONEY IN BALANCE");
		}
		else{
			resultSet.updateInt(2, resultSet.getInt(2)-amount);
			this.balance -= amount*currentPrice+20;

			String query = "select * from Account WHERE Account.username = "+this.username+" AND Account.aid IN (select aid from MarketAccount)";
			ResultSet resultSet = statement.executeQuery(query);

			resultSet.absolute(1);
			resultSet.updateInt(2, this.balance);

			String query = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, 'Stock purchase', date, this.aid)";
			ResultSet resultSet = statement.executeQuery(query);

			String query = "INSERT INTO StockTransaction(tid, quantity, price, symbol) VALUES ####, "+amount+", "+amount*currentPrice+", "+key+")";
			ResultSet resultSet = statement.executeQuery(query);
			//UPDATE MarketAccount SET Balance = balance WHERE aid = this.aid;
			//UPDATE Stock SET numStocks = numStocks - amount WHERE symbol = key;

			//INSERT INTO Transactions(tid, type, date, aid)
			//VALUES (####, "Stock purchase", date, this.aid)
			
			//INSERT INTO StockTransaction(tid, quantity, price, symbol) 
			//VALUES (####, amount, amount*currentPrice, key);	
		}
	}

	public static void sellStock(String key, int amount, float ogprice){
		Class.forName("com.mysql.jdbc.Driver");
	  Connection connection = DriverManager.getConnection(HOST, USER, PWD);

	  Statement statement = connection.createStatement();

	  String query = "select currentPrice, numStocks from Stock where Stock.key = "+key;
	  ResultSet resultSet = statement.executeQuery(query);
	  float currentPrice = resultSet.getFloat(1);
		//select currentPrice, numStocks from Stock where Stock.key = key;
		resultSet.updateInt(2, resultSet.getInt(2)+amount);
		this.balance += amount*currentPrice-20;


		String query = "select * from Account WHERE Account.username = "+this.username+" AND Account.aid IN (select aid from MarketAccount)";
		ResultSet resultSet = statement.executeQuery(query);

		resultSet.absolute(1);
		resultSet.updateInt(2, this.balance);


		String query = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, 'Stock sold', date, this.aid)";
		ResultSet resultSet = statement.executeQuery(query);

		String query = "INSERT INTO StockTransaction(tid, quantity, price, symbol) VALUES ####, "+amount+", "+amount*currentPrice+", "+key+")";
		ResultSet resultSet = statement.executeQuery(query);
	//select currentPrice, numStocks from Stock where Stock.key = key;
	//UPDATE MarketAccount SET Balance = balance WHERE aid = this.aid;
	//UPDATE Stock SET numStocks = numStocks + amount WHERE symbol = key;

	//INSERT INTO Transactions(tid, type, date, aid)
	//VALUES (####, "Stock sold", date, this.aid)
			
	//INSERT INTO StockTransaction(tid, quantity, price, symbol) 
	//VALUES (####, amount, amount*currentPrice, key);	
	}

	public static void displayMovieDetails(String movieTitle){

	}
}

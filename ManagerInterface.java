import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class ManagerInterface {
	private User user;
	private String HOST = Config.host;
	private String USER = Config.user;
	private String PWD = Config.pwd;

	public ManagerInterface() {
		user = new User();
	}

	public boolean login(String username, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(HOST, USER, PWD);

			Statement statement = connection.createStatement();

			String query = "select * from Customer where Customer.username = \"" + username + "\" and Customer.password = \"" + password + "\"";
			ResultSet resultSet = statement.executeQuery(query);

			boolean success = false;

			if (resultSet.next())
				success = true;
			else {
				return false;
			}
			this.user.setUsername(resultSet.getString(6));
			this.user.setName(resultSet.getString(1));

			String query_2 = "select balance from MarketAccount where MarketAccount.aid in (select aid from Account where Account.username = \"" + username + "\")";
			ResultSet resultSet_2 = statement.executeQuery(query);

			this.user.setBalance(resultSet.getFloat(1));
			statement.close();
			connection.close();

			return success;
		} catch (Exception e){
			System.err.println(e);
		}
		return false;
	}

	public static void main(String[] args) {
		ManagerInterface managerifc = new ManagerInterface();

		boolean loggedIn = false;
		System.out.println("Welcome to the Manager Interface of StarsRUs");
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
				else {
          managerifc.user.setUsername(username);
          managerifc.user.setInfo(username);
        }
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		boolean exitPortal = false;
		while (!exitPortal) {
			// Start of application logic
			System.out.println("Welcome " + managerifc.user.getName() + " to your portal!");
			System.out.println("Issue commands using the number key associated with your request");
			System.out.println("\n\n"
							+ "\n1.     Add interest"
							+ "\n2.     Generate monthly statement"
							+ "\n3.     List active customers"
							+ "\n4.     Generate Government Drug & Tax Evasion Report (DTER)"
							+ "\n5.     Customer Report"
							+ "\n6.     Delete Transactions"
							+ "\n7.    exit"
							+ "\n\n"
			);

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
        int answer = Integer.parseInt(reader.readLine());
        switch (answer) {
          case 1:
            break;
          case 2:
            break;
          case 3:
            break;
          case 4:
            break;
          case 5:
            break;
          case 7:
            exitPortal = true;
            break;
        }
      } catch (Exception e) {
				System.err.println(e);
			}
		}
	}
	public void generateMonthlyStatement(String username){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(HOST, USER, PWD);

			Statement statement = connection.createStatement();

			String query = "select cname, email from Customer where Customer.username = \"" + username + "\"";
			ResultSet resultSet = statement.executeQuery(query);
			String cname = resultSet.getString(1);
			String email = resultSet.getString(2);

			String query_2 = "select * from Transaction t, where t.aid IN( SELECT aid FROM Account a WHERE a.username = \"" + username + "\")";
			ResultSet resultSet_2 = statement.executeQuery(query);

			System.out.println("MONTHLY STATEMENT FOR: " + cname + " (" + email + ")\n");
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1) + "|" + resultSet.getString(2) + "|" + resultSet.getDate(3) + "|" + resultSet.getString(4));
			}
			statement.close();
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void generateCustomerReport(String username){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(HOST, USER, PWD);
			Statement statement = connection.createStatement();

			String query = "select * from Account a where a.username = \"" + username + "\" and a.aid IN( select aid from StockAccount sa where sa.aid = a.aid) join select * from Account a where a.username = \"" + username + "\" and a.aid IN( select aid from MarketAccount ma where ma.aid = a.aid)";
			ResultSet resultSet = statement.executeQuery(query);
			System.out.println("CUSTOMER REPORT FOR: " + username + "\n");
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1) + "|" + resultSet.getInt(2) + "|" + resultSet.getString(3));
			}
			statement.close();
			connection.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void addInterest(){
		try {
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

			while (resultSet.next()) {
				resultSet.updateFloat(1, resultSet.getFloat(1) * (1+resultSet.getFloat(2)));
				resultSet.updateRow();
			}
			statement.close();
			connection.close();

			System.out.println("INTEREST ADDED TO MARKET ACCOUNTS");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void deleteTransaction(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(HOST, USER, PWD);
			Statement statement = connection.createStatement();

			String query = "delete from Transaction";
			ResultSet resultSet = statement.executeQuery(query);

			String query_2 = "delete from MarketTransaction";
			ResultSet resultSet_2 = statement.executeQuery(query);

			String query_3 = "delete from StockTransaction";
			ResultSet resultSet_4 = statement.executeQuery(query);

			statement.close();
			connection.close();

			System.out.println("ALL TRANSACTIONS DELETED");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
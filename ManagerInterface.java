import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class ManagerInterface {
	private User user;
	private String HOST = Config.host;
	private String USER = Config.user;
	private String PWD = Config.pwd;
	private SystemManager systemManager;

	public ManagerInterface() {
    user = new User();
    systemManager = new SystemManager();
  }

	public boolean login(String username, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(HOST, USER, PWD);

			Statement statement = connection.createStatement();
			Statement statement_2 = connection.createStatement();

			String query = "select * from Customer where Customer.username = \"" + username + "\" and Customer.password = \"" + password +
              "\" and Customer.isManager = 1";
			ResultSet resultSet = statement.executeQuery(query);

			boolean success = false;

      if (resultSet.isBeforeFirst()) {
        // Matching row in the database
        success = true;
        resultSet.next();
        this.user.setUsername(resultSet.getString(6));
        this.user.setName(resultSet.getString(1));
      }
      else {
        success = false;
      }

			statement.close();
			statement_2.close();
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
		System.out.println("\n\nWelcome to the Manager Interface of StarsRUs");
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
			System.out.println("\n\nWelcome " + managerifc.user.getName() + " to your portal!");
			System.out.println("Issue commands using the number key associated with your request");
			System.out.println("\n\n"
							+ "\n1.     Add interest"
							+ "\n2.     Generate monthly statement"
							+ "\n3.     List active customers"
							+ "\n4.     Generate Government Drug & Tax Evasion Report (DTER)"
							+ "\n5.     Customer Report"
							+ "\n6.     Delete Transactions"
							+ "\n7.     exit"
							+ "\n\n"
			);

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
        int answer = Integer.parseInt(reader.readLine());
        switch (answer) {
          case 1:
            System.out.println("\n");
            break;
          case 2:
            System.out.println("\nEnter username of customer");
            String username = reader.readLine();
            managerifc.generateMonthlyStatement(username);
            break;
          case 3:
            break;
          case 4:
            break;
          case 5:
            System.out.println("\nEnter username of customer");
            String username2 = reader.readLine();
            managerifc.generateCustomerReport(username2);
            break;
          case 6:
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

			String query = "select cname, email, taxId from Customer where Customer.username = \"" + username + "\"";
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
			String cname = resultSet.getString(1);
			String email = resultSet.getString(2);
			int taxId = resultSet.getInt(3);

      String query_2 = "select * from Transaction t where t.aid in (select aid from Account a where a.taxId = ?) "
              + "and MONTH(t.date) = MONTH(?) and YEAR(t.date) = YEAR(?) and DAY(t.date) <= DAY(?)";

      java.util.Date currDate = systemManager.getDate();
      java.sql.Date dateDB = new java.sql.Date(currDate.getTime());
      PreparedStatement preparedStatement = connection.prepareStatement(query_2);
      preparedStatement.setInt(1, taxId);
      preparedStatement.setDate(2, dateDB);
      preparedStatement.setDate(3, dateDB);
      preparedStatement.setDate(4, dateDB);
      ResultSet resultSet_2 = preparedStatement.executeQuery();

      resultSet_2.next();
      int aid = resultSet_2.getInt(4);
      resultSet_2.beforeFirst();

      Calendar c = Calendar.getInstance();
      c.setTime(currDate);
      c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
      java.util.Date temp = c.getTime();
      java.sql.Date firstOfMonth = new java.sql.Date(temp.getTime());
      c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
      temp = c.getTime();
      java.sql.Date lastOfMonth = new java.sql.Date(temp.getTime());

      String query_3 = "select balance from BalanceHistory where aid = ? and (date = ? or date = ?)";
      PreparedStatement dateStatement = connection.prepareStatement(query_3);
      dateStatement.setInt(1, aid);
      dateStatement.setDate(2, firstOfMonth);
      dateStatement.setDate(3, lastOfMonth);
      ResultSet resultSet_3 = dateStatement.executeQuery();
      resultSet_3.next();
      double startBalance = resultSet_3.getDouble(1);
      resultSet_3.next();
      double endBalance = resultSet_3.getDouble(1);

      // Getting the net earnings and net losses
      String transactionQuery = "select * from Transaction t join StockTransaction s on t.tid = s.tid where" +
              " date >= ? and date <= ?";
      PreparedStatement netStatement = connection.prepareStatement(transactionQuery);
      netStatement.setDate(1, firstOfMonth);
      netStatement.setDate(2, lastOfMonth);
      ResultSet resultSet_4 = netStatement.executeQuery();
      double earnings = 0;
      double losses = 0;
      double interest = 0;
      while (resultSet_4.next()) {
        if (resultSet_4.getString(2).toLowerCase().contains("stock bought")) {
          losses += resultSet_4.getDouble(9);
        }
        else if (resultSet_4.getString(2).toLowerCase().contains("stock sold")) {
          earnings += resultSet_4.getDouble(9);
        }
        else if (resultSet_4.getString(2).toLowerCase().contains("interest")) {
          interest += resultSet_4.getDouble(9);
        }
      }

			System.out.println("MONTHLY STATEMENT FOR: " + cname + " (" + email + ")\n");
      System.out.println("Starting Balance:   " + startBalance);
      System.out.println("End Balance:        " + endBalance);
      System.out.println("Total earnings:     " + earnings + interest);
      System.out.println("  from stocks: "      + earnings);
      System.out.println("  from interest: "    + interest);
      System.out.println("Total losses:       " + losses + "\n");
      while (resultSet_2.next()) {
				System.out.println(resultSet_2.getString(1) + " | " + resultSet_2.getString(2) + " | " + resultSet_2.getDate(3) + " | " + resultSet_2.getString(4));
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

  //input is the market account after getting it from the user
//	public void addInterest(int aid) {
//	  try {
//      java.util.Date currDate = systemManager.getDate();
//      java.sql.Date dateDB = new java.sql.Date(currDate.getTime());
//	    Class.forName("com.mysql.jdbc.Driver");
//	    Connection connection = DriverManager.getConnection(HOST, USER, PWD);
//	    String query = "select * from Transaction t left join StockTransaction s on t.tid=s.tid left join MarketTransaction m on t.tid=m.tid " +
//              "where MONTH(t.date) = MONTH(?) and YEAR(t.date) = YEAR(?) order by date";
//	    PreparedStatement preparedStatement = connection.prepareStatement(query);
//	    preparedStatement.setDate(1, dateDB);
//	    preparedStatement.setDate(2, dateDB);
//      ResultSet resultSet = preparedStatement.executeQuery();
//
//      Calendar c = Calendar.getInstance();
//      c.setTime(currDate);
//      c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
//      java.util.Date temp = c.getTime();
//      java.sql.Date firstOfMonth = new java.sql.Date(temp.getTime());
//      query = "select balance from BalanceHistory where aid = ? and date = ?";
//      PreparedStatement preparedStatement1 = connection.prepareStatement(query);
//      preparedStatement1.setInt(1, aid);
//      preparedStatement1.setDate(2, firstOfMonth);
//      ResultSet resultSet1 = preparedStatement1.executeQuery();
//      resultSet1.next();
//
//      double dailyBalance = resultSet1.getDouble(3); // set initial daily balance to beginning of month
//      Map<Integer, Double> balancePeriods = new HashMap<Integer, Double>(); // Maps num days to balance
//      java.util.Date resultDate = firstOfMonth;
//      int numDays = 0;
//      int tempDays = 0;
//
//      while (resultSet.next()) {
//        resultDate = resultSet.getDate(3);
//        numDays = (int)(resultDate.getTime() - firstOfMonth.getTime()); // Get number of days between current date and first of month
//        if (resultSet.getDate(3).equals(resultDate)) { // is a repeat
//          continue;
//        }
//        else {
//          balancePeriods.put(tempDays, dailyBalance);
//          tempDays = numDays;
//        }
//
//        String type = resultSet.getString(2).toLowerCase();
//        double currentTransaction = 0.0;
//        if (type.contains("deposit")) {
//          currentTransaction = resultSet.getDouble(11);
//          dailyBalance += currentTransaction;
//        }
//        else if (type.contains("withdrawal")) {
//          currentTransaction = resultSet.getDouble(11);
//          dailyBalance -= currentTransaction;
//        }
//        else if (type.contains("stock sold")) {
//          currentTransaction = resultSet.getDouble(9);
//          dailyBalance += currentTransaction;
//        }
//        else if (type.contains("stock bought")) {
//          currentTransaction = resultSet.getDouble(9);
//          dailyBalance -= currentTransaction;
//        }
//
//      }
//    } catch (Exception e) {
//	    System.err.println(e);
//    }
//  }

  public void addAllInterest() {
	  
  }

  public void addInterest(int aid) {
	  try {
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(HOST, USER, PWD);

      java.util.Date currDate = systemManager.getDate();
      java.sql.Date dateDB = new java.sql.Date(currDate.getTime());
      String query = "select balance from BalanceHistory where aid = ? and MONTH(t.date) = MONTH(?) and YEAR(t.date) = YEAR(?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, aid);
      preparedStatement.setDate(2, dateDB);
      preparedStatement.setDate(2, dateDB);
      ResultSet resultSet = preparedStatement.executeQuery();

      double sum = 0;
      Calendar c = Calendar.getInstance();
      c.setTime(currDate);
      double days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
      while (resultSet.next()) {
        sum += resultSet.getDouble(1);
      }

      query = "select balance, taxId from Account where aid = ?";
      PreparedStatement lastBalance = connection.prepareStatement(query);
      ResultSet balanceSet = lastBalance.executeQuery();
      balanceSet.next();
      double lastDayBalance = balanceSet.getDouble(1);
      int taxId = balanceSet.getInt(2);
      sum += lastDayBalance;
      double dailyAverage = sum/days;
      double interest = dailyAverage * 0.03;

      // Create record in the balance history
      query = "insert into BalanceHistory values(?,?,?)";
      PreparedStatement insert = connection.prepareStatement(query);
      insert.setInt(1, aid);
      insert.setDate(2, dateDB);
      insert.setDouble(3, lastDayBalance);
      insert.executeUpdate();

      // Update the user's balance in account
      User currentUser = new User();
      currentUser.setTaxId(taxId);
      currentUser.setBalance(currentUser.getBalance() + interest);

      // Insert record into Transaction
      query = "select COUNT(*) from Transaction";
      Statement statement = connection.createStatement();
      resultSet = statement.executeQuery(query);
      resultSet.next();
      int tid = resultSet.getInt(1) + 1;

      String transactionString = "INSERT INTO Transaction(tid, type, date, aid) VALUES " +
              "(?, ?, ?, ?)";

      preparedStatement = connection.prepareStatement(transactionString);
      preparedStatement.setInt(1, tid);
      preparedStatement.setString(2, "Accrue interest");
      preparedStatement.setDate(3, dateDB);
      preparedStatement.setInt(4, aid);
      preparedStatement.executeUpdate();

      // insert record into Market Transasction
      transactionString = "INSERT INTO MarketTransaction(tid, amount) VALUES " +
              "(?, ?)";

      preparedStatement = connection.prepareStatement(transactionString);
      preparedStatement.setInt(1, tid);
      preparedStatement.setDouble(2, interest);
      preparedStatement.executeUpdate();

      statement.close();
      preparedStatement.close();
      lastBalance.close();
      insert.close();
      connection.close();

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
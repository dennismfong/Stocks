import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

public class TraderInterface {
/*
  private String name;
	private int aid;
	private float balance; //market balance
	private String username;*/

  private User user;
  private String HOST = Config.host;
  private String USER = Config.user;
  private String PWD = Config.pwd;

  public TraderInterface() {
    user = new User();
  }

  public void displayGreeting() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();

      String query = "select * from MarketDate";
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.absolute(1);
      Date date = resultSet.getDate(1);

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String dateStr = sdf.format(date);

      System.out.println("Welcome to the Trader Interface of StarsRUs");
      System.out.println("Today's date is " + dateStr);
      System.out.println("Issue commands using the number key associated with your request");
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public boolean login(String username, String password) {
    try {
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();
      String query = "select * from Customer where username = \"" + username+ "\""
              + " AND " + "password = \"" + password + "\"";
      ResultSet resultSet = statement.executeQuery(query);
      if (resultSet.isBeforeFirst()) {
        // Matching row in the database
        return true;
      }
      else {
        return false;
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return false;
  }

  public boolean registerUser() {
    System.out.println("Welcome to the register portal of StarsRUs");
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      boolean unique = false;
      String username = "";
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      while (!unique) {
        System.out.println("Enter in your desired username");
        username = br.readLine();
        Statement statement = connection.createStatement();
        String query = "select * from Customer where username = \"" + username + "\"";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.isBeforeFirst()) {
          System.out.println("Username already exists, please select a new username");
          continue;
        }
        else {
          break;
        }
      }
      System.out.println("Enter in your desired password");
      String password = br.readLine();
      System.out.println("Enter in your full name");
      String name = br.readLine();
      System.out.println("Enter in your residential address");
      String address = br.readLine();
      System.out.println("Enter in your state's two letter code (ie. CA, AL)");
      String state = br.readLine();
      System.out.println("Enter in your phone number (ie. 1234567890)");
      String phone = br.readLine();
      System.out.println("Enter in your email address (name@domain.suffix)");
      String email = br.readLine();
      System.out.println("Enter in your tax ID");
      int taxId = Integer.parseInt(br.readLine());
      System.out.println("Enter in your social security number (123-45-6789)");
      String ssn = br.readLine();

      // Add code to create a new row in the Customer table

      String registerString = "insert into Customer"
              + "(cname, state, phoneNum, email, taxId, username, password, SSN, address) VALUES"
              + "(?,?,?,?,?,?,?,?,?)";

      PreparedStatement preparedStatement = connection.prepareStatement(registerString);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, state);
      preparedStatement.setString(3, phone);
      preparedStatement.setString(4, email);
      preparedStatement.setInt(5, taxId);
      preparedStatement.setString(6, username);
      preparedStatement.setString(7, password);
      preparedStatement.setString(8, ssn);
      preparedStatement.setString(9, address);
      preparedStatement.executeUpdate();

      return true;

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
        System.out.println("\n\nLOGIN PORTAL");
        System.out.println("Are you a registered user?");
        System.out.println("1.	yes");
        System.out.println("2.	no");
        System.out.println("3.	exit");

        String answer = reader.readLine();
        if ("2".equals(answer)) {
          if (!traderifc.registerUser()) {
            continue;
          }
        } else if ("3".equals(answer)) {
          loggedIn = false;
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
            traderifc.user.setUsername(username);
            traderifc.user.setInfo(username);
            break;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      boolean exitPortal = false;
      while (!exitPortal) {
        // Start of application logic
        System.out.println("Welcome " + traderifc.user.getName() + " to your portal!");
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
                + "\n11.    exit"
                + "\n\n"
        );

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
          int answer = Integer.parseInt(reader.readLine());
          if(!traderifc.checkForMarketAccount(traderifc.user.getTaxId()))
            ;
          else {
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
                traderifc.showBalance();
                break;
              case 6:
                break;
              case 7:
                break;
              case 8:
                break;
              case 9:
                break;
              case 10:
                break;
              case 11:
                loggedIn = false;
                exitPortal = true;
                break;
              default:
                System.out.println("Invalid input");
                break;
            }
          }
        } catch (Exception e) {
          System.err.println(e);
        }
      }
    }
  }

  public boolean checkForMarketAccount(int taxId) {
    try {
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();
      String query = "select * from MarketAccount where aid in (select a.aid from Account a where a.taxId = " 
        + taxId + ")";
      ResultSet resultSet = statement.executeQuery(query);
      if (resultSet.isBeforeFirst()) {
        resultSet.next();
        int aid = resultSet.getInt(2);
        statement = connection.createStatement();
        query = "select balance from Account where aid = " + aid;
        resultSet = statement.executeQuery(query);
        resultSet.next();
        this.user.setBalance(resultSet.getDouble(1));
        statement.close();
        connection.close();
        // Matching row in the database
        return true;
      }
      else {
        statement.close();
        connection.close();
        System.out.println("It appears you do not have a market account in our database. Would you like to open one? (You must deposit at least 1000 dollars when opening a new market account)");
        System.out.println("\n\n"
                + "\n1.     Yes"
                + "\n2.     No"
                + "\n\n"
        );    
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int answer = Integer.parseInt(reader.readLine());
        switch (answer) {
            case 1:
              makeNewAccount(taxId,0);
              return true;
            case 2:
              break;
        }
        return false;
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return false;
  }

  public void makeNewAccount(int taxId, int mode) { //0 = market, 1 = stock
    while(true){
      try {
        int aid = ThreadLocalRandom.current().nextInt(0, 10001);
        Connection connection = DriverManager.getConnection(HOST, USER, PWD);
        Statement statement = connection.createStatement();
        String query = "select * from Account where aid = " + aid;
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.isBeforeFirst()) {
          ;  
        }
        else {
          String registerString = "insert into Account"
            + "(balance, aid, taxId) VALUES "
            + "(?,?,?)";

          PreparedStatement preparedStatement = connection.prepareStatement(registerString);
          preparedStatement.setDouble(1, 1000);
          preparedStatement.setInt(2, aid);
          preparedStatement.setInt(3, taxId);
          preparedStatement.executeUpdate();

          registerString = "insert into MarketAccount"
            + "(interest, aid) VALUES "
            + "(?,?)";

          preparedStatement = connection.prepareStatement(registerString);
          preparedStatement.setDouble(1, 0.03);
          preparedStatement.setInt(2, aid);
          preparedStatement.executeUpdate();
          statement.close();
          connection.close();
          return;
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  public void showBalance() {
    try {
      System.out.println("CURRENT BALANCE: " + this.user.getBalance());
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void depositBalance(int money) {
    //UPDATE MarketAccount SET Balance = Balance + money WHERE aid = this.aid";
    this.user.setBalance(this.user.getBalance()+money);

    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();

      String query = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, \" Deposit \", date, this.aid)";
      statement.executeUpdate(query);

      String query_2 = "INSERT INTO MarketTransaction(tid, amount) VALUES (####, " + money + ")";
      statement.executeUpdate(query);

      statement.close();
      connection.close();
      System.out.println("DEPOSITED " + money + " DOLLARS.");
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void withdrawBalance(int money) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();

      if (this.user.getBalance() < money)
        System.out.println("CANNOT WITHDRAW MONEY: INSUFFICIENT BALANCE");
      else {
        this.user.setBalance(-1 * money);

        String query = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, \" Withdrawal \", date, this.aid)";
        statement.executeUpdate(query);

        String query_2 = "INSERT INTO MarketTransaction(tid, amount) VALUES (####, " + money + ")";
        statement.executeUpdate(query);

        //UPDATE MarketAccount SET Balance = Balance - money WHERe aid = this.aid;

        //INSERT INTO Transaction(tid, type, date, aid)
        //VALUES (####, "Withdrawal", date, this.aid)

        //INSERT INTO MarketTransaction(tid, amount)
        //VALUES (####, money);
        System.out.println("WITHDREW " + money + " DOLLARS.");
      }
      statement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void listStockDetails(String stock) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);

      Statement statement = connection.createStatement();

      String query = "select currentPrice from Stock where Stock.symbol = " + stock;
      ResultSet resultSet = statement.executeQuery(query);
      float currentPrice = resultSet.getFloat(1);
      System.out.println("CURRENT PRICE FOR STOCK " + stock + ": " + currentPrice);

      //select * from ActorDirector where ActorDirector.symbol = stock;
      String query_2 = "select * from ActorDirector where ActorDirector.symbol = " + stock;
      ResultSet resultSet_2 = statement.executeQuery(query);
      System.out.println("STOCK DETAILS");
      ResultSetMetaData rsmd = resultSet.getMetaData();
      int numColumns = rsmd.getColumnCount();
      for (int i = 1; i <= numColumns; i++) {
        System.out.println(rsmd.getColumnName(i) + " " + resultSet.getString(i));
        System.out.print(", ");
      }
      statement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void buyStock(String key, int amount) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);

      Statement statement = connection.createStatement();

      String query = "select currentPrice, numStocks from Stock where Stock.key = " + key;
      ResultSet resultSet = statement.executeQuery(query);
      float currentPrice = resultSet.getFloat(1);
      //select currentPrice, numStocks from Stock where Stock.key = key;
      if (amount > resultSet.getInt(2)) {
        System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH STOCKS REMAINING");
      } else if (this.user.getBalance() < amount * resultSet.getFloat(1) + 20) {
        System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH MONEY IN BALANCE");
      } else {
        resultSet.updateInt(2, resultSet.getInt(2) - amount);
        this.user.setBalance((amount * resultSet.getFloat(1) + 20) * -1);

        String query_2 = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, 'Stock purchase', date, this.aid)";
        statement.executeUpdate(query);

        String query_3 = "INSERT INTO StockTransaction(tid, quantity, price, symbol) VALUES ####, " + amount + ", " + amount * currentPrice + ", " + key + ")";
        statement.executeUpdate(query);
        //UPDATE MarketAccount SET Balance = balance WHERE aid = this.aid;
        //UPDATE Stock SET numStocks = numStocks - amount WHERE symbol = key;

        //INSERT INTO Transactions(tid, type, date, aid)
        //VALUES (####, "Stock purchase", date, this.aid)

        //INSERT INTO StockTransaction(tid, quantity, price, symbol)
        //VALUES (####, amount, amount*currentPrice, key);
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void sellStock(String key, int amount, float ogprice) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);

      Statement statement = connection.createStatement();

      String query = "select currentPrice, numStocks from Stock where Stock.key = " + key;
      ResultSet resultSet = statement.executeQuery(query);
      float currentPrice = resultSet.getFloat(1);
      //select currentPrice, numStocks from Stock where Stock.key = key;
      resultSet.updateInt(2, resultSet.getInt(2) + amount);

      this.user.setBalance((amount * resultSet.getFloat(1) + 20) * -1);

      String query_2 = "INSERT INTO Transaction(tid, type, date, aid) VALUES (####, 'Stock sold', date, this.aid)";
      statement.executeUpdate(query);

      String query_3 = "INSERT INTO StockTransaction(tid, quantity, price, symbol) VALUES ####, " + amount + ", " + amount * currentPrice + ", " + key + ")";
      statement.executeUpdate(query);
      //select currentPrice, numStocks from Stock where Stock.key = key;
      //UPDATE MarketAccount SET Balance = balance WHERE aid = this.aid;
      //UPDATE Stock SET numStocks = numStocks + amount WHERE symbol = key;

      //INSERT INTO Transactions(tid, type, date, aid)
      //VALUES (####, "Stock sold", date, this.aid)

      //INSERT INTO StockTransaction(tid, quantity, price, symbol)
      //VALUES (####, amount, amount*currentPrice, key);
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void displayMovieDetails(String movieTitle) {

  }
}

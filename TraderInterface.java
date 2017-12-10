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

  private String MOVIE = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/moviesDB";

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
      statement.close();
      connection.close();
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
        statement.close();
        connection.close();
        return true;
      }
      else {
        statement.close();
        connection.close();
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

      preparedStatement.close();
      connection.close();
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
                traderifc.depositBalance();
                break;
              case 2:
                traderifc.withdrawBalance();
                break;
              case 3:
                traderifc.buyStock();
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
          preparedStatement.close();
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
      System.out.printf("CURRENT BALANCE: %.2f \n", this.user.getBalance());
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void depositBalance() {

    try {
      System.out.println("How much money do you want to deposit?");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      double money = Double.parseDouble(reader.readLine());
      //UPDATE MarketAccount SET Balance = Balance + money WHERE aid = this.aid";
      this.user.setBalance(this.user.getBalance()+money);
      int aid = this.user.getMarketAccountID();
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();

      String query = "select COUNT(*) from Transaction";
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      int tid = resultSet.getInt(1) + 1;      
      Class.forName("com.mysql.jdbc.Driver");

      query = "select * from MarketDate";
      resultSet = statement.executeQuery(query);
      resultSet.next();
      Date date = resultSet.getDate(1);
      java.sql.Date dateDB = new java.sql.Date(date.getTime());
      
      String transactionString = "INSERT INTO Transaction(tid, type, date, aid) VALUES " +
      "(?, ?, ?, ?)";
  
      PreparedStatement preparedStatement = connection.prepareStatement(transactionString);
      preparedStatement.setInt(1, tid);
      preparedStatement.setString(2, " Deposit ");
      preparedStatement.setDate(3, dateDB);
      preparedStatement.setInt(4, aid);
      preparedStatement.executeUpdate();

      transactionString = "INSERT INTO MarketTransaction(tid, amount) VALUES " +
      "(?, ?)";
  
      preparedStatement = connection.prepareStatement(transactionString);
      preparedStatement.setInt(1, tid);
      preparedStatement.setDouble(2, money);
      preparedStatement.executeUpdate();

      preparedStatement.close();
      statement.close();
      connection.close();
      System.out.printf("DEPOSITED " + money + " DOLLARS. NEW BALANCE: %.2f \n", this.user.getBalance());
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void withdrawBalance() {
    try {
      System.out.printf("CURRENT BALANCE: %.2f \n", this.user.getBalance());
      System.out.println("How much money do you want to withdraw?");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      double money = Double.parseDouble(reader.readLine());
      if (this.user.getBalance() < money)
        System.out.println("CANNOT WITHDRAW MONEY: INSUFFICIENT BALANCE");
      else {
        this.user.setBalance(this.user.getBalance() - money);
        int aid = this.user.getMarketAccountID();

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(HOST, USER, PWD);
        Statement statement = connection.createStatement();

        String query = "select COUNT(*) from Transaction";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        int tid = resultSet.getInt(1) + 1;      

        query = "select * from MarketDate";
        resultSet = statement.executeQuery(query);
        resultSet.next();
        Date date = resultSet.getDate(1);
        java.sql.Date dateDB = new java.sql.Date(date.getTime());

        String transactionString = "INSERT INTO Transaction(tid, type, date, aid) VALUES " +
        "(?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(transactionString);
        preparedStatement.setInt(1, tid);
        preparedStatement.setString(2, " Withdrawal ");
        preparedStatement.setDate(3, dateDB);
        preparedStatement.setInt(4, aid);
        preparedStatement.executeUpdate();

        transactionString = "INSERT INTO MarketTransaction(tid, amount) VALUES " +
        "(?, ?)";

        preparedStatement = connection.prepareStatement(transactionString);
        preparedStatement.setInt(1, tid);
        preparedStatement.setDouble(2, money);
        preparedStatement.executeUpdate();

        preparedStatement.close();
        statement.close();
        connection.close();
        System.out.printf("WITHDREW " + money + " DOLLARS. REMAINING BALANCE: %.2f DOLLARS. \n", this.user.getBalance());

        }
      } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void listStockDetails() {
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

  public void buyStock() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();
      while(true){
        System.out.println("Which stock would you like to buy?");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String symbol = reader.readLine();
        String query = "select * from Stock where symbol = \"" + symbol + "\"";      
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.isBeforeFirst()) {
          resultSet.next();
          while(true){
            double currentPrice = resultSet.getDouble(3);
            double numStocks = resultSet.getInt(4);
            System.out.println("STOCK: "+symbol+". CURRENT PRICE: "+resultSet.getDouble(3)+" DOLLARS. AVAILABLE SHARES: "+resultSet.getInt(4));
            System.out.println("How many shares would you like to purchase? Enter 0 in order to change stock name. (There is $20 commission with every transaction)");
            reader = new BufferedReader(new InputStreamReader(System.in));
            int amount = Integer.parseInt(reader.readLine());
            double totalPrice = currentPrice * amount + 20;
            if (amount == 0){
              break;
            } else if (amount > numStocks) {
              System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH STOCKS REMAINING");
            } else if (this.user.getBalance() < totalPrice) {
              System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH MONEY IN BALANCE");
            } else if (amount > 0) {
              query = "UPDATE Stock SET numStocks = "+ (numStocks-amount) +" WHERE symbol = \"" + symbol + "\"";  
              statement.executeUpdate(query);
              
              this.user.setBalance(this.user.getBalance() - totalPrice);
              int aid = this.user.getMarketAccountID();
              
              query = "select * from MarketDate";
              resultSet = statement.executeQuery(query);
              resultSet.next();
              Date date = resultSet.getDate(1);
              java.sql.Date dateDB = new java.sql.Date(date.getTime());

              query = "select COUNT(*) from Transaction";
              resultSet = statement.executeQuery(query);
              resultSet.next();
              int tid = resultSet.getInt(1) + 1;      

              String transactionString = "INSERT INTO Transaction(tid, type, date, aid) VALUES " +
              "(?, ?, ?, ?)";

              PreparedStatement preparedStatement = connection.prepareStatement(transactionString);
              preparedStatement.setInt(1, tid);
              preparedStatement.setString(2, " Stock bought ");
              preparedStatement.setDate(3, dateDB);
              preparedStatement.setInt(4, aid);
              preparedStatement.executeUpdate();

              transactionString = "INSERT INTO StockTransaction(tid, symbol, quantity, price, totalPrice) VALUES " +
              "(?, ?, ?, ?, ?)";

              preparedStatement = connection.prepareStatement(transactionString);
              preparedStatement.setInt(1, tid);
              preparedStatement.setString(2, symbol);
              preparedStatement.setInt(3, amount);
              preparedStatement.setDouble(4, currentPrice);
              preparedStatement.setDouble(5, totalPrice);
              preparedStatement.executeUpdate();
              
              System.out.printf("SUCCESSFULLY PURCHASED "+amount+" SHARES OF "+symbol+" FOR "+totalPrice+" DOLLARS. REMAINING BALANCE:  %.2f \n", this.user.getBalance());
        
              preparedStatement.close();
              statement.close();
              connection.close();
              break;
            }
            else {
              System.out.println("ENTER A VALUE OVER 0");
            }
          }
        break;
        }
        else{
          System.out.println("NO SUCH STOCK: "+symbol);
        }
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void sellStock() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();
      while(true){
        System.out.println("Which stock would you like to sell?");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String symbol = reader.readLine();
        String query = "select * from Stock where symbol = \"" + symbol + "\"";      
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.isBeforeFirst()) {
          resultSet.next();
          while(true){
            double currentPrice = resultSet.getDouble(3);
            double numStocks = resultSet.getInt(4);
            System.out.println("STOCK: "+symbol+". CURRENT PRICE: "+resultSet.getDouble(3)+" DOLLARS. YOUR AVAILABLE SHARES: "+ 1);//userShares;
            System.out.println("How many shares would you like to sell? Enter 0 in order to change stock name. (There is $20 commission with every transaction)");
            reader = new BufferedReader(new InputStreamReader(System.in));
            int amount = Integer.parseInt(reader.readLine());
            double totalPrice = currentPrice * amount + 20;
            if (amount == 0){
              break;
            } else if (amount > 0) {
              query = "UPDATE Stock SET numStocks = "+ (numStocks+amount) +" WHERE symbol = \"" + symbol + "\"";  
              statement.executeUpdate(query);
              
              this.user.setBalance(this.user.getBalance() - totalPrice);
              int aid = this.user.getMarketAccountID();
              
              query = "select * from MarketDate";
              resultSet = statement.executeQuery(query);
              resultSet.next();
              Date date = resultSet.getDate(1);
              java.sql.Date dateDB = new java.sql.Date(date.getTime());

              query = "select COUNT(*) from Transaction";
              resultSet = statement.executeQuery(query);
              resultSet.next();
              int tid = resultSet.getInt(1) + 1;      

              String transactionString = "INSERT INTO Transaction(tid, type, date, aid) VALUES " +
              "(?, ?, ?, ?)";

              PreparedStatement preparedStatement = connection.prepareStatement(transactionString);
              preparedStatement.setInt(1, tid);
              preparedStatement.setString(2, " Stock sold ");
              preparedStatement.setDate(3, dateDB);
              preparedStatement.setInt(4, aid);
              preparedStatement.executeUpdate();

              transactionString = "INSERT INTO StockTransaction(tid, symbol, quantity, price, totalPrice) VALUES " +
              "(?, ?, ?, ?, ?)";

              preparedStatement = connection.prepareStatement(transactionString);
              preparedStatement.setInt(1, tid);
              preparedStatement.setString(2, symbol);
              preparedStatement.setInt(3, amount);
              preparedStatement.setDouble(4, currentPrice);
              preparedStatement.setDouble(5, totalPrice);
              preparedStatement.executeUpdate();
              
              System.out.printf("SUCCESSFULLY SOLD "+amount+" SHARES OF "+symbol+" FOR "+totalPrice+" DOLLARS. NEW BALANCE:  %.2f \n", this.user.getBalance());
        
              preparedStatement.close();
              statement.close();
              connection.close();
              break;
            }
            else {
              System.out.println("ENTER A VALUE OVER 0");
            }
          }
        break;
        }
        else{
          System.out.println("NO SUCH STOCK: "+symbol);
        }
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void displayMovieDetails(String movieTitle) {

  }

/*  public java.sql.Date getDate(){
    java.sql.Date dateDB;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);

      Statement statement = connection.createStatement();

      String query = "select * from MarketDate";
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      Date date = resultSet.getDate(1);
      java.sql.Date dateDB = new java.sql.Date(date.getTime());
      statement.close();
      connection.close();
      return dateDB;
    } catch (Exception e) {
      System.err.println(e);
    }
    return dateDB;
  }*/
}

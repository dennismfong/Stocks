import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.*;
import java.util.ArrayList;
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

  private String MOVIEHOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/moviesDB";

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
                traderifc.sellStock();
                break;
              case 5:
                traderifc.showBalance();
                break;
              case 6:
                traderifc.getStockTransHistory();
                break;
              case 7:
                traderifc.listStockDetails();
                break;
              case 8:
                traderifc.displayMovieDetails();
                break;
              case 9:
                traderifc.displayTopMovies();
                break;
              case 10:
                traderifc.displayMovieReviews();
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
      preparedStatement.setString(2, "Deposit");
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
        preparedStatement.setString(2, "Withdrawal");
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
      while(true){
        System.out.println("Which stock would you like to view? Enter \"exit\" to leave search.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String symbol = reader.readLine();
        if(symbol == "exit")
          break;
        String query = "select * from Stock where symbol = \"" + symbol + "\"";
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.isBeforeFirst()) {
            System.out.println("CANNOT FIND INFORMATION FOR STOCK: "+symbol);
        }
        else {
          resultSet.next();
          double openingPrice = resultSet.getDouble(2);
          double currentPrice = resultSet.getDouble(3);
          int numStocks = resultSet.getInt(4);
          int adid = resultSet.getInt(5);
          
          System.out.println("----STATISTICS FOR STOCK "+symbol+"----");
          System.out.println("OPENING PRICE: " + openingPrice);
          System.out.println("CURRENT PRICE: " + currentPrice);
          System.out.println("SHARES AVAILABLE: " + numStocks);

          query = "select * from ActorDirector where adid = "+adid;
          resultSet = statement.executeQuery(query);
          resultSet.next();
          String adname = resultSet.getString(1);
          Date dob = resultSet.getDate(2);

          System.out.println("\n----INFORMATION ABOUT STOCK "+symbol+"----");
          System.out.println("STAR NAME: " + adname);
          System.out.println("DATE OF BIRTH: " + dob);

          query = "select * from Contract where adid = "+adid;
          resultSet = statement.executeQuery(query);

          System.out.println("\n----CONTRACTS FOR "+adname+"----");

          while(resultSet.next()){
            String movieTitle = resultSet.getString(1);
            String role = resultSet.getString(3);
            int year = resultSet.getInt(4);
            double value = resultSet.getDouble(5);

            System.out.println("MOVIE TITLE: " + movieTitle);
            System.out.println("ROLE: " + role);
            System.out.println("YEAR: " + year);
            System.out.println("CONTRACT VALUE: " + value + "\n");
          }
          break;
        }
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
            double totalPrice = currentPrice * amount;
            if (amount == 0){
              break;
            } else if (amount > numStocks) {
              System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH STOCKS REMAINING");
            } else if (this.user.getBalance() < totalPrice + 20) {
              System.out.println("CANNOT PURCHASE STOCK: NOT ENOUGH MONEY IN BALANCE");
            } else if (amount > 0) {
              query = "UPDATE Stock SET numStocks = "+ (numStocks-amount) +" WHERE symbol = \"" + symbol + "\"";  
              statement.executeUpdate(query);
              
              this.user.setBalance(this.user.getBalance() - totalPrice - 20);
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
              preparedStatement.setString(2, "Stock bought");
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
              
              System.out.printf("SUCCESSFULLY PURCHASED "+amount+" SHARES OF "+symbol+" FOR %.2f. ", (totalPrice+20));
                    System.out.printf("NEW BALANCE:  %.2f \n",this.user.getBalance());              

              query = "SELECT * FROM StockBalance where buyPrice = "+currentPrice
                + " and aid = "+aid 
                + " and symbol = \""+symbol+"\"";
              resultSet = statement.executeQuery(query);

              if(!resultSet.next()){
                String updateString = "INSERT INTO StockBalance(aid, symbol, quantity, buyPrice, totalValue) VALUES " +
                "(?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(updateString);
                preparedStatement.setInt(1, aid);
                preparedStatement.setString(2, symbol);
                preparedStatement.setInt(3, amount);
                preparedStatement.setDouble(4, currentPrice);
                preparedStatement.setDouble(5, totalPrice);
                preparedStatement.executeUpdate();
              }

              else{
                query = "UPDATE StockBalance SET quantity = quantity +"+amount
                + " where buyPrice = "+currentPrice
                + " and aid = "+aid 
                + " and symbol = \""+symbol+"\"";
                statement.executeUpdate(query);

                query = "UPDATE StockBalance SET totalValue = totalValue +"+(totalPrice-20)
                + " where buyPrice = "+currentPrice
                + " and aid = "+aid 
                + " and symbol = \""+symbol+"\"";
                statement.executeUpdate(query);
              }

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
      int aid = this.user.getMarketAccountID();
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();
      while(true){
        System.out.println("Which stock would you like to sell?");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String symbol = reader.readLine();
        String query = "select buyPrice, quantity, totalValue from StockBalance where aid = "+aid+" and symbol = \"" + symbol + "\"";      
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.isBeforeFirst()){
          ArrayList<Double> stockValues = new ArrayList<Double>();
          ArrayList<Integer> stockQuantity = new ArrayList<Integer>();
          ArrayList<Double> totalValues = new ArrayList<Double>();
          while(resultSet.next()){
            double value = resultSet.getDouble(1);
            int quantity = resultSet.getInt(2);
            double totalValue = resultSet.getDouble(3);
            stockValues.add(value);
            stockQuantity.add(quantity);
            totalValues.add(totalValue);
          }
          query = "select currentPrice from Stock where symbol = \"" + symbol + "\"";
          resultSet = statement.executeQuery(query);
          resultSet.next();
          double currentPrice = resultSet.getDouble(1);
          System.out.printf("CURRENT VALUE OF STOCK "+symbol+": %.2f DOLLARS \n", currentPrice);
          System.out.println("Which share value of your stock would you like to sell? Enter the corresponding row number.");
          int i = 0;
          for(double val: stockValues){
            System.out.printf(i+". "+val+" DOLLARS. QUANTITY OWNED: "
              +stockQuantity.get(i)+ " SHARES. TOTAL VALUE: %.2f DOLLARS. \n", totalValues.get(i));
            i++;
          }
          while(true){
            reader = new BufferedReader(new InputStreamReader(System.in));
            int row = Integer.parseInt(reader.readLine());
            if(row > i || row < 0)
              System.out.println("INVALID ROW.");
            else{
              double targetVal = stockValues.get(row);
              int targetQuantity = stockQuantity.get(row);
              System.out.printf("QUANTITES OF STOCK "+symbol+" OWNED AT VALUE %.2f DOLLARS: "+targetQuantity+"\n", targetVal);
              System.out.println("How many shares of your stock would you like to sell? There is a $20 commission with every transaction. Enter 0 to go back");
              while(true){
                reader = new BufferedReader(new InputStreamReader(System.in));
                int quantity = Integer.parseInt(reader.readLine());
                double totalPrice = quantity * currentPrice;
                if(quantity == 0) break;
                if(quantity > targetQuantity)
                  System.out.println("CANNOT SELL "+quantity+" SHARES: NOT ENOUGH IN BALANCE.");
                else if (quantity > 0) {
                  if(this.user.getBalance()+totalPrice-20 < 0){
                    System.out.println("CANNOT SELL "+quantity+" SHARES: TRANSACTION WILL RESULT IN BALANCE LESS THAN 0! (REMEMBER THERE IS $20 COMMISSION FEE)");
                  }
                  else{
                    query = "UPDATE Stock SET numStocks = numStocks + "+ quantity +" WHERE symbol = \"" + symbol + "\"";  
                    statement.executeUpdate(query);
                  
                    this.user.setBalance(this.user.getBalance()+totalPrice-20);
                    
                    query = "UPDATE StockBalance SET quantity = quantity - "+quantity+" WHERE aid = "+aid
                      +" AND symbol = \"" + symbol + "\" AND buyPrice = "+targetVal; 
                    statement.executeUpdate(query);

                    query = "select quantity from StockBalance where aid = "+aid
                      +" AND symbol = \"" + symbol + "\" AND buyPrice = "+targetVal; 
                    resultSet = statement.executeQuery(query);
                    resultSet.next();
                    if(resultSet.getInt(1) == 0){
                      query = "DELETE FROM StockBalance WHERE aid = "+aid
                        +" AND symbol = \"" + symbol + "\" AND buyPrice = "+targetVal; 
                      statement.executeUpdate(query);
                    }

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
                    preparedStatement.setString(2, "Stock sold");
                    preparedStatement.setDate(3, dateDB);
                    preparedStatement.setInt(4, aid);
                    preparedStatement.executeUpdate();

                    transactionString = "INSERT INTO StockTransaction(tid, symbol, quantity, price, totalPrice) VALUES " +
                    "(?, ?, ?, ?, ?)";

                    preparedStatement = connection.prepareStatement(transactionString);
                    preparedStatement.setInt(1, tid);
                    preparedStatement.setString(2, symbol);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.setDouble(4, currentPrice);
                    preparedStatement.setDouble(5, totalPrice);
                    preparedStatement.executeUpdate();
                    
                    System.out.printf("SUCCESSFULLY SOLD "+quantity+" SHARES OF "+symbol+" FOR %.2f. ", (totalPrice-20));
                    System.out.printf("NEW BALANCE:  %.2f \n",this.user.getBalance());
              
                    preparedStatement.close();
                    statement.close();
                    connection.close();
                    break; 
                  }
                }
              }
              break;
            }
          }
          break;
        }
        else{
          System.out.println("YOU DO NOT OWN ANY SHARES OF STOCK "+symbol);
        }
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void getStockTransHistory(){
    try {
      int aid = this.user.getMarketAccountID();
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();

      String query = "select * from Transaction t JOIN StockTransaction s ON t.tid = s.tid WHERE t.aid = aid";
      ResultSet resultSet = statement.executeQuery(query);

      System.out.println("DISPLAYING ALL STOCK TRANSACTIONS IN PAST MONTH");

      while(resultSet.next()){
        String type = resultSet.getString(2);
        Date date = resultSet.getDate(3);
        int quantity = resultSet.getInt(6);
        double price = resultSet.getDouble(7);
        String symbol = resultSet.getString(8);
        double totalPrice = resultSet.getDouble(9);

        System.out.println("--------\n"+date);
        System.out.println(type);
        System.out.printf(quantity + " OF " + symbol + " FOR %.2f \n", price);
        System.out.printf("TOTAL COST OF TRANSACTION (WITH $20 COMMISSION INCLUDED): %.2f \n", (totalPrice+20));
        System.out.println("--------");
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void displayMovieDetails() {
    try{
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(MOVIEHOST, USER, PWD);
      Statement statement = connection.createStatement();
      System.out.println("What movie would you like information on?");
      while(true){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String title = reader.readLine();

        String query = "select rating, production_year from Movies where title = \"" + title + "\"";
        ResultSet resultSet = statement.executeQuery(query);

        if(resultSet.next()){
          float rating = resultSet.getFloat(1);
          int production_year = resultSet.getInt(2);
          System.out.println("Produced in: "+production_year+"\nMovie Rating: "+rating);
          break;
        }
        else{
          System.out.println("ERROR: NO SUCH MOVIE IN DATABASE");
        }
      }
      statement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void displayTopMovies() {
    try{
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(MOVIEHOST, USER, PWD);
      Statement statement = connection.createStatement();
      System.out.println("What time period do you want see the top movies for?");

      System.out.println("Lower bound: ");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      int lb = Integer.parseInt(reader.readLine());

      System.out.println("Upper bound: ");
      reader = new BufferedReader(new InputStreamReader(System.in));
      int ub = Integer.parseInt(reader.readLine());

      String query = "select title, production_year from Movies where rating = 5 and production_year >= "+lb+" and production_year <= "+ub; 
      ResultSet resultSet = statement.executeQuery(query);

      System.out.println("--------\nMovies between "+lb+"-"+ub+" with a rating of 5:");
      while(resultSet.next()){
        System.out.println(resultSet.getString(1) + " ("+resultSet.getInt(2)+")");
      }
      System.out.println("--------");
      statement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void displayMovieReviews() {
    try{
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(MOVIEHOST, USER, PWD);
      Statement statement = connection.createStatement();
      System.out.println("What movie would you like reviews on?");
      while(true){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String title = reader.readLine();

        String query = "select rating, production_year from Movies where title = \"" + title + "\"";
        ResultSet resultSet = statement.executeQuery(query);

        if(resultSet.next()){
          float rating = resultSet.getFloat(1);
          int production_year = resultSet.getInt(2);
          System.out.println("Produced in: "+production_year+"\nMovie Rating: "+rating);
          break;
        }
        else{
          System.out.println("ERROR: NO SUCH MOVIE IN DATABASE");
        }
      }
      statement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}

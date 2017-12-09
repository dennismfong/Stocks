import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SystemManager {

  public String HOST = Config.host;
  public String USER = Config.user;
  public String PWD = Config.pwd;
  public Date currDate;

  public SystemManager() {
    currDate = this.getDate();
  }

  private void incrementDate() {
    Calendar c = Calendar.getInstance();
    c.setTime(currDate);
    c.add(Calendar.DATE, 1);  // number of days to add
    currDate = c.getTime();

    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(this.HOST,
              this.USER,
              this.PWD);
      Statement statement = connection.createStatement();
      String update = "delete from MarketDate";
      statement.executeUpdate(update);


      java.sql.Date dateDB = new java.sql.Date(currDate.getTime());
      String updateString = "insert into MarketDate (date) VALUES (?)";
      PreparedStatement preparedStatement = connection.prepareStatement(updateString);
      preparedStatement.setDate(1, dateDB);
      preparedStatement.executeUpdate();

      preparedStatement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void openMarket() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(this.HOST,
              this.USER,
              this.PWD);
      Statement statement = connection.createStatement();
      incrementDate();
      String query = "update Stock set openingPrice = Stock.currentPrice";
      statement.executeUpdate(query);

      statement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void closeMarket() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(this.HOST,
              this.USER,
              this.PWD);
      Statement statement = connection.createStatement();
      String query = "select * from Stock";
      ResultSet resultSet = statement.executeQuery(query);
      PreparedStatement preparedStatement = null;

      while (resultSet.next()) {
        java.sql.Date dateDB = new java.sql.Date(this.currDate.getTime());
        query = "insert into StockInstance (date, closingPrice, symbol) VALUES (?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDate(1, dateDB);
        preparedStatement.setDouble(2, resultSet.getDouble(3));
        preparedStatement.setString(3, resultSet.getString(1));
        preparedStatement.executeUpdate();

      }

      preparedStatement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void setStockPrice() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(this.HOST,
              this.USER,
              this.PWD);
      Statement statement = connection.createStatement();

      System.out.println("Type in the 3 letter symbol of the stock to change");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String symbolToChange = br.readLine();

      String query = "select * from Stock where symbol = \"" + symbolToChange +"\"";
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      Double currPrice = resultSet.getDouble(3);
      System.out.println("The current price is " + currPrice);
      System.out.println("What would you like to change it to?");
      String priceToSet = br.readLine();

      query = "update Stock set currentPrice = "
              + priceToSet
              + " where symbol = \""
              + symbolToChange
              + "\"";
      statement.executeUpdate(query);
      statement.close();
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public Date getDate() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(this.HOST,
              this.USER,
              this.PWD);
      Statement statement = connection.createStatement();
      String query = "select * from MarketDate";
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      Date returnDate = resultSet.getDate(1);
      this.currDate = returnDate;
      statement.close();
      connection.close();
      return returnDate;
    } catch (Exception e) {
      System.err.println(e);
    }
    return null;
  }

  public void setDate() {
    try {
      StringBuilder sb = new StringBuilder();
      System.out.println("Enter in the year (ie. 2000)");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      sb.append(br.readLine()).append("-");
      System.out.println("Enter in the month (ie. 02)");
      sb.append(br.readLine()).append("-");
      System.out.println("Enter in the day (ie. 24)");
      sb.append(br.readLine());

      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      date = sdf.parse(sb.toString());
      String dateStr = sdf.format(date);

      System.out.println("Change the date to " + dateStr + "? (y or n)");
      String answer = br.readLine();
      if (answer.toLowerCase().contains("y")) {
        try {
          Class.forName("com.mysql.jdbc.Driver");
          Connection connection = DriverManager.getConnection(this.HOST,
                  this.USER,
                  this.PWD);
          Statement statement = connection.createStatement();
          String update = "delete from MarketDate";
          statement.executeUpdate(update);


          java.sql.Date dateDB = new java.sql.Date(date.getTime());
          String updateString = "insert into MarketDate (date) VALUES (?)";
          PreparedStatement preparedStatement = connection.prepareStatement(updateString);
          preparedStatement.setDate(1, dateDB);
          preparedStatement.executeUpdate();
          currDate = date;

          preparedStatement.close();
          connection.close();
        } catch (Exception e) {
          System.err.println(e);
        }
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public static void main(String[] args) {
    boolean exit = false;
    SystemManager systemManager = new SystemManager();

    while (!exit) {
      System.out.println("System interface for demo");
      System.out.println("Today's date is " + systemManager.getDate());
      System.out.println("Issue commands using the number key associated with your request");
      System.out.println("\n\n"
              + "\n1.     Open market for the day"
              + "\n2.     Close market for the day"
              + "\n3.     Set a new price for a stock"
              + "\n4.     Set a new date to be today's date"
              + "\n5.     Exit"
              + "\n\n"
      );

      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int answer = Integer.parseInt(reader.readLine());
        switch (answer) {
          case 1:
            systemManager.openMarket();
            break;
          case 2:
            systemManager.closeMarket();
            break;
          case 3:
            systemManager.setStockPrice();
            break;
          case 4:
            systemManager.setDate();
            break;
          case 5:
            exit = true;
            break;
        }
      } catch (Exception e) {
        System.err.println(e);
      }
    }
  }
}

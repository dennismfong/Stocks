import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemManager {

  public String HOST = Config.host;
  public String USER = Config.user;
  public String PWD = Config.pwd;

  public SystemManager() {

  }

  public void openMarket() {

  }

  public void closeMarket() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(this.HOST,
              this.USER,
              this.PWD);
      connection.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void setStockPrice() {

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
    SystemManager systemManager = new SystemManager();

    System.out.println("System interface for demo");
    System.out.println("Issue commands using the number key associated with your request");
    System.out.println("\n\n"
            + "\n1.     Open market for the day"
            + "\n2.     Close market for the day"
            + "\n3.     Set a new price for a stock"
            + "\n4.     Set a new date to be today's date"
            + "\n\n"
    );

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      int answer = Integer.parseInt(reader.readLine());
      switch (answer) {
        case 1:
          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          systemManager.setDate();
          break;
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}

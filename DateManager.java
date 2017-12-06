import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

  String HOST = Config.host;
  String USER = Config.user;
  String PWD = Config.pwd;

  public DateManager() {

  }

  public static void main(String[] args) {
    DateManager dateManager = new DateManager();
    int year, month, day;

    try {
      StringBuilder sb = new StringBuilder();
      System.out.println("Enter in the year (ie. 2000)");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      sb.append(br.readLine()).append("/");
      System.out.println("Enter in the month (ie. 02)");
      sb.append(br.readLine()).append("/");
      System.out.println("Enter in the day (ie. 24)");

      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      date = sdf.parse(sb.toString());
      String dateStr = sdf.format(date);

      System.out.println("Change the date to " + dateStr + "? (y or n)");
      String answer = br.readLine();
      if (answer.toLowerCase().contains("y")) {
        try {
          Class.forName("com.mysql.jdbc.Driver");
          Connection connection = DriverManager.getConnection(dateManager.HOST,
                  dateManager.USER,
                  dateManager.PWD);
          Statement statement = connection.createStatement();
          String query = "delete from MarketDate";
          ResultSet resultSet = statement.executeQuery(query);

        } catch (Exception e) {
          System.err.println(e);
        }
      }
    } catch (Exception e) {
      System.err.println(e);
    }

  }
}

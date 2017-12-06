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

  public static void main() {
    DateManager dateManager = new DateManager();
    int year, month, day;

    try {
      System.out.println("Enter in the year (ie. 2000)");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      year = Integer.parseInt(br.readLine());
      System.out.println("Enter in the month (ie. 02");
      month = Integer.parseInt(br.readLine());
      System.out.println("Enter in the day (ie. 24");
      day = Integer.parseInt(br.readLine());

      Date date = new Date();
      date.setYear(year);
      date.setMonth(month);
      date.setDate(day);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

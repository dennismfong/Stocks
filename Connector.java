import java.sql.*;

public class Connector {

  public void connect {
    Config config = new Config();
    String HOST = Config.host;
    String USER = Config.user;
    String PWD = Config.pwd;

    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);

      Statement statement = connection.createStatement();

      String query = "select * from Customer";
      ResultSet resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        String col1 = resultSet.getString("cname");
        String col2 = resultSet.getString("state");
        String col3 = resultSet.getString("phoneNum");
        String col4 = resultSet.getString("email");
        String col5 = resultSet.getString("taxId");
        String col6 = resultSet.getString("username");
        String col7 = resultSet.getString("password");

        System.out.println(col1 + "\t" 
          + col2 + "\t" 
          + col3 + "\t"
          + col4 + "\t"
          + col5 + "\t"
          + col6 + "\t"
          + col7 + "\t"
          );
        // Perform other operations if needed
      }
      resultSet.close();
      statement.close();
      connection.close();
    }
    catch (Exception e) {
      System.err.println(e);
    }
  }

  public static void main(String[] args) {
    Connector connector = new Connector();
    connector.connect();
  }
}


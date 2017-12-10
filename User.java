import java.sql.*;

public class User{
	private String username;
	private String name;
	private String ssn;
	private int taxId;
	private String HOST;
	private String USER;
	private String PWD;
	private boolean isManager;

	public User() {
		HOST = Config.host;
		USER = Config.user;
		PWD = Config.pwd;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setTaxId(int taxid) {
		this.taxId = taxid; 
	}

	public void setBalance(double value){
		try{
			Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(HOST, USER, PWD);
	    Statement statement = connection.createStatement();

	    String query = "UPDATE Account A, MarketAccount MA SET A.balance = "+value+" WHERE A.taxId= "+this.taxId +" AND A.aid = MA.aid";
	    statement.executeUpdate(query);

	 		statement.close();
	    connection.close();
		} catch (Exception e) {
      System.err.println(e);
    }
  }

	public void setUsername(String username){
		this.username = username;
	}

	public double getBalance(){		
		try{
			Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(HOST, USER, PWD);
	    Statement statement = connection.createStatement();

	    String query = "select balance from Account where taxId = " + this.taxId + " and aid in (select MA.aid from MarketAccount MA)";
	    ResultSet resultSet = statement.executeQuery(query);
	    resultSet.next();
	    double balance = resultSet.getFloat(1);
	 		statement.close();
	    connection.close();
	    return balance; 
		} catch (Exception e) {
      System.err.println(e);
    }
    return 0;
  }


	public String getName(){ return this.name; }

	public String getUsername(){ return this.username; }

	public int getTaxId(){ return this.taxId; }

	public void setInfo(String username) {
    try {
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);
      Statement statement = connection.createStatement();
      String query = "select * from Customer where username = \"" + username+ "\"";
      ResultSet resultSet = statement.executeQuery(query);

      if (resultSet.next()) {
        this.name = resultSet.getString(1);
        this.ssn = resultSet.getString(7);
        this.taxId = resultSet.getInt(5);
        this.isManager = resultSet.getBoolean(10);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  public int getMarketAccountID(){
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(HOST, USER, PWD);

      Statement statement = connection.createStatement();

      String query = "select A.aid from Account A where "
      + "A.taxId = " + this.taxId + " and A.aid in (select MA.aid from MarketAccount MA)";
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      int aid = resultSet.getInt(1);
      statement.close();
      connection.close();
      return aid;
    } catch (Exception e) {
      System.err.println(e);
    }
    return 0;
  }
}
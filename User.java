import java.sql.*;

public class User{
	private String username;
	private float balance;
	private String name;
	private String ssn;
	private int taxId;
	private String HOST;
	private String USER;
	private String PWD;

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

	public void setBalance(float value){
		try{
			Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(HOST, USER, PWD);
	    Statement statement = connection.createStatement();

	    String query = "UPDATE Account A, MarketAccount MA SET A.balance = "+value+" WHERE A.taxId= "+this.taxId +" AND A.aid = MA.aid";
	    statement.executeUpdate(query);
	 		this.balance = balance;

	 		statement.close();
	    connection.close();
		} catch (Exception e) {
      System.err.println(e);
    }
  }

	public void setUsername(String username){
		this.username = username;
	}

	public float getBalance(){ return this.balance; }

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
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
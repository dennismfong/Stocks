import java.sql.*;

public class User{
	private String username;
	private float balance;
	private String name;
	private int ssn;
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

	public void setBalance(float value){
		try{
			Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(HOST, USER, PWD);
	    Statement statement = connection.createStatement();

	    String query = "UPDATE Account SET balance = "+value+" WHERE ssn = "+this.ssn;
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
}
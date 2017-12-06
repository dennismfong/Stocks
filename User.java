public class User{
	private String username;
	private float balance;
	private String name;

	public void setName(String name){
		this.name = name;
	}

	public void setBalance(float balance){
		this.balance = balance;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public float getBalance(){ return this.balance; }

	public String getName(){ return this.name; }

	public String getUsername(){ return this.username; }
}
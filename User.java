public class User{
	private String username;
	private float balance;
	private String name;

	public static void setName(String name){
		this.name = name;
	}

	public static void setBalance(float balance){
		this.balance = balance;
	}

	public static void setUsername(String username){
		this.username = username;
	}

	public static float getBalance(){ return this.balance; }

	public static String getName(){ return this.name; }

	public static String getUsername(){ return this.username; }
}
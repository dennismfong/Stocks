import java.io.InputStreamReader;
import java.io.BufferedReader;

public class TraderInterface {

	private String name;

	public TraderInterface() {

	}

	public void displayGreeting() {
		System.out.println("Welcome to the Trader Interface of StarsRUs");
		System.out.println("Issue commands using the number key associated with your request");
	}

	public boolean login(String username, String password) {
		boolean success = true;
		// Add in code to check if user is in the customer table
		this.name = "Test User"; // Update it with the cname column from query
		return success;
	}

	public boolean registerUser() {
		boolean registered = true; // Set to true for debugging purposes
		System.out.println("Welcome to the register portal of StarsRUs");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter in your desired username");
			String username = br.readLine();
//			Only works on CSIL and not IntelliJ
//			Console console = System.console();
//			String password = new String(console.readPassword("Enter in your desired password"));
			System.out.println("Enter in your desired password");
			String password = br.readLine();

			// Add code to create a new row in the Customer table

			return registered;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String[] args) {
		TraderInterface traderifc = new TraderInterface();

		boolean loggedIn = false;
		boolean exit = false;

		traderifc.displayGreeting();
		while (!exit) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Are you a registered user?");
				System.out.println("1.	yes");
				System.out.println("2.	no");
				System.out.println("3.	exit");

				String answer = reader.readLine();
				if ("2".equals(answer)) {
					if (!traderifc.registerUser()) {
						continue;
					}
				}
				else if ("3".equals(answer)) {
					break;
				}

				while (!loggedIn) {
					System.out.println("Please enter in your username or type register to make a new account");
					String username = reader.readLine();
					if ("register".equals(username.toLowerCase())) {
						break;
					}
					System.out.println("Please enter in your password");
					String password = reader.readLine();

					loggedIn = traderifc.login(username, password);
					if (!loggedIn) {
						System.out.println("Sorry, that user was not found, please try again");
					} else {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Start of application logic
			System.out.println("Welcome " + traderifc.name + " to your portal!");
			System.out.println("Issue commands using the number key associated with your request");

			System.out.println("\n\n"
							+ "\n1.     Deposit"
							+ "\n2.     Withdrawal"
							+ "\n3.     Buy"
							+ "\n4.     Sell"
							+ "\n5.     Get market account balance"
							+ "\n6.     Get stock account transaction history"
							+ "\n7.     Get current price of a stock and actor profile"
							+ "\n8.     Get specific movie information "
							+ "\n9.     Get top movies information"
							+ "\n10.    Get reviews for a movie"
							+ "\n\n"
			);
		}
	}
}
import java.io.Console;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class TraderInterface {

	public TraderInterface() {

	}

	public void displayGreeting() {
		System.out.println("Welcome to the Trader Interface of StarsRUs");
		System.out.println("Issue commands using the number key associated with your request");
	}

	public boolean login(String username, String password) {
		boolean success = true;
		// Add in code to check if user is in the customer table
		return success;
	}

	public void registerUser() {
		System.out.println("Welcome to the register portal of StarsRUs");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter in your desired username");
			String username = br.readLine();
			Console console = System.console();
			char[] password = console.readPassword("Enter password");
			System.out.println(password);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		TraderInterface traderifc = new TraderInterface();

		boolean loggedIn = false;
		traderifc.displayGreeting();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Are you a registered user?");
			System.out.println("1. yes");
			System.out.println("2. no");

			String answer = reader.readLine();
			if ("2".equals(answer)) {
				traderifc.registerUser();
			}

			while (!loggedIn) {
				System.out.println("Please enter in your username");
				String username = reader.readLine();
				System.out.println("Please enter in your password");
				String password = reader.readLine();

				loggedIn = traderifc.login(username, password);
				if (!loggedIn) {
					System.out.println("Sorry, that user was not found, please try again");
				}
				else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ManagerInterface {
	public ManagerInterface() {

	}

	public boolean login(String username, String password) {
		boolean success = true;
		// Add in code to check if user is in the customer table
		return success;
	}

	public static void main(String[] args) {
		ManagerInterface managerifc = new ManagerInterface();

		boolean loggedIn = false;
		System.out.println("Welcome to the Trader Interface of StarsRUs");
		while (!loggedIn) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Please enter in your username");
				String username = reader.readLine();
				System.out.println("Please enter in your password");
				String password = reader.readLine();

				loggedIn = managerifc.login(username, password);
				if (!loggedIn) {
					System.out.println("Sorry, that user was not found, please try again");
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}
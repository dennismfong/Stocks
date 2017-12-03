import java.io.BufferedReader;

public class TraderInterface {
	public TraderInterface() {
		public boolean login(String username, String password) {
			boolean success = true;
			// Add in code to check if user is in the customer table
			return success;
		}

		public static void main(String[] args) {
			boolean loggedIn = false;
			System.out.println("Welcome to the Trader Interface of StarsRUs");
			while (!loggedIn) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Please enter in your username");
				String username = reader.readLine();
				System.out.println("Please enter in your password");
				String password = reader.readLine();

				loggedIn = login(username, password);
				if (!loggedIn) {
					System.out.println("Sorry, that user was not found, please try again");
				}
			}
		}
	}
}
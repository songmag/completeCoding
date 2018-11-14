public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserServer userServer = new UserServer();

		try {
			userServer.ServerOpen();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

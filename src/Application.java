import java.util.ArrayList;

/**
 * This class is responsible for initiating the program
 */
public class Application {

    /**
     * this method automates creating a chat server with 2 chat rooms then automatically
     * having 20 users to interact with the server until eventually all rooms are closed
     * and the server is closed.
     *
     * @param args CLI arguments
     * @throws InterruptedException if the threads are interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        Admin admin = new Admin("Liam");
        Thread main = new Thread(admin);
        ChatServer server = new ChatServer(10, 2, admin);

        ArrayList<Thread> threads = new ArrayList<Thread>();
        main.start();

        // Create 20 users with random UserIDs (1-100) and start their threads
        for (int i = 0; i < 20; i++) {
            int id = (int) (Math.random() * (100 - 1 + 1) + 1);
            User user = new User(id, server);

            Thread userThread = new Thread(user);
            userThread.start();
        }

        // Make sure this waits for all user threads to end
        for (Thread thread : threads) {
            thread.join();
        }
        main.join();
    }

}
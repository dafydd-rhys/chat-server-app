import java.util.List;
import java.util.Random;

/**
 * This class is used to manage the Admin, it extends runnable.
 * A server is opened and this admin is assigned to it, after
 * completing 15 actions, it closes all rooms then the server follows
 */
public class Admin implements Runnable {

	/**
	 * sleep scale for the admin 1000 = 1s
	 */
	private int sleepScale = 1000;
	/**
	 * name of admin
	 */
	private String name;
	/**
	 * chat server assigned to admin
	 */
	private ChatServer server;
	/**
	 * number of action for admin to complete
	 */
	private int actionCount = 15;

	/**
	 * sets name of this admin
	 *
	 * @param name name of admin
	 */
	public Admin(String name) {
		this.name = name;
	}

	/**
	 * assigns a server to this admin
	 *
	 * @param server server being assigned
	 */
	public void assignServer(ChatServer server) {
		this.server = server;
	}

	/**
	 * This method is the implementation required to use the runnable interface,
	 * this method simply opens the server assigned to this admin performs 15
	 * actions on a randomly selected chat room then closes each room and server.
	 */
	@Override
	public void run() {
		try {
			//open server and main chat room
			server.open();
			server.getMainRoom().open();

			//perform 15 actions
			for (int i = 0; i < actionCount; i++) {
				// pick a random chat room to open or close, that is not main room
				List<ChatRoom> rooms = server.getChatRooms();
				int randomIndex = new Random().nextInt(rooms.size() - 1) + 1;
				ChatRoom room = rooms.get(randomIndex);

				if (room.isOpen()) {
					room.close();
				} else {
					room.open();
				}

				//sleep for a random time between 1s and 2s
				Thread.sleep(new Random().nextInt(sleepScale + 1) + sleepScale);
			}

			// close all chat rooms
			for (ChatRoom room : server.getChatRooms()) {
				if (room.isOpen()) {
					room.close();
				}
			}

			//close chat server
			server.getMainRoom().close();
			server.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * sets name of admin
	 *
	 * @param name name of admin
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets name of admin
	 *
	 * @return name of admin
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets chat server this admin is responsible for
	 *
	 * @param server new server
	 */
	public void setServer(ChatServer server) {
		this.server = server;
	}

	/**
	 * gets the current server
	 *
	 * @return chat server admin is responsible for
	 */
	public ChatServer getServer() {
		return server;
	}

	/**
	 * sets number of actions for admin to complete
	 *
	 * @param actionCount number of actions to complete
	 */
	public void setActionCount(int actionCount) {
		this.actionCount = actionCount;
	}

	/**
	 * gets current number of actions admin wants to complete
	 *
	 * @return number of actions of admin
	 */
	public int getActionCount() {
		return actionCount;
	}

	/**
	 * sets the default wait time when sleeping
	 *
	 * @param sleepScale default wait time when sleeping
	 */
	public void setSleepScale(int sleepScale) {
		this.sleepScale = sleepScale;
	}

	/**
	 * gets sleep default value
	 *
	 * @return default sleep value
	 */
	public int getSleepScale() {
		return sleepScale;
	}

}
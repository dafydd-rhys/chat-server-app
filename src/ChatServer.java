import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the ChatServer object used to manage flow into the server,
 * it holds methods such as join, leave, open which all manage the server.
 * This class is an active class
 */
public class ChatServer {

	/**
	 * list of rooms in the server
	 */
	private ArrayList<ChatRoom> rooms;
	/**
	 * list of users in the server
	 */
	private List<User> users;
	/**
	 * admin assigned to the server
	 */
	private Admin admin;
	/**
	 * capacity of the server
	 */
	private int capacity;
	/**
	 * whether the server is open or closed
	 */
	private boolean isOpen;
	/**
	 * main chat room of the server
	 */
	private ChatRoom mainRoom;

	/**
	 * constructor for a chat server, all required attributes are set.
	 * afterwards the server is opened/
	 *
	 * @param capacity capacity of server
	 * @param numOfRooms number of rooms in this server
	 * @param admin admin of this server
	 */
	public ChatServer(int capacity, int numOfRooms, Admin admin) {
		this.rooms = new ArrayList<>();
		this.users = new ArrayList<>();
		this.admin = admin;
		this.capacity = capacity;

		if (numOfRooms > 0) {
			for (int i = 0; i < numOfRooms; i++) {
				rooms.add(new ChatRoom(i, capacity));
			}
			mainRoom = rooms.get(0);
		}

		admin.assignServer(this);
		open();
	}

	/**
	 * opens the server
	 */
	public synchronized void open() {
		if (!isOpen) {
			this.isOpen = true;
			System.out.println("Chat Server is Opened.");
		}
	}


	/**
	 * closes the server, clears all rooms and removes all users before doing so
	 */
	public synchronized void close() {
		System.out.println("Chat Server is being Closed.");

		for (ChatRoom room : rooms) {
			closeChatRoom(room.getRoomID());
		}
		users.clear();

		if (isOpen && allRoomsClosed()) {
			isOpen = false;
			System.out.println("Chat Server is Closed.");
		}
	}

	/**
	 * attempts to join a user into this server, ensures the user is unique, already not in server
	 * and that the server is not at capacity
	 *
	 * @param user user attempting to join server
	 * @return whether the user joined the server
	 */
	public synchronized boolean join(User user) {
		if (!users.contains(user) && users.size() < capacity && hasUniqueID(user)) {
			users.add(user);
			System.out.println(capacity);
			System.out.println("User " + user.getUserID() + " admitted to Chat Server ("
					+ user.getWantToChat() + ").");

			return true;
		} else {
			System.out.println("User " + user.getUserID() + " failed to join Chat Server ("
					+ user.getWantToChat() + ").");

			return false;
		}
	}

	/**
	 * attempts to leave a user from this server, ensures the user is actually in
	 * server before attempting to remove them
	 *
	 * @param user user attempting to leave the server
	 */
	public synchronized void leave(User user) {
		if (users.contains(user)) {
			users.remove(user);
			System.out.println("User " + user.getUserID() + " left Chat Server ");
		} else {
			System.out.println("Could not remove User " + user.getUserID() + " as is not in the Chat Server.");
		}
	}

	/**
	 * opens a specific chat room
	 *
	 * @param chatRoomID the id of the chat room attempted to be open
	 */
	public void openChatRoom(int chatRoomID) {
		for (ChatRoom chatRoom : rooms) {
			if (chatRoom.getRoomID() == chatRoomID) {
				chatRoom.open();
				System.out.println("ChatRoom " + chatRoomID + " opened.");
			}
		}
	}

	/**
	 * closes a specific chat room
	 *
	 * @param chatRoomID the id of the chat room attempted to be closed
	 */
	public void closeChatRoom(int chatRoomID) {
		for (ChatRoom chatRoom : rooms) {
			if (chatRoom.getRoomID() == chatRoomID) {
				chatRoom.close();
				break;
			}
		}
	}

	/**
	 * attempts to get a user to join a room with x chat room id
	 *
	 * @param user user attempting to join room
	 * @param chatRoomID id of the room attempting to be joined
	 * @return whether the user entered the room
	 */
	public boolean enterRoom(User user, int chatRoomID) {
		for (ChatRoom chatRoom : rooms) {
			if (chatRoom.getRoomID() == chatRoomID) {
				boolean joined = chatRoom.enterRoom(user);
				if (joined) {
					System.out.println("User " + user.getUserID() + " joined room " + chatRoomID);
				}

				return joined;
			}
		}

		return false;
	}

	/**
	 * attempts to get a user to leave a room with x chat room id
	 *
	 * @param user user attempting to leave room
	 * @param chatRoomID id of the room attempting to be left
	 * @return whether the user left the room
	 */
	public boolean leaveRoom(User user, int chatRoomID) {
		for (ChatRoom chatRoom : rooms) {
			if (chatRoom.getRoomID() == chatRoomID) {
				chatRoom.leaveRoom(user);
				System.out.println("User " + user.getUserID() + " left room " + chatRoomID);

				return true;
			}
		}

		return false;
	}

	/**
	 * gets the number of rooms in server
	 *
	 * @return number of rooms
	 */
	public int getNumberOfRooms() {
		return rooms.size();
	}

	/**
	 * gets whether a room with id x is open
	 *
	 * @return whether room is open
	 */
	public boolean isRoomOpen(int chatRoomID) {
		return rooms.get(chatRoomID).isOpen();
	}

	/**
	 * gets number of users in this server
	 *
	 * @return number of users
	 */
	public int getNumberOfUsers() {
		return users.size();
	}

	/**
	 * gets main chat room in this server
	 *
	 * @return main room of this server
	 */
	public ChatRoom getMainRoom() {
		return mainRoom;
	}

	/**
	 * attempts to remove user from all chat rooms
	 *
	 * @param user user being removed from chat rooms
	 */
	public void leaveChatRoom(User user) {
		if (users.contains(user)) {
			for (ChatRoom room : rooms) {
				room.leaveRoom(user);
			}
			users.remove(user);

			System.out.println("User " + user.getUserID() + " left Chat Server ");
		}
	}

	/**
	 * gets list of rooms in server
	 *
	 * @return rooms in server
	 */
	public List<ChatRoom> getChatRooms() {
		return rooms;
	}

	/**
	 * gets whether the passed user enters the main room or not
	 *
	 * @return whether user enter main room
	 */
	public boolean joinMainChatRoom(User user) {
		return mainRoom.enterRoom(user);
	}

	/**
	 * gets whether the passed user enters the random room or not
	 *
	 * @return whether user enter random room
	 */
	public boolean joinRandomChatRoom(User user) {
		int random = new Random().nextInt(rooms.size() - 1) + 1;

		return rooms.get(random).enterRoom(user);
	}

	/**
	 * checks whether all rooms are closed
	 *
	 * @return whether all rooms are closed
	 */
	private boolean allRoomsClosed() {
		for (ChatRoom room : rooms) {
			if (room.isOpen()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checks whether user is unique or not, checks by looking at id
	 * against existing users in room
	 *
	 * @return whether the user is unique
	 */
	private boolean hasUniqueID(User user) {
		for (User user1 : users) {
			if (user1.getUserID() == user.getUserID()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * sets the admin of the server
	 *
	 * @param admin the admin responsible for this server
	 */
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	/**
	 * gets the admin of the server
	 *
	 * @return the admin of server
	 */
	public Admin getAdmin() {
		return admin;
	}

	/**
	 * sets the server to open or closed
	 *
	 * @param open whether server should be open or closed
	 */
	public void setOpen(boolean open) {
		isOpen = open;
	}

	/**
	 * gets whether server is open
	 *
	 * @return whether server is open or not
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * sets the main room of the server
	 *
	 * @param mainRoom gets the main room of server
	 */
	public void setMainRoom(ChatRoom mainRoom) {
		this.mainRoom = mainRoom;
	}

	/**
	 * gets all rooms in server
	 *
	 * @return rooms in server
	 */
	public ArrayList<ChatRoom> getRooms() {
		return rooms;
	}

	/**
	 * gets capacity of server
	 *
	 * @return the maximum amount of people who can join server
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * gets the users in the server
	 *
	 * @return list of users in server
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * sets the capacity of the server
	 *
	 * @param capacity the new capacity of server
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * sets the rooms of the server
	 *
	 * @param rooms the new rooms of server
	 */
	public void setRooms(ArrayList<ChatRoom> rooms) {
		this.rooms = rooms;
	}

	/**
	 * sets the users of the server
	 *
	 * @param users the new users of server
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

}
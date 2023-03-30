import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing chat room, you can open, close
 * enter and leave rooms. This room is active as it interacts with chat server
 * through multiple methods. Each room is unique and has a capacity of users.
 */
public class ChatRoom {

	/**
	 * the unique id of this chat room
	 */
	private int roomID;
	/**
	 * capacity of this room
	 */
	private int capacity;
	/**
	 * list of users in this room
	 */
	private List<User> users;
	/**
	 * whether this room is open or closed
	 */
	private boolean isOpen;

	/**
	 * Used to initialise a chat room object, unique id and capacity of room is passed.
	 *
	 * @param chatRoomID unique id used to identify chat room
	 * @param capacity capacity of this chat room
	 */
	public ChatRoom(int chatRoomID, int capacity) {
		this.roomID = chatRoomID;
		this.capacity = capacity;
		this.users = new ArrayList<>();
	}

	/**
	 * opens the chat room
	 */
	public synchronized void open() {
		if (!isOpen) {
			System.out.println("Chat Room " + roomID + " is being opened.");
			isOpen = true;
		}
	}

	/**
	 * leaves all users from room and closes the room
	 */
	public synchronized void close() {
		for (int i = 0; i < users.size(); i++) {
			leaveRoom(users.get(i));
		}

		if (isOpen) {
			System.out.println("Chat Room " + roomID + " is being closed.");
			isOpen = false;
		}
	}

	/**
	 * attempts to enter user into room, the user must not already be in room, capacity must not be
	 * reached and the user must be unique
	 *
	 * @param user user attempting to join room
	 * @return whether user joined the room or not
	 */
	public synchronized boolean enterRoom(User user) {
		if (!users.contains(user) && users.size() + 1 < capacity && hasUniqueID(user)) {
			users.add(user);
			System.out.println("User " + user.getUserID() + " joined Chat Room " + roomID
					+ ". (" + user.getWantToChat() + ")");

			return true;
		} else {
			System.out.println("User " + user.getUserID() + " not joined Chat Room " + roomID
					+ ". (" + user.getWantToChat() + ")");

			return false;
		}
	}

	/**
	 * checks whether the user is unique by checking there id
	 * against other users in room
	 *
	 * @param user user being checked to be unique
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
	 * removes user from room
	 */
	public void leaveRoom(User user) {
		if (users.contains(user)) {
			users.remove(user);
			System.out.println("User " + user.getUserID() + " left Chat Room " + roomID
					+ ". (" + user.getWantToChat() + ")");
		}
	}

	/**
	 * checks whether the room is open or not
	 *
	 * @return is room open
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * get the id of this room
	 *
	 * @return room id
	 */
	public int getRoomID() {
		return roomID;
	}

	/**
	 * sets the list of users in this room
	 *
	 * @param users users in this room
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * gets the users of this room
	 *
	 * @return gets the users in this room
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * sets the capacity of this room
	 *
	 * @param capacity the new capacity of this room
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * gets the current capacity of this room
	 *
	 * @return capacity of room
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * sets the room to open or closed
	 *
	 * @param open whether room is open or closed
	 */
	public void setOpen(boolean open) {
		isOpen = open;
	}

	/**
	 * sets id of the chat room
	 *
	 * @param roomID the new room id
	 */
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

}
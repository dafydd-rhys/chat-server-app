import java.util.Random;

/**
 * This class is responsible for handling everything to do with the user,
 * this is an active class as it interacts with Chat servers and rooms.
 */
public class User implements Runnable {

	/**
	 * Default sleep time between actions
	 */
	private static int sleepScale = 1000;
	/**
	 * id of the current user
	 */
	private int userID;
	/**
	 * server the user is interacting with
	 */
	private ChatServer server;
	/**
	 * whether the user has joined a server
	 */
	private boolean joinedServer;
	/**
	 * whether the user has joined the main room
	 */
	private boolean joinedMainRoom;
	/**
	 * whether the user has joined a random room
	 */
	private boolean joinedRandomRoom;
	/**
	 * value used to identify how much the user wants to chat
	 */
	private int wantToChat;

	/**
	 * Creates a user object and assigns them a server
	 *
	 * @param userID id of current user
	 * @param chatServer server assigned to user
	 */
	public User(int userID, ChatServer chatServer) {
		this.userID = userID;
		this.server = chatServer;
		this.wantToChat = new Random().nextInt(6) + 10;
	}

	/**
	 * attempts to join the user to the chat server
	 *
	 * @return whether the user joined the server
	 */
	private boolean attemptJoinChatServer() throws InterruptedException {
		wantToChat -= 0.5;
		return server.join(this);
	}

	/**
	 * attempts to join the user to the main chat room
	 *
	 * @return whether the user joined the main chat room
	 */
	private boolean attemptJoinMainChatRoom() throws InterruptedException {
		wantToChat -= 1;
		return server.joinMainChatRoom(this);
	}

	/**
	 * attempts to join the user to a random chat room
	 *
	 * @return whether the user joined the random chat room
	 */
	private boolean attemptJoinRandomChatRoom() throws InterruptedException {
		wantToChat -= 2;
		return server.joinRandomChatRoom(this);
	}

	/**
	 * this method checks whether the user is in a server, main chat room or main room,
	 * if the user is not in any attempts to join one until successful when successful
	 * the user stays in the room until it decides to leave, this is repeated until wants to chat now
	 * equals 0
	 */
	@Override
	public void run() {
		while (wantToChat > 0) {
			try {
				//waits BETWEEN actions and NOT after every action (per specification)
				//if action fails, sleep as then it's between actions
				if (!joinedServer) {
					joinedServer = attemptJoinChatServer();

					if (!joinedServer) {
						Thread.sleep(new Random().nextInt(sleepScale + 1) + sleepScale);
					}
				} else if (!joinedMainRoom) {
					joinedMainRoom = attemptJoinMainChatRoom();

					if (!joinedMainRoom) {
						Thread.sleep(new Random().nextInt(sleepScale + 1) + sleepScale);
					}
				} else {
					joinedRandomRoom = attemptJoinRandomChatRoom();

					if (!joinedRandomRoom) {
						Thread.sleep(new Random().nextInt(sleepScale + 1) + sleepScale);
					}
				}

				//if in a room wait for random time between 2 and 5, then leaves
				if (joinedMainRoom || joinedRandomRoom) {
					int stayTime = new Random().nextInt(4) + 2;
					Thread.sleep((long) stayTime * sleepScale);

					wantToChat -= stayTime;
					server.leaveChatRoom(this);
				}
			} catch (InterruptedException e) {
				System.out.println("Interrupted User Thread (" + userID + ")");
			}
		}
		System.out.println("User Thread (" + userID + ") has ended!");

		server.leave(this);  // Leave the chat server
	}

	/**
	 * sets the id of the user
	 *
	 * @param userID new id of user
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	/**
	 * gets id of user
	 *
	 * @return id of user
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * sets want to chat value of user
	 *
	 * @param wantToChat want to chat value of user
	 */
	public void setWantToChat(int wantToChat) {
		this.wantToChat = wantToChat;
	}

	/**
	 * gets want to chat value
	 *
	 * @return want to chat value
	 */
	public int getWantToChat() {
		return wantToChat;
	}

	/**
	 * sets default sleep value
	 *
	 * @param sleepScale default sleep value
	 */
	public static void setSleepScale(int sleepScale) {
		User.sleepScale = sleepScale;
	}

	/**
	 * gets default sleep value
	 *
	 * @return sleep value
	 */
	public static int getSleepScale() {
		return sleepScale;
	}

	/**
	 * sets new server to assign user too
	 *
	 * @param server server being assigned to user
	 */
	public void setServer(ChatServer server) {
		this.server = server;
	}

	/**
	 * gets current chat server
	 *
	 * @return the chat server assigned to user
	 */
	public ChatServer getServer() {
		return server;
	}

	/**
	 * sets whether the user has joined the main room
	 *
	 * @param joinedMainRoom whether the user is in main room
	 */
	public void setJoinedMainRoom(boolean joinedMainRoom) {
		this.joinedMainRoom = joinedMainRoom;
	}

	/**
	 * gets whether the user has joined the main room
	 *
	 * @return whether the user has joined the main room
	 */
	public boolean isJoinedMainRoom() {
		return joinedMainRoom;
	}

	/**
	 * sets whether the user has joined the random room
	 *
	 * @param joinedRandomRoom whether the user has joined the random room
	 */
	public void setJoinedRandomRoom(boolean joinedRandomRoom) {
		this.joinedRandomRoom = joinedRandomRoom;
	}

	/**
	 * gets whether the user has joined the random room
	 *
	 * @return whether the user has joined the random room
	 */
	public boolean isJoinedRandomRoom() {
		return joinedRandomRoom;
	}

	/**
	 * sets whether the user has joined the server
	 *
	 * @param joinedServer whether the user has joined the server
	 */
	public void setJoinedServer(boolean joinedServer) {
		this.joinedServer = joinedServer;
	}

	/**
	 * gets whether the user has joined the server
	 *
	 * @return whether the user has joined the server
	 */
	public boolean isJoinedServer() {
		return joinedServer;
	}

}
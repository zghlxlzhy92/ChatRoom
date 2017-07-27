package hz44_rx4.chatApp.view;

import common.IChatroom;

/**
 * @author crespo5609
 * Adapter for Main Gui to communicate to model
 * @param <TDropList> 
 */
public interface IView2ModelAdapter<TIUser, TChatroom> {

	/**
	 * @param chatRoomName
	 */
	//public void creatRoom(String chatRoomName);

	/**
	 * @param username
	 */
	public void setUsername(String username);

	/**
	 * @param ipAddr
	 */
	public void connetToRemoteHost(String ipAddr);

	/**
	 * 
	 */
	public void quitApp();

	/**
	 * @param chatroom
	 */
	public void joinChatRoom(TChatroom chatroom);
	public void inviteUserToChatroom(TIUser user, TChatroom chatroom);
	public void getRoomList(TIUser user);
	public void creatRoom();
}

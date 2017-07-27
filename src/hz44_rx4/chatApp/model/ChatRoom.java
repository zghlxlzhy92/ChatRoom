package hz44_rx4.chatApp.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;

import common.IChatServer;
import common.IChatroom;
import common.OurDataPacket;
import common.msg.IChatMsg;
import provided.datapacket.ADataPacket;

/**
 * @author crespo5609
 * chat room
 */
public class ChatRoom implements IChatroom, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 729146528238550431L;
	/**
	 * chatRoomId
	 */
	private UUID chatRoomId;
	/**
	 * chatRoomName
	 */
	private String chatRoomName;
	/**
	 * chatServers
	 */
	private HashSet<IChatServer> chatServers;

	/**
	 * constructor
	 * @param name
	 */
	public ChatRoom(String name) {
		this.chatRoomId = UUID.randomUUID();
		this.chatRoomName = name;
		chatServers = new HashSet<IChatServer>();
	}

	@Override
	public UUID getId() {

		return chatRoomId;
	}

	@Override
	public String getName() {

		return chatRoomName;
	}

	public void setName(String name) {
		this.chatRoomName = name;
	}

	@Override
	public HashSet<IChatServer> getChatServers() {

		return this.chatServers;
	}

	@Override
	public boolean addChatServer(IChatServer chatServer) {
		if (chatServers.add(chatServer)) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean removeChatServer(IChatServer chatServer) {
		if (chatServers.remove(chatServer)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return this.chatRoomName;
	}

	@Override
	public <S> void send(OurDataPacket<? extends IChatMsg, S> dp) {
              		
	}

}

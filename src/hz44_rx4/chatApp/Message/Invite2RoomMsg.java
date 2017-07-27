package hz44_rx4.chatApp.Message;

import common.IChatServer;
import common.IChatroom;
import common.IUser;
import common.OurDataPacket;
import common.msg.IUserMsg;
import common.msg.user.IInvite2RoomMsg;

public class Invite2RoomMsg implements IInvite2RoomMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2865382307013686429L;
    private IChatroom chatRoom;
    private IChatServer sender;
    public Invite2RoomMsg(IChatroom chatroom, IChatServer sender) {
    	this.chatRoom = chatroom;
    	this.sender = sender;
	}
	@Override
	public IChatroom getChatroom() {
		return this.chatRoom;
	}
    public OurDataPacket<? extends IUserMsg,IChatServer> getDataPacket(){
    	 return new OurDataPacket<IInvite2RoomMsg, IChatServer>(IInvite2RoomMsg.class, this, sender);
    }
}

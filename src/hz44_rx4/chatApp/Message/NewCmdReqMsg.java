package hz44_rx4.chatApp.Message;

import java.util.UUID;

import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.INewCmdReqMsg;

public class NewCmdReqMsg implements INewCmdReqMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = 259007496323102509L;
    private Class<?> index;
    private IChatServer chatServer;
    public NewCmdReqMsg(Class<?> index, IChatServer chatServer) {
    	this.index = index;
    	this.chatServer = chatServer;
	}
	@Override
	public Class<?> getReqClassIdx() {
		return this.index;
	}
    public OurDataPacket<? extends IChatMsg, Object> getDataPacket(){
    	return new OurDataPacket<INewCmdReqMsg, Object>(INewCmdReqMsg.class, this, chatServer);
    }
	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

}

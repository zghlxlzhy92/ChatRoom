package hz44_rx4.chatApp.Message;

import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.IAddMeMsg;

public class AddMeMsg implements IAddMeMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8918460080153347496L;
    private IChatServer chatserver;
    public AddMeMsg(IChatServer iChatServer) {
    	this.chatserver = iChatServer;
	}

    public OurDataPacket<? extends IChatMsg, Object> getDataPacket(){
    	  return new OurDataPacket<AddMeMsg, Object>(AddMeMsg.class, this, chatserver);
    }
}

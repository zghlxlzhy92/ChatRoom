package hz44_rx4.chatApp.Message;

import java.util.UUID;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.IAddCmdMsg;
import common.msg.chat.INewCmdReqMsg;

public class AddCmdMsg implements IAddCmdMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5109959953523336922L;
	private Class<?> index;
	private AOurDataPacketAlgoCmd<?,IChatServer> cmd;
	private IChatServer sender;
    public AddCmdMsg(INewCmdReqMsg iNewCmdReqMsg,Class<?> index,AOurDataPacketAlgoCmd<?,IChatServer> cmd, IChatServer sender) {
    	super();
    	this.index = index;
    	this.cmd = cmd;
    	this.sender = sender;
	}
	@Override
	public AOurDataPacketAlgoCmd<?,IChatServer> getCmd() {
		return this.cmd;
	}

	@Override
	public Class<?> getClassIdx() {
		return this.index;
	}
	public OurDataPacket<?extends IChatMsg, IChatServer> getDataPacket(){
		return new OurDataPacket<AddCmdMsg, IChatServer>(AddCmdMsg.class, this, sender);
	}
	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

}

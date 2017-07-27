package hz44_rx4.server.serverMessage;

import java.util.UUID;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.IAddCmdMsg;
import common.msg.chat.INewCmdReqMsg;
import hz44_rx4.server.MessageCmd.StartGameCmd;

public class StartGameCmdRe implements IAddCmdMsg{
	/**
	 * 
	 */
	private static final long serialVersionUID = 298090050810609784L;
	private UUID uuid;
	private Class<?> unknownType;
	private IChatServer sender;
	public StartGameCmdRe(INewCmdReqMsg aNewCmdReqMsg,Class<?> unknownType,UUID uuid, IChatServer sender){
		//super(aNewCmdReqMsg);
		this.unknownType = unknownType;
		this.uuid = uuid;
		this.sender = sender;
	}
    public OurDataPacket<? extends IChatMsg, IChatServer> getDataPacket(){
    	return new OurDataPacket<IAddCmdMsg, IChatServer>(IAddCmdMsg.class, this, sender);
    }

    public AOurDataPacketAlgoCmd<?,IChatServer> getCmd(){
    	return new StartGameCmd(uuid);
    }
	@Override
	public Class<?> getClassIdx() {
		return unknownType;
	}
	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}
}

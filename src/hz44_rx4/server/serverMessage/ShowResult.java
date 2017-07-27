package hz44_rx4.server.serverMessage;

import java.util.UUID;

import common.IChatServer;
import common.IUser;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.IUserMsg;

public class ShowResult implements IChatMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4500201711708247376L;
    private UUID gameID;
    private IChatServer player;
    private long time;
    private IChatServer sender;
    public ShowResult(UUID gameID, IChatServer player, long time, IChatServer server){
    	this.gameID = gameID;
    	this.player = player;
    	this.time = time;
    	this.sender = server;
    }
    public UUID getID(){
    	return this.gameID;
    }
    public IChatServer getPlayer(){
    	return this.player;
    }
    public long getTime(){
    	return this.time;
    }
    public OurDataPacket<? extends IChatMsg,Object> getDataPacket(){
    	return new OurDataPacket<ShowResult, Object>(ShowResult.class, this, sender);
    }
}

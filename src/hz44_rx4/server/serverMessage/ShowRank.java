package hz44_rx4.server.serverMessage;

import java.util.UUID;

import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;

public class ShowRank implements IChatMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2344834938405683311L;
    private String rank;
    private IChatServer sender;
    public ShowRank(String rank, IChatServer sender){
    	this.rank = rank;
    	this.sender = sender;
    }
    public OurDataPacket<? extends IChatMsg, IChatServer> getDataPacket(){
    	return new OurDataPacket<>(ShowRank.class, this, sender);
    }
    public String getRank(){
    	return this.rank;
    }

}

package hz44_rx4.server.serverMessage;

import java.util.UUID;

import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;
import hz44_rx4.server.sController.GameFactory;


public class StartGame implements IChatMsg{

	/**
	 * 
	 */
	private GameFactory gameFactory;
	private IChatServer sender;
	private static final long serialVersionUID = 7721341980553945635L;
    public StartGame(GameFactory gameFactory, IChatServer sender){
    	this.gameFactory = gameFactory;
    	this.sender = sender;
    }
    public GameFactory get(){
    	return gameFactory;
    }
    public OurDataPacket<? extends IChatMsg, Object> getDataPacket(){
    	return new OurDataPacket<StartGame, Object>(StartGame.class, this, sender);
    }

}

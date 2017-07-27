package hz44_rx4.server.sController;

import java.io.Serializable;
import java.util.UUID;

import common.IChatServer;
import hz44_rx4.server.gController.GameController;
import hz44_rx4.server.gModel.Adapter;


public class GameFactory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6125777171644803727L;
    private IChatServer server;
    private IChatServer player;
    private UUID gameID;
    public GameFactory(IChatServer server, IChatServer player, UUID gameID){
    	this.server = server;
    	this.player = player;
    	this.gameID = gameID;
    }
    public Adapter makeGame(){
    	 GameController controller = new GameController();
    	 controller.start(server, player, gameID);
    	 return controller.getAdp();
    }
}

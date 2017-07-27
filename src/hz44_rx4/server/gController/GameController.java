package hz44_rx4.server.gController;

import java.io.Serializable;
import java.util.UUID;

import common.IChatServer;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import hz44_rx4.server.gModel.GameModel;
import hz44_rx4.server.gView.GameView;
import hz44_rx4.server.gView.GameViewModelAdpater;
import hz44_rx4.server.gModel.Adapter;
import hz44_rx4.server.gModel.GameModeViewAdapter;

public class GameController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3503592294669475702L;
    private GameView gameView;
    private GameModel gameModel;
    public GameController(){
    	 gameView = new GameView(new GameViewModelAdpater() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -5340015409147121840L;

			@Override
			public void timeOut() {
				  gameModel.timeOut();
			}
			
			@Override
			public String goToPlace(String answer) {
                    return gameModel.goToPlace(answer);				
			}
			
			@Override
			public void StartGame() {
                  gameModel.StartGame();				
			}
		});
    	 
    	gameModel  = new GameModel(new GameModeViewAdapter() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6808509385799294781L;

			@Override
			public void update() {
                  gameView.update();		
			}
			
			@Override
			public void disabledBton() {
                   gameView.disabledBton();				
			}
			
			@Override
			public void addLayer(Layer layer) {
                   gameView.addLayer(layer);				
			}

			@Override
			public void goToPlace(Position position) {
                     gameView.goToPlace(position);				
			}

			@Override
			public void showRank(String rank) {
                     gameView.showRank(rank);			
			}
		});
    	
    }
     public void start(IChatServer server, IChatServer player, UUID gameID){
    	        gameModel.setServer(server);
    	        gameModel.setPlayer(player);
    	        gameModel.setID(gameID);
    	        gameModel.start();
    	        gameView.start();
     }
     public Adapter getAdp(){
    	   return gameModel.getAdp();
     }
}

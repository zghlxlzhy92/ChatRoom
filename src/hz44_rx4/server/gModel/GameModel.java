package hz44_rx4.server.gModel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;

import javax.swing.Timer;


import common.IChatServer;
import common.IUser;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.util.WWUtil;
import hz44_rx4.server.serverMessage.ShowResult;
import map.MapLayer;

public class GameModel implements Serializable{
     /**
	 * 
	 */
	private static final long serialVersionUID = -2230920920118672713L;
	Stack<PosQsq> posQsqs = new Stack<>();
	Stack<String> answers = new Stack<>();
	private MapLayer mapLayer;
	private RenderableLayer renderableLayer;
	private GameModeViewAdapter gameModeViewAdapter;
	private ShapeAttributes shapeAttributes;
	private SurfaceCircle surfaceCircle;
	private Adapter adapter;
	int timeInterval = 1000;
	int timeInterval1 = 200;
	IChatServer server;
	IChatServer player;
	private UUID gameID;
	boolean pass;
	boolean start;
	long startTime;
	private Timer timer = new Timer(timeInterval, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
                   gameModeViewAdapter.update();			
		}
	});
   private Timer timer1 = new Timer(timeInterval1, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			  GameModel.this.detect();
		}
	});
    public void setServer(IChatServer server){
    	this.server = server;
    }
    public void setPlayer(IChatServer player){
    	this.player = player;
    }
    public void setID(UUID gameID){
    	this.gameID = gameID;
    }
	public GameModel(GameModeViewAdapter gameModeViewAdapter){
		 this.gameModeViewAdapter = gameModeViewAdapter;
		 
		 start = false;
		 pass = false;
		 adapter = new Adapter() {
			
			@Override
			public void show(String rank) {
                   GameModel.this.gameModeViewAdapter.showRank(rank);
			}
		};
		 
	}
	public Adapter getAdp(){
		return adapter;
	}
	public void detect() {
           if(!pass){
        	   PosQsq cur = posQsqs.peek();
        	   LatLon gap = cur.position.subtract(surfaceCircle.getCenter());
        	   if(Math.abs(gap.latitude.degrees) < 3.0 && Math.abs(gap.longitude.degrees) < 3.0){
        		   mapLayer.addAnnotation(new GlobeAnnotation(cur.question, cur.position));
        		   pass = true;
        		   posQsqs.pop();
        	   }
           }
	}
	
	public void timeOut(){
		 timer.stop();
		 timer1.stop();
		 gameModeViewAdapter.disabledBton();
		 try {
			  server.receive(new ShowResult(this.gameID, player, (long)600, server).getDataPacket());
		} catch (Exception e) {
			 System.err.println("fail to report final result when time is out");
			 e.printStackTrace();
		}
		 
	}
    public String goToPlace(String answer){
    	   if(!start)
    		   return " you have not started the game";
    	   else{
    		   long endTime = System.currentTimeMillis();
   			   long min = (endTime - startTime) / 60000;
   			   long sec = (endTime - startTime - min * 60000) / 1000;
               if(!answer.equals(answers.peek()))
                   return "Wrong anser" + min + "minutes" + sec + "seconds has passed";
               else if(posQsqs.isEmpty()){
            	    timer.stop();
            	    timer1.stop();
            	    gameModeViewAdapter.disabledBton();
            	    try {
          			  server.receive(new ShowResult(this.gameID, player, endTime - startTime, server).getDataPacket());
          	     	} catch (Exception e) {
          			 System.err.println("fail to report final result when time is out");
          			 e.printStackTrace();
          		   }
            	   return "You have finised this game" + min + "minutes" + sec + "seconds has passed";
               }
               else{
            	   mapLayer.removeAllAnnotations();
            	   answers.pop();
            	   pass = false;
            	   PosQsq cur = posQsqs.peek();
   				   cur.position.add(LatLon.fromDegrees((new Random().nextDouble()) * 5, (new Random().nextDouble()) * 5));
                   gameModeViewAdapter.goToPlace(cur.position);
                   return min + "minutes" + sec + "seconds has passed";
               }
    	   }
    }
	public void start(){
		shapeAttributes = new BasicShapeAttributes();
        shapeAttributes.setInteriorMaterial(Material.PINK);
        shapeAttributes.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.PINK)));
        shapeAttributes.setInteriorOpacity(0.5);
        shapeAttributes.setOutlineOpacity((double) 1);
        shapeAttributes.setOutlineWidth(3);  
        surfaceCircle = new SurfaceCircle(LatLon.fromDegrees(30, 30), 0.5e6);
        surfaceCircle.setAttributes(shapeAttributes);
        renderableLayer.addRenderable(surfaceCircle);
		load();
		gameModeViewAdapter.addLayer(mapLayer);
		gameModeViewAdapter.addLayer(renderableLayer);
		
		
	}
    private void load(){
    	 posQsqs.add(new PosQsq(Position.fromDegrees(40, 50), "what is the country neariest to the south pole?"));
    	 posQsqs.add(new PosQsq(Position.fromDegrees(-40, -60), "what is the capital of Iceland?"));
    	 posQsqs.add(new PosQsq(Position.fromDegrees(100,60), "When did kennedy get killed?"));
    	 posQsqs.add(new PosQsq(Position.fromDegrees(200, 30), "When was the first version of Godzilla on screen?"));
    	 posQsqs.add(new PosQsq(Position.fromDegrees(120, 300), "Who invented the telegram?"));
    	 posQsqs.add(new PosQsq(Position.fromDegrees(-40, 80), "What is the name of the actor who played the winter soidier?"));
    	 answers.add("Argentina");
    	 answers.add("Reykjavik");
    	 answers.add("1963");
    	 answers.add("1954");
    	 answers.add("Sameul Morse");
    	 answers.add("Sebastion Stan");

    }
    public void StartGame(){
    	if(start) return;
    	start = true;
    	timer.start();
    	timer1.start();
    	startTime = System.currentTimeMillis();
 	    PosQsq cur = posQsqs.peek();
		cur.position.add(LatLon.fromDegrees((new Random().nextDouble()) * 5, (new Random().nextDouble()) * 5));
        gameModeViewAdapter.goToPlace(cur.position);
    }
}

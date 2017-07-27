package hz44_rx4.server.sModel;


import java.util.List;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import java.util.UUID;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.IChatroom;
import common.ICmd2ModelAdapter;
import common.IUser;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.IUserMsg;
import common.msg.chat.INewCmdReqMsg;
import common.msg.user.IInvite2RoomMsg;
import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import provided.util.IVoidLambda;
import hz44_rx4.chatApp.model.ChatRoom;
import hz44_rx4.server.ChatServer;
import hz44_rx4.server.User;
import hz44_rx4.server.sController.GameFactory;
import hz44_rx4.server.serverMessage.ShowRank;
import hz44_rx4.server.serverMessage.ShowRankCmdRe;
import hz44_rx4.server.serverMessage.ShowResult;
import hz44_rx4.server.serverMessage.StartGame;
import hz44_rx4.server.serverMessage.StartGameCmdRe;

public class ServerModel {
	   private ServerModelViewAdapter serverModelViewAdapter;
	   private IChatServer server;
	   private IUser sUser;
	   private DataPacketAlgo<Void, Object> userAlgo;
	   private DataPacketAlgo<Void, Object> chatAlgo;
       private IChatroom chatroom = new ChatRoom("Game");
	   private Map<UUID, Team> teamSet = new HashMap<>();
	   private UUID adpterID;
	   private List<Long> results = new ArrayList<>();
	   private Timer timer = new Timer(200, (e)->ServerModel.this.check());
	   private IRMIUtils rmiUtils;
	   private Registry registry;
       public ServerModel(ServerModelViewAdapter serverModelViewAdapter){
           rmiUtils = new RMIUtils(new IVoidLambda<String>() {
	            
	            @Override
	            public void apply(String... params) {
	                for (String s : params) {
	                	System.out.println(s);
	                }
	            }});

    	   this.serverModelViewAdapter = serverModelViewAdapter;
    	   adpterID = UUID.randomUUID();
           userAlgo = new DataPacketAlgo<Void, Object>(null);
    	   sUser = new User("Game",System.getProperty("java.rmi.server.hostname"), userAlgo)
         {  
    		/**
			 * 
			 */
			private static final long serialVersionUID = -3357400994194820256L;
			    @Override
                public<IChatServer> void receive(OurDataPacket<? extends IUserMsg, IChatServer> dp) throws RemoteException {
        		       dp.execute(userAlgo);
        		}
           };
           userAlgo.setCmd(IInvite2RoomMsg.class, new AOurDataPacketAlgoCmd<IInvite2RoomMsg, IChatServer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1735615850918056394L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<IInvite2RoomMsg,IChatServer> host, Object... params) {
				UUID gameID = host.getData().getChatroom().getId();
				if(teamSet.containsKey(gameID))
					return null;
				teamSet.put(gameID, new Team(host.getData().getChatroom().getChatServers()));
				for(IChatServer player : host.getData().getChatroom().getChatServers()){
					    try {
					    	player.receive(new StartGame(new GameFactory(server, player, gameID), server).getDataPacket());
							
						} catch (Exception e) {
							  System.err.println("it is an error when sending a game to the clients");
							  e.printStackTrace();
						}
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				
			}


		});
           chatAlgo = new DataPacketAlgo<Void, Object>(null);
    	   server = new ChatServer(sUser,chatroom,chatAlgo)
         {  
    		/**
			 * 
			 */
			private static final long serialVersionUID = -3357400994194820256L;

				public<IChatServer> void receive(OurDataPacket<? extends IChatMsg, IChatServer> dp) throws RemoteException {
        		       dp.execute(userAlgo);
        		}
           };

           chatAlgo.setCmd(INewCmdReqMsg.class, new AOurDataPacketAlgoCmd<INewCmdReqMsg,IChatServer>() {

			@Override
			public Void apply(Class<?> index, OurDataPacket<INewCmdReqMsg,IChatServer> host, Object... params) {
				IChatServer sender = host.getSender();
				try {
					if(host.getData() .equals(StartGame.class)){
					sender.receive(new StartGameCmdRe(host.getData(),host.getData().getReqClassIdx(), adpterID, server).getDataPacket());
					}
					else
						sender.receive(new ShowRankCmdRe(host.getData(), host.getData().getReqClassIdx(), adpterID, server).getDataPacket());
					
				} catch (Exception e) {
					System.err.println("Cannot not handle the ACommandRequest. Exception: " + e + "\n");
					e.printStackTrace();

				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				
			}
		});
         chatAlgo.setCmd(ShowResult.class, new AOurDataPacketAlgoCmd<ShowResult,IChatServer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3412195401437071283L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<ShowResult,IChatServer> host, Object... params) {
				UUID gameID = host.getData().getID();
				IChatServer player = host.getData().getPlayer();
				Team curTeam = teamSet.get(gameID);
				curTeam.result = Math.min(curTeam.result, host.getData().getTime());
				curTeam.replier.add(player);
				if(curTeam.player.size() == curTeam.replier.size()){
					results.add(curTeam.result);
					teamSet.remove(gameID);
					Collections.sort(results);
					int rank = results.indexOf(curTeam.result) + 1;
					String score = "The final result for your team is" + rank;
					serverModelViewAdapter.ShowScore(score);
					for(IChatServer client : curTeam.player){
						  try {
							client.receive(new ShowRank(score, server).getDataPacket());
						} catch (Exception e) {
							System.err.println("Exception happened when finding a team having crashed teammates: " + e);
							e.printStackTrace();
                        }
					}
				}
				return null;
			}

			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				
			}
        	 
		});
       }
      public void start(){ 
    	  try {
  			IUser sStub = (IUser) UnicastRemoteObject.exportObject(sUser, IUser.BOUND_PORT_SERVER);
  			IChatServer chatStub = (IChatServer) UnicastRemoteObject.exportObject(server, IChatServer.BOUND_PORT_SERVER);
  			rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);
  			registry = rmiUtils.getLocalRegistry();
  			registry.rebind(IUser.BOUND_NAME, sStub);
  			//registry.rebind(IChatServer., chatStub);
  			System.out.println("SERVER HAS BEEN CREATED");

			  
		} catch (RemoteException e) {
			System.err.println("Connect exception:" + e + "\n");
			e.printStackTrace();
			System.exit(-1);

		}
    	 timer.start();

      }
      public void close() throws RemoteException{
			try {
				registry.unbind(IUser.BOUND_NAME);
				//registry.unbind(IChatServer.BOUND_NAME);
				System.out.println("Server's stub has been unbounded.");
				rmiUtils.stopRMI();
				System.exit(0);
			} catch (NotBoundException e) {
				System.err.println("Error unbounding.");
				e.printStackTrace();
				System.exit(-1);
           }
			
    }
      public void check(){
    	  for(UUID gameID : teamSet.keySet()){
    		  Team team = teamSet.get(gameID);
    		  if(team.result == 600 && team.replier.size() != team.player.size()){
					teamSet.remove(gameID);
					Collections.sort(results);
					int rank = results.indexOf(team.result) + 1;
					String score = "The final result for your team is" + rank;
					serverModelViewAdapter.ShowScore(score);
					for(IChatServer client : team.player){
						  try {
							client.receive(new ShowRank(score, server).getDataPacket());
						} catch (Exception e) {
							System.err.println("Exception happened when finding a team having crashed teammates: " + e);
							e.printStackTrace();
                      }
					}
    		  }
    	  }
      }
}

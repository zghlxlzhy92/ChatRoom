

package hz44_rx4.Game.model;

import java.rmi.RemoteException;
import java.rmi.dgc.Lease;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.swing.JPanel;

import org.junit.validator.PublicClassValidator;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.IChatroom;
import common.ICmd2ModelAdapter;
import common.IUser;
import common.OurDataPacket;
import common.msg.user.IInvite2RoomMsg;
import hz44_rx4.chatApp.Message.AddMeMsg;
import hz44_rx4.chatApp.Message.Invite2RoomMsg;
import hz44_rx4.chatApp.Message.LeaveMsg;
import hz44_rx4.server.ChatServer;
import hz44_rx4.server.User;
import hz44_rx4.server.gView.Map;
import provided.datapacket.DataPacketAlgo;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import provided.util.IVoidLambda;

/**
 * @author crespo5609
 * Main model 
 */
public class ChatAppModel {
	  private IModel2ViewAdapter iModel2ViewAdapter;
	  private DataPacketAlgo<Void, Object> initAlgo = new DataPacketAlgo<>(null);
	  private Set<MiniModel> chatRooms = new HashSet<>();
	  private HashMap<String,IUser> friends = new HashMap<>();
	  private String ip;
	  private IUser me;
	  private IUser stub;
	  private IChatServer serverStub;
	  private IChatServer chatServer;
	  private Registry registry;
	  private String username;
	  private List<Long> results = new ArrayList<>();
      public ChatAppModel(IModel2ViewAdapter iModel2ViewAdapter){
		  this.iModel2ViewAdapter = iModel2ViewAdapter;
	      initDataPacketAlgo();
	      registry = new Registry(new IVoidLambda<String>() {
	            
	            @Override
	            public void apply(String... params) {
	                for (String s : params) {
	                    iModel2ViewAdapter.append(String.format("%s\n", s));
	                }
	            }
	        });

	  }
      public void initDataPacketAlgo(){
    	  initAlgo.setDefaultCmd(new AOurDataPacketAlgoCmd<Object, Object>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7891213878825438683L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<Object, Object> host, Object... params) {
                iModel2ViewAdapter.append(String.format("Received Unknown data packet from %s.\n",
                        params[0].toString()));
                registry = new Registry(new IVoidLambda<String>() {
                    
                    @Override
                    public void apply(String... params) {
                        for (String s : params) {
                            iModel2ViewAdapter.append(String.format("%s\n", s));
                        }
                    }});
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				
			}
    		  
		});
        initAlgo.setCmd(IInvite2RoomMsg.class, new AOurDataPacketAlgoCmd<IInvite2RoomMsg, IChatServer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8741140536016426146L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<IInvite2RoomMsg,IChatServer> host, Object... params) {
				iModel2ViewAdapter.append("reveive and invitation");
				IChatroom chatroom = host.getData().getChatroom();
				String name = chatroom.getName();
				if(chatRooms.contains(chatroom))
					 return null;
				else{
	                if (iModel2ViewAdapter.willJoinChatroom(host.getSender().toString(), name)) {
	                    joinChatroom(chatroom, name);
	                }

				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}
		});
      }
      public void start(){
    	    stub = new User(username, ip, initAlgo);
    	    me = registry.register(stub);
    	    serverStub = new ChatServer(me, null, null);
    	    chatServer = registry.register(serverStub);
    	    ip =registry.getIP();
            iModel2ViewAdapter.setTitle(String.format("%s: %s", username, ip));
            try {
				iModel2ViewAdapter.addUser(me.getName(),me);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
      }
      public void setName(String username){
    	   this.username = username;
      }
      public void connect(String ip){
    	  if(friends.containsKey(ip)){
    		  iModel2ViewAdapter.append("you have this friend");
    		  return;
    	  }
    	  IUser friend = registry.connectToUser(ip);
    	  friends.put(ip, friend);
    	  try {
			iModel2ViewAdapter.addUser(friend.getName(),friend);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
          iModel2ViewAdapter.append(String.format("Successfully connected to the user %s!\n", friend));

      }
      public MiniModel creatNewChatroom(UUID uuid,String name){
          MiniModel chatroomModel = new MiniModel(uuid, name, ip, me);
          chatRooms.add(chatroomModel);
          try {
			me.getChatrooms().add(chatroomModel);
      	    System.out.println(me.getChatrooms().size());

		} catch (RemoteException e) {
			e.printStackTrace();
		}
          chatroomModel.setAdapter(iModel2ViewAdapter.makeChatroomViewAdapter(chatroomModel));
          //iModel2ViewAdapter.addChatroom(chatroomModel);
          chatroomModel.start();
          return chatroomModel;
 	  
      }
      public void getChatRoomList(IUser user){
    	  try {
			Set<IChatroom> chatrooms = user.getChatrooms();
			System.out.println(chatrooms.size());
			for(IChatroom chatroom : chatrooms){
				iModel2ViewAdapter.addChatroom((MiniModel) chatroom);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
      }
      public void invite(IUser user, IChatroom chatroom){
          try {
			if (user.getIP().equals(ip)) {
			      iModel2ViewAdapter.append("Will not invite myself\n");
			      return;
			  }
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
          
          if (chatRooms.contains(user)) {
              return;
          }
          
          (new Thread() {
              
              @Override
              public void run() {
                  super.run();
                  try {
                      user.receive(new Invite2RoomMsg(chatroom, chatServer).getDataPacket());
                  } catch (RemoteException e) {
                      e.printStackTrace();
                  }
              }
          }).start();
          
          try {
			iModel2ViewAdapter.append(String.format("Successfully sent a InviteToChatroom to %s at IP: %s\n", 
			                                   user.getName(), 
			                                   user.getIP()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

      }
      public void joinChatroom(IChatroom chatroom, String name){
          MiniModel joinRoom = creatNewChatroom(chatroom.getId(), name);
          for (IChatServer otherUser : chatroom.getChatServers()) {
              joinRoom.addChatServer(otherUser);
          }
          joinRoom.send(new AddMeMsg(joinRoom.getChatServer()).getDataPacket());
          joinRoom.addChatServer(joinRoom.getChatServer());
      }
      public void quitChatroom(MiniModel chatroom) {
          chatroom.removeChatServer(chatroom.getChatServer());
          chatroom.send(new LeaveMsg(chatroom.getChatServer()).getDataPacket());
          //viewAdapter.removeChatroom(chatroom);
          chatRooms.remove(chatroom);
      }
      
      /**
       * Quite all chat rooms
       */
      public void quitAllChatrooms() {
          for (MiniModel chatroom : chatRooms) {
              chatroom.removeChatServer(chatroom.getChatServer());
              chatroom.send(new LeaveMsg(chatroom.getChatServer()).getDataPacket());
          }
      }


}

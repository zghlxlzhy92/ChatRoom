package hz44_rx4.chatApp.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.DoublePredicate;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.IChatroom;
import common.ICmd2ModelAdapter;
import common.IComponentFactory;
import common.IUser;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.IAddCmdMsg;
import common.msg.chat.ILeaveMsg;
import common.msg.chat.INewCmdReqMsg;
import common.msg.chat.ITextMsg;
import hz44_rx4.chatApp.Message.AddMeMsg;
import hz44_rx4.chatApp.Message.NewCmdReqMsg;
import hz44_rx4.chatApp.Message.TextMsg;
import hz44_rx4.server.ChatServer;
import hz44_rx4.server.gView.Map;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacketAlgo;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.util.IVoidLambda;

public class MiniModel implements IChatroom{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2042773543868922096L;
	private MiniModel2ViewAdapter chatAdapter;
	private IChatServer hostServer;
	private IChatServer hostStub;
	private DataPacketAlgo<Void, Object> dataPacketAlgo = new DataPacketAlgo<>(null);
	private UUID uuid;
	private String name;
	private String ip;
	private IUser user;
	private HashSet<IChatServer> chatServers = new HashSet<>();
	private HashMap<Class<?>, OurDataPacket<Object,IChatServer>> unkownMessages = new HashMap<>();
	private IRMIUtils rmiUtils;	
	private Registry registry;

	private transient ICmd2ModelAdapter Adapter = new ICmd2ModelAdapter() {
		private MixedDataDictionary mixedDataDictionary = new MixedDataDictionary();
		@Override
		public <T extends IChatMsg> void sendMsg2LocalChatroom(Class<T> index, T msg) {
                     // dataPacketAlgo.setCmd(index, msg);			
		}
		
		@Override
		public <T> boolean putIntoLocalDict(MixedDataKey<T> key, T value) {
			mixedDataDictionary.put(key, value);
			return true;
		}
		
		@Override
		public <T> T getFromLocalDict(MixedDataKey<T> key) {
			mixedDataDictionary.get(key);
			return null;
		}
		
		@Override
		public IChatServer getChatServer() {
			return hostServer;
		}
		
		@Override
		public void buildComponentInScrollable(IComponentFactory fac) {
                  chatAdapter.addComponent(fac.make());			
		}
		
		@Override
		public void buildComponentInNonScrollable(IComponentFactory fac) {
			// TODO Auto-generated method stub
			
		}
	};
    public MiniModel(UUID id, String name, String ip, IUser user) {
	    registry = new Registry(new IVoidLambda<String>() {
            
            @Override
            public void apply(String... params) {
                for (String s : params) {
                	System.out.println(s);
                }
            }
        });
        this.uuid = id;
        this.name = name;
        this.ip = ip;
        this.user = user;
        hostStub = new ChatServer(this.user, this, dataPacketAlgo);
        register(hostStub);
        initDataPacketAlgo();

        
    }
    public void register(IChatServer chatServer){
                hostServer = registry.register(chatServer);
				System.out.println("SERVER HAS BEEN CREATED");
    }
    public void setAdapter(MiniModel2ViewAdapter miniModel2ViewAdapter){
    	  this.chatAdapter = miniModel2ViewAdapter;
    }
    public void start(){
    	 chatAdapter.setTitle(name);
    	 chatAdapter.start();
    }
    public void initDataPacketAlgo(){
    	dataPacketAlgo.setDefaultCmd(new AOurDataPacketAlgoCmd<Object,IChatServer>(){
           	/**
			 * 
			 */
			private static final long serialVersionUID = -8702716480751320792L;

			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}

		@Override
		public Void apply(Class<?> index, OurDataPacket<Object,IChatServer> host, Object... params) {
            unkownMessages.put(index, host);
            try {
				host.getSender().receive(new NewCmdReqMsg(index, hostServer).getDataPacket());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return null;
		}
    		  
    	});
    	dataPacketAlgo.setCmd(IAddCmdMsg.class, new AOurDataPacketAlgoCmd<IAddCmdMsg,IChatServer>() {


			/**
			 * 
			 */
			private static final long serialVersionUID = -8699079017850166314L;

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Void apply(Class<?> index, OurDataPacket<IAddCmdMsg,IChatServer> host, Object... params) {
				IAddCmdMsg data = host.getData();
				AOurDataPacketAlgoCmd<?, IChatServer> cmd = data.getCmd();
				dataPacketAlgo.setCmd(data.getClassIdx(), cmd);
				cmd.setCmd2ModelAdpt(Adapter);
				unkownMessages.get(data.getClassIdx()).execute(dataPacketAlgo, host.getSender());
				unkownMessages.remove(unkownMessages.get(data.getClassIdx()));
				return null;
			}

		});
         dataPacketAlgo.setCmd(ITextMsg.class, new AOurDataPacketAlgoCmd<ITextMsg, IChatServer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7019358715581532590L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<ITextMsg, IChatServer> host, Object... params) {
				try {
					chatAdapter.sendMessage(String.format("%s: %s\n", host.getSender().getUser().getName(),host.getData().getContent()));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				
			}
		});
         dataPacketAlgo.setCmd(AddMeMsg.class, new AOurDataPacketAlgoCmd<AddMeMsg, IChatServer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8145217986466897895L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<AddMeMsg, IChatServer> host, Object... params) {
				addChatServer(host.getSender());
				return null;
			}

			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}
        	 
		});
         dataPacketAlgo.setCmd(ILeaveMsg.class, new AOurDataPacketAlgoCmd<ILeaveMsg, IChatServer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7209617789409971942L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<ILeaveMsg, IChatServer> host, Object... params) {
				removeChatServer(host.getSender());
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				
			}
		});
    }
	
	@Override
	public UUID getId() {
		return uuid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public HashSet<IChatServer> getChatServers() {
		return chatServers;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	@Override
	public boolean addChatServer(IChatServer chatStub) {
        if (!chatServers.contains(chatStub)) {
            chatServers.add(chatStub);
            try {
				chatAdapter.addUser(chatStub.getUser().getName());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
            return true;
        }
        
        return false;
	}

	@Override
	public boolean removeChatServer(IChatServer chatServer) {
        if (chatServers.contains(chatServer)) {
            try {
				chatAdapter.removeUser(chatServer.getUser().getName());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
            chatServers.remove(chatServer);
        }

		return false;
	}
    public void sendMessage(String message){
    	 send(new TextMsg(message, hostServer).getDataPacket());
    }
    public IChatServer getChatServer(){
    	return this.hostServer;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = hash * 23 + uuid.hashCode();
        hash = hash * 11 + name.hashCode();
        return hash;
    }

    /**
     * Check if this chat room equals to other object
     * @param obj The other obj to check
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj instanceof IChatroom) {
            IChatroom chatroom = (IChatroom)obj;
            return uuid.equals(chatroom.getId());
        }
        
        return false;
    }

    /**
     * Override the toString method
     * @return The string
     */
    @Override
    public String toString() {
        return name;
    }

	@Override
	public <S> void send(OurDataPacket<? extends IChatMsg, S> message) {
        (new Thread() {

			@Override
            public void run() {
                super.run();
                for (IChatServer chatServer : chatServers) {
                    try {
                    	chatServer.receive(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        }).start();		
	}


}

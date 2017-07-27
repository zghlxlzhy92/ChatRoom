package hz44_rx4.server;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.UUID;

import common.IChatroom;
import common.IUser;
import common.OurDataPacket;
import common.msg.IUserMsg;
import provided.datapacket.DataPacketAlgo;

public class User implements IUser{
    private String name;
    private UUID uuid;
    private String ip;
    private DataPacketAlgo<Void, Object> algo;
    public HashSet<IChatroom> chatrooms;
    public User(String name, String ip, DataPacketAlgo<Void, Object> algo){
    	this.name = name;
    	this.ip = ip;
    	this.algo = algo;
    	this.uuid = UUID.randomUUID();
    	chatrooms = new HashSet<>();
    }


	@Override
	public void connectBack(IUser userStub) throws RemoteException {
		
	}

	@Override
	public UUID getId() throws RemoteException {
		return uuid;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public String getIP() throws RemoteException {
		return ip;
	}

	@Override
	public HashSet<IChatroom> getChatrooms() throws RemoteException {
		return chatrooms;
	}
	@Override
	public <S> void receive(OurDataPacket<? extends IUserMsg, S> dp) throws RemoteException {
              dp.execute(algo);	
	}
}

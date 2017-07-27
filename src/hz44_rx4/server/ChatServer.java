package hz44_rx4.server;

import java.rmi.RemoteException;

import common.IChatServer;
import common.IChatroom;
import common.IUser;
import common.OurDataPacket;
import common.msg.IChatMsg;
import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacketAlgo;

public class ChatServer implements IChatServer{
    private IUser user;
    private IChatroom chatRoom;
    private DataPacketAlgo<Void, Object> algo;

    public ChatServer(IUser user, IChatroom chatroom, DataPacketAlgo<Void, Object> algo) 
    {
    	this.user = user;
    	this.chatRoom = chatroom;
    	this.algo = algo;
	}

	@Override
	public IUser getUser() throws RemoteException {
		return user;
	}

	@Override
	public IChatroom getChatroom() throws RemoteException {
		return chatRoom;
	}
	@Override
	public <S> void receive(OurDataPacket<? extends IChatMsg, S> dp) throws RemoteException {
		dp.execute(algo);
 	}
}

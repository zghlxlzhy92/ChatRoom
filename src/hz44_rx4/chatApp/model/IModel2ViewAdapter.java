package hz44_rx4.chatApp.model;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.UUID;
import javax.swing.JPanel;
import common.IChatServer;
import common.IChatroom;
import common.IUser;

/**
 * @author crespo5609
 * Adapter for model to communicate to view
 * @param <TDropList> 
 */
public interface IModel2ViewAdapter {

	public void append(String format);
	public MiniModel2ViewAdapter makeChatroomViewAdapter(MiniModel miniModel);
    public boolean willJoinChatroom(String user, String charName);
    public void addChatroom(MiniModel miniModel);
    public void addUser(String name, IUser user);
    public void setTitle(String sentence);
	/**
	 * @param myChatServer
	 * @return mini MVC Adpt
	 * @throws RemoteException
	 */


}

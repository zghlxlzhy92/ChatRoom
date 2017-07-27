package hz44_rx4.Game.model;

import java.awt.Component;
import java.rmi.RemoteException;

import javax.swing.JPanel;

import common.IUser;

/**
 * @author crespo5609
 * MiniModel2ViewAdapter
 */
public interface MiniModel2ViewAdapter {

	/**
	 * 
	 */
	public static final MiniModel2ViewAdapter NULL_OBJECT = new MiniModel2ViewAdapter() {


		@Override
		public void sendMessage(String msg) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addComponent(Component component) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addUser(String user) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeUser(String user) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void start() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setTitle(String name) {
			// TODO Auto-generated method stub
			
		}

	};



	/**
	 * sendMessage
	 * @param msg
	 */
	public void sendMessage(String msg);
	public void addComponent(Component component);
	public void addUser(String user);
	public void removeUser(String user);
	public void start();
	public void setTitle(String name);
}

package hz44_rx4.server.sController;

import java.rmi.RemoteException;

import javax.swing.SwingUtilities;

import hz44_rx4.server.sModel.ServerModel;
import hz44_rx4.server.sView.ServerView;
import hz44_rx4.server.sView.ServerViewModelAdapter;
import hz44_rx4.server.sModel.ServerModelViewAdapter;

public class ServerController {
	/**
	 * Game's view
	 */
	private ServerView sView;
	
	/**
	 * Game's model
	 */
	private ServerModel sModel;
	
	/**
	 * Constructor
	 */
	public ServerController() {
		sView = new ServerView(new ServerViewModelAdapter() {
			
			@Override
			public void close() {
                   try {
					sModel.close();
				} catch (RemoteException e) {
					e.printStackTrace();
				}				
			}
		});
		
		
		sModel = new ServerModel(new ServerModelViewAdapter() {
			
			@Override
			public void ShowScore(String score) {
				  sView.ShowScore(score);
				
			}
		});
	}
	
	/**
	 * The main method
	 * @param args Useless arguments
	 */
	public static void main(String[] args) {
		final ServerController[] c = new ServerController[1];
		
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				c[0] = new ServerController();
				c[0].start();
			}
			
		});
		
	}
	
	/**
	 * Start the server
	 */
	public void start() {
		sView.start();
		sModel.start();
	}

}

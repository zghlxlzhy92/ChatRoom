package hz44_rx4.Game.view;

import javax.swing.ImageIcon;

/**
 * @author crespo5609
 * MiniView2ModelAdapter
 */
public interface MiniView2ModelAdapter {

	/**
	 * 
	 */
	public static final MiniView2ModelAdapter NULL_OBJECT = new MiniView2ModelAdapter() {
		@Override
		public void exitRoom() {
			// TODO Auto-generated method stub

		}

		@Override
		public void sendMessage(String msg) {
			// TODO Auto-generated method stub

		}

		@Override
		public void sendFile(ImageIcon image) {
			// TODO Auto-generated method stub

		}

		@Override
		public void startGame() {
			// TODO Auto-generated method stub
			
		}

	};

	/**
	 * 
	 */
	public void exitRoom();

	/**
	 * @param msg
	 */
	public void sendMessage(String msg);

	/**
	 * @param image
	 */
	public void sendFile(ImageIcon image);
	
	public void startGame();
}

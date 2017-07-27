package hz44_rx4.server.MessageCmd;

import java.util.UUID;

import javax.swing.SwingUtilities;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.ICmd2ModelAdapter;
import common.OurDataPacket;
import hz44_rx4.server.gModel.Adapter;
import hz44_rx4.server.serverMessage.StartGame;
import provided.mixedData.MixedDataKey;


public class StartGameCmd extends AOurDataPacketAlgoCmd<StartGame,IChatServer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4473888689110811288L;
    private ICmd2ModelAdapter adpt;
    private UUID uuid;
    public StartGameCmd(UUID uuid) {
    	this.uuid = uuid;
	}
	

	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
            	this.adpt = cmd2ModelAdpt;	
	}
	@Override
	public Void apply(Class<?> index, OurDataPacket<StartGame, IChatServer> host, Object... params) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Adapter adapter = host.getData().get().makeGame();
				adpt.putIntoLocalDict(new MixedDataKey<Adapter>(uuid, "Adapter", Adapter.class), adapter);
			}
			
		});
		return null;
		
	}
     
}

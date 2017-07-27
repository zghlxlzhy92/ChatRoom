package hz44_rx4.server.MessageCmd;

import java.util.UUID;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.ICmd2ModelAdapter;
import common.OurDataPacket;
import hz44_rx4.server.gModel.Adapter;
import hz44_rx4.server.serverMessage.ShowRank;
import provided.mixedData.MixedDataKey;


public class ShowRankCmd extends AOurDataPacketAlgoCmd<ShowRank,IChatServer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5420368865966994377L;
    private UUID uuid;
	transient private ICmd2ModelAdapter adpt;

    public ShowRankCmd(UUID uuid){
    	this.uuid = uuid;
    }
	
	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		  this.adpt = cmd2ModelAdpt;
	}
	@Override
	public Void apply(Class<?> index, OurDataPacket<ShowRank, IChatServer> host, Object... params) {
		Adapter adapter = adpt.getFromLocalDict(new MixedDataKey<Adapter>(uuid, "Adapter", Adapter.class));
		adapter.show(host.getData().getRank());
		return null;
	}
    
}

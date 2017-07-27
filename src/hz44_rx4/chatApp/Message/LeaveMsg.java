package hz44_rx4.chatApp.Message;


import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.ILeaveMsg;

public class LeaveMsg implements ILeaveMsg{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6221547152845637338L;
	private IChatServer sender;
	public LeaveMsg(IChatServer sender){
		 this.sender = sender;
	}
    public OurDataPacket<? extends IChatMsg, Object> getDataPacket(){
  	  return new OurDataPacket<LeaveMsg, Object>(LeaveMsg.class, this, sender);
  }
}

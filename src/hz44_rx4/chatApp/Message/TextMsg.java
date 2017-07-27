package hz44_rx4.chatApp.Message;

import common.IChatServer;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.ITextMsg;

public class TextMsg implements ITextMsg{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8151377155240321035L;
    private String content;
    private IChatServer sender;
    public TextMsg(String content, IChatServer sender){
    	this.content = content;
    	this.sender = sender;
    }
	public String getContent() {
		return this.content;
	}
	public OurDataPacket<? extends IChatMsg, Object> getDataPacket(){
		return new OurDataPacket<ITextMsg, Object>(ITextMsg.class, this, sender);
	}
}

package hz44_rx4.server.gModel;

import java.io.Serializable;

import gov.nasa.worldwind.geom.Position;

public class PosQsq implements Serializable{
     /**
	 * 
	 */
	private static final long serialVersionUID = -4045666867511956144L;
	public Position position;
     public String question;
     public PosQsq(Position position, String question){
    	    this.position = position;
    	    this.question = question;
     }
}

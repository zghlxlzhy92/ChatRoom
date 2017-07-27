package hz44_rx4.server.gView;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import map.MapPanel;

public class Map extends MapPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5011045632130995054L;
	
    public WorldWindowGLCanvas getCanvas(){
    	return super.getWWD();
    }
}

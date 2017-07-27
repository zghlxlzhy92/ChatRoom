package hz44_rx4.server.gModel;

import java.io.Serializable;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;

public interface GameModeViewAdapter extends Serializable{
       public void addLayer(Layer layer);
       public void update();
       public void disabledBton();
       public void goToPlace(Position positon);
       public void showRank(String rank);
}

package hz44_rx4.server.gView;

import java.io.Serializable;

public interface GameViewModelAdpater extends Serializable{
      public void StartGame();
      public String goToPlace(String answer);
      //public void update();
      public void timeOut();
}

package hz44_rx4.server.sModel;

import java.util.HashSet;
import java.util.Set;

import common.IChatServer;

public class Team {
     public Set<IChatServer> player;
     public Set<IChatServer> replier;
     public long result;
     public Team(Set<IChatServer> players){
    	 this.player = players;
    	 replier = new HashSet<>();
    	 result = Long.MAX_VALUE;
     }
}

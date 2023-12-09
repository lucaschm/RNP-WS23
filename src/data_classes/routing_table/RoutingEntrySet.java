package data_classes.routing_table;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RoutingEntrySet implements IRoutingTable {
    
    Set<RoutingEntry> set;

    public RoutingEntrySet() {
        set = new HashSet<RoutingEntry>();
    }

    public String findNextHop(String destination) {
        String bestNextHop = null;
        int minHopCout = Integer.MAX_VALUE;
        for (RoutingEntry entry : set) {
            if (entry.getDestination() == destination) {
                if(minHopCout < entry.getHops()) {
                    minHopCout = entry.getHops();
                    bestNextHop = entry.getNextHop();
                }
            }
        }
        return bestNextHop;
    }

    public void mergeWith(Set<RoutingEntry> routingEntries) {
        return;
    }

    public Set<RoutingEntry> getEntriesWithout(String originIP) {
         return set;
    }
}

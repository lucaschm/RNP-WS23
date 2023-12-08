package de.haw.rn.luca_steven.data_classes.routing_table;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RoutingEntrySet implements IRoutingTable {
    
    Set<RoutingEntry> set;

    public RoutingEntrySet() {
        set = new HashSet<RoutingEntry>();
    }
    
    /**
     * @return null if there is no entry for the destination
     */
    public String findNextHop(String destination) {
        String bestNextHop = null;
        int minHopCout = Integer.MAX_VALUE;

        for (RoutingEntry entry : set) {
            if (entry.getDestination() == destination) {
                if(entry.getHops() < minHopCout) {
                    minHopCout = entry.getHops();
                    bestNextHop = entry.getNextHop();
                }
            }
        }
        return bestNextHop;
    }
    

    /**
     * merge routing entries from another client in the net with our entries
     * 1. delete all entries we have from this other client
     * 2. add the new entries
     * 
     * with that we have added all new entries and deleted all entries that the other client also deleted 
     * 
     * @param origin The source of the routing entries
     */
    public void mergeWith(Set<RoutingEntry> routingEntries, String origin) {
        for (RoutingEntry entry : set) {
            if (entry.getOr)
        }
        return;
    }

    public Set<RoutingEntry> getEntriesWithout(String originIP) {
         return set;
    }

    /**
     * only for testing
     */
    public void addEntry(RoutingEntry entry) {
        set.add(entry);
    }
}

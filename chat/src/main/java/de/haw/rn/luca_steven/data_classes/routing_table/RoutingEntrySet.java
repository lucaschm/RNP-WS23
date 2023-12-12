package de.haw.rn.luca_steven.data_classes.routing_table;

import java.util.HashSet;
import java.util.Iterator;
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
            if (entry.getDestination().equals(destination)) {
                if(entry.getHops() < minHopCout) {
                    minHopCout = entry.getHops();
                    bestNextHop = entry.getNextHop();
                }
            }
        }
        return bestNextHop;
    }
    

    /**
     * merge routing entries from another client in the network with our entries
     * 1. delete all entries we have from this other client
     * 2. add the new entries
     * 
     * with that we have added all new entries and deleted all entries that the other client also deleted 
     * 
     * @param routingEntries all have to have the origin given in origin !!!
     * @param origin The source of the routing entries
     */
    public void mergeWith(Set<RoutingEntry> routingEntries, String origin) {
        for (RoutingEntry entry : routingEntries) {
            if (entry.getOrigin().equals(origin)) {
                throw new IllegalArgumentException("Entry: " + entry + " did not have the given origin: " + origin);
            }
        }
        
        Iterator<RoutingEntry> iterator = set.iterator();
        while (iterator.hasNext()) {
            RoutingEntry entry = iterator.next();
            if (entry.getOrigin().equals(origin)) {
                iterator.remove();
            }
        }

        set.addAll(routingEntries);

        return;
    }

    public Set<RoutingEntry> getEntriesWithout(String origin) {
        Set<RoutingEntry> result = new HashSet<RoutingEntry>();
        for (RoutingEntry entry : set) {
            if (entry.getOrigin().equals(origin)) {
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * only for testing
     * (maybe for adding entries by this client)
     */
    public void addEntry(RoutingEntry entry) {
        set.add(entry);
    }

    public Set<String> getAllUniqueDestinations() {
        Set<String> result = new HashSet<String>();
        for (RoutingEntry entry : set) {
            result.add(entry.getDestination());
        }
        return result;
    }

    @Override
    public Set<RoutingEntry> getNeighbours() {
        Set<RoutingEntry> resultSet = new HashSet<RoutingEntry>();

        for (RoutingEntry entry : set) {
            if (entry.getHops() == 1) {
                resultSet.add(entry);
            }
        }

        return resultSet;
    }
}

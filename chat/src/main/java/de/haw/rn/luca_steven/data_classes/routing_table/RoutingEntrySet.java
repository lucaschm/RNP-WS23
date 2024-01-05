package de.haw.rn.luca_steven.data_classes.routing_table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.haw.rn.luca_steven.Config;
import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.ui.Status;

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
            if (!origin.equals(entry.getOrigin())) {
                throw new IllegalArgumentException("Entry: " + entry + " did not have the same origin as the message, which has origin: " + origin);
            }
        }

        if(Config.onlyStoreBestRoutingEntry){
            //get only the best entry
             //< dest , routingEntry>
            Map<String, RoutingEntry> findShortestWayMap = new HashMap<String, RoutingEntry>();
            for (RoutingEntry entry : set) {
                RoutingEntry currentlyBestEntry = findShortestWayMap.get(entry.getDestination());
                if (currentlyBestEntry == null || entry.getHops() < currentlyBestEntry.getHops()) {
                    findShortestWayMap.put(entry.getDestination(), entry);
                }
            };

            for (RoutingEntry entry: routingEntries) {
                RoutingEntry currentlyBestEntry = findShortestWayMap.get(entry.getDestination());
                if (currentlyBestEntry == null || entry.getHops() < currentlyBestEntry.getHops()) {
                    findShortestWayMap.put(entry.getDestination(), entry);
                }
            }
            set = new HashSet<RoutingEntry>(findShortestWayMap.values());

        } else {
            Iterator<RoutingEntry> iterator = set.iterator();
                while (iterator.hasNext()) {
                    RoutingEntry entry = iterator.next();
                    if (origin.equals(entry.getOrigin())) {
                        iterator.remove();
                        Status.removeRoutingEntry(entry);
                    }
                }
            set.addAll(routingEntries);
        }

        return;
    }

    public Set<RoutingEntry> getEntriesWithout(String origin) {
        Set<RoutingEntry> result = new HashSet<RoutingEntry>();
        for (RoutingEntry entry : set) {
            if (!origin.equals(entry.getOrigin())) {
                result.add(entry);
            }
        }
        return result;
    }

    public Set<RoutingEntry> getEntries() {
        return set;
    }

    /**
     * only for testing
     * (maybe for adding entries by this client)
     */
    public void addEntry(RoutingEntry newEntry) {
        Iterator<RoutingEntry> iterator = set.iterator();
        Status.addRoutingEntry(newEntry);
            while (iterator.hasNext()) {
                RoutingEntry entry = iterator.next();
                if (newEntry.getDestination().equals(entry.getDestination()) && 
                    newEntry.getHops() < (entry.getHops())) {
                    iterator.remove();
                    Status.removeRoutingEntry(entry);
                }
            }
        set.add(newEntry);
    }

    public Set<String> getAllUniqueDestinations() {
        Set<String> result = new HashSet<String>();
        for (RoutingEntry entry : set) {
            if (entry.getHops() > 0) {
                result.add(entry.getDestination() + " [" + entry.getHops() + "]");
            }
        }
        return result;
    }

    @Override
    public Set<RoutingEntry> getNeighbours() {
        Set<RoutingEntry> resultSet = new HashSet<RoutingEntry>();

        for (RoutingEntry entry : set) {
            // if (entry.getDestinationIDPort() == 1111 && entry.getHops() == 1) {
            //     Logger.log("hier, 1111 hat hops 1" + entry);
            // }
            if (entry.getHops() == 1) {
                resultSet.add(entry);
            }
        }

        return resultSet;
    }

    public void deleteAllFor(String ipPort) {
        Iterator<RoutingEntry> i = set.iterator();
         while (i.hasNext()) {
            RoutingEntry entry = i.next();
            if (entry.getNextHop().equals(ipPort)) {
                i.remove();
                Status.removeRoutingEntry(entry);
            }
        }
    }
}      

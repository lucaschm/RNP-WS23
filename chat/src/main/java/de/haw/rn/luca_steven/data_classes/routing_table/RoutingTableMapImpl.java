package de.haw.rn.luca_steven.data_classes.routing_table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.haw.rn.luca_steven.ui.Status;

public class RoutingTableMapImpl implements IRoutingTable {
     //<dest. , routing entry>
    Map<String, RoutingEntry> map = new HashMap<String, RoutingEntry>();

    public String findNextHop(String destination){
        return map.get(destination).getNextHop();
    }

    public void mergeWith(Set<RoutingEntry> routingEntries, String origin){
        //put routingEntries in map for easy handling
        Map<String, RoutingEntry> newEntries = new HashMap<String, RoutingEntry>();
        boolean tableHasChanged = false;
        for (RoutingEntry entry : routingEntries) {
            newEntries.put(entry.getDestination(), entry);
        }
        //when a destination is no longer reachable from the origin, then remove it
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            RoutingEntry entry = map.get(key);
            boolean hasSameOrigin = entry.getOrigin().equals(origin);
            if (hasSameOrigin && !newEntries.containsKey(key)) {
                iter.remove(); 
                Status.removeRoutingEntry(entry);
                tableHasChanged = true;     
            }
        }
        for (RoutingEntry entry : newEntries.values()) {
            this.addEntry(entry);
        }
        if (tableHasChanged) {
            Status.routingTableChanged(this);
        }
    }

    public Set<RoutingEntry> getEntriesWithout(String origin){
        Set<RoutingEntry> result = new HashSet<RoutingEntry>();
        for (RoutingEntry entry : map.values()) {
            if (!origin.equals(entry.getOrigin())) {
                result.add(entry);
            }
        }
        
        return result;
    }

    public Set<RoutingEntry> getEntries(){
        return new HashSet<RoutingEntry>(map.values());
    }

    public void addEntry(RoutingEntry newEntry) {
        //put new Entry in map if same origin has new hop information or if it has less hops then other orinins entry
        String destination = newEntry.getDestination();
        String newOrigin = newEntry.getOrigin();
            if (map.containsKey(destination)) {
                RoutingEntry existingEntry = map.get(destination);
                if (newOrigin.equals(existingEntry.getOrigin())) {
                    if (!newEntry.equals(map.put(destination, newEntry))) {
                        Status.addRoutingEntry(newEntry);
                        Status.routingTableChanged(this); 
                    } 
                } else if (newEntry.getHops() < existingEntry.getHops()) {
                    map.put(destination, newEntry);
                    Status.addRoutingEntry(newEntry);
                    Status.routingTableChanged(this);  
                }
            } else {
                map.put(destination, newEntry);
                Status.addRoutingEntry(newEntry);
                Status.routingTableChanged(this);  
            }
    }

    public Set<RoutingEntry> getAllButSelfEntry(){
        Set<RoutingEntry> result = new HashSet<RoutingEntry>();
        for (RoutingEntry entry : map.values()) {
            if (entry.getHops() != 0) {
                result.add(entry);
            }
        }
        return result;
    }

    public Set<RoutingEntry> getNeighbours(){
        Set<RoutingEntry> result = new HashSet<RoutingEntry>();
        for (RoutingEntry entry : map.values()) {
            if (entry.getHops() == 1) {
                result.add(entry);
            }
        }

        return result;
    }

    public void deleteAllFor(String targetNextHop){
        Iterator<String> i = map.keySet().iterator();
        boolean tableHasChanged = false;
        while (i.hasNext()) {
            RoutingEntry entry = map.get(i.next());
            String nextHop = entry.getNextHop();
            if (nextHop.equals(targetNextHop)) {
                i.remove();
                Status.removeRoutingEntry(entry);
                tableHasChanged = true;  
            }
        }
        if (tableHasChanged) {
            Status.routingTableChanged(this);
        }
    }

    public void delete(Set<RoutingEntry> lostConnections){
        if (lostConnections.size() > 0) {
            for (RoutingEntry entry : lostConnections) {
                map.remove(entry.getDestination());
                Status.removeRoutingEntry(entry);
            }
            Status.routingTableChanged(this);
        }
    }
     
}

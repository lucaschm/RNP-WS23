package de.haw.rn.luca_steven.data_classes.routing_table;

import java.util.Set;

public interface IRoutingTable {

    public String findNextHop(String destination);

    public void mergeWith(Set<RoutingEntry> routingEntries, String origin);

    public Set<RoutingEntry> getEntriesWithout(String originIP);

    public void addEntry(RoutingEntry entry);

    public Set<String> getAllUniqueDestinations();

    public Set<RoutingEntry> getNeighbours();

    public void deleteAllFor(String ipPort);
     
}

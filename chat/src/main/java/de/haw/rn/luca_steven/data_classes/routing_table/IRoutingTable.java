package de.haw.rn.luca_steven.data_classes.routing_table;

import java.util.Set;

import de.haw.rn.luca_steven.connection_handler.exceptions.DoubleConnectionException;

public interface IRoutingTable {

    public String findNextHop(String destination);

    public void mergeWith(Set<RoutingEntry> routingEntries, String origin) throws DoubleConnectionException;

    public Set<RoutingEntry> getEntriesWithout(RoutingEntry entry);

    public Set<RoutingEntry> getEntries();

    public void addEntry(RoutingEntry entry) throws DoubleConnectionException;

    public Set<RoutingEntry> getAllButSelfEntry();

    public Set<RoutingEntry> getNeighbours();

    public void deleteAllFor(String targetNextHop);

    public void delete(Set<RoutingEntry> lostConnections);

    public boolean isNeighbor(String ipPort);
     
}

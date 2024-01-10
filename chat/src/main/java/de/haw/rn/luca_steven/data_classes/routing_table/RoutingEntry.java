package de.haw.rn.luca_steven.data_classes.routing_table;

import java.util.Objects;

/**
 * description of class RoutingEntry 
 *
 * @author steven
 * 
 */
public class RoutingEntry {

    private String destination;
    private int hops;
    private String nextHop;
    private String origin;
    private int localPort;

    public static final int PORT_INFORMATION_MISSING = -2;

    
    public RoutingEntry(String destination, int hops, String nextHop, String origin, int localPort) {
        this.destination = destination;
        this.hops = hops;
        this.nextHop = nextHop;
        this.origin = origin;
        this.localPort = localPort;
    }

    /**
     * especially for debugger
     */
    public String toString() {
        return "d: " + this.destination + " h: " + this.hops + " nH: " + this.nextHop;
    }

    /**
     * @return String return the destination
     */
    public String getDestination() {
        return destination;
    }

    public String getDestinationIP() {
        return destination.split(":")[0];
    }

    public int getDestinationIDPort() {
        return Integer.parseInt(destination.split(":")[1]);
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return int return the hops
     */
    public int getHops() {
        return hops;
    }

    /**
     * @param hops the hops to set
     */
    public void setHops(int hops) {
        this.hops = hops;
    }

    /**
     * @return String return the nextHop
     */
    public String getNextHop() {
        return nextHop;
    }

    /**
     * @param nextHop the nextHop to set
     */
    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }

    /**
     * @return String return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin If this routing entry is created by this client is has to have the IP:Port of this client
     * @throws NullPointerException if origin is null
     * 
     * 
     */
    public void setOrigin(String origin) {
        this.origin = Objects.requireNonNull(origin, "Origin cannot be null");
    }

    public int getLocalPort() {
        return this.localPort;
    }

    public void setLocalPort(int port) {
        this.localPort = port;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RoutingEntry))
        {
            return false;
        }
        RoutingEntry other = (RoutingEntry) obj;
        
        boolean equalDestination = this.destination.equals(other.getDestination());
        boolean equalHops = this.hops == other.getHops();
        boolean equalNextHop = this.nextHop.equals(other.nextHop);
        boolean equalOrigin = this.origin.equals(other.getOrigin());

        return equalDestination && equalHops && equalNextHop && equalOrigin;
    }

    @Override
    public int hashCode() {
        return hops;
    }

    
}



package data_classes.routing_table;

public class RoutingEntry {

    private String destination;
    private int hops;
    private String nextHop;
    
    public RoutingEntry(String destination, int hops) {
        this.destination = destination;
        this.hops = hops;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public int getHops() {
        return hops;
    }

    public String getNextHop() {
        return nextHop;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }

}



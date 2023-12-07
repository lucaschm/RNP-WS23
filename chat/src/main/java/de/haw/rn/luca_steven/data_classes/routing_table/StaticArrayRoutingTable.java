package data_classes.routing_table;

import data_classes.ChatMessage;

/*
 * fixed size 2d array
 * 
 * example table with rowCont 3
 *  
 *       | destinationIP | hops | nextHop | originIP |
 *  row0 |               |      |         |          |
 *  row1 |               |      |         |          |
 *  row2 |               |      |         |          |
 * 
 */
public class StaticArrayRoutingTable {
    String[][] table;
    int nextEmptyRow; //beginning with 

    public StaticArrayRoutingTable(int rowCount) {
        table = new String[4][rowCount];
    }

    public void put(RoutingMessage msg) {
        table[0][nextEmptyRow] = msg.getIP();
        table[1][nextEmptyRow] = msg.g
    }

    public int nextHopFromDestination(int destinationIP) {
        return nextHop
    }
}

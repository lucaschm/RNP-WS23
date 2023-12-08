package de.haw.rn.luca_steven;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntrySet;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;

public class TestRoutingEntrySet {

    RoutingEntrySet entries;

    RoutingEntry entry1;
    RoutingEntry entry2;
    RoutingEntry entry3;
    RoutingEntry entry4;

    @Before
    public void setUp() throws Exception {
        entries = new RoutingEntrySet();

        entry1 = new RoutingEntry("1.2.3.4", 2, "2.2.2.2", null);

        entry2 = new RoutingEntry("1.1.1.1", 2, "2.2.2.2", null); //only use 2-4 for findNoHop and findNextHop
        entry3 = new RoutingEntry("1.1.1.1", 3, "3.3.3.3", null); //null because it doesnt matter
        entry4 = new RoutingEntry("1.1.1.1", 4, "4.4.4.4", null);
    }

    @Test
    public void findNoHop() {
        entries.addEntry(entry1);
        assertEquals("should not find a next Hop", null, entries.findNextHop("1.1.1.1"));
    }

    @Test
    public void findNextHop() {
        entries.addEntry(entry1);
        entries.addEntry(entry3);
        entries.addEntry(entry2); //watch order to check if its not luck and it always finds fist insterted
        entries.addEntry(entry4);
        assertEquals("should find the next Hop", "2.2.2.2", entries.findNextHop("1.1.1.1"));
    }
}

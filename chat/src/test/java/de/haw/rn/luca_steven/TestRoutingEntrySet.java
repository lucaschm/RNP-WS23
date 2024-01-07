package de.haw.rn.luca_steven;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.haw.rn.luca_steven.data_classes.routing_table.RoutingTableSetImpl;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;

public class TestRoutingEntrySet {

    RoutingTableSetImpl entries;

    RoutingEntry entry1;
    RoutingEntry entry2;
    RoutingEntry entry3;
    RoutingEntry entry4;
    RoutingEntry entry5;
    RoutingEntry entry6;
    RoutingEntry entry7;

    @Before
    public void setUp() throws Exception {
        entries = new RoutingTableSetImpl();

        entry1 = new RoutingEntry("100.2.2.2", 2, "1.1.1.1", "127.0.0.1"); 

        entry2 = new RoutingEntry("100.1.1.1", 2, "1.2.2.2", "127.0.0.1"); 
        entry3 = new RoutingEntry("100.1.1.1", 3, "1.3.3.3", "127.0.0.1"); 
        entry4 = new RoutingEntry("100.1.1.1", 4, "1.4.4.4", "127.0.0.1");

        entry5 = new RoutingEntry("200.1.1.1", 5, "2.5.5.5", "2.0.0.0");
        entry6 = new RoutingEntry("200.1.1.1", 6, "2.6.6.6", "2.0.0.0");

        entry7 = new RoutingEntry("200.1.1.1", 6, "2.7.7.7", "2.0.0.0");
    }

    @Test
    public void testFindNoHop() {
        entries.addEntry(entry1);
        assertEquals("should not find a next Hop", null, entries.findNextHop("100.1.1.1"));
    }

    @Test
    public void testFindNextHop() {
        entries.addEntry(entry1);
        entries.addEntry(entry3);
        entries.addEntry(entry2); //watch order to check if its not luck and it always finds fist insterted
        entries.addEntry(entry4);
        assertEquals("should find the next Hop", "1.2.2.2", entries.findNextHop("100.1.1.1"));
    }

    @Test
    public void testMergeOneInEmptySet() {
        Set<RoutingEntry> incomingEntry = new HashSet<RoutingEntry>();
        incomingEntry.add(entry1);

        entries.mergeWith(incomingEntry, "127.0.0.1");

        assertEquals(
            "should find the one Entry that just got merges in", 
            "1.1.1.1", 
            entries.findNextHop("100.2.2.2")
            );
    }

    @Test //TODO implement show entries method in Routing Set so you see every entry
    public void testMergeMultipleInEmptySet() {
        Set<RoutingEntry> incomingEntries = new HashSet<RoutingEntry>();
        incomingEntries.add(entry1);
        incomingEntries.add(entry3);
        incomingEntries.add(entry2);

        entries.mergeWith(incomingEntries, "127.0.0.1");

        assertEquals(
            "should find the Entry", 
            "1.1.1.1", 
            entries.findNextHop("100.2.2.2")
            );

        assertEquals(
            "should find the Entry", 
            "1.2.2.2", 
            entries.findNextHop("100.1.1.1")
            );
    }

    @Test
    public void testMergeInExistingSetWithSameOrigins() {
        Set<RoutingEntry> incomingEntries = new HashSet<RoutingEntry>();
        incomingEntries.add(entry2);
        incomingEntries.add(entry3);

        entries.addEntry(entry1);
        entries.mergeWith(incomingEntries, "127.0.0.1");

        assertEquals(
            "should not find enty1 bc it got merged with newer info",
            null,
            entries.findNextHop("100.2.2.2")
            );
    }

    @Test
    public void testMergeInExistingSetWithOtherOrigins() {
        entries.addEntry(entry2);
        entries.addEntry(entry3);
        
        Set<RoutingEntry> incomingEntries = new HashSet<RoutingEntry>();
        incomingEntries.add(entry5);
        incomingEntries.add(entry6);
        incomingEntries.add(entry7);

        entries.mergeWith(incomingEntries, "2.0.0.0");

        assertEquals(
            "should find the Entry that just got merges in", 
            "1.2.2.2", 
            entries.findNextHop("100.1.1.1")
            );

        assertEquals(
            "should find the Entry that just got merges in", 
            "2.5.5.5", 
            entries.findNextHop("200.1.1.1")
            );

        incomingEntries = new HashSet<RoutingEntry>();
        entries.mergeWith(incomingEntries, "2.0.0.0");

        assertEquals(
            "should not find a nextHop", 
            null, 
            entries.findNextHop("200.1.1.1")
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergeThrowsException() {
        Set<RoutingEntry> incomingEntries = new HashSet<RoutingEntry>();
        incomingEntries.add(entry1);
        incomingEntries.add(entry3);
        incomingEntries.add(entry5);
        
        entries.mergeWith(incomingEntries, "127.0.0.1"); //throw exception bc entry5 is not 127.0.0.1
    } 

    @Test
    public void testNothingToGet() {
        Set<RoutingEntry> emptySet = new HashSet<RoutingEntry>();
        assertEquals(
            "there is nothing: null should be the output",
            emptySet, 
            entries.getEntriesWithout("127.0.0.1")
            );

        entries.addEntry(entry1);
        assertEquals(
            "should also result null",
            emptySet, 
            entries.getEntriesWithout("127.0.0.1")
            );
    }

    @Test
    public void testGetEntriesWithoutOrigin() {
        entries.addEntry(entry1);
        entries.addEntry(entry5);

        HashSet<RoutingEntry> testSet1 = new HashSet<RoutingEntry>();
        testSet1.add(entry1);

        assertEquals(
            "should also result null",
            testSet1, 
            entries.getEntriesWithout("2.0.0.0")
            );

        entries.addEntry(entry6);
        entries.addEntry(entry7);

        HashSet<RoutingEntry> testSet2 = new HashSet<RoutingEntry>();
        testSet2.add(entry5);
        testSet2.add(entry6);
        testSet2.add(entry7);

        assertEquals(
            "should also result null",
            testSet2, 
            entries.getEntriesWithout("127.0.0.1")
            );
        
    }
}

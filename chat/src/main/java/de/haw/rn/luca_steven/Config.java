package de.haw.rn.luca_steven;

/**
 * Das ist nur ein Config-File Template
 */
public class Config {
    // wenn localIPAdress = null, dann wird versucht die lokale IP-Adresse zu ermitteln
    // Es kann vorkommen, dass eine falsche Adresse ermittelt wird. 
    // In diesem Fall kann man die Adresse hier manuell setzen.
    public static String localIPAdress = null;
    
    //if true, all known ways to any client will be rememberd (even if not the shortest)
    public static boolean onlyStoreBestRoutingEntry = true;
}

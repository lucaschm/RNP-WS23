package de.haw.rn.luca_steven;

import java.util.zip.CRC32;
import java.nio.charset.StandardCharsets;
public class CRC32Checksum {

    public static long crc32(String input) {
        // Konvertiert den String in ein Byte-Array
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        // Erstellt ein CRC32-Objekt
        CRC32 crc32 = new CRC32();
        // Aktualisiert die CRC32-Checksumme mit dem Byte-Array
        crc32.update(bytes, 0, bytes.length);
        // Holt die berechnete CRC32-Checksumme
        long checksum = crc32.getValue();
        return checksum;
    }
}
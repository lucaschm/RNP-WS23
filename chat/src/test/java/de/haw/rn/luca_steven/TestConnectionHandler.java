package de.haw.rn.luca_steven;

import java.nio.ByteBuffer;

import org.junit.Test;

public class TestConnectionHandler {

    @Test
    public void testBuffer() {
        long number = 0b00000000_00000000_00000000_10011111_01010101_01010101_00000000_11111111L;
        ByteBuffer headerBuffer = ByteBuffer.allocate(Long.SIZE);
        headerBuffer.putLong(number);

        int messageLength = headerBuffer.getInt(0);
        int crc32 = headerBuffer.getInt(1);

        System.out.println(messageLength);
        System.err.println(crc32);
    }
}

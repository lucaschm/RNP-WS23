package de.haw.rn.luca_steven;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;
import de.haw.rn.luca_steven.data_classes.RoutingMessage;

public class TestJsonParser {
    private ChatMessage chatMessage;
    private RoutingMessage routingMessage;
    private String jsonChatMessage = "{\"message_type\":\"chat_message\",\"destination_ip\":\"192.168.10.3\",\"destination_id_port\":\"12345\",\"origin_ip\":\"192.168.104.12\",\"origin_id_port\":\"9999\",\"ttl\":\"3\",\"content\":\"hello world!\"}";
    private String jsonRoutingMessage = "{\"message_type\":\"routing_message\",\"ip\":\"172.11.14\",\"source_port\":\"52443\",\"id_port\":\"8081\",\"table\":[{\"ip\":\"172.11.14.11\",\"id_port\":\"8081\",\"hops\":\"0\"},{\"ip\":\"172.11.14.10\",\"id_port\":\"8080\",\"hops\":\"1\"},{\"ip\":\"172.11.14.12\",\"id_port\":\"8080\",\"hops\":\"1\"}]}";

    @Before @Test
    public void testExtractChatMessage() {
        JsonParser jsonParser = new JsonParser();
        
        Message message = jsonParser.convertJsonStringToMessage(jsonChatMessage);
        assertTrue("Die Nachricht müsste eigentlich eine chat_message sein!", message.isChatMessage());

        chatMessage = (ChatMessage) message;

        assertEquals("hello world!", chatMessage.getContent());
    }

    @Before @Test
    public void testExtractRoutingMessage() {
        JsonParser jsonParser = new JsonParser();

        Message message = jsonParser.convertJsonStringToMessage(jsonRoutingMessage);
        assertTrue("Die Nachricht müsste eigentlich eine routing_message sein!", !message.isChatMessage());

        routingMessage = (RoutingMessage) message;

        assertEquals("172.11.14", routingMessage.getIp());
    }

    @Test
    public void testConvertChatMessageToJsonString() {
        JsonParser jsonParser = new JsonParser();

        String parsedString = jsonParser.convertChatMessageToJsonString(chatMessage);

        assertEquals(jsonChatMessage, parsedString);
    }
}

package de.haw.rn.luca_steven;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;

public class TestJsonParser {
    private ChatMessage chatMessage;
    private String jsonString = "{\"message_type\":\"chat_message\",\"destination_ip\":\"192.168.10.3\",\"destination_id_port\":\"12345\",\"origin_ip\":\"192.168.104.12\",\"origin_id_port\":\"9999\",\"ttl\":\"3\",\"content\":\"hello world!\"}";
        

    @Before
    public void testConvertJsonStringToMessage() {
        JsonParser jsonParser = new JsonParser();
        
        Message message = jsonParser.convertJsonStringToMessage(jsonString);
        assertTrue("Die Nachricht müsste eigentlich eine chat_message sein!", message.isChatMessage());

        chatMessage = (ChatMessage) message;

        assertEquals("hello world!", chatMessage.getContent());
    }

    @Test
    public void test() {
        JsonParser jsonParser = new JsonParser();

        String parsedString = jsonParser.convertChatMessageToJsonString(chatMessage);

        assertEquals(jsonString, parsedString);


    }
}

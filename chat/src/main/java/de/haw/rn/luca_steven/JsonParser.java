package de.haw.rn.luca_steven;

import java.io.StringReader;

import javax.json.*;

import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;

//bekommt Infos rein (z.b. IP-Adresse)
//Infos werden verpackt in ein Json Objekt

public class JsonParser {
    private static final String MESSAGE_TYPE = "message_type";
    private static final String CHAT_MESSAGE = "chat_message";
    private static final String DESTINATION_IP = "destination_ip";
    private static final String DESTINATION_ID_PORT = "destination_id_port";
    private static final String ORIGIN_IP = "origin_ip";
    private static final  String ORIGIN_ID_PORT = "origin_id_port";
    private static final String TTL = "ttl";
    private static final String CONTENT = "content";

    public JsonParser() {

    }

    public String convertChatMessageToJsonString(ChatMessage message) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(MESSAGE_TYPE, CHAT_MESSAGE);
        builder.add(DESTINATION_IP, message.getDestinationIP());
        builder.add(DESTINATION_ID_PORT, "" + message.getDestinationIDPort());
        builder.add(ORIGIN_IP, message.getOriginIP());
        builder.add(ORIGIN_ID_PORT, "" + message.getOriginIDPort());
        builder.add(TTL, "" + message.getTtl());
        builder.add(CONTENT, message.getContent());

        JsonObject json = builder.build();
        return json.toString();
    }

    //TODO: anpassen, damit es für beide Arten von Nachrichten funktioniert
    public Message convertJsonStringToMessage(String messageString) throws IllegalArgumentException {
        // String to JSONObject
        JsonReader jsonReader = Json.createReader(new StringReader(messageString));
        JsonObject json = jsonReader.readObject();
        
        String testString = json.getString(MESSAGE_TYPE);

        // read contents from json
        String destinationIp = json.getString(DESTINATION_IP);
        int destinationPort = Integer.parseInt(json.getString(DESTINATION_ID_PORT));
        String sourceIp = json.getString(ORIGIN_IP);
        int sourcePort = Integer.parseInt(json.getString(ORIGIN_ID_PORT));
        int ttl = Integer.parseInt(json.getString(TTL));
        String content = json.getString(CONTENT);

        return new ChatMessage(
                destinationIp,
                destinationPort,
                sourceIp,
                sourcePort,
                ttl,
                content);
    }

}

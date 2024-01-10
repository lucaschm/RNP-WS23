package de.haw.rn.luca_steven;

import java.io.StringReader;
import java.util.Set;

import javax.json.*;

import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;
import de.haw.rn.luca_steven.data_classes.RoutingMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;
import de.haw.rn.luca_steven.ui.Status;

//bekommt Infos rein (z.b. IP-Adresse)
//Infos werden verpackt in ein Json Objekt

public class JsonParser {

    //JSON KEYS FOR CHAT MESSAGE
    private static final String MESSAGE_TYPE = "message_type";
    private static final String CHAT_MESSAGE = "chat_message";
    private static final String DESTINATION_IP = "destination_ip";
    private static final String DESTINATION_ID_PORT = "destination_id_port";
    private static final String ORIGIN_IP = "origin_ip";
    private static final String ORIGIN_ID_PORT = "origin_id_port";
    private static final String TTL = "ttl";
    private static final String CONTENT = "content";

    //JSON KEYS FOR ROUTING MESSAGE
    private static final String ROUTING_MESSAGE = "routing_message";
    private static final String IP = "ip";
    private static final String SOURCE_PORT = "source_port";
    private static final String ID_PORT = "id_port";
    private static final String TABLE = "table";
    private static final String HOPS = "hops";

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

    public String buildRoutingTableJsonString(Set<RoutingEntry> routingTable, String ip, String sourcePort, String idPort) {
        JsonObjectBuilder outerBuilder = Json.createObjectBuilder();
        outerBuilder.add(MESSAGE_TYPE, ROUTING_MESSAGE);
        outerBuilder.add(IP, ip);
        outerBuilder.add(SOURCE_PORT, sourcePort);
        outerBuilder.add(ID_PORT, idPort);

        JsonArrayBuilder tableBuilder = Json.createArrayBuilder();
        
        for (RoutingEntry routingEntry : routingTable) {
            JsonObjectBuilder entryBuilder = Json.createObjectBuilder();

            String destinationIp = routingEntry.getDestinationIP();
            String id_port = "" + routingEntry.getDestinationIDPort();
            String hops = "" + routingEntry.getHops();

            entryBuilder.add(IP, destinationIp);
            entryBuilder.add(ID_PORT, id_port);
            entryBuilder.add(HOPS, hops);

            tableBuilder.add(entryBuilder);
        }

        outerBuilder.add(TABLE, tableBuilder);
        JsonObject json = outerBuilder.build();
        return json.toString();
    }

    public Message convertJsonStringToMessage(String messageString) throws IllegalArgumentException {
        // String to JSONObject
        JsonReader jsonReader = Json.createReader(new StringReader(messageString));
        JsonObject json = jsonReader.readObject();

        // read contents from json
        Message result = null;

        if (hasValidChatJsonKeys(json)) 
        {
            result = extractChatMessage(json);
        } 
        else if (hasValidRoutingJsonKeys(json)) 
        {
            result = extractRoutingMessage(json);
        } 
        else 
        {
            Status.wrongJsonKeys();
        }
        return result;
    }

    private ChatMessage extractChatMessage(JsonObject json) {
        ChatMessage result = null;
        
        try {
            String destinationIp = json.getString(DESTINATION_IP);
            int destinationPort = Integer.parseInt(json.getString(DESTINATION_ID_PORT));
            String sourceIp = json.getString(ORIGIN_IP);
            int sourcePort = Integer.parseInt(json.getString(ORIGIN_ID_PORT));
            int ttl = Integer.parseInt(json.getString(TTL));
            String content = json.getString(CONTENT);

            result = new ChatMessage(
                destinationIp,
                destinationPort,
                sourceIp,
                sourcePort,
                ttl,
                content);
        } 
        catch (Exception e) {
            Status.wrongJsonValues();
        }
            
        return result;
    }

    private RoutingMessage extractRoutingMessage(JsonObject json) {
        RoutingMessage result = null;

        try {
            String ip = json.getString(IP);
            int sourcePort = Integer.parseInt(json.getString(SOURCE_PORT));
            int idPort = Integer.parseInt(json.getString(ID_PORT));
            JsonArray table = json.getJsonArray(TABLE);

            result = new RoutingMessage(ip, sourcePort, idPort, table);
        } catch (Exception e) {
            Status.wrongJsonValues();
        }

        return result;
    }

    private boolean hasValidChatJsonKeys(JsonObject json) {
        boolean result = true;

        //Prüfen ob nötige Felder vorhanden sind
        result &= json.containsKey(MESSAGE_TYPE);
        result &= json.containsKey(ORIGIN_IP);
        result &= json.containsKey(ORIGIN_ID_PORT);
        result &= json.containsKey(DESTINATION_IP);
        result &= json.containsKey(DESTINATION_ID_PORT);
        result &= json.containsKey(TTL);
        result &= json.containsKey(CONTENT);

        //Prüfen ob für das Feld MESSAGE_TYPE der richtige Wert gesetzt ist
        if (result) {
            result &= json.getString(MESSAGE_TYPE).equals(CHAT_MESSAGE);
        }

        return result;
    }

    private boolean hasValidRoutingJsonKeys(JsonObject json) {
        boolean result = true;

        //Prüfen ob nötige Felder vorhanden sind
        result &= json.containsKey(MESSAGE_TYPE);
        result &= json.containsKey(IP);
        result &= json.containsKey(SOURCE_PORT);
        result &= json.containsKey(ID_PORT);
        result &= json.containsKey(TABLE);

        //Prüfen ob für das Feld MESSAGE_TYPE der richtige Wert gesetzt ist
        if (result) {
            result &= json.getString(MESSAGE_TYPE).equals(ROUTING_MESSAGE);
        }

        if (result) {
            JsonArray tableArray = json.getJsonArray(TABLE);
            for (JsonValue tableEntry : tableArray) {
                JsonObject jsonObject = tableEntry.asJsonObject();

                //Prüfen ob nötige Felder vorhanden sind
                result &= jsonObject.containsKey(IP);
                result &= jsonObject.containsKey(ID_PORT);
                result &= jsonObject.containsKey(HOPS);
            }
        }

        return result;
    }

}

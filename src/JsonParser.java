import javax.json.*;
import java.io.StringReader;
//bekommt Infos rein (z.b. IP-Adresse)
//Infos werden verpackt in ein Json Objekt

public class JsonParser {
    private static final String MESSAGE_TYPE = "message_type";
    private static final String CHAT_MESSAGE = "chat_message";
    private static final String DESTINATION_IP = "destination_ip";
    private static final String DESTINATION_PORT = "destination_port";
    private static final String SOURCE_IP = "source_ip";
    private static final  String SOURCE_PORT = "source_port";
    private static final String TTL = "ttl";
    private static final String CONTENT = "content";

    public JsonParser() {

    }

    public JsonObject convertChatMessageToJson(ChatMessage message) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(MESSAGE_TYPE, CHAT_MESSAGE);
        builder.add(DESTINATION_IP, message.getDestinationIP());
        builder.add(DESTINATION_PORT, message.getDestinationPort());
        builder.add(SOURCE_IP, message.getSourceIP());
        builder.add(SOURCE_PORT, message.getSourcePort());
        builder.add(TTL, message.getTtl());
        builder.add(CONTENT, message.getContent());

        return builder.build();
    }

    public ChatMessage convertJsonToChatMessage(JsonObject json) {
        JsonReader jsonReader = Json.createReader(new StringReader(json.toString()));

        JsonStructure jsonStructure = jsonReader.read();
        
        return null;
    }
}

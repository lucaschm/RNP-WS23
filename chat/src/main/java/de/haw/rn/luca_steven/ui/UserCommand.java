package de.haw.rn.luca_steven.ui;

//TODO: Exceptions werfen, wenn auf das Objekt falsch zugegriffen wird.
// Zum Beispiel: command == Command.LIST und es wird versucht getPort aufzurufen
public class UserCommand {

    private Command command;
    private String ip;
    private int port;
    private String messageContent;
    
    
    public UserCommand(Command command, String ip, int port, String messageString) {
        this.command = command;
        this.ip = ip;
        this.port = port;
        this.messageContent = messageString;
    }


    public Command getCommand() {
        return this.command;
    }

    public String getIP() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getMessageContent() {
        return this.messageContent;
    }


}

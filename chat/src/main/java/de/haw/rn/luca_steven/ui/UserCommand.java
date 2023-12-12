package de.haw.rn.luca_steven.ui;

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

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getMessageContent() {
        return this.messageContent;
    }


}

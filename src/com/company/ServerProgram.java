package com.company;

import com.company.ChatRooms.ChatRoom;
import com.company.ChatRooms.ChatRoomList;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerProgram {

    private SocketAddress lastIncomingMessageAdress;
    private Thread myListeningThread;
    private static ServerProgram _singleton = new ServerProgram();
    private Wrapper chatRoomOptions = new Wrapper();
    private UserList userList;

    public ServerProgram() {
    }

    public void start() {
        NetworkServer.get();
        ChatRoomList.get();
        ChatRoomList.get().createChatRoom("General");
        ChatRoomList.get().createChatRoom("Study Room");
        Thread incommingMessages = new Thread(this::checkIncommingPackage);
        incommingMessages.setDaemon(true);
        incommingMessages.start();
    }

    public void checkIncommingPackage(){
        while (true) {
            var srvMsg = NetworkServer.get().pollMessage();
            if (srvMsg != null) {
                if (srvMsg.right instanceof Message) {
                    System.out.println("Message object revieved from client in check incommingPackage " +
                            ((Message) srvMsg.right).getMessage());
                    System.out.println("Message object channel ID: " + ((Message) srvMsg.right).getChannelID());
                    ChatRoomList.get().getChatRooms().get(((Message) srvMsg.right).getChannelID()).updateMessages(srvMsg);
                }
                else if (srvMsg.right instanceof Wrapper){
                    ChatRoomList.get().getChatRooms().get(((Wrapper) srvMsg.right).getChatRoomID()).addUserToChatRoom(((Wrapper) srvMsg.right).getUser());
                    NetworkServer.get().sendObjectToClient(ChatRoomList.get().getChatRooms().get(((Wrapper) srvMsg.right).getChatRoomID()), srvMsg.left);
                }
                else if(srvMsg.right instanceof LogInRequestMessage){
                    userList = new UserList(((LogInRequestMessage) srvMsg.right).getName());
                    System.out.println("Recivied user: " + ((LogInRequestMessage) srvMsg.right).getName());
                    System.out.println(srvMsg.left);
                    userList.checkUsers(((LogInRequestMessage) srvMsg.right).getName(), srvMsg.left);
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public Wrapper getChatRoomsName(){
        return chatRoomOptions;
    }

    public static ServerProgram get(){
        return _singleton;
    }
}

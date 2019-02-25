package com.company.ChatRooms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatRoomList implements Serializable {
    private Map<String,ChatRoom> chatRoomList;
    private static ChatRoomList _singleton = new ChatRoomList();
    static final long serialVersionUID = 30;

    public ChatRoomList() {
        chatRoomList = new HashMap<String,ChatRoom>();
    }

    public static ChatRoomList get() {
        return _singleton;
    }

    public Map<String, ChatRoom> getChatRooms() {
        return chatRoomList;
    }

    public void createChatRoom(String name) {
        String uniqeID = UUID.randomUUID().toString();
        chatRoomList.put(uniqeID,new ChatRoom(name, uniqeID));
    }

    public void displayChatRooms() {

    }
}
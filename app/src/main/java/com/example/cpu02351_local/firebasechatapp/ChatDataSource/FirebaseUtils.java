package com.example.cpu02351_local.firebasechatapp.ChatDataSource;

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation;

import java.util.Arrays;
import java.util.HashMap;

import static com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource.CONVERSATION_PARTICIPANT;
import static com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource.CONVERSATION_TIME;

public class FirebaseUtils {
    static Conversation convertToConversation(String uuid, HashMap<String, String> in) {
        Conversation res = new Conversation(uuid);
        res.setCreatedTime(in.get(CONVERSATION_TIME));
        res.setParticipantIds(Arrays.asList(in.get(CONVERSATION_PARTICIPANT).split(" ")));
        return res;
    }
}

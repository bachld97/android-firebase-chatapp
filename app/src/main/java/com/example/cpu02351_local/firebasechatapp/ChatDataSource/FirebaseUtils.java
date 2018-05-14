package com.example.cpu02351_local.firebasechatapp.ChatDataSource;

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation;
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource.CONVERSATION_PARTICIPANT;
import static com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource.CONVERSATION_TIME;

public class FirebaseUtils {
    static Conversation convertToConversation(String uuid, HashMap<String, String> in) {
        Conversation res = new Conversation(uuid);
        res.setCreatedTime(Long.parseLong(in.get(CONVERSATION_TIME)));
        res.setParticipantIds(Arrays.asList(in.get(CONVERSATION_PARTICIPANT).split(" ")));
        return res;
    }

    @NotNull
    public static Map<String, Object> convertToMessageMap(@NotNull Message message) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("at_time", message.getAtTime());
        result.put("by_user", message.getByUser());
        result.put("content", message.getContent());
        result.put("type", "text");
        return result;
    }

    @NotNull
    public static Message convertToMessage(@NotNull Map<String, ?> msgMap, String msgId) {
        Message msg = new Message(msgId);
        msg.setAtTime((Long) msgMap.get("at_time"));
        msg.setByUser((String) msgMap.get("by_user"));
        msg.setContent((String) msgMap.get("content"));
        return msg;
    }
}

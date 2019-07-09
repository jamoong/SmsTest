package com.data;

public class Message {
    public String day;
    public String room;
    public String body;

    public static Message create(String body) {

        // 문자 사용여부
        if( body == null ) {
            return null;
        }

        String day = "";
        String room = "";


        Message msg = new Message();
        msg.day = day;
        msg.room = room;
        msg.body  = body;
        return msg;
    }
}

package com.example.jjscheckmate.messageservice;

import android.content.Context;
import android.util.Log;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.login.Session;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MessageManager {

    private static MessageManager messageManager=null;
    private static  MessageSaveManager messageSaveManager=null;

//    private static Socket mSocket; // 어떻게 쓰는가..

    private Context mContext;

    private MessageManager(){ }
    private MessageManager(Context context){this.mContext=context;}

    public static MessageManager getInstance(Context context){
        if(messageManager==null){
            messageManager=new MessageManager(context);
        }
        if(messageSaveManager==null){
            messageSaveManager=MessageSaveManager.getInstance(context);
        }
        return messageManager;
    }

    public void connect(){
        try{
//            mSocket= IO.socket(mContext.getString(R.string.baseUrl));
//            mSocket.connect();
            Log.d("mawang", "MessageManager connect - socket connected ! ");
        }catch (Exception error){
            Log.d("mawang", "MessageManager connect - err :"+error.getMessage());
            error.printStackTrace();
        }
    }

//    public Socket getSocket(){
//        return mSocket;
//    }

    public static void login(){
        JSONObject jObject=new JSONObject();
        try{
            jObject.put("userEmail", Session.getUserEmail());
//            mSocket.emit("login",jObject);
//            Log.v("login","메세지 전송함!!!");
            Log.d("mawang", "MessageManager login - jObject :"+jObject);
        }catch (Exception error){
            error.printStackTrace();
        }
    }

    public static void msgSend(MessageDTO msg){

        //로컬저장
//        messageSaveManager.insert(msg);

        JSONObject jObject=new JSONObject();
        try{
            jObject.put("roomKey",msg.getRoomKey());
            jObject.put("userEmail", msg.getUserEmail());
            jObject.put("message", msg.getMessage());
            jObject.put("date", msg.getDate());

//            mSocket.emit("message",jObject);

            Log.d("mawang", "MessageManager msgSend - jObject :"+jObject);
        }catch (Exception error){
            error.printStackTrace();
        }
    }

    public static void roomJoin(){
        JSONObject jObject=new JSONObject();
        try{
            jObject.put("userEmail", Session.getUserEmail());
//            mSocket.emit("roomjoin",jObject);
//            Log.v("messageSend","메세지 전송함!!!");
            Log.d("mawang", "MessageManager roomJoin - jObject :"+jObject);
        }catch (Exception error){
            error.printStackTrace();
        }
    }
}

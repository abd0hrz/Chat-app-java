package dz.esi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Client {
    Socket client;
    PrintWriter out;
    BufferedReader in;
    ArrayList<String> s=new ArrayList<>();


    public Client(SocketAddress server,InetAddress local)throws Exception {
        client=new Socket();

        int p= new Random().nextInt(65535);
        boolean f=false;
        while (true){
            if(p>65535)throw new IllegalStateException("ereur pas de port disponible");
            try{
                client.bind(new InetSocketAddress(local,p++));
                break;
            }
            catch (Exception e){
                if(!f){
                    p=1025;
                    f=true;
                }
            }

        }

        client.connect(server);
        out =new PrintWriter(client.getOutputStream());
        in=new BufferedReader(new InputStreamReader(client.getInputStream()));
        start();

    }



    public void send(String dest,String content){
        for (String m:content.split("\n")) {
            Thread th=new Thread(()->{
                s(dest,m);
            });
            th.start();
        }

    }
    synchronized public  void s(String dest,String content){
        if(client.isClosed())return;
        out.println(XMLMsgHandler.createMessage(dest,content));

        out.flush();
        System.out.println("printed. ...........");
    }
    public void start(){

        Task <MsgWrapper> task=new Task<MsgWrapper>(){
            @Override
            protected MsgWrapper call() throws Exception {
                while (true&&!client.isClosed()){
                    try {
                        MsgWrapper msg=read();
                        updateMessage(msg.msg);
                        updateValue(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Connection is dead");
                    }
                }
                return null;
            }

        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

        task.setOnFailed(e->{
            Alert a=new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Server disconnected");
            a.setContentText("cliquez OK pour retourner");
            Optional<ButtonType > o=a.showAndWait();
            if(o.get()==ButtonType.OK){
                Main.ROOT.setLeft(null);
                Main.ROOT.setCenter(new Init());
                Main.stage.setTitle("Chat");
            }

        });


        task.valueProperty().addListener((e,old,newV)->{
            Chat chat=(Chat) Main.ROOT.getLeft();
            Contact contact=new Contact(newV.address);
            boolean exist=chat.contacts.contains(contact);
            System.out.println(exist+"     not");

            if(!exist){

                chat.addContact(contact,newV.msg);
                chat.contacts.add(contact);
            }
            else {
                for (Contact c:chat.contacts) {
                    if(c.compareTo(contact)==0){
                        contact=c;
                        break;
                    }
                }
                contact.conversation.send(newV.msg,false);
            }
            contact.select();

            System.out.println(newV+"     ;;;;;;");
        });


    }
    private MsgWrapper read()throws Exception{
        String msg="";
        while(msg.length()<316){
            msg+=in.readLine();
        }
        String a=XMLMsgHandler.extractAddress(msg);
        msg=XMLMsgHandler.extractMsg(msg);
        System.out.println(msg.trim()+"  >>");
        return new MsgWrapper(a,msg.trim());
    }
    private class MsgWrapper{
        String address,msg;


        public MsgWrapper(String address, String msg) {
            this.address = address;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg+" : "+address;
        }
    }
}

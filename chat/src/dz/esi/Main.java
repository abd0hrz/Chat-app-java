package dz.esi;

import javafx.application.Application;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main extends Application {
    public static SocketAddress SERVER;
    public  static BorderPane ROOT=new BorderPane();
    public  static Styleable selected;
    public static Client client;
    public static Server server;
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage=primaryStage;
        ROOT.setCenter(new Start());
        Scene sn=new Scene(ROOT,400,400);
        sn.getStylesheets().addAll(getClass().getResource("app.css").toExternalForm());
        primaryStage.setScene(sn);
        primaryStage.show();
        primaryStage.setTitle("Chat");
        primaryStage.setOnCloseRequest(e->{
            System.exit(0);
        });


    }
    static {
        try {
            SERVER = new InetSocketAddress(InetAddress.getByName(""),2018);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        launch();
        //m1();


    }

    public static void m1()throws Exception{
        System.out.println("server is running ....");
        Server server=new Server(2018);
    }
    public static void m2(){
        System.out.print("client: 2 : ");
        try {
            new Scanner(System.in).next();
            Client c=new Client(new InetSocketAddress(InetAddress.getByName("localhost"),2018),null);
            c.send("127.0.0.3","i want to connect with you >> fromb 2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void m3(){
        System.out.print("client: 3 : ");

        try {
            Client c=new Client(new InetSocketAddress(InetAddress.getByName("localhost"),2018),null);
            c.send("127.0.0.2","i want to connect with you to >> from3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static ServerSocket start(int port){
        ServerSocket server= null;
        try {
            server = new ServerSocket(2018);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return server;
    }
    public static void error(String msg){
        Alert a=new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }
    public static void hover(double effect,Node... nodes){
        Glow g=new Glow(effect);
        for(Node n:nodes){
            n.setOnMouseEntered(e->n.setEffect(g));
            n.setOnMouseExited(e->n.setEffect(null));
        }
    }
    public static void hover(Node... nodes){
        Glow g=new Glow(.7);
        for(Node n:nodes){
            n.setOnMouseEntered(e->n.setEffect(g));
            n.setOnMouseExited(e->n.setEffect(null));
        }
    }
    public static void growAlways(Node ... nodes){
        for (Node n:nodes) HBox.setHgrow(n, Priority.ALWAYS);
    }


}

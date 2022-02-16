package dz.esi;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class Start extends GridPane {
    RadioButton server=new RadioButton("Server"),
                client=new RadioButton("Client");
    ToggleGroup group=new ToggleGroup();
    Button start=new Button("start");
    ServerWrapper serverWrapper=new ServerWrapper();
    public Start() {
        setAlignment(Pos.CENTER);
        group.getToggles().addAll(server,client);
        addRow(0,server);
        addRow(2,client);
        client.setSelected(true);
        setVgap(20);
        setHgap(20);
        add(start,1,1);

        start.setOnAction(e->{
            if(server.isSelected()){
                ToggleButton on=new ToggleButton("ON");
                ToggleButton off=new ToggleButton("OFF");
                ToggleGroup group=new ToggleGroup();
                group.getToggles().addAll(on,off);
                HBox hb=new HBox(20,on,off);
                hb.setAlignment(Pos.CENTER);
                group.selectedToggleProperty().addListener((p,o,nv)->{
                    if(on.isSelected()){
                        Server s=null;
                        try {
                            s = new Server(2018);
                            serverWrapper.setServer(s);
                            Main.ROOT.setCenter(hb);
                            Main.stage.setTitle("Server "+serverWrapper.server.server.getLocalSocketAddress().toString());

                        } catch (Exception e1) {
                            Main.error("le port 2018 esr deja utilisee");
                            Main.ROOT.setCenter(new Start());
                            return;
                        }

                        Thread th=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    serverWrapper.getServer().start();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                        th.start();
                    }
                    else {
                        Main.stage.setTitle("OFF");
                        serverWrapper.getServer().shutdown();
                    }
                });
                on.setSelected(true);
            }
            else {
                Main.ROOT.setCenter(new Init());
            }
        });
    }
    private class ServerWrapper{
        private Server server;

        public ServerWrapper() {
        }

        public Server getServer() {
            return server;
        }

        public void setServer(Server server) {
            this.server = server;
            System.out.println(server);
        }
    }
}

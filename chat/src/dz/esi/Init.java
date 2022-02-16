package dz.esi;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Init extends GridPane{
    TextField tfServer=new TextField("127.0.0.1"),
            tflocal=new TextField("127.0.0."),
            tfport=new TextField("2018")
    ;
    Label lbServer=new Label("@ serveur"),
            lbLocal=new Label("@ local"),
            lbPort=new Label("port")
    ;
    Button btnStart=new Button("Start");
    HBox hb=new HBox(btnStart);


    public Init() {
        addColumn(0,lbLocal,lbServer,lbPort);
        addColumn(1,tflocal,tfServer,tfport);
        add(hb,0,3,2,1);
        GridPane.setMargin(hb,new Insets(20));
        hb.getStyleClass().addAll("hb");
        btnStart.getStyleClass().addAll("btn-start");
        btnStart.setOnAction(e->start());
        getStyleClass().addAll("init");
    }

    private void  start(){
        try{
            InetSocketAddress server=new InetSocketAddress(InetAddress.
                    getByName(tfServer.getText()),Integer.valueOf(tfport.getText()));
            InetAddress local=InetAddress.getByName(tflocal.getText());
            Main.client=new Client(server,local);
            new Chat();
            Main.ROOT.setCenter(null);

        }
        catch (Exception e){
            e.printStackTrace();
            Main.error(e.getMessage());
        }
    }
}

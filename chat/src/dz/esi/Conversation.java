package dz.esi;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class Conversation extends VBox implements IView{
    VBox vbMsgs;
    ScrollPane scConver;
    TextArea areaMsg;
    Button btnSend;
    HBox hbMsg;
    String address;
    public Conversation(String address) {
        this.address=address;
        skin();
    }

    @Override
    public void init() {

        vbMsgs=new VBox();
        areaMsg=new TextArea();
        btnSend=new Button("Send");
        scConver=new ScrollPane();
        hbMsg=new HBox(5);
        Main.ROOT.setCenter(this);

    }

    @Override
    public void assemble() {
        scConver.setContent(vbMsgs);
        hbMsg.getChildren().addAll(areaMsg,btnSend);
        super.getChildren().addAll(scConver,hbMsg);


    }

    @Override
    public void style() {

        VBox.setVgrow(scConver, Priority.ALWAYS);
        areaMsg.setPromptText("Ecrire message ici");
        vbMsgs.getStyleClass().addAll("vb-msgs");
        areaMsg.getStyleClass().addAll("msg-area");
        areaMsg.setPrefColumnCount(50);
        areaMsg.setPrefRowCount(2);
        btnSend.getStyleClass().addAll("send");
        hbMsg.getStyleClass().addAll("hb-area");
        Main.hover(btnSend);
        areaMsg.setWrapText(true);
    }

    @Override
    public void event() {
        areaMsg.textProperty().addListener((obs,oldV,newV)->{
            if(newV.contains("\n")) areaMsg.setText(oldV);
            if(newV.length()>256)areaMsg.setText(oldV);
        });
        btnSend.setOnAction(e->{
            send(areaMsg.getText(),true);
        });
    }

    public void send(String msg ,boolean sended){
        if(msg.trim().length()==0||Main.client.client.isClosed())return;
        vbMsgs.getChildren().addAll(new Message(msg,sended));
        if(sended){
            Main.client.send(address,areaMsg.getText());
            areaMsg.setText("");

        }
        scConver.setVvalue(Double.MAX_VALUE);
    }

    private class Message extends HBox{
        TextFlow flow=new TextFlow();
        Text msg=new Text();

        public Message(String m,boolean sended) {
            this.msg.setText(m);

            if(sended){

                this.setAlignment(Pos.CENTER_LEFT);
                this.getStyleClass().addAll("hb-sended");
            }
            else {
                this.getStyleClass().addAll("hb-received");

            }
            flow.getStyleClass().addAll("flow");
            flow.getChildren().addAll(msg);
            this.getChildren().addAll(flow);

        }

    }


}

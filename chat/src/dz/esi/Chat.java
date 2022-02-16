package dz.esi;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Optional;
import java.util.TreeSet;

public class Chat extends BorderPane{
    VBox vbClients=new VBox(10);
    Button btnAdd=new Button("Ajouter Contact");
    TreeSet<Contact> contacts=new TreeSet<>();
    public Chat() {
        Main.ROOT.setLeft(this);
        Main.stage.setTitle("Client "+Main.client.client.getLocalSocketAddress().toString());
        setCenter(vbClients);
        getStyleClass().addAll("chat");
        btnAdd.getStyleClass().addAll("btn-add");
        btnAdd.setOnAction(e->{
            TextField tfAddress=new TextField();
            Alert a=new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dp=new DialogPane();
            dp.setContent(new HBox(20 ,new Label("Avec"),tfAddress));
            a.setDialogPane(dp);
            a.getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
            Optional<ButtonType> o=a.showAndWait();
            if(o.get()==ButtonType.OK){
                try{
                    String address=tfAddress.getText();
                    Inet4Address.getByName(address);
                    Inet6Address.getByName(address);
                    Contact c=new Contact(address);
                    if(contacts.contains(c))return;
                    contacts.add(c);
                    c.select();
                    vbClients.getChildren().addAll(c);

                    tfAddress.setText("");
                    System.out.println(contacts);
                }
                catch (Exception ef){
                    Main.error(ef.getMessage());
                }
            }
        });
        HBox hbAdd=new HBox(btnAdd);
        Main.growAlways(hbAdd);
        hbAdd.setAlignment(Pos.CENTER);
        setBottom(hbAdd);

        Main.hover(btnAdd);
    }
    public void addContact(Contact contact,String msg){
        System.out.println("ddin..."+msg+contact.address);
        contact.conversation.send(msg,false);
        vbClients.getChildren().addAll(contact);
        contact.select();
        System.out.println("2zaszasaz..."+msg+contact.address);


    }
}

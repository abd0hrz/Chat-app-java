package dz.esi;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class Contact extends HBox implements Comparable<Contact>{
    @Override
    public int compareTo(Contact o) {
        return address.getText().compareTo(o.address.getText());
    }

    Label address;
    Button btnRemove=new Button();
    Conversation conversation;

    public Contact(String a ) {
        a=a.trim();
        conversation=new Conversation(a);
        HBox hbRem=new HBox(btnRemove);
        hbRem.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(this, Priority.ALWAYS);
        HBox.setHgrow(hbRem, Priority.ALWAYS);
        address=new Label(a);
        getChildren().addAll(address,hbRem);
        getStyleClass().addAll("conatct");
        setOnMouseClicked(e->{
            select();
        });
        this.getStyleClass().add("selected");
        btnRemove.setOnAction(e->{
            try{
                ((Chat)Main.ROOT.getLeft()).vbClients.getChildren().remove(this);
                ((Chat)Main.ROOT.getLeft()).contacts.remove(this);
                Main.ROOT.setCenter(null);
            }
            catch (Exception se){
                btnRemove.fire();
            }
        });
        btnRemove.getStyleClass().addAll("btn-remove");
        address.getStyleClass().addAll("lb-address");
        Main.hover(this);


    }
    public void select(){
        try{
            Main.ROOT.setCenter(conversation);
            Main.selected.getStyleClass().removeAll("selected");

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            this.getStyleClass().add("selected");
            Main.selected=this;
        }

        Main.selected=this;

    }
    @Override
    public boolean equals(Object obj) {
        System.out.println(address.getText()+"   uuu");
        return ((Contact)obj).address.getText().equals(address.getText());
    }


}

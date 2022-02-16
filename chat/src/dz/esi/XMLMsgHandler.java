package dz.esi;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

public class XMLMsgHandler implements IMessages {

    private String msg;
    private Socket from;

    public XMLMsgHandler(String msg, Socket from) {

        this.setMsg(msg);
        this.from=from;

    }
    public  static String extractMsg(String xml)throws Exception{
        String content="";
        InputStream in=
                new ByteArrayInputStream(xml.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(in);
        content=document.getElementsByTagName("content").item(0).getTextContent();

        return content;
    }
    public  static String extractAddress(String msg)throws Exception{
        String dest;
        InputStream in=
                new ByteArrayInputStream(msg.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(in);
        document.getDocumentElement().normalize();
        dest=document.getElementsByTagName("from").item(0).getTextContent();
        return dest;
    }
    public static String createMessage(String dest,String content){
        //311
        while(content.length()<256)content+=" ";
        while(dest.length()<15)dest+=" ";
        String msg="<chat><to>"+dest+"</to><content>"+content+"</content></chat>";
        return msg;

    }


    @Override
    public String format() throws Exception{
        //316
        String xml="";
        String content=extractMsg(msg);
        while(content.length()<256)content+=" ";
        String dest=from.getRemoteSocketAddress().toString().split("/")[1].split(":")[0];
        while(dest.length()<15)dest+=" ";

        xml="<chat><from>"+ dest+"</from><content>"+content+"</content></chat>";

        return xml;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public InetAddress getDestination() throws Exception{
        String dest;
        InetAddress address=null;
        InputStream in=
                new ByteArrayInputStream(msg.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(in);
        document.getDocumentElement().normalize();

        dest=document.getElementsByTagName("to").item(0).getTextContent();

        address=InetAddress.getByName(dest);


        return address ;
    }
}

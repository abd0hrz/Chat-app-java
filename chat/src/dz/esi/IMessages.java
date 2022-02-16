package dz.esi;

import java.net.InetAddress;
import java.net.Socket;

public interface IMessages {
    String format() throws Exception;
    InetAddress getDestination() throws Exception;
}

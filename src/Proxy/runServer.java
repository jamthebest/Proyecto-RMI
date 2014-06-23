/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Proxy;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author usuario
 */
public class runServer {
    
    public static void startServer() {
        try {
            Registry Reg = LocateRegistry.createRegistry(1993);
            Reg.rebind("Chat", new Server());
        } catch (RemoteException ex) {
            System.out.println("Error mientras se levantaba el Servidor. " +ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        startServer();
    }
}

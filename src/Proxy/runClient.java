/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Proxy;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author JairoDavid
 */
public class runClient {
     
    public static void startClient(String u, String p) {
        try {
            int i = 0;
            Registry Reg = LocateRegistry.getRegistry("localhost", 1993);
            IServer Server = (IServer) Reg.lookup("Chat");
            Client Client = new Client(u,p,Server);
            Server.registerClient(Client);
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
            System.out.println("Error al conectarse al Servidor.");
        } catch (NotBoundException ex) {
            System.out.println("Error en el nombre del Servidor.");
        }    
    }
    
    public static void main(String[] args) {
        //String[] Names = {"Jairo","René","Martha","Eduardo","Luis","Martín"};
        Scanner s = new Scanner(System.in);
        Random r = new Random();
        /*String u, p;
        do {
            System.out.print("Ingrese usuario: ");
            u = s.nextLine();
            System.out.print("Ingrese contraseña: ");
            p = s.nextLine();
        } while (u.length() == 0 || p.length() == 0);*/
        //String u = Names[r.nextInt(Names.length)], p = u;
        String u = String.valueOf(10000 + r.nextInt(9999)), p = u;
        System.out.println("Client: " + u);
        startClient(u,p);
    }    
}

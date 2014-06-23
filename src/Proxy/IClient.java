/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Proxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author usuario
 */
public interface IClient extends Remote {
    public void setUser(String User) throws RemoteException;
    
    public void run() throws RemoteException;
    
    public String getUser() throws RemoteException;
    
    public void setPass(String Pass) throws RemoteException;
    
    public String getPass() throws RemoteException;
    
    public void setID(int ID) throws RemoteException;
    
    public int getID() throws RemoteException;
    
    public void sendMessage(Message Message) throws RemoteException;    
 
    public void getMessage(Message Message) throws RemoteException;
    
    public void seConectoUnUsuario(String user) throws RemoteException;
    
    public ArrayList<String> getOnlineUsers(String user) throws RemoteException;
}

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
public interface IServer extends Remote {
    //void sendMessage(int id, String name) throws RemoteException;
    
    public void registerClient(IClient c) throws RemoteException;
    
    public IClient findUser(int id) throws RemoteException;
    
    public int getUserIdByUsername(String username) throws RemoteException;
    
    public int insertClient(String u,String p,String nombre, String imagen) throws RemoteException;
    
    public ArrayList<Message> getConversationFromDatabase(int client, int friend) throws RemoteException;
    
    public void releaseClient(IClient c) throws RemoteException;
    
    public void sendMessage() throws RemoteException;
 
    public void getMessage(Message Message) throws RemoteException;
    
    public ArrayList<String> getUsers() throws RemoteException;
    
    public boolean authenClient(String user,String password)throws RemoteException;

    public ArrayList<String> FindFriends(String username)throws RemoteException;
    
    public void actualizarListado(String user)throws RemoteException;
}

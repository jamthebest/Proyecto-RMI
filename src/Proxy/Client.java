/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Proxy;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements IClient {
    private int ID;
    private String User;
    private String Pass;
    private final IServer Server;
    private final Queue<Message> Messages;

    public IServer getServer() {
        return Server;
    }

    public Queue<Message> getMessages() {
        return Messages;
    }
    private ArrayList<String> usuarios = new ArrayList();
    private LandingV2 landing;
    
    public Client(String User, String Pass, final IServer Server) throws RemoteException, NotBoundException {
        super();
        this.User = User;        
        this.Pass = Pass;        
        this.Server = Server;
        Messages = new LinkedList<>();        
    }
    
    @Override
    public void run()  throws RemoteException {
        while (true) {
                    String resp;
                    Scanner s = new Scanner(System.in);
                    do {
                        System.out.print("What? (m/r/e) ");
                        //s.nextLine();
                        resp = s.nextLine().trim();
                        //int r = Integer.valueOf(s.nextLine());
                        //resp = Integer.toString(r);
                        
                    } while (resp.length() == 0);
                    switch (resp.toLowerCase().charAt(0)) {
                        case 'm':
                            int usid;
                            String mess;
                            System.out.print("\nWhich user? ");
                            usid = s.nextInt();
                            s.nextLine();
                            System.out.print("Message? ");
                            mess = s.nextLine();
                            Message newe = new Message(getID(),usid,mess,"");
                            System.out.println(newe.toString() + "\n");
                            sendMessage(newe);
                            break;
                        case 'r':
                            System.out.println();
                            if (Messages.size() == 0) {
                                System.out.println("No hay mensajes nuevos.");
                            } else {
                                while (!Messages.isEmpty())
                                    System.out.println(Messages.peek().getStart() + " says: " + Messages.poll().getMessage());
                            }
                            System.out.println();
                            break;
                        case 'e':
                            go();
                            System.exit(0);
                            break;
                        default:
                            System.out.println("...Waiting...");
                    }
                }
    }
    
    @Override
    public void seConectoUnUsuario(String user){
        this.landing.hello(this.getOnlineUsers(user));
    }
    
    public void go() {
        try {
            Server.releaseClient(this);
        } catch (RemoteException ex) {
            System.out.println("Error cerrando sesión.");
        }
    }

    @Override
    public String getUser() {
        return User;
    }

    @Override
    public void setUser(String User) {
        this.User = User;
    }

    @Override
    public String getPass() {
        return Pass;
    }

    @Override
    public void setPass(String Pass) {
        this.Pass = Pass;
    }
        
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void setID(int ID) {
        this.ID = ID;
        this.landing = new LandingV2(this);
    }
    
    @Override
    public void getMessage(Message Message) {
        Messages.add(Message);
        this.landing.paint(Message);
        //String user = 
        //this.landing.recibirMensaje(Message.getMessage(),""+Message.getStart(),0);
    }
    
    @Override
    public void sendMessage(Message Message)  {
        try {
            Server.getMessage(Message);            
        } catch (RemoteException ex) {
            System.out.println("Error enviando mensaje al servidor.");
        }
    }
    
    @Override
    public String toString(){
        return "ID: " + this.ID + "\nUser: " + this.User + "\n";
    }
    
    /**
     *
     * @param user
     * @return
     */
    @Override
        public ArrayList<String> getOnlineUsers(String user){
        usuarios.clear();
        dbUsers db = new dbUsers();
        db.connectDataBase();
        usuarios = db.getFriends(user);
        return this.usuarios;
    }

    public ArrayList<String> Ismyfriend(ArrayList<String>friends,ArrayList<String> Users){
        ArrayList<String> Myfriends=new ArrayList<String>();
        for (int i = 0; i <friends.size() ; i++) {
            for (int j = 0; j < Users.size(); j++) {
                if(friends.get(i).toString().equals(Users.get(j).toString())){
                    Myfriends.add(friends.get(i).toString());
                }
            }
        }
        return Myfriends;
    }

}

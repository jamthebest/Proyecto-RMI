/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
/**
 *
 * @author Martha Hidalgo
 */
public class dbUsers {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    public void connectDataBase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error, no se encontro el driver de la base de datos.");
        } 
        try {
            String servidor = "jdbc:mysql://localhost/db_os_users";
            String usuarioDB="root";
            String passwordDB="root";
            connect = DriverManager.getConnection(servidor, usuarioDB, passwordDB);
            //connect = DriverManager.getConnection("jdbc:mysql://localhost/db_os_users?"
              //+ "user=root");//&password=123456");
            //System.out.println("se conecto");
        } catch (SQLException ex) {
            System.out.println("Error, No se pudo conectar a la base de datos!\n" + ex);
        }/*finally {
            close();
            }*/
    }
    public boolean isUnique(String u){
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM db_os_users.usuarios WHERE username = '"+u+"'");
            return resultSet.first();
        } catch (SQLException ex) {
            System.out.println("Error verificando que el username "+u+" es unico.");
        }
        return false;
    }
    public boolean authClient(String u,String p){
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM db_os_users.usuarios WHERE username = '"+u+"'");
            while(resultSet.next()){
                if(resultSet.getString("password").equals(p)){
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error, autenticando al usuario "+u+"con contraseña "+p);
        }
        return false;
    }
    public int insertClient(String u, String p,String nombre, String imagen){
        String res = "";
        for (int i = 0; i < imagen.length(); i++) {
            char x = imagen.charAt(i);
            if (x != '\\') {
                res += Character.toString(x);
            }else{
                res += "/";
            }
        }
        imagen = res;
        //System.err.println(imagen);
        try {
            preparedStatement = connect.prepareStatement("INSERT INTO `db_os_users`.`usuarios` ( `username`,`password`,`nombre_completo`,`foto`,`estado`) "+"VALUES ( '"+u+"','"+p+"','"+nombre+"','"+imagen+"','0')");
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("No se pudo agregar el cliente "+u+" a la base de datos.");
        }//*/
        return -1;
    }
    public String[] getClient(String u){
        String datos[]=new String[5];
        int i=0;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM db_os_users.usuarios WHERE username = '"+u+"'");
            while(resultSet.next()){
                datos[0]=resultSet.getString("id_user");
                datos[1]=resultSet.getString("username");
                datos[2]=resultSet.getString("password");
                datos[3]=resultSet.getString("nombre_completo");
                datos[4]=resultSet.getString("estado");
            }
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario: "+u +"\n"+ex.getMessage());
        }
        return datos;
    }
    
    public String getUsername(int id){
        String user = "";
        int i=0;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT username FROM db_os_users.usuarios WHERE id_user = '"+id+"'");
            while(resultSet.next()){
                return resultSet.getString("username");
            }
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario: "+id +"\n"+ex.getMessage());
        }
        return user;
    }
    
    public void setClienteOnline(String u){
        try {
            //System.out.print(u);
            preparedStatement = connect
                    .prepareStatement("UPDATE `db_os_users`.`usuarios` SET  `estado` = 1 WHERE username = '"+u+"'");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            //System.out.println("No se pudo agregar el cliente "+u+" a la base de datos.");
        }
    }
    public void setClienteOffline(String u){
        try {
            //System.out.print(u);
            preparedStatement = connect
                    .prepareStatement("UPDATE `db_os_users`.`usuarios` SET  `estado` = 0 WHERE username = '"+u+"'");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            //System.out.println("No se pudo agregar el cliente "+u+" a la base de datos.");
        }
    }
    public int getClientid(String u){
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM db_os_users.usuarios WHERE username = '"+u+"'");
            while(resultSet.next()){
                return resultSet.getInt("id_user");
            }
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario: "+u);
        }
        return -1;
    }
    
    public int getClientId2(String nombre){
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM db_os_users.usuarios WHERE nombre_completo = '"+nombre+"'");
            while(resultSet.next()){
                return resultSet.getInt("id_user");
            }
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario: "+nombre);
        }
        return -1;
    }
    
    public void insertMessage(int u1,int u2,String m){
        
        //Date now = new Date();
        //String date = now.
        String date;
        date = Calendar.getInstance().get(Calendar.YEAR)+"-"+Calendar.getInstance().get(Calendar.MONTH)+"-"+Calendar.getInstance().get(Calendar.DATE)+" "+Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+":"+Calendar.getInstance().get(Calendar.SECOND);
        try {
            preparedStatement = connect.prepareStatement("INSERT INTO `db_os_users`.`mensajes` ( `id_user1`,`id_user2`,`mensaje`,`hora`) "+"VALUES ( '"+u1+"','"+u2+"','"+m+"','"+date+"')");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
            //2013-12-10 08:16:31
            //System.out.println("No se pudo agregar el cliente "+u+" a la base de datos.");
        }
    }
    
    public ArrayList<Message> getConversation(int client, int friend) {
        ArrayList<Message> conversation = new ArrayList();
        try {
            statement = connect.createStatement();
            String query =
                      "SELECT * FROM db_os_users.mensajes "
                    + "WHERE (id_user1 = '"+client+"' "
                    + "AND id_user2 = '"+friend+"') "
                    + "OR (id_user1 = '"+friend+"' AND id_user2 = '"+client+"')"
                    + "ORDER BY hora ASC";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                int user1 = resultSet.getInt("id_user1");
                int user2 = resultSet.getInt("id_user2");
                String mensaje = resultSet.getString("mensaje");
                String hora = resultSet.getString("hora");
                conversation.add(new Message(user1,user2,mensaje,hora));
            }                           
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario");
        }        
        return  conversation;
    }
    
    public String getFoto(String u){
        try {
            statement = connect.createStatement();
            String query = "SELECT foto FROM db_os_users.usuarios "
                    + "WHERE username = '"+u+"'";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                String re = resultSet.getString("foto");
                String res = "";
                for (int i = 0; i < re.length(); i++) {
                    char x = re.charAt(i);
                   // if (x != '/') {
                        res += Character.toString(x);
                   // }else{
                       // res += "\\";
                   // }
                }
                return res;
            }
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario");
        }        
        return "";
    }
    
    public int enviarSolicitud(int u1, int u2){
        try {
            String date;
            date = Calendar.getInstance().get(Calendar.YEAR)+"-"+Calendar.getInstance().get(Calendar.MONTH)+"-"+Calendar.getInstance().get(Calendar.DATE)+" "+Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+":"+Calendar.getInstance().get(Calendar.SECOND);
            preparedStatement = connect.prepareStatement("INSERT INTO `db_os_users`.`solicitud` ( `id_user1`,`id_user2`,`activo`,`fecha`) "+"VALUES ( '"+u1+"','"+u2+"','1','"+date+"')");
            int enviar;
            enviar = preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Solicitud Enviada Correctamente!");
            return enviar;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo enviar la Solicitud!\n" + ex.getMessage());
        }
        return -1;
    }
    
    public ArrayList<String> obtenerUsuarios(String user) throws RemoteException, NotBoundException{
        ArrayList<String> usuarios = new ArrayList();
        try {
            statement = connect.createStatement();
            String query = "SELECT * FROM `db_os_users`.`usuarios` u inner join solicitud s on "
                    + "s.id_user2 != u.id_user where s.id_user1 != (select id_user from usuarios "
                    + "where username = '" + user + "') and s.activo = 1";
            resultSet = statement.executeQuery(query);
            
            while(resultSet.next()){
                String nombre = resultSet.getString("nombre_completo");
                usuarios.add(nombre);              
            }                           
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario\n" + ex.getMessage());
        }        
        return  usuarios;
    }
    
    public void responderSolicitud(int user, int resp, boolean respuesta){
        if (respuesta) {
            try {
                preparedStatement = connect.prepareStatement("INSERT INTO `db_os_users`.`amigos` ( `id_user`,`id_user2`) "+"VALUES ( '"+user+"','"+resp+"')");
                preparedStatement.executeUpdate();
                preparedStatement = connect
                    .prepareStatement("UPDATE `db_os_users`.`solicitud` SET  `activo` = 0 WHERE id_user2 = '"+user+"' and "
                            + "id_user1 = '" + resp + "'");
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Solicitud Aceptada!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo aceptar la Solicitud!\n" + ex.getMessage());
            }
        }else{
            try{
                preparedStatement = connect
                    .prepareStatement("UPDATE `db_os_users`.`solicitud` SET  `activo` = 0 WHERE id_user1 = '"+user+"' and "
                    + "id_user2 = '" + resp + "'");
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Solicitud Rechazada!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo rechazar la Solicitud!\n" + ex.getMessage());
            }
        }
    }
    
    public ArrayList<String> obtenerSolicitudes(String user) throws RemoteException, NotBoundException{
        ArrayList<String> usuarios = new ArrayList();
        try {
            statement = connect.createStatement();
            String query = "SELECT * FROM `db_os_users`.`usuarios` u inner join solicitud s on "
                    + "s.id_user1 = u.id_user where s.id_user2 = (select id_user from usuarios where "
                    + "username = '" + user + "') and s.activo = 1";
            resultSet = statement.executeQuery(query);
            
            while(resultSet.next()){
                String nombre = resultSet.getString("nombre_completo");
                usuarios.add(nombre);              
            }                           
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario\n" + ex.getMessage());
        }        
        return  usuarios;
    }

    public ArrayList<String>getFriends(String u){
        ArrayList <String> Friends =new ArrayList<>();
        int i=0;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT username FROM usuarios u inner join amigos a on "
                    + "a.id_user2 = u.id_user or a.id_user = u.id_user where u.id_user != ("
                    + "select id_user from usuarios where username = '" + u + "') and u.estado = 1");
            while(resultSet.next()){
                Friends.add(resultSet.getString("username"));
            }
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario: "+u);
        }
        return Friends;
    }
    
    public int Post(int user, String comment, String imagen){
        String res = "";
        for (int i = 0; i < imagen.length(); i++) {
            char x = imagen.charAt(i);
            if (x != '\\') {
                res += Character.toString(x);
            }else{
                res += "/";
            }
        }
        imagen = res;
        
        try {
            preparedStatement = connect.prepareStatement("INSERT INTO `db_os_users`.`comentarios` "
                    + "( `id_user`,`comentario`, `imagen`) "+"VALUES ( '"+user+"','"+comment+"','"+imagen+"')");
            int ret = preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Post Publicado!");
            return ret;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo realizar el Post!\n" + ex.getMessage());
        }
        return -1;
    }
    
    public ArrayList<ArrayList<String>>getPosts(String usuario){
        ArrayList <String> Post = new ArrayList<>();
        ArrayList <ArrayList<String>> Posts =new ArrayList<>();
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * from comentarios where id_user = (select id_user from "
                    + "usuarios where username = '"+usuario+"') or id_user in (SELECT u.id_user FROM usuarios u inner join "
                    + "amigos a on a.id_user2 = u.id_user or a.id_user = u.id_user where u.id_user != (select id_user "
                    + "from usuarios where username = '"+usuario+"')) ORDER BY id DESC");
            while(resultSet.next()){
                String imagen = resultSet.getString("imagen");
                String res = "";
                for (int i = 0; i < imagen.length(); i++) {
                    char x = imagen.charAt(i);
                   if (x != '/') {
                        res += Character.toString(x);
                   }else{
                       res += "\\";
                   }
                }
                Post.add(resultSet.getString("id_user"));
                Post.add(resultSet.getString("comentario"));
                Post.add(res);
                Posts.add(Post);
                Post = new ArrayList<>();
            }
            //return datos;
        } catch (SQLException ex) {
            System.out.println("Error extrayendo la informacion del usuario: "+usuario);
        }
        return Posts;
    }
    
    public int likeComment(int user, String comment, String imagen){
        try {
            statement = connect.createStatement();
            String query = "SELECT id FROM `db_os_users`.`comentarios` where id_user = '"+user+"'"
                    + "and comentario = '"+comment+"'";
            resultSet = statement.executeQuery(query);
            int comentario = 0;
            while(resultSet.next()){
                comentario = resultSet.getInt("id");
            }
            if (comentario != 0 && comentario != -1) {
                preparedStatement = connect.prepareStatement("INSERT INTO `db_os_users`.`likes_comments` "
                    + "( `user`,`comment`) "+"VALUES ( '"+user+"','"+comentario+"')");
                int enviar;
                enviar = preparedStatement.executeUpdate();
                return enviar;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al almacenar LIKE!\n" + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Error al almacenar LIKE!\n");
        return -1;
    }
    
    public int getLikeComment(int user,int user2, String comment){
        try {
            statement = connect.createStatement();
            String query = "SELECT id FROM `db_os_users`.`likes_comments` where user = '" +user2+"' and "
                    + "comment = (SELECT id FROM `db_os_users`.`comentarios` where id_user = '"+user+"'"
                    + "and comentario = '"+comment+"')";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                return resultSet.getInt("id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener Informacion del Post!\n" + ex.getMessage());
        }
        return -1;
    }
}
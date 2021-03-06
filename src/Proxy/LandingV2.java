/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author usuario
 */
public class LandingV2 extends javax.swing.JFrame {

    /**
     * Creates new form RMI_App
     */
    private final HTMLEditorKit kit = new HTMLEditorKit();
    private final HTMLDocument doc = new HTMLDocument();
    private int chatUser;
    private final Client client;
    private final dbUsers db;
    int cont = 1;
    
    public LandingV2(final Client client) {
        initComponents();
         setSize(312, 534);
        setLocationRelativeTo(null);
        this.client = client;
        db = new dbUsers();
        db.connectDataBase();
        right_sidebar.setVisible(false);
        ImageIcon ic;
        if (db.getFoto(client.getUser()).equals("")) {
            ic = new ImageIcon(getClass().getResource("/ProxyImages/user.png"));
        }else{
            ic = new ImageIcon(db.getFoto(client.getUser()));
        }
        //lineas agregadas
        Image img = ic.getImage();
        BufferedImage bi = new BufferedImage(78, 78, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, 78, 78, null);
        
        ImageIcon newIcon = new ImageIcon(bi);
        Icon icono = new ImageIcon(newIcon.getImage().getScaledInstance(foto.getWidth(), foto.getHeight(), Image.SCALE_DEFAULT));
        foto.setIcon(icono);
        foto.setSize(78,78);
        this.repaint();
         
        this.setVisible(true);
        Color containerColor = new Color(111,168,162);
        Color headerColor = new Color(111,168,162);
        Color content = new Color(111,168,162);
        getContentPane().setBackground(content);
        container.setBackground(containerColor);
        header.setBackground(headerColor);        
        right_sidebar.setBackground(content);
        left_sidebar.setBackground(content);
        jScroll.getViewport().setBackground(containerColor);
        txtuserid.setText("Bienvenido: "+client.getUser()+" -> "+client.getID());
        lista.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    if(right_sidebar.isVisible()) {
                        textPanel.setText("");
                    }
                    agregarMensaje("Chateando con: "+lista.getSelectedValue().toString());
                    try {                        
                        //System.out.println("id obtenido es: "+client.getServer().getUserIdByUsername(lista.getSelectedValue().toString()));
                        chatUser = (int) client.getServer().getUserIdByUsername(lista.getSelectedValue().toString());
                        ArrayList<Message> mensaje = client.getServer().getConversationFromDatabase(client.getID(),chatUser);
                        
                        for(Message msj:mensaje){                            
                            if(msj.getStart() == client.getID()){
                                
                                recibirMensaje(msj.getMessage(),""+msj.getEnd(),1);
                            } else{
                                recibirMensaje(msj.getMessage(),""+msj.getEnd(),0);
                            }
                            //System.out.println("el end es: "+msj.getEnd());
                            
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(LandingV2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    right_sidebar.setVisible(true);
                }
            }
        });
      
    }
    
    public void hello(ArrayList<String> array){        
        final DefaultListModel model = new DefaultListModel();
        
        for(String u:array){
            if(!u.equals(client.getUser())){
                model.addElement(u);
            }
        }
        lista.setModel(model);
    }
    
    public int getOpenChat(){
        return this.chatUser;
    }
    
    private void agregarMensaje(String mensaje){
        try {
            textPanel.setEditorKit(kit);
            textPanel.setDocument(doc);
            String style = "<style>\n"+
                        "body{"
                        + "background:rgb(240,240,240);"+
                        "}" +
"			.left{\n" +
                                "background: rgb(165,207,231);\n" +
                                "padding: 15px;\n" +
                                "margin-right: 50px;\n" +
                                "margin-left: 10px;\n" +
                                "margin-bottom: 10px;\n" +
                                "border-radius: 30px;\n" +
                                "-moz-border-radius: 30px \n"+
                                "-webkit-border-radius: 30px \n"+                                 
                        "}\n" +
                        ".right{\n" +
                                "background: rgb(215,232,164);\n" +
                                "padding: 15px;\n" +
                                "margin-left: 50px;\n" +
                                "margin-right: 10px;\n" +
                                "margin-bottom: 10px;\n" +
                                "border-radius: 30px;\n" +
                                "-moz-border-radius: 30px \n"+
                                "-webkit-border-radius: 30px \n"+
                        "}\n" +
                    "</style>";
            kit.insertHTML(doc, doc.getLength(), style+"<b>"+mensaje+"</b><hr>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            System.out.println(ex);
        }        
    }
    
    public void paint(Message Message){                
        if(Message.getStart() == this.chatUser) {
            recibirMensaje(Message.getMessage(),""+Message.getStart(),0);
        }
    }
    
    public void recibirMensaje(String mensaje, String usuario, int tipo){        
        try {
            textPanel.setEditorKit(kit);
            textPanel.setDocument(doc);
            if(tipo == 1){
                //kit.insertHTML(doc, doc.getLength(),"<font color='green'><b>yo digo que:</b></font>" , 0, 0, null);
                String div = "<div class='left'><p>"+mensaje+"</p></div>";
                kit.insertHTML(doc, doc.getLength(),div, 0, 0,null);
            } else {
                //kit.insertHTML(doc, doc.getLength(),"<font color='blue'><b>"+usuario+" dice que:</b></font>" , 0, 0, null);
                String div = "<div class='right'><p>"+mensaje+"</p></div>";
                kit.insertHTML(doc, doc.getLength(), div, 0, 0,null);                                
            }
            
            textPanel.setAutoscrolls(true);
            
        } catch (BadLocationException | IOException ex) {
            System.out.println(ex);
        }        
    }    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        foto = new javax.swing.JLabel();
        txtuserid = new javax.swing.JLabel();
        cerrarSesion = new javax.swing.JButton();
        right_sidebar = new javax.swing.JPanel();
        messagePanel = new javax.swing.JScrollPane();
        textPanel = new javax.swing.JTextPane();
        btnEnviarMensaje = new javax.swing.JButton();
        btnCancelarMensaje = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        mensajeAEnviar = new javax.swing.JTextArea();
        jScroll = new javax.swing.JScrollPane();
        left_sidebar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        lista = new javax.swing.JList();
        btnBuscar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnpost = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(940, 700));
        setResizable(false);

        container.setPreferredSize(new java.awt.Dimension(824, 650));

        foto.setBackground(new java.awt.Color(43, 128, 125));
        foto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProxyImages/user.png"))); // NOI18N
        foto.setToolTipText("");

        txtuserid.setFont(new java.awt.Font("Futura", 0, 13)); // NOI18N
        txtuserid.setForeground(new java.awt.Color(255, 255, 192));
        txtuserid.setText("Mi Usuario es: ");

        cerrarSesion.setBackground(new java.awt.Color(43, 128, 125));
        cerrarSesion.setFont(new java.awt.Font("Futura", 1, 13)); // NOI18N
        cerrarSesion.setForeground(new java.awt.Color(255, 255, 192));
        cerrarSesion.setText("Salir");
        cerrarSesion.setPreferredSize(new java.awt.Dimension(78, 78));
        cerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtuserid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(foto, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(330, 330, 330))
            .addGroup(headerLayout.createSequentialGroup()
                .addComponent(txtuserid)
                .addGap(30, 30, 30)
                .addComponent(foto, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        right_sidebar.setPreferredSize(new java.awt.Dimension(544, 520));

        textPanel.setEditable(false);
        textPanel.setFont(new java.awt.Font("Futura", 0, 13)); // NOI18N
        messagePanel.setViewportView(textPanel);

        btnEnviarMensaje.setBackground(new java.awt.Color(43, 128, 125));
        btnEnviarMensaje.setFont(new java.awt.Font("Futura", 1, 13)); // NOI18N
        btnEnviarMensaje.setForeground(new java.awt.Color(255, 255, 192));
        btnEnviarMensaje.setText("Enviar");
        btnEnviarMensaje.setPreferredSize(new java.awt.Dimension(138, 78));
        btnEnviarMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarMensajeActionPerformed(evt);
            }
        });

        btnCancelarMensaje.setBackground(new java.awt.Color(43, 128, 125));
        btnCancelarMensaje.setFont(new java.awt.Font("Futura", 1, 13)); // NOI18N
        btnCancelarMensaje.setForeground(new java.awt.Color(255, 255, 192));
        btnCancelarMensaje.setText("Cancelar");
        btnCancelarMensaje.setPreferredSize(new java.awt.Dimension(138, 78));

        mensajeAEnviar.setColumns(20);
        mensajeAEnviar.setFont(new java.awt.Font("Futura", 0, 13)); // NOI18N
        mensajeAEnviar.setRows(5);
        mensajeAEnviar.setAutoscrolls(false);
        jScrollPane2.setViewportView(mensajeAEnviar);

        javax.swing.GroupLayout right_sidebarLayout = new javax.swing.GroupLayout(right_sidebar);
        right_sidebar.setLayout(right_sidebarLayout);
        right_sidebarLayout.setHorizontalGroup(
            right_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(right_sidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(right_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messagePanel)
                    .addGroup(right_sidebarLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(right_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEnviarMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(btnCancelarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        right_sidebarLayout.setVerticalGroup(
            right_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(right_sidebarLayout.createSequentialGroup()
                .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(right_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(right_sidebarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnEnviarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(118, 118, 118))
        );

        left_sidebar.setBackground(new java.awt.Color(111, 168, 162));
        left_sidebar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        left_sidebar.setMaximumSize(null);
        left_sidebar.setPreferredSize(new java.awt.Dimension(240, 518));

        jLabel2.setFont(new java.awt.Font("Futura", 0, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 192));
        jLabel2.setText("Usuarios");

        jComboBox1.setBackground(new java.awt.Color(43, 128, 125));
        jComboBox1.setFont(new java.awt.Font("Futura", 1, 13)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(0, 0, 102));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Conectados", "No Conectados", "Todos" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        lista.setFont(new java.awt.Font("Futura", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(lista);

        javax.swing.GroupLayout left_sidebarLayout = new javax.swing.GroupLayout(left_sidebar);
        left_sidebar.setLayout(left_sidebarLayout);
        left_sidebarLayout.setHorizontalGroup(
            left_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(left_sidebarLayout.createSequentialGroup()
                .addGroup(left_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator1)
                    .addGroup(left_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(left_sidebarLayout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(left_sidebarLayout.createSequentialGroup()
                            .addGap(105, 105, 105)
                            .addComponent(jLabel2))
                        .addGroup(left_sidebarLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(left_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        left_sidebarLayout.setVerticalGroup(
            left_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(left_sidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        jScroll.setViewportView(left_sidebar);

        btnBuscar.setBackground(new java.awt.Color(43, 128, 125));
        btnBuscar.setFont(new java.awt.Font("Futura", 1, 13)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 192));
        btnBuscar.setText("Buscar Amigos");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(43, 128, 125));
        jButton1.setFont(new java.awt.Font("Futura", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 192));
        jButton1.setText("Ver Solicitudes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnpost.setBackground(new java.awt.Color(43, 128, 125));
        btnpost.setFont(new java.awt.Font("Futura", 1, 13)); // NOI18N
        btnpost.setForeground(new java.awt.Color(255, 255, 192));
        btnpost.setText("POSTS");
        btnpost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpostActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(containerLayout.createSequentialGroup()
                        .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnpost, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(containerLayout.createSequentialGroup()
                                    .addComponent(btnBuscar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(right_sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)))
                .addContainerGap())
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(jScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnpost))
                    .addComponent(right_sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(111, 111, 111))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, 556, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarMensajeActionPerformed

        try {
            Message newe = new Message(client.getID(),chatUser,mensajeAEnviar.getText(),"");
            if("".equals(newe.getMessage())){
                newe.setMessage(this.client.getID()+"-"+chatUser +"-c "+cont++);
            }
            client.sendMessage(newe);
            //kit.insertHTML(doc, doc.getLength(),"<font color='green'><b> yo digo que :</b></font>" , 0, 0, null);
            kit.insertHTML(doc, doc.getLength(), "<div class='left'<p>"+mensajeAEnviar.getText()+"</p></div>", 0, 0,null);
        } catch (BadLocationException | IOException ex) {
            System.out.println(ex);
        }
        mensajeAEnviar.setText("");
    }//GEN-LAST:event_btnEnviarMensajeActionPerformed

    private void cerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarSesionActionPerformed
        client.go();
        System.exit(0);
    }//GEN-LAST:event_cerrarSesionActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        final Client c = client;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new EnviarSolicitud(c).setVisible(true);
                } catch (        RemoteException | NotBoundException ex) {
                    Logger.getLogger(LandingV2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        final Client c = client;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Solicitudes(c).setVisible(true);
                } catch (        RemoteException | NotBoundException ex) {
                    Logger.getLogger(LandingV2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnpostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpostActionPerformed
        final Client c = client;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Post(c).setVisible(true);
            }
        });
    }//GEN-LAST:event_btnpostActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelarMensaje;
    private javax.swing.JButton btnEnviarMensaje;
    private javax.swing.JButton btnpost;
    private javax.swing.JButton cerrarSesion;
    private javax.swing.JPanel container;
    private javax.swing.JLabel foto;
    private javax.swing.JPanel header;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScroll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel left_sidebar;
    private javax.swing.JList lista;
    private javax.swing.JTextArea mensajeAEnviar;
    private javax.swing.JScrollPane messagePanel;
    private javax.swing.JPanel right_sidebar;
    private javax.swing.JTextPane textPanel;
    private javax.swing.JLabel txtuserid;
    // End of variables declaration//GEN-END:variables
}


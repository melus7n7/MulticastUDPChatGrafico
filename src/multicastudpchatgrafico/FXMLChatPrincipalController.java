/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package multicastudpchatgrafico;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author sulem
 */
public class FXMLChatPrincipalController implements Initializable {
    
    private String nombreUsuario;
    private InetAddress direccion;
    private int puerto;
    private MulticastSocket socket;
    
    private static boolean haTerminado;
    
    @FXML
    private Label label;
    @FXML
    private TextArea txtAreaChat;
    @FXML
    private Label lblUsuario;
    @FXML
    private TextField txtFieldMensaje;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        haTerminado = false;
        try {
            direccion = InetAddress.getByName("224.0.0.0");
            puerto = 8080;
            socket = new MulticastSocket(puerto);
            socket.joinGroup(direccion);
            
            recibirMensajes();
             
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
       
    }    
    
    public void mostrarChat(String nombreUsuario, Stage escenario){        
        this.nombreUsuario = nombreUsuario;
        lblUsuario.setText("Usuario: " + nombreUsuario);
        
        escenario.setOnHiding( event -> {
            haTerminado = true;
            String linea = nombreUsuario + " ha salido del chat";
            byte[] m = linea.getBytes();
            DatagramPacket mensaje = new DatagramPacket(m, m.length, direccion, puerto);
            try {
                socket.send(mensaje);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } );
        
        String linea = nombreUsuario + " se ha unido al chat ";
        byte[] m = linea.getBytes();
        
        try {
            DatagramPacket mensaje = new DatagramPacket(m, m.length, direccion, puerto);
            socket.send(mensaje);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
    }

    @FXML
    private void clicEnviar(ActionEvent event) {
        enviarMensaje();
        txtFieldMensaje.setText("");
    }
    
    private void enviarMensaje(){
        String linea = nombreUsuario + ": " + txtFieldMensaje.getText().trim();
        byte[] m = linea.getBytes();
        
        Thread hiloEnviar = new Thread(()->{
            try {
                DatagramPacket mensaje = new DatagramPacket(m, m.length, direccion, puerto);
                socket.send(mensaje);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        
        hiloEnviar.start();
    }
    
    private void recibirMensajes(){
        Thread hiloRecibir = new Thread(()->{
            try {
                byte[] mensaje = new byte[1024];
                String linea;
                while(!haTerminado){
                    DatagramPacket recibido = new DatagramPacket(mensaje, mensaje.length, direccion, puerto);
                    socket.receive(recibido);
                    
                    linea = new String(recibido.getData(), 0, recibido.getLength());
                    
                    txtAreaChat.setText(txtAreaChat.getText() + "\n" + linea);

                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        
        hiloRecibir.start();
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package multicastudpchatgrafico;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sulem
 */
public class FXMLNombreUsuarioController implements Initializable {

    @FXML
    private TextField txtFieldNombreUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicConfirmar(ActionEvent event) {
        String nombreUsuario = txtFieldNombreUsuario.getText().trim();
        if(nombreUsuario.isEmpty()){
            System.out.println("Necesita tener algo");
            return;
        }
        
        Stage escenarioBase = (Stage) txtFieldNombreUsuario.getScene().getWindow();
        try {
            FXMLLoader accesoControlador = new FXMLLoader(MulticastUDPChatGrafico.class.getResource("FXMLChatPrincipal.fxml"));
            Parent vista = accesoControlador.load();
            FXMLChatPrincipalController chat = accesoControlador.getController();
            chat.mostrarChat(nombreUsuario, escenarioBase);
            
            escenarioBase.setScene(new Scene (vista));
            escenarioBase.setTitle("Chat");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage escenarioBase = (Stage) txtFieldNombreUsuario.getScene().getWindow();
        escenarioBase.close();
    }
    
}

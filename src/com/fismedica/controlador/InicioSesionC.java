package com.fismedica.controlador;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.CitaCabecera;
import com.fismedica.modelo.CitaCabeceraDAO;
import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.modelo.Usuario;
import com.fismedica.modelo.UsuarioDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.Encriptado;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InicioSesionC {
	Tooltip toolTip;
	@FXML private ImageView pbFoto;
	@FXML private Button btnAceptar;
	@FXML private Button btnCancelar;
	@FXML private TextField txtUsuario;
	@FXML private PasswordField txtContrasenia;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	MedicoDAO medicoDAO = new MedicoDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Aceptar");
			btnAceptar.setStyle("-fx-graphic: url('/aceptar.png');-fx-cursor: hand;");
			btnAceptar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Cancelar");
			btnCancelar.setStyle("-fx-graphic: url('/cancelar.png');-fx-cursor: hand;");
			btnCancelar.setTooltip(toolTip);
			
			Image image = new Image("usuario_login.png");
			pbFoto.setImage(image);
			pbFoto.setSmooth(true);
			pbFoto.setCache(true);
			pbFoto.setPreserveRatio(true);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void aceptar() {
		try {
			List<Usuario> usuario;
    		usuario = usuarioDAO.getUsuario(Encriptado.Encriptar(txtUsuario.getText()),Encriptado.Encriptar(txtContrasenia.getText()));
    		System.out.println(usuario.size());
    		if(usuario.size() > 0){
    			System.out.println(usuario.size());
    			Context.getInstance().setUsuario(usuario.get(0));
    			helper.abrirPantallaPrincipal("SISTEMA - FISMEDICA","/principal/FrmPrincipal.fxml");
    			if(usuario.get(0).getPerfil() != null)//si el perfil esta nulo
    				listaCitasDia();
    			else
    				listaCitasDiaMedico();
    		}else {
    			helper.mostrarAlertaError("Usuario o clave incorrecto", Context.getInstance().getStage());
    		}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	CitaCabeceraDAO citaDAO = new CitaCabeceraDAO();
	public void listaCitasDia() {
		try {
			List<CitaCabecera> citasDia = citaDAO.getCitasPorDia(new Date());
			if(citasDia.size() > 0) {
				Context.getInstance().setBanderaFacturar(true);
				helper.abrirPantallaModal("/procesos_inicio/PCitasDia.fxml","Citas del día", Context.getInstance().getStage());
				Context.getInstance().setBanderaFacturar(false);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void listaCitasDiaMedico() {
		try {
			//hay que buscar el id del medico... se puede buscar por el id del usuario
			List<Medico> listaMedico = medicoDAO.getListaMedicosPorIdUsuario(Context.getInstance().getUsuario().getIdUsuario());
			Medico medico = new Medico();
			if(listaMedico.size() > 0)
				medico = listaMedico.get(0);
			
			List<CitaCabecera> citasDia = citaDAO.getCitasPorDiaMedico(new Date(), medico.getIdMedico());
			if(citasDia.size() > 0) {
				Context.getInstance().setBanderaFacturar(true);
				helper.abrirPantallaModal("/procesos_inicio/PCitasDia.fxml","Citas del día", Context.getInstance().getStage());
				Context.getInstance().setBanderaFacturar(false);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void cancelar() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Salir?",Context.getInstance().getStagePrincipal());
			if(result.get() == ButtonType.OK)
				System.exit(0);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

package com.fismedica.controlador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.modelo.Perfil;
import com.fismedica.modelo.PerfilDAO;
import com.fismedica.modelo.Usuario;
import com.fismedica.modelo.UsuarioDAO;
import com.fismedica.util.Constantes;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.Encriptado;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

public class SUsuariosC {
	@FXML private TextField txtCodigo;
	@FXML private TextField txtCedula;
	@FXML private TextField txtNombres;
	@FXML private TextField txtApellidos;
	@FXML private CheckBox chkEstado;
    @FXML private ComboBox<Perfil> cboRol;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtUsuario;
	@FXML private PasswordField txtClave;
	@FXML private ImageView ivFoto;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnExaminar;
	@FXML private Button btnQuitar;
	@FXML private Button btnBuscar;
	@FXML private Label lbCodigo;
	@FXML private Label lblMensajeCedula;
	

	ControllerHelper helper = new ControllerHelper();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	PerfilDAO perfilDAO = new PerfilDAO();
	MedicoDAO medicoDAO = new MedicoDAO();
	Usuario usuarioSeleccionado;
	Integer idMedicoSeleccionado;
	
	public void initialize(){
		btnBuscar.setStyle("-fx-cursor: hand;");
		btnExaminar.setStyle("-fx-cursor: hand;");
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		btnQuitar.setStyle("-fx-cursor: hand;");
		lblMensajeCedula.setStyle("-fx-font-size: 14px;\r\n" + 
				"    -fx-font-weight: bold;\r\n" + 
				"    -fx-text-fill: #ff0000;");
		lblMensajeCedula.setVisible(false);
		
		int maxLength = 10;
		limpiar();
		Context.getInstance().setUsuarioSeleccionado(null);
		Context.getInstance().setCodigoTipoCliente(Constantes.ID_NORMAL);
		//validar solo letras.... igual se va con puntuaciones
		txtNombres.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\D*")) {
					txtNombres.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}
		});

		//validar solo letras.... igual se va con puntuaciones
		txtApellidos.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\D*")) {
					txtApellidos.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}
		});

		//solo letras mayusculas
		txtNombres.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtNombres.getText().toUpperCase();
				txtNombres.setText(cadena);
			}
		});
		txtDireccion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDireccion.getText().toUpperCase();
				txtDireccion.setText(cadena);
			}
		});
		txtApellidos.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtApellidos.getText().toUpperCase();
				txtApellidos.setText(cadena);
			}
		});
		//validar solo numeros
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtCedula.setText(oldValue);
				}
			}
		});
		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtTelefono.setText(oldValue);
				}
			}
		});
		//validar solo 10 valores
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtCedula.getText().length() > maxLength) {
					String s = txtCedula.getText().substring(0, maxLength);
					txtCedula.setText(s);
				}
			}
		});

		//repetir cedula en usuario y contraseña
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				txtUsuario.setText(newValue);
				txtClave.setText(newValue);	
			}
		});

		txtCedula.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					if (validarCedula(txtCedula.getText()) == false){
						//helper.mostrarAlertaError("El número de cedula es incorrecto!", Context.getInstance().getStage())
						limpiar();
						txtCedula.requestFocus();
					}else
						recuperarDatos(txtCedula.getText());
					//txtNombres.requestFocus();
				}
			}
		});

		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtTelefono.setText(oldValue);
				}
			}
		});

		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtTelefono.getText().length() > maxLength) {
					String s = txtTelefono.getText().substring(0, maxLength);
					txtTelefono.setText(s);
				}
			}
		});
		txtCedula.focusedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue){
				if (newPropertyValue){
					//System.out.println("Textfield on focus");
				}
				else{
					if (validarCedula(txtCedula.getText()) == false){
						lblMensajeCedula.setVisible(true);
						//helper.mostrarAlertaError("El número de cedula es incorrecto!", Context.getInstance().getStage());
						limpiar();
						txtCedula.requestFocus();
						lblMensajeCedula.setText("Numero de cedula no valido");
					}else {
						lblMensajeCedula.setVisible(true);
						limpiar();
						recuperarDatos(txtCedula.getText());
						lblMensajeCedula.setText("Numero de cedula  valido");
					}
					//txtNombres.requestFocus();
				}
			}
		});
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		    	if (validarCedula(txtCedula.getText()) == false){
					lblMensajeCedula.setVisible(true);
					//helper.mostrarAlertaError("El número de cedula es incorrecto!", Context.getInstance().getStage());
					lblMensajeCedula.setText("Numero de cedula no valido");
					limpiar();
					txtCedula.requestFocus();
				}else {
					lblMensajeCedula.setVisible(true);
					//limpiar();
					recuperarDatos(txtCedula.getText());
					lblMensajeCedula.setText("Numero de cedula  valido");
				}
		    }
		});
		//txtCodigo.setVisible(false);
		lbCodigo.setVisible(false);
		chkEstado.setSelected(true);
		txtCodigo.setEditable(false);
		txtCodigo.setVisible(false);
		llenarCombos();
	}
	private void llenarCombos() {
		try {
			cboRol.getItems().clear();
			ObservableList<Perfil> datos = FXCollections.observableArrayList();
			List<Perfil> lista = perfilDAO.getAllListaPerfil();
			datos.addAll(lista);
			cboRol.setItems(datos);
			cboRol.setPromptText(" --- Seleccione Rol del usuario --- ");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void recuperarDatos(String cedula){
		try{
			List<Usuario> listaUsuario = new ArrayList<Usuario>();
			listaUsuario = usuarioDAO.getRecuperaUsuario(cedula);
			for(int i = 0 ; i < listaUsuario.size() ; i ++) {
				usuarioSeleccionado = listaUsuario.get(i);
				txtCodigo.setText(Integer.toString(listaUsuario.get(i).getIdUsuario()));
				txtCedula.setText(listaUsuario.get(i).getCedula());
				txtNombres.setText(listaUsuario.get(i).getNombre());
				txtApellidos.setText(listaUsuario.get(i).getApellido());
				//cboPerfil.setValue(listaUsuario.get(i).getSegPerfil());
				if(listaUsuario.get(i).getPerfil() != null) {
					cboRol.getSelectionModel().select(listaUsuario.get(i).getPerfil());
					
				}else {
					List<Perfil> perfil = perfilDAO.getPerfilById(Constantes.ID_PERFIL_MEDICO);
					if(perfil.size() > 0) {
						cboRol.getSelectionModel().select(perfil.get(0));
					}
					cboRol.setDisable(true);
					idMedicoSeleccionado = 0;
					Context.getInstance().setCodigoTipoCliente(Constantes.ID_MEDICO);
					
				}
				txtTelefono.setText(listaUsuario.get(i).getTelefono());
				txtDireccion.setText(listaUsuario.get(i).getDireccion());
				txtUsuario.setText(Encriptado.Desencriptar(listaUsuario.get(i).getUsuario()));
				txtClave.setText(Encriptado.Desencriptar(listaUsuario.get(i).getClave()));
				if(listaUsuario.get(i).getFoto() != null) {
					String imgString = new String(listaUsuario.get(i).getFoto(), "UTF-8");
					ivFoto.setImage(helper.getImageFromBase64String(imgString).getImage());
				}
				else {
					Image img = new Image("/usuario.jpg");
					ivFoto.setImage(img);
				}

				if(listaUsuario.get(i).getEstado().equals("A"))
					chkEstado.setSelected(true);
				else
					chkEstado.setSelected(false);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void grabar(){
		try {
			String estado;
			if(validarDatos() == false)
				return;
			if(chkEstado.isSelected() == true)
				estado = "A";
			else
				estado = "I";

			if(usuarioSeleccionado == null)
				usuarioSeleccionado = new Usuario();
			usuarioSeleccionado.setEstado(estado);
			//usuario.setSegPerfil(cboPerfil.getSelectionModel().getSelectedItem());
			usuarioSeleccionado.setCedula(txtCedula.getText());
			usuarioSeleccionado.setNombre(txtNombres.getText());
			usuarioSeleccionado.setApellido(txtApellidos.getText());
			usuarioSeleccionado.setDireccion(txtDireccion.getText());
			usuarioSeleccionado.setTelefono(txtTelefono.getText());
			if(Context.getInstance().getCodigoTipoCliente() == Constantes.ID_NORMAL)
				usuarioSeleccionado.setPerfil(cboRol.getSelectionModel().getSelectedItem());
			else {
				List<Perfil> perfil = perfilDAO.getPerfilById(Constantes.ID_PERFIL_MEDICO);
				if(perfil.size() > 0) {
					usuarioSeleccionado.setPerfil(perfil.get(0));
				}
			}
			
			usuarioSeleccionado.setUsuario(Encriptado.Encriptar(txtUsuario.getText()));
			usuarioSeleccionado.setClave(Encriptado.Encriptar(txtClave.getText()));
			usuarioSeleccionado.setFoto(helper.encodeFileToBase64Binary(ivFoto.getImage()).getBytes());
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				usuarioSeleccionado.setEstado(estado);
				usuarioDAO.getEntityManager().getTransaction().begin();
				if(usuarioSeleccionado.getIdUsuario() == null) {//inserta
					usuarioSeleccionado.setIdUsuario(null);
					usuarioDAO.getEntityManager().persist(usuarioSeleccionado);
				}else {//modifica
					usuarioDAO.getEntityManager().merge(usuarioSeleccionado);
				}
				usuarioDAO.getEntityManager().getTransaction().commit();
				System.out.println("Usuario grabado: " + usuarioSeleccionado.getIdUsuario());
				//modificar el medico para agregar el id del usuario
				if(Context.getInstance().getCodigoTipoCliente() == Constantes.ID_MEDICO) {
					if(idMedicoSeleccionado != 0) {
						List<Medico> listMed = medicoDAO.getListaMedicosPorId(idMedicoSeleccionado);
						System.out.println("Medicos " + listMed.size());
						Medico medSel = new Medico();
						if(listMed.size() > 0) {
							medSel = listMed.get(0);
							usuarioDAO.getEntityManager().getTransaction().begin();
							medSel.setIdUsuario(usuarioSeleccionado.getIdUsuario());
							usuarioDAO.getEntityManager().merge(medSel);
							usuarioDAO.getEntityManager().getTransaction().commit();
						}
					}
				}
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				nuevo();
				txtCedula.requestFocus();
				txtUsuario.setText("");
				txtClave.setText("");
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			usuarioDAO.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
		}
	}

	boolean validarDatos() {
		try {
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar cedula del Usuario", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}
			if(helper.validarDeCedula(txtCedula.getText()) == false) {
				helper.mostrarAlertaAdvertencia("Cédula Ingresada Incorrecta", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}
			if(txtNombres.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar el nombre del Usuario", Context.getInstance().getStage());
				txtNombres.requestFocus();
				return false;
			}
			if(txtApellidos.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar el apellidos del Usuario", Context.getInstance().getStage());
				txtApellidos.requestFocus();
				return false;
			}
			if(Context.getInstance().getCodigoTipoCliente() == Constantes.ID_NORMAL) {
				if(cboRol.getSelectionModel().getSelectedItem() == null) {
					helper.mostrarAlertaAdvertencia("Debe seleccionar el rol del usuario", Context.getInstance().getStage());
					return false;
				}
				if(cboRol.getSelectionModel().getSelectedItem().getIdPerfil() == Constantes.ID_PERFIL_MEDICO) {
					helper.mostrarAlertaAdvertencia("No se puede registrar un usuario médico directamente", Context.getInstance().getStage());
					return false;
				}
			}
			if(txtUsuario.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar un Usuario", Context.getInstance().getStage());
				txtUsuario.requestFocus();
				return false;	
			}
			if(txtClave.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar una clave para el usuario", Context.getInstance().getStage());
				txtClave.requestFocus();
				return false;	
			}
			if(validarUsuario() == true) {
				helper.mostrarAlertaAdvertencia("El usuario ya existe!!", Context.getInstance().getStage());
				txtUsuario.requestFocus();
				return false;	
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	boolean validarUsuario() {
		try {
			List<Usuario> listaUsuario;
			listaUsuario = usuarioDAO.getValidarUsuario(Encriptado.Encriptar(txtUsuario.getText()), Integer.parseInt(txtCodigo.getText()));
			if(listaUsuario.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public void nuevo() throws IOException{
		limpiar();
		txtCedula.setText("");
		txtCedula.requestFocus();
		txtUsuario.setText("");
		txtClave.setText("");
		cboRol.setDisable(false);
		idMedicoSeleccionado = 0;
		lblMensajeCedula.setVisible(false);
	}

	void limpiar() {
		usuarioSeleccionado = null;
		txtCodigo.setText("0");
		txtCodigo.setEditable(false);
		txtNombres.setText("");
		txtApellidos.setText("");
		chkEstado.setSelected(true);
		cboRol.getSelectionModel().select(-1);
		txtTelefono.setText("");
		txtDireccion.setText("");
		Image img = new Image("/usuario.jpg");
		ivFoto.setImage(img);
		
	}

	public void buscar() {
		try{
			System.out.println("entra buscar");
			Context.getInstance().setCodigoTipoCliente(Constantes.ID_NORMAL);
			Context.getInstance().setMedicoSeleccionado(null);
			//nuevo();
			helper.abrirPantallaModal("/seguridad/SUsuariosListado.fxml","Listado de Usuarios", Context.getInstance().getStage());
			if (Context.getInstance().getUsuarioSeleccionado() != null) {
				//preguntar si el usuario es medico o usuario normal
				if(Context.getInstance().getCodigoTipoCliente() == Constantes.ID_MEDICO) {
					Usuario datoSeleccionado = Context.getInstance().getUsuarioSeleccionado();
					if(datoSeleccionado.getIdUsuario() != null) {
						System.out.println("Medico tiene usuario");
						idMedicoSeleccionado = 0;
						llenarDatos(datoSeleccionado);
						Context.getInstance().setUsuarioSeleccionado(null);
					}else {
						System.out.println("Medico no tiene usuario");
						idMedicoSeleccionado = Integer.parseInt(datoSeleccionado.getTelefono());
						datoSeleccionado.setIdUsuario(null);
						datoSeleccionado.setTelefono("");
						llenarDatos(datoSeleccionado);
						Context.getInstance().setUsuarioSeleccionado(null);
					}
					//bloquear algunos controladores
					cboRol.setDisable(true);
					
				}else {
					Usuario datoSeleccionado = Context.getInstance().getUsuarioSeleccionado();
					llenarDatos(datoSeleccionado);
					Context.getInstance().setUsuarioSeleccionado(null);
					cboRol.setDisable(false);
				}
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	void llenarDatos(Usuario datoSeleccionado){
		try {
			usuarioSeleccionado = datoSeleccionado;

			txtCodigo.setText(String.valueOf(datoSeleccionado.getIdUsuario()));
			if(datoSeleccionado.getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCedula());

			if(datoSeleccionado.getNombre() == null)
				txtNombres.setText("");
			else
				txtNombres.setText(datoSeleccionado.getNombre());

			if(datoSeleccionado.getApellido() == null)
				txtApellidos.setText("");
			else
				txtApellidos.setText(datoSeleccionado.getApellido());

			if(datoSeleccionado.getEstado().equals("A")) 
				chkEstado.setSelected(true);
			else
				chkEstado.setSelected(false);

			//cboPerfil.getSelectionModel().select(datoSeleccionado.getSegPerfil());

			if(datoSeleccionado.getPerfil() == null)
				cboRol.getSelectionModel().select(-1);
			else
				cboRol.getSelectionModel().select(datoSeleccionado.getPerfil());

			if(datoSeleccionado.getTelefono() == null)
				txtTelefono.setText("");
			else
				txtTelefono.setText(datoSeleccionado.getTelefono());

			if(datoSeleccionado.getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getDireccion());

			txtUsuario.setText(Encriptado.Desencriptar(datoSeleccionado.getUsuario()));
			txtClave.setText(Encriptado.Desencriptar(datoSeleccionado.getClave()));
			if(datoSeleccionado.getFoto() != null) {
				String imgString = new String(datoSeleccionado.getFoto(), "UTF-8");
				ivFoto.setImage(helper.getImageFromBase64String(imgString).getImage());
			}
			else {
				Image img = new Image("/usuario.jpg");
				ivFoto.setImage(img);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void quitar() {
		Image img = new Image("/usuario.jpg");
		ivFoto.setImage(img);
	}
	public void examinar(){
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Buscar Imagen");
			// Agregar filtros para facilitar la busqueda
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Imagen jpg", "*.jpg"),
					new FileChooser.ExtensionFilter("Imagen png", "*.png")
					);
			// Obtener la imagen seleccionada
			File imgFile = fileChooser.showOpenDialog(Context.getInstance().getStage());
			// Mostar la imagen
			if (imgFile != null) {
				Image image = new Image("file:" + imgFile.getAbsolutePath());
				ivFoto.setImage(image);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	boolean validarCedula(String cedula) {
		int total = 0;  
		int tamanoLongitudCedula = 10;  
		int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};  
		int numeroProviancias = 24;  
		int tercerdigito = 6;  
		if (cedula.matches("[0-9]*") && cedula.length() == tamanoLongitudCedula) {  
			int provincia = Integer.parseInt(cedula.charAt(0) + "" + cedula.charAt(1));  
			int digitoTres = Integer.parseInt(cedula.charAt(2) + "");  
			if ((provincia > 0 && provincia <= numeroProviancias) && digitoTres < tercerdigito) {  
				int digitoVerificadorRecibido = Integer.parseInt(cedula.charAt(9) + "");  
				for (int i = 0; i < coeficientes.length; i++) {  
					int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(cedula.charAt(i) + "");  
					total = valor >= 10 ? total + (valor - 9) : total + valor;  
				}  
				int digitoVerificadorObtenido = total >= 10 ? (total % 10) != 0 ? 10 - (total % 10) : (total % 10) : total;  
				if (digitoVerificadorObtenido == digitoVerificadorRecibido) {  
					return true;  
				}  
			}
			return false;
		}
		return false;		  
	}

	boolean ValidarEmail (String correo) {
		Pattern pat = null;
		Matcher mat = null;        
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
		mat = pat.matcher(correo);
		if (mat.find()) {
			return true;
		}else{
			return false;
		}        
	}
}

package com.fismedica.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Cliente;
import com.fismedica.modelo.ClienteDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class RClientesEditarC {
	Tooltip toolTip;
	@FXML private Button btnSalir;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtEmail;
	@FXML private DatePicker dtpFechaNacimiento;
	@FXML private TextField txtNombres;
	@FXML private TextField txtCedula;
	@FXML private Button btnGrabar;
	
	ClienteDAO clienteDAO = new ClienteDAO();
	Cliente clienteSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	private int longitudCedula = 13;
	private int longitudTelefono = 15;
	
	public void initialize() {
		try {
			restricciones();

			toolTip = new Tooltip("Grabar cliente");
			btnGrabar.setStyle("-fx-graphic: url('/save.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);

			toolTip = new Tooltip("Salir");
			btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
			btnSalir.setTooltip(toolTip);
			if(Context.getInstance().getClienteSeleccionado() != null) {
				clienteSeleccionado = Context.getInstance().getClienteSeleccionado();
				Context.getInstance().setClienteSeleccionado(null);
				cargarDatos();
			}else {
				clienteSeleccionado = new Cliente();
				clienteSeleccionado.setIdCliente(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatos() {
		txtApellidos.setText(clienteSeleccionado.getApellido());
		txtDireccion.setText(clienteSeleccionado.getDireccion());
		txtTelefono.setText(clienteSeleccionado.getTelefono());
		txtNombres.setText(clienteSeleccionado.getNombre());
		txtCedula.setText(clienteSeleccionado.getCedula());
		txtEmail.setText(clienteSeleccionado.getEmail());
		dtpFechaNacimiento.setValue(convertToLocalDate(clienteSeleccionado.getFechaNacimiento()));
	}
	
	private void restricciones() {
		//longitud de las cadenas de textos ingresadas
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtCedula.getText().length() > longitudCedula) {
					String s = txtCedula.getText().substring(0, longitudCedula);
					txtCedula.setText(s);
				}
			}
		});
		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtTelefono.getText().length() > longitudTelefono) {
					String s = txtTelefono.getText().substring(0, longitudTelefono);
					txtTelefono.setText(s);
				}
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
		//solo letras mayusculas
		txtApellidos.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtApellidos.getText().toUpperCase();
				txtApellidos.setText(cadena);
			}
		});
		txtDireccion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtDireccion.getText().toUpperCase();
				txtDireccion.setText(cadena);
			}
		});
		txtNombres.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtNombres.getText().toUpperCase();
				txtNombres.setText(cadena);
			}
		});
	}
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				clienteSeleccionado.setApellido(txtApellidos.getText());
				clienteSeleccionado.setCedula(txtCedula.getText());
				clienteSeleccionado.setDireccion(txtDireccion.getText());
				clienteSeleccionado.setEmail(txtEmail.getText());
				clienteSeleccionado.setEstado("A");
				clienteSeleccionado.setFechaNacimiento(convertToDate(dtpFechaNacimiento.getValue()));
				clienteSeleccionado.setNombre(txtNombres.getText());
				clienteSeleccionado.setTelefono(txtTelefono.getText());
				
				if(clienteSeleccionado.getIdCliente() != null) {//modifica
					clienteSeleccionado.setFechaModifica(new Date());
					clienteSeleccionado.setUsuarioModifica(Context.getInstance().getUsuario().getUsuario());
					clienteDAO.getEntityManager().getTransaction().begin();
					clienteDAO.getEntityManager().merge(clienteSeleccionado);
					clienteDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					clienteSeleccionado.setFechaCreacion(new Date());
					clienteSeleccionado.setUsuarioCrea(Context.getInstance().getUsuario().getUsuario());
					clienteDAO.getEntityManager().getTransaction().begin();
					clienteDAO.getEntityManager().persist(clienteSeleccionado);
					clienteDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
		}
	}
	private boolean validarDatos() {
		try {
			boolean bandera = false;
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar cédula de identidad", Context.getInstance().getStage());
				return false;
			}
			if(clienteSeleccionado.getIdCliente() == null) {
				if(verificarCedula() == false) {
					helper.mostrarAlertaAdvertencia("Cédula ya existe en el registro", Context.getInstance().getStage());
					return false;
				}
			}
			if(txtNombres.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar nombres", Context.getInstance().getStage());
				return false;
			}
			if(txtApellidos.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar apellidos", Context.getInstance().getStage());
				return false;
			}			
			if(!txtEmail.getText().equals("")) {
				if(!helper.validarEmail(txtEmail.getText())) {
					helper.mostrarAlertaAdvertencia("Correo electrónico ingresado es incorrecto", Context.getInstance().getStage());
					return false;
				}
			}
			if(dtpFechaNacimiento.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Fecha de nacimiento es incorrecto", Context.getInstance().getStage());
				return false;
			}
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	private boolean verificarCedula() {
		try {
			boolean bandera = false;
			List<Cliente> listaCliente = clienteDAO.getBuscarPorCedula(txtCedula.getText());
			if(listaCliente.size() > 0) {
				return false;
			}
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	public void salir() {
		try {
			Context.getInstance().getStageModal().close();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public LocalDate convertToLocalDate(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	public Date convertToDate(LocalDate dateToConvert) {
	    return java.sql.Date.valueOf(dateToConvert);
	}
}

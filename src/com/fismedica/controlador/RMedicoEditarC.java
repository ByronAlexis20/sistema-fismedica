package com.fismedica.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class RMedicoEditarC {
	Tooltip toolTip;
	@FXML private Button btnSalir;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtTitulo;
	@FXML private TextField txtEspecialidad;
	@FXML private DatePicker dtpFechaNacimiento;
	@FXML private TextField txtNombres;
	@FXML private TextField txtCedula;
	@FXML private Button btnGrabar;
	@FXML private Label lblMensajeCedula;
	
	MedicoDAO medicoDAO = new MedicoDAO();
	Medico seleccionado;
	ControllerHelper helper = new ControllerHelper();
	private int longitudCedula = 13;
	
	public void initialize() {
		try {
			lblMensajeCedula.setStyle("-fx-font-size: 14px;\r\n" + 
					"    -fx-font-weight: bold;\r\n" + 
					"    -fx-text-fill: #ff0000;");
			restricciones();

			toolTip = new Tooltip("Grabar médico");
			btnGrabar.setStyle("-fx-graphic: url('/save.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);

			toolTip = new Tooltip("Salir");
			btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
			btnSalir.setTooltip(toolTip);
			if(Context.getInstance().getMedicoSeleccionado() != null) {
				seleccionado = Context.getInstance().getMedicoSeleccionado();
				Context.getInstance().setMedicoSeleccionado(null);
				cargarDatos();
			}else {
				seleccionado = new Medico();
				seleccionado.setIdMedico(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatos() {
		txtApellidos.setText(seleccionado.getApellido());
		txtTitulo.setText(seleccionado.getTitulo());
		txtNombres.setText(seleccionado.getNombre());
		txtCedula.setText(seleccionado.getCedula());
		txtEspecialidad.setText(seleccionado.getEspecialidad());
		dtpFechaNacimiento.setValue(convertToLocalDate(seleccionado.getFechaNacimiento()));
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
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		    	if (validarCedula(txtCedula.getText()) == false){
					lblMensajeCedula.setVisible(true);
					//helper.mostrarAlertaError("El número de cedula es incorrecto!", Context.getInstance().getStage());
					lblMensajeCedula.setText("Numero de cedula no valido");
					txtCedula.requestFocus();
					btnGrabar.setVisible(false);
				}else {
					lblMensajeCedula.setVisible(true);
					lblMensajeCedula.setText("Numero de cedula valido");
					btnGrabar.setVisible(true);
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
		txtTitulo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtTitulo.getText().toUpperCase();
				txtTitulo.setText(cadena);
			}
		});
		txtEspecialidad.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtEspecialidad.getText().toUpperCase();
				txtEspecialidad.setText(cadena);
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
				seleccionado.setApellido(txtApellidos.getText());
				seleccionado.setCedula(txtCedula.getText());
				seleccionado.setEspecialidad(txtEspecialidad.getText());
				seleccionado.setTitulo(txtTitulo.getText());
				seleccionado.setEstado("A");
				seleccionado.setFechaNacimiento(convertToDate(dtpFechaNacimiento.getValue()));
				seleccionado.setNombre(txtNombres.getText());
				
				if(seleccionado.getIdMedico() != null) {//modifica
					medicoDAO.getEntityManager().getTransaction().begin();
					medicoDAO.getEntityManager().merge(seleccionado);
					medicoDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					medicoDAO.getEntityManager().getTransaction().begin();
					medicoDAO.getEntityManager().persist(seleccionado);
					medicoDAO.getEntityManager().getTransaction().commit();
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
			if(seleccionado.getIdMedico() == null) {
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
			List<Medico> listaMedico = medicoDAO.getBuscarPorCedula(txtCedula.getText());
			if(listaMedico.size() > 0) {
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

}

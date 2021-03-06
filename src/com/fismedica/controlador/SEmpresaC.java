package com.fismedica.controlador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Empresa;
import com.fismedica.modelo.EmpresaDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class SEmpresaC {
	@FXML TextField txtCodigo;

	@FXML TextField txtRuc;
	@FXML TextField txtRazonSocial;
	@FXML TextField txtRepresentante;
	@FXML TextField txtTelefono;
	@FXML TextField txtEmail;
	@FXML TextField txtDireccion;
	@FXML ImageView ivLogo;	

	@FXML Button btnExaminar;
	@FXML Button btnQuitar;
	@FXML Button btnGrabar;

	ControllerHelper helper = new ControllerHelper();
	EmpresaDAO empresaDao = new EmpresaDAO();

	public void initialize() {
		btnExaminar.setStyle("-fx-cursor: hand;");
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnQuitar.setStyle("-fx-cursor: hand;");

		txtCodigo.setText("0");
		txtCodigo.setEditable(false);
		txtCodigo.setVisible(false);
		int maxLength = 13;
		int maxLengthTelf = 10;
		recuperarDatos();

		//validar solo numeros
		txtRuc.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtRuc.setText(oldValue);
				}
			}
		});
		//validar solo numeros
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
		txtRazonSocial.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtRazonSocial.getText().toUpperCase();
				txtRazonSocial.setText(cadena);
			}
		});

		//validar solo 13 valores
		txtRuc.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtRuc.getText().length() > maxLength) {
					String s = txtRuc.getText().substring(0, maxLength);
					txtRuc.setText(s);
				}
			}
		});

		//validar solo 10 valores
		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtTelefono.getText().length() > maxLengthTelf) {
					String s = txtTelefono.getText().substring(0, maxLengthTelf);
					txtTelefono.setText(s);
				}
			}
		});

		//solo letras mayusculas
		txtRepresentante.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtRepresentante.getText().toUpperCase();
				txtRepresentante.setText(cadena);
			}
		});

		//validar solo letras.... igual se va con puntuaciones
		txtRepresentante.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\D*")) {
					txtRepresentante.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}
		});

		//solo letras mayusculas
		txtDireccion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDireccion.getText().toUpperCase();
				txtDireccion.setText(cadena);
			}
		});
	}

	public void recuperarDatos(){
		try{
			List<Empresa> listaEmpresa = new ArrayList<Empresa>();
			listaEmpresa = empresaDao.getRecuperaDatosEmpresa();
			limpiar();
			for(int i = 0 ; i < listaEmpresa.size() ; i ++) {
				txtCodigo.setText(Integer.toString(listaEmpresa.get(i).getIdEmpresa()));
				txtRuc.setText(listaEmpresa.get(i).getRuc());
				txtRazonSocial.setText(listaEmpresa.get(i).getRazonSocial());
				txtRepresentante.setText(listaEmpresa.get(i).getRepresentante());
				txtTelefono.setText(listaEmpresa.get(i).getTelefono());
				txtEmail.setText(listaEmpresa.get(i).getEmail());
				txtDireccion.setText(listaEmpresa.get(i).getDireccion());

				if(listaEmpresa.get(i).getLogo() != null) {
					String imgString = new String(listaEmpresa.get(i).getLogo(), "UTF-8");
					ivLogo.setImage(helper.getImageFromBase64String(imgString).getImage());
				}
				else {
					Image img = new Image("/icono_logo.jpg");
					ivLogo.setImage(img);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void examinar() {
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
				ivLogo.setImage(image);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void quitar() {
		Image img = new Image("/icono_logo.jpg");
		ivLogo.setImage(img);
	}

	public void grabar() {
		try {
			if(validarDatos() == false)
				return;

			Empresa empresa = new Empresa();
			empresa.setRuc(txtRuc.getText());
			empresa.setRazonSocial(txtRazonSocial.getText());
			empresa.setRepresentante(txtRepresentante.getText());
			empresa.setTelefono(txtTelefono.getText());
			empresa.setEmail(txtEmail.getText());
			empresa.setDireccion(txtDireccion.getText());
			empresa.setEstado("A");
			empresa.setLogo(helper.encodeFileToBase64Binary(ivLogo.getImage()).getBytes());

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				empresaDao.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					empresa.setIdEmpresa(null);
					empresaDao.getEntityManager().persist(empresa);
				}else {//modifica
					empresa.setIdEmpresa(Integer.parseInt(txtCodigo.getText()));
					empresaDao.getEntityManager().merge(empresa);
				}
				empresaDao.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				recuperarDatos();
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			empresaDao.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
		}
	}

	void limpiar() {
		txtCodigo.setText("0");
		txtCodigo.setEditable(false);
		txtRuc.setText("");
		txtRazonSocial.setText("");
		txtRepresentante.setText("");
		txtTelefono.setText("");
		txtEmail.setText("");
		txtDireccion.setText("");
		Image img = new Image("/icono_logo.jpg");
		ivLogo.setImage(img);
	}

	boolean validarDatos() {
		try {
			if(txtRuc.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar RUC de la empresa", Context.getInstance().getStage());
				txtRuc.requestFocus();
				return false;
			}

			if(txtRazonSocial.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Raz�n Social", Context.getInstance().getStage());
				txtRazonSocial.requestFocus();
				return false;
			}

			if(txtRepresentante.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Representante Legal de la Empresa", Context.getInstance().getStage());
				txtRepresentante.requestFocus();
				return false;	
			}

			if(txtTelefono.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar tel�fono de contacto", Context.getInstance().getStage());
				txtTelefono.requestFocus();
				return false;	
			}

			if(txtEmail.getText() != null) {
				if(!txtEmail.getText().toString().equals("")) {
					if(helper.validarEmail(txtEmail.getText()) == false) {
						helper.mostrarAlertaAdvertencia("Correo electr�nico no valido", Context.getInstance().getStage());
						return false;
					}
				}
			}

			if(txtDireccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar direcci�n de la empresa", Context.getInstance().getStage());
				txtDireccion.requestFocus();
				return false;	
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}	
}

package com.fismedica.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Perfil;
import com.fismedica.modelo.PerfilDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SPerfilesC {
	@FXML TableView<Perfil> tvDatos;
	@FXML TextField txtCodigo;
	@FXML TextField txtNombre;
	@FXML TextField txtDescripcion;
	@FXML CheckBox chkEstado;
	@FXML Button btnAceptar;
	@FXML Button btnNuevo;

	ControllerHelper helper = new ControllerHelper();
	PerfilDAO perfilDAO = new PerfilDAO();

	public void initialize(){
		btnAceptar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		txtCodigo.setEditable(false);
		txtCodigo.setVisible(false);
		limpiar();
		llenarDatos();
		//solo letras mayusculas
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDescripcion.getText().toUpperCase();
				txtDescripcion.setText(cadena);
			}
		});

		txtNombre.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtNombre.getText().toUpperCase();
				txtNombre.setText(cadena);
			}
		});

		//validar solo letras.... igual se va con puntuaciones
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\D*")) {
					txtDescripcion.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}
		});

		//validar solo letras.... igual se va con puntuaciones
		txtNombre.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\D*")) {
					txtNombre.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}
		});

		tvDatos.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				recuperarDatos(tvDatos.getSelectionModel().getSelectedItem().getIdPerfil());
			}
		});
	}

	void limpiar(){
		txtCodigo.setText("0");
		txtNombre.setText("");
		txtDescripcion.setText("");
		chkEstado.setSelected(true);
	}

	public void recuperarDatos(int codigo){
		try{
			List<Perfil> listaPerfil = new ArrayList<Perfil>();
			listaPerfil = perfilDAO.getPerfilById(codigo);
			for(int i = 0 ; i < listaPerfil.size() ; i ++) {

				txtCodigo.setText(String.valueOf(listaPerfil.get(i).getIdPerfil()));
				txtNombre.setText(listaPerfil.get(i).getPerfil());
				txtDescripcion.setText(listaPerfil.get(i).getDescripcion());
				if(listaPerfil.get(i).getEstado().equals("A"))
					chkEstado.setSelected(true);
				else
					chkEstado.setSelected(false);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try {
			tvDatos.getColumns().clear();
			List<Perfil> listaPerfiles = new ArrayList<Perfil>();
			listaPerfiles = perfilDAO.getAllListaPerfil();

			ObservableList<Perfil> datos = FXCollections.observableArrayList();
			datos.addAll(listaPerfiles);

			//llenar los datos en la tabla
			TableColumn<Perfil, String> idColum = new TableColumn<>("Codigo");
			idColum.setMinWidth(100);
			idColum.setCellValueFactory(new PropertyValueFactory<Perfil, String>("idPerfil"));
			TableColumn<Perfil, String> descripcionColum = new TableColumn<>("Perfil");
			descripcionColum.setMinWidth(300);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<Perfil, String>("perfil"));
			TableColumn<Perfil, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(100);
			estadoColum.setCellValueFactory(new PropertyValueFactory<Perfil, String>("estado"));
			tvDatos.getColumns().addAll(idColum, descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void nuevo(){
		limpiar();
	}

	boolean validarDatos() {
		try {
			if(txtNombre.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar el nombre del perfil", Context.getInstance().getStage());
				txtNombre.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public void grabar(){
		try{
			if(validarDatos() == false)
				return;
			String estado = "";
			Perfil perfil = new Perfil();
			if(chkEstado.isSelected() == true)
				estado = "A";
			else
				estado = "I";
			perfil.setEstado(estado);
			perfil.setDescripcion(txtDescripcion.getText());
			perfil.setPerfil(txtNombre.getText());
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				perfilDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")){//inserta
					perfil.setIdPerfil(null);
					perfilDAO.getEntityManager().persist(perfil);
				}else {//modifica
					perfil.setIdPerfil(Integer.parseInt(txtCodigo.getText()));
					perfilDAO.getEntityManager().merge(perfil);
				}
				perfilDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados con exito!!!", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex){
			perfilDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
}

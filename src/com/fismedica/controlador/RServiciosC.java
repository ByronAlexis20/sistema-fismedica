package com.fismedica.controlador;

import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Servicio;
import com.fismedica.modelo.ServicioDAO;
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

public class RServiciosC {
	private @FXML TableView<Servicio> tvDatos;
	private @FXML TextField txtCodigo;
	private @FXML TextField txtDescripcion;
	private @FXML CheckBox chkEstado;
	private @FXML Button btnAceptar;
	private @FXML Button btnNuevo;
	ServicioDAO servicioDAO = new ServicioDAO();
	ControllerHelper helper = new ControllerHelper();

	public void initialize() {
		btnNuevo.setStyle("-fx-cursor: hand;");
		btnAceptar.setStyle("-fx-cursor: hand;");
		txtCodigo.setEditable(false);
		txtCodigo.setVisible(false);

		limpiar();
		llenarDatos();
		tvDatos.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if(tvDatos.getSelectionModel().getSelectedItem() != null)
					recuperarDatos(tvDatos.getSelectionModel().getSelectedItem());
			}
		});

		//solo letras mayusculas
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDescripcion.getText().toUpperCase();
				txtDescripcion.setText(cadena);
			}
		});
	}

	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			List<Servicio> lista;
			ObservableList<Servicio> datos = FXCollections.observableArrayList();
			lista = servicioDAO.getListaServiciosTodos();
			datos.setAll(lista);

			//llenar los datos en la tabla
			TableColumn<Servicio, String> idColum = new TableColumn<>("Ítem");
			idColum.setMinWidth(80);
			idColum.setCellValueFactory(new PropertyValueFactory<Servicio, String>("idServicio"));
			TableColumn<Servicio, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(200);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<Servicio, String>("servicio"));
			TableColumn<Servicio, String> duracionColum = new TableColumn<>("Duración");
			duracionColum.setMinWidth(200);
			duracionColum.setCellValueFactory(new PropertyValueFactory<Servicio, String>("duracionMinutos"));
			TableColumn<Servicio, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(83);
			estadoColum.setCellValueFactory(new PropertyValueFactory<Servicio, String>("estado"));

			tvDatos.getColumns().addAll(idColum, descripcionColum,duracionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void recuperarDatos(Servicio datos){
		try{
			txtCodigo.setText(String.valueOf(datos.getIdServicio()));
			txtDescripcion.setText(String.valueOf(datos.getServicio()));
			if(datos.getEstado().equals("A"))
				chkEstado.setSelected(true);
			else
				chkEstado.setSelected(false);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	void limpiar(){
		txtCodigo.setText("0");
		txtDescripcion.setText("");
		chkEstado.setSelected(true);
	}

	public void grabar() {
		try {
			Servicio servicio = new Servicio();
			if(chkEstado.isSelected() == true)
				servicio.setEstado("A");
			else
				servicio.setEstado("I");
			servicio.setServicio(txtDescripcion.getText());
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				servicioDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					servicio.setIdServicio(null);
					servicioDAO.getEntityManager().persist(servicio);
				}else {//modifica
					servicio.setIdServicio(Integer.parseInt(txtCodigo.getText()));
					servicioDAO.getEntityManager().merge(servicio);
				}
				servicioDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex) {
			servicioDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}

	public void nuevo() {
		limpiar();
	}
}

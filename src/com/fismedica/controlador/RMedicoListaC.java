package com.fismedica.controlador;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class RMedicoListaC {
	@FXML private TableView<Medico> tvDatos;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	ControllerHelper helper = new ControllerHelper();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	MedicoDAO medicoDAO = new MedicoDAO();
	
	public void initialize() {	
		try {
			btnEditar.setStyle("-fx-cursor: hand;");
			btnEliminar.setStyle("-fx-cursor: hand;");
			
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnNuevo.setStyle("-fx-graphic: url('/new.png');-fx-cursor: hand;");
			llenarDatos("");
			//solo letras mayusculas
			txtBuscar.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtBuscar.getText().toUpperCase();
					txtBuscar.setText(cadena);
				}
			});
			//evento enter
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarMedico();
					} 
				} 
			}); 
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	void llenarDatos(String busqueda){
		try{
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Medico> listaMedicos;
			ObservableList<Medico> datos = FXCollections.observableArrayList();
			listaMedicos = medicoDAO.getListaMedicos(busqueda);
			datos.setAll(listaMedicos);

			//llenar los datos en la tabla
			TableColumn<Medico, String> idColum = new TableColumn<>("Cédula");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCedula().toString());
				}
			});
			TableColumn<Medico, String> nombresColum = new TableColumn<>("Nombres y Apellidos");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(260);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre() + " " + param.getValue().getApellido());
				}
			});

			TableColumn<Medico, String> tituloColum = new TableColumn<>("Título");
			tituloColum.setMinWidth(10);
			tituloColum.setPrefWidth(140);
			tituloColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTitulo());
				}
			});

			TableColumn<Medico, String> especialidadColum = new TableColumn<>("Especialidad");
			especialidadColum.setMinWidth(10);
			especialidadColum.setPrefWidth(200);
			especialidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEspecialidad());
				}
			});
			TableColumn<Medico, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					String estado = "";
					if(param.getValue().getEstado().toString().equals("A"))
						estado = "Activo";
					else
						estado = "Inactivo";
					return new SimpleObjectProperty<String>(estado);
				}
			});

			tvDatos.getColumns().addAll(idColum, nombresColum,tituloColum,especialidadColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void buscarMedico() {
		try {
			List<Medico> lista;
			ObservableList<Medico> datos = FXCollections.observableArrayList();
			lista = medicoDAO.getListaMedicos(txtBuscar.getText().toString());
			datos.setAll(lista);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		try {
			Context.getInstance().setClienteSeleccionado(null);
			helper.abrirPantallaModal("/registro/RMedicoEditar.fxml","Datos del Médico", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void editar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setMedicoSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/registro/RMedicoEditar.fxml","Datos del Médico", Context.getInstance().getStage());
				llenarDatos("");
				Context.getInstance().setMedicoSeleccionado(null);
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Médico a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void eliminar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al médico.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Medico seleccionada = new Medico();
					seleccionada = tvDatos.getSelectionModel().getSelectedItem();
					seleccionada.setEstado("I");
					medicoDAO.getEntityManager().getTransaction().begin();
					medicoDAO.getEntityManager().merge(seleccionada);
					medicoDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Médico Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Médico a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

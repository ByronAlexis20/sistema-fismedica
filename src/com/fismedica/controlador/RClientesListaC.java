package com.fismedica.controlador;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Cliente;
import com.fismedica.modelo.ClienteDAO;
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

public class RClientesListaC {
	@FXML private TableView<Cliente> tvDatos;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	ControllerHelper helper = new ControllerHelper();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	ClienteDAO clienteDAO = new ClienteDAO();
	
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
						buscarCliente();
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
			List<Cliente> listaClientes;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			listaClientes = clienteDAO.getListaClientes(busqueda);
			datos.setAll(listaClientes);

			//llenar los datos en la tabla
			TableColumn<Cliente, String> idColum = new TableColumn<>("Cédula");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCedula().toString());
				}
			});
			TableColumn<Cliente, String> nombresColum = new TableColumn<>("Nombres y Apellidos");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(260);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre() + " " + param.getValue().getApellido());
				}
			});

			TableColumn<Cliente, String> fechaColum = new TableColumn<>("Telefono");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(110);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTelefono());
				}
			});

			TableColumn<Cliente, String> medidorColum = new TableColumn<>("Email");
			medidorColum.setMinWidth(10);
			medidorColum.setPrefWidth(200);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEmail());
				}
			});
			TableColumn<Cliente, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					String estado = "";
					if(param.getValue().getEstado().toString().equals("A"))
						estado = "Activo";
					else
						estado = "Inactivo";
					return new SimpleObjectProperty<String>(estado);
				}
			});

			tvDatos.getColumns().addAll(idColum, nombresColum,fechaColum,medidorColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void buscarCliente() {
		try {
			List<Cliente> listaClientes;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			listaClientes = clienteDAO.getListaClientes(txtBuscar.getText().toString());
			datos.setAll(listaClientes);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevoCliente() {
		try {
			Context.getInstance().setClienteSeleccionado(null);
			helper.abrirPantallaModal("/registro/RClientesEditar.fxml","Datos del Cliente", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void editarCliente() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setClienteSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/registro/RClientesEditar.fxml","Datos del Cliente", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void eliminarCliente() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al cliente.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Cliente cuentaSeleccionada = new Cliente();
					cuentaSeleccionada = tvDatos.getSelectionModel().getSelectedItem();
					cuentaSeleccionada.setEstado("I");
					clienteDAO.getEntityManager().getTransaction().begin();
					clienteDAO.getEntityManager().merge(cuentaSeleccionada);
					clienteDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Cliente Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

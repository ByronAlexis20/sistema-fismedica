package com.fismedica.controlador;

import java.util.List;

import com.fismedica.modelo.Cliente;
import com.fismedica.modelo.ClienteDAO;
import com.fismedica.util.Context;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class TClientesListaC {
	Tooltip toolTip;
	@FXML private TableView<Cliente> tvDatos;
	@FXML private TextField txtBuscar;
	ClienteDAO empleadoDAO = new ClienteDAO();
	
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarCliente();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<Cliente> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setClienteSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
	    					Context.getInstance().getStageModal().close();
	    				}
	                }
	            });
	            return row ;
	        });
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarCliente() {
		try {
			List<Cliente> lista;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			lista = empleadoDAO.getListaClientes(txtBuscar.getText().toString());
			datos.setAll(lista);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatos(String busqueda) {
		try {
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Cliente> lista;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			lista = empleadoDAO.getListaClientes(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Cliente, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCedula());
				}
			});
			
			TableColumn<Cliente, String> nombresColum = new TableColumn<>("Apellidos y nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					String nombres = param.getValue().getNombre() + " " + param.getValue().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			
			TableColumn<Cliente, String> telefonoColum = new TableColumn<>("Telefono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTelefono());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,telefonoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

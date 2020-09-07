package com.fismedica.controlador;

import java.util.List;

import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.util.Context;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class TMedicoListaC {
	Tooltip toolTip;
	@FXML private TableView<Medico> tvDatos;
	@FXML private TextField txtBuscar;
	MedicoDAO medicoDAO = new MedicoDAO();
	
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarMedico();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<Medico> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setMedicoSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
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
	@SuppressWarnings("unchecked")
	private void llenarDatos(String busqueda) {
		try {
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Medico> lista;
			ObservableList<Medico> datos = FXCollections.observableArrayList();
			lista = medicoDAO.getListaMedicos(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Medico, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCedula());
				}
			});
			
			TableColumn<Medico, String> nombresColum = new TableColumn<>("Apellidos y nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					String nombres = param.getValue().getNombre() + " " + param.getValue().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			
			TableColumn<Medico, String> telefonoColum = new TableColumn<>("Profesión");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medico,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medico, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTitulo());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,telefonoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

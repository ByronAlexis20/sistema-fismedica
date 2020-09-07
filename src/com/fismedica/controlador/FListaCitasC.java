package com.fismedica.controlador;

import java.text.SimpleDateFormat;
import java.util.List;

import com.fismedica.modelo.CitaCabecera;
import com.fismedica.modelo.CitaCabeceraDAO;
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

public class FListaCitasC {
	Tooltip toolTip;
	@FXML private TableView<CitaCabecera> tvDatos;
	@FXML private TextField txtBuscar;
	CitaCabeceraDAO citaDAO = new CitaCabeceraDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscar();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<CitaCabecera> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setCita(tvDatos.getSelectionModel().getSelectedItem());
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
	public void buscar() {
		try {
			List<CitaCabecera> lista;
			ObservableList<CitaCabecera> datos = FXCollections.observableArrayList();
			lista = citaDAO.getListaCitas(txtBuscar.getText());
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
			List<CitaCabecera> lista;
			ObservableList<CitaCabecera> datos = FXCollections.observableArrayList();
			lista = citaDAO.getListaCitas(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<CitaCabecera, Integer> cedulaColum = new TableColumn<>("No. Cita");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,Integer>, ObservableValue<Integer>>() {
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<CitaCabecera, Integer> param) {
					return new SimpleObjectProperty<Integer>(param.getValue().getIdCita());
				}
			});
			
			TableColumn<CitaCabecera, String> nombresColum = new TableColumn<>("Cliente");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					String nombres = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			
			TableColumn<CitaCabecera, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFechaCita()));
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,fechaColum);
			tvDatos.setItems(datos);
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

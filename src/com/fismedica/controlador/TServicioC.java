package com.fismedica.controlador;

import java.util.List;

import com.fismedica.modelo.Precio;
import com.fismedica.modelo.PrecioDAO;
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

public class TServicioC {
	Tooltip toolTip;
	@FXML private TableView<Precio> tvDatos;
	@FXML private TextField txtBuscar;
	PrecioDAO precioDAO = new PrecioDAO();
	
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarServicio();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<Precio> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setPrecio(tvDatos.getSelectionModel().getSelectedItem());
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
	public void buscarServicio() {
		try {
			List<Precio> lista;
			ObservableList<Precio> datos = FXCollections.observableArrayList();
			lista = precioDAO.getListaServicios(txtBuscar.getText().toString());
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
			List<Precio> lista;
			ObservableList<Precio> datos = FXCollections.observableArrayList();
			lista = precioDAO.getListaServicios(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Precio, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(90);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdPrecio()));
				}
			});
			
			TableColumn<Precio, String> servicioColum = new TableColumn<>("Servicio");
			servicioColum.setMinWidth(10);
			servicioColum.setPrefWidth(200);
			servicioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getServicio().getServicio());
				}
			});
			
			TableColumn<Precio, String> categoriaColum = new TableColumn<>("Categoría");
			categoriaColum.setMinWidth(10);
			categoriaColum.setPrefWidth(200);
			categoriaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCategoria().getCategoria());
				}
			});
			
			TableColumn<Precio, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(100);
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
				}
			});
			tvDatos.getColumns().addAll(codigoColum, servicioColum,categoriaColum,precioColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

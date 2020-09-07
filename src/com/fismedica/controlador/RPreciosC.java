package com.fismedica.controlador;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Categoria;
import com.fismedica.modelo.CategoriaDAO;
import com.fismedica.modelo.Precio;
import com.fismedica.modelo.PrecioDAO;
import com.fismedica.modelo.Servicio;
import com.fismedica.modelo.ServicioDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class RPreciosC {
	@FXML private TableView<Precio> tvDatos;
	@FXML private Button btnAceptar;
	@FXML private ComboBox<Servicio> cboServicio;
	@FXML private ComboBox<Categoria> cboCategoria;
	@FXML private TextField txtPrecio;
	@FXML private TextField txtMinutos;

	PrecioDAO precioDAO = new PrecioDAO();
	ServicioDAO servicioDAO = new ServicioDAO();
	CategoriaDAO categoriaDAO = new CategoriaDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			llenarDatos();
			llenarCombos();
			cboServicio.setStyle("-fx-cursor: hand;");
			cboCategoria.setStyle("-fx-cursor: hand;");
			txtPrecio.setText("0.00");
			txtMinutos.setText("0");
			btnAceptar.setStyle("-fx-graphic: url('/save.png');-fx-cursor: hand;");
			txtPrecio.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*(\\.\\d*)?")) {
						txtPrecio.setText(oldValue);
					}
				}
			});
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void llenarCombos() {
		try {
			cboServicio.getItems().clear();
			ObservableList<Servicio> datos = FXCollections.observableArrayList();
			List<Servicio> lista = servicioDAO.getListaServiciosActivos();
			datos.addAll(lista);
			cboServicio.setItems(datos);
			cboServicio.setPromptText(" -- Seleccione servicio --");
			
			cboCategoria.getItems().clear();
			ObservableList<Categoria> datosC = FXCollections.observableArrayList();
			List<Categoria> listaC = categoriaDAO.getListaCategoriaActivos();
			datosC.addAll(listaC);
			cboCategoria.setItems(datosC);
			cboCategoria.setPromptText(" -- Seleccione categoria -- ");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Precio> lista;
			ObservableList<Precio> datos = FXCollections.observableArrayList();
			lista = precioDAO.getListaPrecios();
			datos.setAll(lista);

			//llenar los datos en la tabla
			TableColumn<Precio, String> servicioColum = new TableColumn<>("Servicio");
			servicioColum.setMinWidth(10);
			servicioColum.setPrefWidth(150);
			servicioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getServicio().getServicio());
				}
			});
			TableColumn<Precio, String> categoriaColum = new TableColumn<>("Categoría");
			categoriaColum.setMinWidth(10);
			categoriaColum.setPrefWidth(150);
			categoriaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCategoria().getCategoria());
				}
			});

			TableColumn<Precio, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
				}
			});
			
			TableColumn<Precio, String> minutosColum = new TableColumn<>("Minutos");
			minutosColum.setMinWidth(10);
			minutosColum.setPrefWidth(90);
			minutosColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Precio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Precio, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTiempoMinutos()));
				}
			});


			tvDatos.getColumns().addAll(servicioColum, categoriaColum,precioColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			if(cboCategoria.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar una categoría", Context.getInstance().getStage());
				return;
			}
			if(cboServicio.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un servicio", Context.getInstance().getStage());
				return;
			}
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Precio precioGrabar = new Precio();
				List<Precio> lista = precioDAO.getListaPreciosBusqueda(cboServicio.getSelectionModel().getSelectedItem().getIdServicio(), cboCategoria.getSelectionModel().getSelectedItem().getIdCategoria());
				if(lista.size() > 0)
					precioGrabar.setIdPrecio(lista.get(0).getIdPrecio());
				else
					precioGrabar.setIdPrecio(null);
				precioGrabar.setEstado("A");
				precioGrabar.setCategoria(cboCategoria.getSelectionModel().getSelectedItem());
				precioGrabar.setServicio(cboServicio.getSelectionModel().getSelectedItem());
				precioGrabar.setPrecio(BigDecimal.valueOf(Double.valueOf(txtPrecio.getText())));
				precioGrabar.setTiempoMinutos(Integer.parseInt(txtMinutos.getText()));
				precioDAO.getEntityManager().getTransaction().begin();
				if(precioGrabar.getIdPrecio() != null)
					precioDAO.getEntityManager().merge(precioGrabar);
				else
					precioDAO.getEntityManager().persist(precioGrabar);
				precioDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
				llenarDatos();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

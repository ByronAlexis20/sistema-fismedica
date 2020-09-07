package com.fismedica.controlador;

import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Categoria;
import com.fismedica.modelo.CategoriaDAO;
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

public class RCategoriaC {
	private @FXML TableView<Categoria> tvDatos;
	private @FXML TextField txtCodigo;
	private @FXML TextField txtDescripcion;
	private @FXML CheckBox chkEstado;
	private @FXML Button btnAceptar;
	private @FXML Button btnNuevo;
	CategoriaDAO categoriaDAO = new CategoriaDAO();
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
			List<Categoria> lista;
			ObservableList<Categoria> datos = FXCollections.observableArrayList();
			lista = categoriaDAO.getListaCategoriaTodos();
			datos.setAll(lista);

			//llenar los datos en la tabla
			TableColumn<Categoria, String> idColum = new TableColumn<>("Ítem");
			idColum.setMinWidth(125);
			idColum.setCellValueFactory(new PropertyValueFactory<Categoria, String>("idCategoria"));
			TableColumn<Categoria, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(400);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<Categoria, String>("categoria"));
			TableColumn<Categoria, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(83);
			estadoColum.setCellValueFactory(new PropertyValueFactory<Categoria, String>("estado"));

			tvDatos.getColumns().addAll(idColum, descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void recuperarDatos(Categoria datos){
		try{
			txtCodigo.setText(String.valueOf(datos.getIdCategoria()));
			txtDescripcion.setText(String.valueOf(datos.getCategoria()));
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
			Categoria categoria = new Categoria();
			if(chkEstado.isSelected() == true)
				categoria.setEstado("A");
			else
				categoria.setEstado("I");
			categoria.setCategoria(txtDescripcion.getText());

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				categoriaDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					categoria.setIdCategoria(null);
					categoriaDAO.getEntityManager().persist(categoria);
				}else {//modifica
					categoria.setIdCategoria(Integer.parseInt(txtCodigo.getText()));
					categoriaDAO.getEntityManager().merge(categoria);
				}
				categoriaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex) {
			categoriaDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}

	public void nuevo() {
		limpiar();
	}
}

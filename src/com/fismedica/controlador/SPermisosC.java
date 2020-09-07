package com.fismedica.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Menu;
import com.fismedica.modelo.MenuDAO;
import com.fismedica.modelo.Perfil;
import com.fismedica.modelo.PerfilDAO;
import com.fismedica.modelo.Permiso;
import com.fismedica.modelo.PermisoDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class SPermisosC {
	@FXML private ComboBox<Perfil> cboPerfil;
	@FXML private Button btnGuardar;
	@FXML private TableView<Menu> tvMenu;
	@FXML private TableView<Permiso> tvPermiso;
	@FXML private Button btnAnadir;
	@FXML private Button btnQuitar;

	ControllerHelper helper = new ControllerHelper();
	PerfilDAO perfilDAO = new PerfilDAO();
	MenuDAO menuDAO = new MenuDAO();
	PermisoDAO permisoDAO = new PermisoDAO();
	Permiso permisoSeleccionado = new Permiso(); 
	List<Permiso> permisosSeleccionados = new ArrayList<Permiso>();

	public void initialize(){
		btnAnadir.setStyle("-fx-cursor: hand;");
		btnGuardar.setStyle("-fx-cursor: hand;");
		btnQuitar.setStyle("-fx-cursor: hand;");
		cboPerfil.setStyle("-fx-cursor: hand;");
		
		llenarComboPerfil();
		llenar_Datos();
	}

	public void grabar(){
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				permisoDAO.getEntityManager().getTransaction().begin();
				ObservableList<Permiso> datos = tvPermiso.getItems();
				//System.out.println(datos.size());
				for(int i = 0 ; i < datos.size() ; i++) {
					if(datos.get(i).getIdPermiso() == null)
						permisoDAO.getEntityManager().persist(datos.get(i));
					else
						permisoDAO.getEntityManager().merge(datos.get(i));	
				}
				permisoDAO.getEntityManager().getTransaction().commit();	
				helper.mostrarAlertaInformacion("Datos grabados Corectamente", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void llenarComboPerfil(){
		try{
			cboPerfil.setPromptText(" --- Seleccionar perfil --- ");
			List<Perfil> listaPerfiles;
			listaPerfiles = perfilDAO.getListaPerfil();
			ObservableList<Perfil> datos = FXCollections.observableArrayList();
			datos.addAll(listaPerfiles);
			cboPerfil.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void cargarAccesos() {
		try {
			cargarPermisosPerfil(cboPerfil.getSelectionModel().getSelectedItem().getIdPerfil());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarPermisosPerfil(int idPerfil) {
		try {
			List<Permiso> resultado = permisoDAO.getPermiso(idPerfil);
			if(resultado.size() > 0) {
				boolean bandera = false;
				List<Menu> listaMenus = menuDAO.getListaMenuAccesos();
				
				ObservableList<Menu> datos = FXCollections.observableArrayList();
				datos.setAll(listaMenus);
				ObservableList<Menu> datosMenu = FXCollections.observableArrayList();

				tvMenu.getColumns().clear();
				tvMenu.getItems().clear();

				//verificar si el menu esta asignado a un perfil
				for(int i = 0 ; i < datos.size() ; i ++) {
					bandera = false;
					for(int j = 0 ; j < resultado.size() ; j ++) {
						if(datos.get(i).getIdMenu().equals(resultado.get(j).getMenu().getIdMenu()))
							bandera = true;
					}
					if(bandera == false)
						datosMenu.add(datos.get(i));
				}

				//llenar los datos en la tabla
				TableColumn<Menu, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(70);
				idColum.setCellValueFactory(new PropertyValueFactory<Menu, String>("idMenu"));

				TableColumn<Menu, String> nombreColum = new TableColumn<>("Nombre del Menu");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(400);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Menu, String> param) {
						String dato = param.getValue().getDescripcion();
						List<Menu> listaMenuAll = new ArrayList<Menu>();
						listaMenuAll = menuDAO.getListaMenu();
						for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
							if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
								dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
						}
						return new SimpleObjectProperty<String>(dato);
					}
				});

				tvMenu.getColumns().addAll(idColum,nombreColum);
				tvMenu.setItems(datosMenu);

			}else {
				tvMenu.getColumns().clear();
				llenar_Datos();
			}

			//recupera los asignados a esa persona
			if(resultado.size() > 0) {
				//llenar los datos en la tabla
				tvPermiso.getColumns().clear();
				tvPermiso.getItems().clear();
				List<Permiso> listaPermisos = resultado;
				ObservableList<Permiso> datos = FXCollections.observableArrayList();
				datos.setAll(listaPermisos);
				TableColumn<Permiso, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(70);
				idColum.setCellValueFactory(new PropertyValueFactory<Permiso, String>("idPermiso"));

				TableColumn<Permiso, String> nombreColum = new TableColumn<>("Nombre del Menu");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(400);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Permiso,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Permiso, String> param) {
						String dato = param.getValue().getMenu().getDescripcion();
						List<Menu> listaMenuAll = new ArrayList<Menu>();
						listaMenuAll = menuDAO.getListaMenu();
						for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
							if(param.getValue().getMenu().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
								dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
						}
						return new SimpleObjectProperty<String>(dato);
					}
				});
				tvPermiso.getColumns().addAll(idColum,nombreColum);
				tvPermiso.setItems(datos);				
			}else {
				tvPermiso.getColumns().clear();
				tvPermiso.getItems().clear();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void anadir() {
		try {
			Menu menuSeleccionado = new Menu(); 
			menuSeleccionado = tvMenu.getSelectionModel().getSelectedItem();
			if(tvMenu.getSelectionModel().getSelectedItem() != null) {
				if (cboPerfil.getSelectionModel().getSelectedItem() != null){
					tvMenu.getItems().remove(menuSeleccionado);
					ObservableList<Permiso> datos = FXCollections.observableArrayList();
					datos.setAll(tvPermiso.getItems());

					tvPermiso.getColumns().clear();
					Permiso nuevo = new Permiso();
					nuevo.setMenu(menuSeleccionado);
					nuevo.setPerfil(cboPerfil.getSelectionModel().getSelectedItem());
					nuevo.setEstado("A");
					datos.add(nuevo);

					TableColumn<Permiso, String> idColum = new TableColumn<>("Código");
					idColum.setMinWidth(10);
					idColum.setPrefWidth(70);
					idColum.setCellValueFactory(new PropertyValueFactory<Permiso, String>("idPermiso"));

					TableColumn<Permiso, String> nombreColum = new TableColumn<>("Nombre del Menu");
					nombreColum.setMinWidth(10);
					nombreColum.setPrefWidth(400);
					nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Permiso,String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(CellDataFeatures<Permiso, String> param) {
							String dato = param.getValue().getMenu().getDescripcion();
							List<Menu> listaMenuAll = new ArrayList<Menu>();
							listaMenuAll = menuDAO.getListaMenu();
							for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
								if(param.getValue().getMenu().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
									dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
							}
							return new SimpleObjectProperty<String>(dato);
						}
					});
					tvPermiso.getColumns().addAll(idColum,nombreColum);
					tvPermiso.setItems(datos);
				}else {
					helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
				}
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un item de Menú", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void agregarTodo() {
		try {
			List<Menu> listaMenus = new ArrayList<Menu>();
			listaMenus = menuDAO.getListaMenuAccesos();
			
			if (cboPerfil.getSelectionModel().getSelectedItem() != null) {
				tvMenu.getItems().removeAll(listaMenus);
				ObservableList<Permiso> datos = FXCollections.observableArrayList();
				datos.setAll(tvPermiso.getItems());
				
				tvPermiso.getColumns().clear();
				
				for (Menu menu : listaMenus) {
					Permiso nuevo = new Permiso();
					nuevo.setMenu(menu);
					nuevo.setPerfil(cboPerfil.getSelectionModel().getSelectedItem());
					nuevo.setEstado("A");
					datos.add(nuevo);
				}
				
				TableColumn<Permiso, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(65);
				idColum.setCellValueFactory(new PropertyValueFactory<Permiso, String>("idPermiso"));
				
				TableColumn<Permiso, String> idMenuColum = new TableColumn<>("IdMenu");
				idMenuColum.setMinWidth(10);
				idMenuColum.setPrefWidth(90);
				idMenuColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Permiso, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Permiso, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getMenu().getIdMenu()));
					}
				});

				TableColumn<Permiso, String> nombreColum = new TableColumn<>("Nombre del Menu");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(400);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Permiso,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Permiso, String> param) {
						String dato = param.getValue().getMenu().getDescripcion();
						List<Menu> listaMenuAll = new ArrayList<Menu>();
						listaMenuAll = menuDAO.getListaMenu();
						for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
							if(param.getValue().getMenu().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
								dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
						}
						return new SimpleObjectProperty<String>(dato);
					}
				});
				tvPermiso.getColumns().addAll(idColum, idMenuColum, nombreColum);
				tvPermiso.setItems(datos);
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
			}			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void quitar(){
		try {
			permisoSeleccionado = tvPermiso.getSelectionModel().getSelectedItem();
			if(tvPermiso.getSelectionModel().getSelectedItem() != null) {
				if (cboPerfil.getSelectionModel().getSelectedItem() != null){
					tvPermiso.getItems().remove(permisoSeleccionado);
					
					permisoSeleccionado.setEstado("I");
					permisosSeleccionados.add(permisoSeleccionado);
					
					ObservableList<Menu> datos = FXCollections.observableArrayList();
					datos.setAll(tvMenu.getItems());
					
					datos.add(permisoSeleccionado.getMenu());
					tvMenu.getColumns().clear();

					TableColumn<Menu, String> idColum = new TableColumn<>("Código");
					idColum.setMinWidth(10);
					idColum.setPrefWidth(72);
					idColum.setCellValueFactory(new PropertyValueFactory<Menu, String>("idMenu"));

					TableColumn<Menu, String> nombreColum = new TableColumn<>("Nombre del Formulario");
					nombreColum.setMinWidth(10);
					nombreColum.setPrefWidth(400);
					nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu,String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(CellDataFeatures<Menu, String> param) {
							String dato = param.getValue().getDescripcion();
							List<Menu> listaMenuAll = new ArrayList<Menu>();
							listaMenuAll = menuDAO.getListaMenu();
							for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
								if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
									dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
							}
							return new SimpleObjectProperty<String>(dato);
						}
					});			
					tvMenu.getColumns().addAll(idColum,nombreColum);
					tvMenu.setItems(datos);
				}else {
					helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
				}
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un item", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void eliminarTodo() {
		try {
			permisosSeleccionados = tvPermiso.getSelectionModel().getSelectedItems();
			if(cboPerfil.getSelectionModel().getSelectedItem() != null) {
				tvPermiso.getItems().removeAll(permisosSeleccionados);
				ObservableList<Menu> datos = FXCollections.observableArrayList();
				datos.setAll(tvMenu.getItems());
				permisoSeleccionado.setEstado("I");
				permisosSeleccionados.add(permisoSeleccionado);
				tvMenu.getColumns().clear();
				for(Permiso permiso : permisosSeleccionados) {
					datos.add(permisoSeleccionado.getMenu());
				}
				
				TableColumn<Menu, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(72);
				idColum.setCellValueFactory(new PropertyValueFactory<Menu, String>("idMenu"));

				TableColumn<Menu, String> nombreColum = new TableColumn<>("Nombre del Formulario");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(280);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Menu, String> param) {
					String dato = param.getValue().getDescripcion();
					List<Menu> listaMenuAll = new ArrayList<Menu>();
					listaMenuAll = menuDAO.getListaMenu();
					for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
						if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
							dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
					}
					return new SimpleObjectProperty<String>(dato);
				}
				});			
				tvMenu.getColumns().addAll(idColum,nombreColum);
				tvMenu.setItems(datos);
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
			}	
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	void llenar_Datos(){
		try{
			tvMenu.getItems().clear();
			tvMenu.getColumns().clear();
			List<Menu> ListaMenu = new ArrayList<Menu>();
			ListaMenu = menuDAO.getListaMenuAccesos();

			ObservableList<Menu> datos = FXCollections.observableArrayList();
			datos.setAll(ListaMenu);

			//llenar los datos en la tabla
			TableColumn<Menu, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(72);
			idColum.setCellValueFactory(new PropertyValueFactory<Menu, String>("idMenu"));

			TableColumn<Menu, String> nombreColum = new TableColumn<>("Nombre del Formulario");
			nombreColum.setMinWidth(10);
			nombreColum.setPrefWidth(400);
			nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Menu, String> param) {
					String dato = param.getValue().getDescripcion();
					List<Menu> listaMenuAll = new ArrayList<Menu>();
					listaMenuAll = menuDAO.getListaMenu();
					for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
						if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
							dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
					}
					return new SimpleObjectProperty<String>(dato);
				}
			});
			tvMenu.getColumns().addAll(idColum,nombreColum);
			tvMenu.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}

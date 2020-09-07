package com.fismedica.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.modelo.Usuario;
import com.fismedica.modelo.UsuarioDAO;
import com.fismedica.util.Constantes;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.Encriptado;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class SUsuariosListadoC {
	private @FXML TableView<Usuario> tvDatos;
	private @FXML TextField txtBuscarUsuario;
    @FXML private ComboBox<TipoUsuario> cboTipoUsuario;
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    MedicoDAO medicoDAO = new MedicoDAO();
    Integer codigoTipoUsuario = 0;
    ControllerHelper helper = new ControllerHelper();
	public void initialize(){
		List<Usuario> lista;
		lista = usuarioDAO.getUsuarios();
		llenarDatos(lista);
		llenarCombos();
		tvDatos.setRowFactory(tv -> {
			TableRow<Usuario> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					if(tvDatos.getSelectionModel().getSelectedItem() != null){
						if(tvDatos.getSelectionModel().getSelectedItem().getIdUsuario() == null) {
							Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("El médico no tiene usuario asignado. Desea agregar un usuario al médico?",Context.getInstance().getStage());
							if(result.get() == ButtonType.CANCEL){
								return;
							}
						}
						Context.getInstance().setUsuarioSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
						Context.getInstance().setCodigoTipoCliente(cboTipoUsuario.getSelectionModel().getSelectedItem().getId());
						Context.getInstance().getStageModal().close();
					}
				}
			});
			return row ;
		});
		cboTipoUsuario.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cboTipoUsuario.getSelectionModel().getSelectedItem() != null) {
					if(cboTipoUsuario.getSelectionModel().getSelectedItem().getId() == Constantes.ID_MEDICO) {
						List<Medico> listaMedico = medicoDAO.getListaMedicosPorUsuario("");
						System.out.println("Cantidad de medicos: " + listaMedico.size());
						List<Usuario> listaUsuarios = new ArrayList<Usuario>();
						for(Medico med : listaMedico) {
							if(med.getIdUsuario() != null) {
								List<Usuario> usu = usuarioDAO.getRecuperaUsuarioPorId(med.getIdUsuario());
								if(usu.size() > 0) {
									listaUsuarios.add(usu.get(0));
								}
							}else {// no es un usuario
								Usuario usu = new Usuario();
								usu.setApellido(med.getApellido());
								usu.setCedula(med.getCedula());
								usu.setEstado("A");
								usu.setTelefono(String.valueOf(med.getIdMedico()));//para llevar el codigo del medico a la siguiente ventana
								usu.setNombre(med.getNombre());
								listaUsuarios.add(usu);
							}
						}
						llenarDatos(listaUsuarios);
					}else {//usuario normal
						List<Usuario> listaUsuarios;
						listaUsuarios = usuarioDAO.getUsuarios();
						llenarDatos(listaUsuarios);
					}
				}
			}
		});
	}
	private void llenarCombos() {
		try {
			cboTipoUsuario.getItems().clear();
			ObservableList<TipoUsuario> datos = FXCollections.observableArrayList();
			List<TipoUsuario> lista = new ArrayList<>();
			TipoUsuario usuarioNormal = new TipoUsuario();
			usuarioNormal.setId(Constantes.ID_NORMAL);
			usuarioNormal.setUsuario("USUARIO NORMAL");
			lista.add(usuarioNormal);
			TipoUsuario usuarioMedico = new TipoUsuario();
			usuarioMedico.setId(Constantes.ID_MEDICO);
			usuarioMedico.setUsuario("USUARIO MÉDICO");
			lista.add(usuarioMedico);
			
			datos.addAll(lista);
			cboTipoUsuario.setItems(datos);
			cboTipoUsuario.getSelectionModel().select(0);
		}catch(Exception ex) {
			
		}
	}
	@SuppressWarnings("unchecked")
	void llenarDatos(List<Usuario> listaUsuarios){
		try{
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			ObservableList<Usuario> datos = FXCollections.observableArrayList();
			datos.setAll(listaUsuarios);

			//llenar los datos en la tabla
			TableColumn<Usuario, String> idColum = new TableColumn<>("Codigo");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(60);
			idColum.setCellValueFactory(new PropertyValueFactory<Usuario, String>("idUsuario"));
			TableColumn<Usuario, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombre"));
			TableColumn<Usuario, String> apellidosColum = new TableColumn<>("Apellidos");
			apellidosColum.setMinWidth(10);
			apellidosColum.setPrefWidth(200);
			apellidosColum.setCellValueFactory(new PropertyValueFactory<Usuario, String>("apellido"));
			TableColumn<Usuario, String> generoColum = new TableColumn<>("Usuario");
			generoColum.setMinWidth(10);
			generoColum.setPrefWidth(90);
			generoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Usuario, String> param) {
					String usuario = "";
					if(param.getValue().getUsuario() != null)
						usuario = Encriptado.Desencriptar(param.getValue().getUsuario());
					return new SimpleObjectProperty<String>(usuario);
				}
			});
			TableColumn<Usuario, String> fechColum = new TableColumn<>("Rol");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(90);
			fechColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Usuario, String> param) {
					String perfil = "";
					if(param.getValue().getPerfil() != null)
						perfil = param.getValue().getPerfil().getPerfil();
					return new SimpleObjectProperty<String>(perfil);
				}
			});
			TableColumn<Usuario, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(50);
			estadoColum.setCellValueFactory(new PropertyValueFactory<Usuario, String>("estado"));

			tvDatos.getColumns().addAll(idColum, nombresColum,apellidosColum,generoColum,fechColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public class TipoUsuario {
		private Integer id;
		private String usuario;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getUsuario() {
			return usuario;
		}
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		@Override
		public String toString() {
			return this.usuario;
		}
		
		
	}
}

package com.fismedica.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fismedica.modelo.CitaCabecera;
import com.fismedica.modelo.CitaCabeceraDAO;
import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.util.Constantes;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class PCitasDiaC {
	@FXML private Label lblFecha;
	@FXML private Button btnFacturarCita;
	@FXML private TableView<CitaCabecera> tvDatos;	
	@FXML private Button btnAtender;

	CitaCabeceraDAO citaDAO = new CitaCabeceraDAO();
	MedicoDAO medicoDAO = new MedicoDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
			btnAtender.setVisible(false);
			btnFacturarCita.setVisible(false);
			btnAtender.setStyle("-fx-cursor: hand;");
			btnFacturarCita.setStyle("-fx-cursor: hand;");
			lblFecha.setText(formateador.format(new Date()));
			if(Context.getInstance().getUsuario().getPerfil().getIdPerfil() == Constantes.ID_PERFIL_MEDICO) {//busca las citas por el medico que inicia sesion
				btnAtender.setVisible(true);
				buscarCitasPorMedicos();
			}else {//busca las citas normales
				btnFacturarCita.setVisible(true);
				buscarCitas();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public void buscarCitas() {
		try {
			//cargar listado de citas
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<CitaCabecera> lista;
			ObservableList<CitaCabecera> datos = FXCollections.observableArrayList();
			lista = citaDAO.getCitasPorDia(new Date());
			datos.setAll(lista);

			//llenar los datos en la tabla
			TableColumn<CitaCabecera, String> noCitaColum = new TableColumn<>("No. Cita");
			noCitaColum.setMinWidth(10);
			noCitaColum.setPrefWidth(90);
			noCitaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumeroCita()));
				}
			});

			TableColumn<CitaCabecera, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(220);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido());
				}
			});

			TableColumn<CitaCabecera, String> medicoColum = new TableColumn<>("Médico");
			medicoColum.setMinWidth(10);
			medicoColum.setPrefWidth(220);
			medicoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMedico().getNombre() + " " + param.getValue().getMedico().getApellido());
				}
			});

			TableColumn<CitaCabecera, String> turnoColum = new TableColumn<>("Turno");
			turnoColum.setMinWidth(10);
			turnoColum.setPrefWidth(80);
			turnoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumeroCita()));
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

			TableColumn<CitaCabecera, String> horarioColum = new TableColumn<>("Horario");
			horarioColum.setMinWidth(10);
			horarioColum.setPrefWidth(120);
			horarioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {

					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getHoraInicio()) + " - " + String.valueOf(param.getValue().getHoraFin()));
				}
			});
			TableColumn<CitaCabecera, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(120);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoCita());
				}
			});
			tvDatos.getColumns().addAll(noCitaColum, clienteColum,medicoColum,turnoColum,fechaColum,horarioColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public void buscarCitasPorMedicos() {
		try {
			List<Medico> listaMedico = medicoDAO.getListaMedicosPorIdUsuario(Context.getInstance().getUsuario().getIdUsuario());
			Medico medico = new Medico();
			if(listaMedico.size() > 0)
				medico = listaMedico.get(0);
			
			//cargar listado de citas
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<CitaCabecera> lista;
			ObservableList<CitaCabecera> datos = FXCollections.observableArrayList();
			lista = citaDAO.getCitasPorDiaMedico(new Date(),medico.getIdMedico());
			datos.setAll(lista);

			//llenar los datos en la tabla
			TableColumn<CitaCabecera, String> noCitaColum = new TableColumn<>("No. Cita");
			noCitaColum.setMinWidth(10);
			noCitaColum.setPrefWidth(90);
			noCitaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumeroCita()));
				}
			});

			TableColumn<CitaCabecera, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(220);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido());
				}
			});

			TableColumn<CitaCabecera, String> medicoColum = new TableColumn<>("Médico");
			medicoColum.setMinWidth(10);
			medicoColum.setPrefWidth(220);
			medicoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMedico().getNombre() + " " + param.getValue().getMedico().getApellido());
				}
			});

			TableColumn<CitaCabecera, String> turnoColum = new TableColumn<>("Turno");
			turnoColum.setMinWidth(10);
			turnoColum.setPrefWidth(80);
			turnoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumeroCita()));
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

			TableColumn<CitaCabecera, String> horarioColum = new TableColumn<>("Horario");
			horarioColum.setMinWidth(10);
			horarioColum.setPrefWidth(120);
			horarioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {

					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getHoraInicio()) + " - " + String.valueOf(param.getValue().getHoraFin()));
				}
			});
			TableColumn<CitaCabecera, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(120);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoCita());
				}
			});
			tvDatos.getColumns().addAll(noCitaColum, clienteColum,medicoColum,turnoColum,fechaColum,horarioColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void atenderCita() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar cita", Context.getInstance().getStage());
    			return;
    		}
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Ingrese observacion a la cita atendida?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Context.getInstance().setCita(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/consultas/CObservacionCita.fxml","Observacion de Cita Atend", Context.getInstance().getStage());
			}
			if(Context.getInstance().getCita() != null) {
				System.out.println("el mensaje es "+ Context.getInstance().getCita().getObservaciones());
				CitaCabecera citaSeleccionada = new CitaCabecera();
				citaSeleccionada = tvDatos.getSelectionModel().getSelectedItem();
				citaSeleccionada.setEstadoCita(Constantes.ESTADO_CITA_ATENDIDO);
				citaDAO.getEntityManager().getTransaction().begin();
				citaDAO.getEntityManager().merge(citaSeleccionada);
				citaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Cita Atendida.. debe pasar por caja a cancelar", Context.getInstance().getStage());
				buscarCitasPorMedicos();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void facturarCita() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar cita", Context.getInstance().getStage());
    			return;
    		};
			if(tvDatos.getSelectionModel().getSelectedItem().getEstadoCita().equals("PENDIENTE")) {
    			helper.mostrarAlertaAdvertencia("Se debe atender la cita primero", Context.getInstance().getStage());
    			return;
    		}
			Context.getInstance().setCita(tvDatos.getSelectionModel().getSelectedItem());
			helper.abrirPantallaModal("/facturacion/FFactura.fxml","Citas del día", Context.getInstance().getStage());
			Context.getInstance().setCita(null);
			buscarCitas();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}

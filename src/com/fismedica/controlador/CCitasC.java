package com.fismedica.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fismedica.modelo.CitaCabecera;
import com.fismedica.modelo.CitaCabeceraDAO;
import com.fismedica.modelo.Medico;
import com.fismedica.modelo.MedicoDAO;
import com.fismedica.util.Constantes;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.PrintReport;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class CCitasC {
    @FXML private TableView<CitaCabecera> tvDatos;
    @FXML private Button btnAtender;
    @FXML private Button btnCancelar;
    @FXML private Button btnTicket;
    @FXML private DatePicker dtpFecha;
    @FXML private Button btnBuscar;
    
    CitaCabeceraDAO citaDAO = new CitaCabeceraDAO();
    ControllerHelper helper = new ControllerHelper();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    MedicoDAO medicoDAO = new MedicoDAO();
    public void initialize() {
    	try {
    		btnAtender.setVisible(false);
    		btnCancelar.setVisible(false);
    		btnTicket.setVisible(false);
    		
    		btnAtender.setStyle("-fx-cursor: hand;");
    		btnCancelar.setStyle("-fx-cursor: hand;");
    		btnTicket.setStyle("-fx-cursor: hand;");
    		btnBuscar.setStyle("-fx-cursor: hand;");
    		dtpFecha.setValue(convertToLocalDate(new Date()));
    		
    		if(Context.getInstance().getUsuario().getPerfil().getIdPerfil() == Constantes.ID_PERFIL_MEDICO) {//busca las citas por el medico que inicia sesion
				btnAtender.setVisible(true);
				buscarCitasPorMedicos();
			}else {//busca las citas normales
				btnCancelar.setVisible(true);
				btnTicket.setVisible(true);
				buscarCitasUsuario();
			}
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    
    public void generarTicket() {
    	try {
    		if(tvDatos.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar cita", Context.getInstance().getStage());
    			return;
    		}
    		imprimirTicket(tvDatos.getSelectionModel().getSelectedItem());
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    private void imprimirTicket(CitaCabecera citaC) {
		try {
			
			System.out.println("Id del medico " + citaC.getMedico().getIdMedico());
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("TURNO", "" + citaC.getNumeroCita());
			param.put("HORARIO", "HORA INICIO: " + String.valueOf(citaC.getHoraInicio()) + " HORA FIN: " + String.valueOf(citaC.getHoraFin()));
			param.put("FECHA", "" + formateador.format(citaC.getFechaCita()));
			param.put("PACIENTE", "" +citaC.getCliente().getNombre()+" "+citaC.getCliente().getApellido());	
			param.put("ID_MEDICO", citaC.getMedico().getIdMedico());
			pr.crearReporte("/recursos/reportes/rptTicketCita.jasper", citaDAO, param);
			pr.showReport("Ticket");
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
				//System.out.println("el mensaje es "+ Context.getInstance().getCita().getObservaciones());
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

    
    public void cancelarCita() {
    	try {
    		if(tvDatos.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar cita", Context.getInstance().getStage());
    			return;
    		}
    		if(tvDatos.getSelectionModel().getSelectedItem().getEstadoCita().equals(Constantes.ESTADO_CITA_ATENDIDO)) {
    			helper.mostrarAlertaAdvertencia("La cita ya ha sido atendida.. no se puede cancelar", Context.getInstance().getStage());
    			return;
    		}
    		Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Cancelar cita.. Desea Continuar?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				CitaCabecera citaCancelar = tvDatos.getSelectionModel().getSelectedItem();
	    		citaCancelar.setEstado("I");
	    		citaCancelar.setEstadoNoCita(0);
	    		citaDAO.getEntityManager().getTransaction().begin();
	    		citaDAO.getEntityManager().merge(citaCancelar);
	    		citaDAO.getEntityManager().getTransaction().commit();
	    		helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
	    		buscarCitasUsuario();
			}
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }

	public void buscarCitas() {
    	try {
    		if(dtpFecha.getValue() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar fecha", Context.getInstance().getStage());
    			return;
    		}
    		if(Context.getInstance().getUsuario().getPerfil().getIdPerfil() == Constantes.ID_PERFIL_MEDICO) {//busca las citas por el medico que inicia sesion
				buscarCitasPorMedicos();
			}else {//busca las citas normales
				buscarCitasUsuario();
			}
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    @SuppressWarnings("unchecked")
	public void buscarCitasUsuario() {
		try {
			//cargar listado de citas
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<CitaCabecera> lista;
			ObservableList<CitaCabecera> datos = FXCollections.observableArrayList();
			lista = citaDAO.getCitasPorDia(convertToDate(dtpFecha.getValue()));
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
    public LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}
	public Date convertToDate(LocalDate dateToConvert) {
		return java.sql.Date.valueOf(dateToConvert);
	}
}

package com.fismedica.controlador;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fismedica.modelo.Categoria;
import com.fismedica.modelo.CategoriaDAO;
import com.fismedica.modelo.CitaCabecera;
import com.fismedica.modelo.CitaCabeceraDAO;
import com.fismedica.modelo.CitaDetalle;
import com.fismedica.modelo.Cliente;
import com.fismedica.modelo.ClienteDAO;
import com.fismedica.modelo.Medico;
import com.fismedica.modelo.Precio;
import com.fismedica.modelo.PrecioDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.PrintReport;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class TTerapiasC {
	Tooltip toolTip;
	@FXML private TextField txtEspecialidadMedico;
	@FXML private Button btnNuevo;
	@FXML private TextField txtIva;
	@FXML private TextField txtSubtotal;
	@FXML private TextField txtCedulaCliente;
	@FXML private TextField txtDireccionCliente;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtTotal;
	@FXML private TextField txtMinutos;
	//@FXML private Label lblHoraFin;
	@FXML private TextField txtNombresCliente;
	@FXML private TextField txtApellidosCliente;
	@FXML private Label lblIva;
	@FXML private TextField txtNombresMedico;
	@FXML private TextField txtApellidosMedico;
	@FXML private TextField txtEmailCliente;
	@FXML private Button btnBuscarCliente;
	@FXML private Button btnAgregar;
	@FXML private TextField txtTituloMedico;
	@FXML private TextField txtCedulaMedico;
	@FXML private Button btnBuscarMedico;
	@FXML private Button btnGenerarCita;
	@FXML private TableView<CitaDetalle> tvDatosDetalle;
	//@FXML private Label lblHoraInicio;
	@FXML private TextField txtNoCita;
	@FXML private Button btnGrabar;
	@FXML private Button btnBuscarCita;
	@FXML private Button btnQuitar;
	DecimalFormat df = new DecimalFormat("#0.00");
	@FXML private TextArea txtObservaciones;
	@FXML private Label lblTurno;
	
	ControllerHelper helper = new ControllerHelper();
	CategoriaDAO categoriaDAO = new CategoriaDAO();
	CitaCabeceraDAO citaDAO = new CitaCabeceraDAO();
	ClienteDAO clienteDAO = new ClienteDAO();
	PrecioDAO precioDAO = new PrecioDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	Cliente clienteSeleccionado = new Cliente();
	Medico medicoSeleccionado = new Medico();
	CitaCabecera cita;
	String horaInicio = "09:00:00";
	String horaFin = "19:00:00";
	
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Grabar factura");
			btnGrabar.setStyle("-fx-graphic: url('/save.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);
			
			
			toolTip = new Tooltip("Buscar cita");
			btnBuscarCita.setStyle("-fx-graphic: url('/icono_buscar.png');-fx-cursor: hand;");
			btnBuscarCita.setTooltip(toolTip);
			
			
			toolTip = new Tooltip("Generar número de consulta");
			btnGenerarCita.setStyle("-fx-graphic: url('/icono_buscar.png');-fx-cursor: hand;");
			btnGenerarCita.setTooltip(toolTip);
			
			
			toolTip = new Tooltip("Buscar cliente");
			btnBuscarCliente.setStyle("-fx-graphic: url('/icono_buscar.png');-fx-cursor: hand;");
			btnBuscarCliente.setTooltip(toolTip);
			
			toolTip = new Tooltip("Buscar médico");
			btnBuscarMedico.setStyle("-fx-graphic: url('/icono_buscar.png');-fx-cursor: hand;");
			btnBuscarMedico.setTooltip(toolTip);
			
			
			toolTip = new Tooltip("Agregar servicio");
			btnAgregar.setStyle("-fx-graphic: url('/agregar.png');-fx-cursor: hand;");
			btnAgregar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Quitar servicio");
			btnQuitar.setStyle("-fx-graphic: url('/quitar.png');-fx-cursor: hand;");
			btnQuitar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nueva factura");
			btnNuevo.setStyle("-fx-graphic: url('/nuevo.png');-fx-cursor: hand;");
			btnNuevo.setTooltip(toolTip);
			cargarTablaDetalle();
			txtNoCita.setText("0");
			txtNoCita.setEditable(false);
			dtpFecha.setValue(convertToLocalDate(new Date()));
			dtpFecha.setDayCellFactory(dayCellFactory);

			//cargarTurnosHora();
			lblTurno.setText("Turno: 1");
			bloquearCamposNuevaCita();
			bloquear();
			txtCedulaCliente.focusedProperty().addListener(new ChangeListener<Boolean>()
			{
			    @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (newPropertyValue){
			        }
			        else{
			            buscarClienteCedula(txtCedulaCliente.getText());
			        }
			    }
			});
			
			
			Boolean band = Context.getInstance().getBanderaNuevaCita();
			if(band == true) {
				desbloquear();
				txtCedulaCliente.setDisable(true);
				Cliente cli = Context.getInstance().getClienteSeleccionado();
				btnBuscarCita.setVisible(false);
				btnBuscarCliente.setVisible(false);
				cargarDatosCliente(cli);
			}
		}catch(Exception ex) {
			
		}
	}
	
	Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
	    @Override
	    public void updateItem(LocalDate item, boolean empty) {

	        super.updateItem(item, empty);
	        
	        this.setDisable(false);
	        this.setBackground(null);
	        this.setTextFill(Color.BLACK);

	        // deshabilitar las fechas pasadas
	        if (item.isBefore(LocalDate.now())) {
	            this.setDisable(true);
	        }

	        // marcar los dias de quincena
	    /*    int day = item.getDayOfMonth();
	        if(day == 15 || day == 30) {

	            Paint color = Color.RED;
	            BackgroundFill fill = new BackgroundFill(color, null, null);
	            
	            this.setBackground(new Background(fill));
	            this.setTextFill(Color.WHITESMOKE);
	        }*/
	        
	        // fines de semana en color verde
	        DayOfWeek dayweek = item.getDayOfWeek();
	        if (dayweek == DayOfWeek.SATURDAY || dayweek == DayOfWeek.SUNDAY) {
	            this.setTextFill(Color.GREEN);
	        }
	    }
	};
	private void buscarClienteCedula(String cedula) {
		List<Cliente> clienteLista = clienteDAO.getBuscarPorCedula(cedula);
		if(clienteLista.size() > 0) {
			cargarDatosCliente(clienteLista.get(0));
		}else {
			limpiarCliente();
		}
	}
	public void cargarTurnosHora() {
		try {
			List<CitaCabecera> lista;
			if(medicoSeleccionado == null)
				lista = citaDAO.getCitasPorFechaMedico(convertToDate(dtpFecha.getValue()),0);
			else
				lista = citaDAO.getCitasPorFechaMedico(convertToDate(dtpFecha.getValue()),medicoSeleccionado.getIdMedico());
			
			System.out.println(lista.size());
			if(lista.size() > 0) {
				//lblHoraInicio.setText(String.valueOf(lista.get(0).getHoraFin()));
				//Time tiempoFin = lista.get(0).getHoraFin();
				//LocalTime local = tiempoFin.toLocalTime();
				//local = local.plusMinutes(60);
				//Time horaFinalizar = Time.valueOf(local);
				
				//lblHoraFin.setText(horaFinalizar.toString());
				lblTurno.setText(String.valueOf("Turno: " + lista.size() + 1));
			}else {
				//lblHoraInicio.setText(horaInicio);
				//lblHoraFin.setText("10:00:00");
				lblTurno.setText("Turno: 1");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarTablaDetalle() {
		try {
			tvDatosDetalle.getColumns().clear();
			tvDatosDetalle.getItems().clear();

			//llenar los datos en la tabla
			TableColumn<CitaDetalle, String> servicioColum = new TableColumn<>("Servicio");
			servicioColum.setMinWidth(10);
			servicioColum.setPrefWidth(200);
			servicioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaDetalle, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getServicio().getServicio());
				}
			});
			TableColumn<CitaDetalle, String> categoriaColum = new TableColumn<>("Categoría");
			categoriaColum.setMinWidth(10);
			categoriaColum.setPrefWidth(200);
			categoriaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaDetalle, String> param) {
					List<Categoria> listaC = categoriaDAO.getListaCategiaById(param.getValue().getIdCategoria());
					String categoria = "";
					if(listaC.size() > 0)
						categoria = listaC.get(0).getCategoria();
					return new SimpleObjectProperty<String>(categoria);
				}
			});
			TableColumn<CitaDetalle, String> tiempoColum = new TableColumn<>("Tiempo (Minutos)");
			tiempoColum.setMinWidth(10);
			tiempoColum.setPrefWidth(110);
			tiempoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaDetalle, String> param) {
					String totalMinutos = "0";
					List<Precio> listaPrecio = precioDAO.gePrecioActivo(param.getValue().getServicio().getIdServicio(), param.getValue().getIdCategoria());
					if(listaPrecio.size() > 0) {
						totalMinutos = String.valueOf(listaPrecio.get(0).getTiempoMinutos());
					}
					return new SimpleObjectProperty<String>(totalMinutos);
				}
			});
			TableColumn<CitaDetalle, String> precioColum = new TableColumn<>("P. Unitario");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(80);
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CitaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CitaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
				}
			});
			tvDatosDetalle.getColumns().addAll(servicioColum,categoriaColum,tiempoColum,precioColum);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void calcularTotales() {
		try {
			double subtotal = 0.0;
			double total = 0.0;
			double iva = 0.0;
			int totalMinutos = 0;
			for(CitaDetalle detalle : tvDatosDetalle.getItems()) {
				total = total + Double.parseDouble(String.valueOf(detalle.getPrecio()));
				List<Precio> listaPrecio = precioDAO.gePrecioActivo(detalle.getServicio().getIdServicio(), detalle.getIdCategoria());
				if(listaPrecio.size() > 0) {
					totalMinutos = totalMinutos + listaPrecio.get(0).getTiempoMinutos();
				}
			}
			iva = total * 0.12;
			subtotal = total - iva;
			
			
			txtSubtotal.setText(String.valueOf(df.format(subtotal).replace(",",".")));
			txtIva.setText(String.valueOf(df.format(iva).replace(",",".")));
			txtTotal.setText(String.valueOf(df.format(total).replace(",",".")));
			txtMinutos.setText(String.valueOf(totalMinutos));
			//tambien se calcula el total de las horas
			//Time tiempoFin = Time.valueOf(lblHoraInicio.getText());
			//LocalTime local = tiempoFin.toLocalTime();
			//local = local.plusMinutes(totalMinutos);
			//System.out.println("tiempo inicio: " + tiempoFin);
			
			//Time horaFinalizar = Time.valueOf(local);
			//System.out.println(horaFinalizar.toString());
			//lblHoraFin.setText(horaFinalizar.toString());
		}catch(Exception ex) {
			
		}
	}
	public void generarCita() {
		try {
			nuevoNumeroCita();
		}catch(Exception ex) {
			
		}
	}
	private Integer nuevoNumeroCita() {
		try {
			if(dtpFecha.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar la fecha de la cita", Context.getInstance().getStage());
				return 0;
			}
			if(medicoSeleccionado == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar el medico", Context.getInstance().getStage());
				return 0;
			}
			Integer numero = 0;
			List<CitaCabecera> lista = citaDAO.getCitasPorNumero(convertToDate(dtpFecha.getValue()), medicoSeleccionado.getIdMedico());
			if(lista.size() > 0) {
				numero = lista.get(0).getNumeroCita() + 1;
			}else
				numero = 1;
			txtNoCita.setText(String.valueOf(numero));
			lblTurno.setText("Turno: " + numero);
			desbloquear();
			grabarCita();
			calcularTotales();
			helper.mostrarAlertaInformacion("Número de cita generado", Context.getInstance().getStage());
			//bloquear los datos del medico, porque el turno generado es por el medico y fecha
			dtpFecha.setDisable(true);
			txtCedulaMedico.setDisable(true);
			txtNombresMedico.setDisable(true);
			txtApellidosMedico.setDisable(true);
			txtTituloMedico.setDisable(true);
			txtEspecialidadMedico.setDisable(true);
			btnBuscarMedico.setDisable(true);
			return numero;
		}catch(Exception ex) {
			return 1;
		}
	}
	private void grabarCita() {
		try {
			cita = new CitaCabecera();
			cita.setEstado("I");
			cita.setEstadoCita("PENDIENTE");
			cita.setEstadoNoCita(1);
			cita.setFechaCita(convertToDate(dtpFecha.getValue()));
			//cita.setHoraInicio(Time.valueOf(lblHoraInicio.getText()));
			//cita.setHoraFin(Time.valueOf(lblHoraInicio.getText()));
			cita.setIdCita(null);
			cita.setMedico(medicoSeleccionado);
			cita.setNumeroCita(Integer.parseInt(txtNoCita.getText()));
			citaDAO.getEntityManager().getTransaction().begin();
			citaDAO.getEntityManager().persist(cita);
			citaDAO.getEntityManager().getTransaction().commit();
			System.out.println("cita grabada " + cita.getIdCita());
			btnGenerarCita.setDisable(true);
		}catch(Exception ex) {
			
		}
	}
	private void bloquear() {
		btnAgregar.setDisable(true);
		btnBuscarCita.setDisable(true);
		btnGrabar.setDisable(true);
		btnNuevo.setDisable(true);
		btnQuitar.setDisable(true);
		txtNoCita.setDisable(true);
		txtObservaciones.setDisable(true);
		txtSubtotal.setDisable(true);
		txtIva.setDisable(true);
		txtTotal.setDisable(true);
	}
	private void desbloquear() {
		btnAgregar.setDisable(false);
		btnBuscarCita.setDisable(false);
		btnGrabar.setDisable(false);
		btnNuevo.setDisable(false);
		btnQuitar.setDisable(false);
		txtNoCita.setDisable(false);
		txtObservaciones.setDisable(false);
		txtSubtotal.setDisable(false);
		txtIva.setDisable(false);
		txtTotal.setDisable(false);
	}
	public void buscarCliente() {
		try {
			Context.getInstance().setClienteSeleccionado(null);
			helper.abrirPantallaModal("/terapias/TClientesLista.fxml","Listado de clientes", Context.getInstance().getStage());
			if(Context.getInstance().getClienteSeleccionado() != null) {
				cargarDatosCliente(Context.getInstance().getClienteSeleccionado());
			}else {
				limpiarCliente();
				txtCedulaCliente.setText("");
			}
			Context.getInstance().setClienteSeleccionado(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarMedico() {
		try {
			Context.getInstance().setMedicoSeleccionado(null);
			helper.abrirPantallaModal("/terapias/TMedicoLista.fxml","Listado de medicos", Context.getInstance().getStage());
			if(Context.getInstance().getMedicoSeleccionado() != null) {
				cargarDatosMedico(Context.getInstance().getMedicoSeleccionado());
				//cargarTurnosHora();
			}else {
				limpiarMedico();
			}
			Context.getInstance().setClienteSeleccionado(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void limpiarMedico() {
		medicoSeleccionado = null;
		txtCedulaMedico.setText("");
		txtNombresMedico.setText("");
		txtApellidosMedico.setText("");
		txtTituloMedico.setText("");
		txtEspecialidadMedico.setText("");
	}
	private void cargarDatosMedico(Medico medico) {
		medicoSeleccionado = medico;
		txtCedulaMedico.setText(medico.getCedula());
		txtNombresMedico.setText(medico.getNombre());
		txtApellidosMedico.setText(medico.getApellido());
		txtTituloMedico.setText(medico.getTitulo());
		txtEspecialidadMedico.setText(medico.getEspecialidad());
	}
	private void cargarDatosCliente(Cliente cliente) {
		clienteSeleccionado = cliente;
		txtNombresCliente.setText(clienteSeleccionado.getNombre());
		txtApellidosCliente.setText(clienteSeleccionado.getApellido());
		txtEmailCliente.setText(clienteSeleccionado.getEmail());
		txtDireccionCliente.setText(clienteSeleccionado.getDireccion());
		txtCedulaCliente.setText(clienteSeleccionado.getCedula());
	}
	private void limpiarCliente() {
		clienteSeleccionado = null;
		txtNombresCliente.setText("");
		txtApellidosCliente.setText("");
		txtEmailCliente.setText("");
		txtDireccionCliente.setText("");
		//txtCedulaCliente.setText("");
	}
	//el metodo devuelve verdadero cuando se pasa
	private boolean booleanVerificarTiempoLimite(Time horaTerminarCita) {
		try {
			boolean bandera = false;
			Time horaMaximo = Time.valueOf(horaFin);
			if(horaTerminarCita.after(horaMaximo)) {//verifica cuando se pasa de la hora limita
				helper.mostrarAlertaAdvertencia("La cantidad de servicios sobrepasa el tiempo liíite", Context.getInstance().getStage());
				bandera = true;
			}
			//System.out.println(horaFinalizar.toString());
			//lblHoraFin.setText(horaFinalizar.toString());
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	public void agregar() {
		try {
			Context.getInstance().setPrecio(null);
			helper.abrirPantallaModal("/terapias/TServicios.fxml","Listado de servicios", Context.getInstance().getStage());
			if(Context.getInstance().getPrecio() != null) {
				Precio seleccionado = Context.getInstance().getPrecio();
				
				CitaDetalle detalle = new CitaDetalle();
				detalle.setPrecio(seleccionado.getPrecio());
				detalle.setIdDetalle(null);
				detalle.setEstado("A");
				detalle.setServicio(seleccionado.getServicio());
				detalle.setIdCategoria(seleccionado.getCategoria().getIdCategoria());
				tvDatosDetalle.getItems().add(detalle);
				tvDatosDetalle.refresh();
				calcularTotales();
			}
			Context.getInstance().setPrecio(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void quitar() {
		try {
			if(tvDatosDetalle.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un servicio a quitar", Context.getInstance().getStage());
				return;
			}
			CitaDetalle detalle = new CitaDetalle();
			detalle = tvDatosDetalle.getSelectionModel().getSelectedItem();
			tvDatosDetalle.getItems().remove(detalle);
			tvDatosDetalle.refresh();
			calcularTotales();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				//hay q tomarse la transaccion desde el inicio
				generarCliente();
				citaDAO.getEntityManager().getTransaction().begin();
				cita.setCliente(clienteSeleccionado);
				cita.setMedico(medicoSeleccionado);
				cita.setFechaCita(convertToDate(dtpFecha.getValue()));
				cita.setEstado("A");
				cita.setEstadoCita("PENDIENTE");
				//hay q calcular la hora de inicio y la hora fin, dependiendo de la cita anterior ya que puede que al grabar se choquen los persist
				List<CitaCabecera> lista = citaDAO.getCitasPorNumeroHora(convertToDate(dtpFecha.getValue()), medicoSeleccionado.getIdMedico());
				if(lista.size() > 0) {
					//obtengo la ultima cita
					CitaCabecera citaUltima = lista.get(0);//el cero porque la ordeno de manera descendente
					Time horaUltima = citaUltima.getHoraFin();
					cita.setHoraInicio(horaUltima);
					int totalMinutos = 0;
					for(CitaDetalle detalle : tvDatosDetalle.getItems()) {
						List<Precio> listaPrecio = precioDAO.gePrecioActivo(detalle.getServicio().getIdServicio(), detalle.getIdCategoria());
						if(listaPrecio.size() > 0) {
							totalMinutos = totalMinutos + listaPrecio.get(0).getTiempoMinutos();
						}
					}
					//tambien se calcula el total de las horas
					Time tiempoFin = horaUltima;
					LocalTime local = tiempoFin.toLocalTime();
					local = local.plusMinutes(totalMinutos);
					//System.out.println("tiempo inicio: " + tiempoFin);
					
					Time horaFinalizar = Time.valueOf(local);
					cita.setHoraFin(horaFinalizar);
					if(booleanVerificarTiempoLimite(horaFinalizar) == true) {
						citaDAO.getEntityManager().getTransaction().rollback();
						return;
					}
					//System.out.println(horaFinalizar.toString());
					//lblHoraFin.setText(horaFinalizar.toString());
					
				}else {
					Time tiempoFin = Time.valueOf("09:00:00");
					cita.setHoraInicio(tiempoFin);
					int totalMinutos = 0;
					for(CitaDetalle detalle : tvDatosDetalle.getItems()) {
						List<Precio> listaPrecio = precioDAO.gePrecioActivo(detalle.getServicio().getIdServicio(), detalle.getIdCategoria());
						if(listaPrecio.size() > 0) {
							totalMinutos = totalMinutos + listaPrecio.get(0).getTiempoMinutos();
						}
					}
					LocalTime local = tiempoFin.toLocalTime();
					local = local.plusMinutes(totalMinutos);
					//System.out.println("tiempo inicio: " + tiempoFin);
					
					Time horaFinalizar = Time.valueOf(local);
					cita.setHoraFin(horaFinalizar);
					if(booleanVerificarTiempoLimite(horaFinalizar) == true) {
						citaDAO.getEntityManager().getTransaction().rollback();
						return;
					}
				}
				cita.setObservaciones(txtObservaciones.getText());
				
				if(cita.getCitaDetalles() == null) {//no tiene datos de detalle
					List<CitaDetalle> detalle = new ArrayList<>();
					for(CitaDetalle det : tvDatosDetalle.getItems()) {
						det.setEstado("A");
						det.setCitaCabecera(cita);
						detalle.add(det);
					}
					cita.setCitaDetalles(detalle);
				}else {// tiene datos de detalle
					for(CitaDetalle det : tvDatosDetalle.getItems()) {
						if(det.getIdDetalle() == null) {
							det.setEstado("A");
							det.setCitaCabecera(cita);
							cita.getCitaDetalles().add(det);
						}
					}
					/*
					for(CitaDetalle deta : cita.getCitaDetalles()) {
						bandera = false;
						if(deta.getIdDetalle() != null) {
							for(CitaDetalle det : tvDatosDetalle.getItems()) {
								if(det.getIdDetalle() != null) {
									if(det.getIdDetalle() == deta.getIdDetalle())
										bandera = true;//si esta en la lista de los que estan grabados
								}
							}
						}
						if(bandera == false) {//si la bandera sigue siendo falso.. es xq se elimino
							deta.setEstado("I");
						}
					}*/
				}
				
				if(cita.getIdCita() != null) {//modifica
					
					citaDAO.getEntityManager().merge(cita);
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					System.out.println(cita.getIdCita() + " modifica");
					//imprimirFactura(cita.getIdCita());
				}else {
					citaDAO.getEntityManager().persist(cita);
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					System.out.println(cita.getIdCita() + " inserta");
					//imprimirFactura(cita.getIdCita());
					
				}

				citaDAO.getEntityManager().getTransaction().commit();
				imprimirTicket(cita);
				bloquearCamposNuevaCita();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
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
			if(clienteSeleccionado!=null)
			param.put("PACIENTE", "" + clienteSeleccionado.getNombre()+" "+clienteSeleccionado.getApellido());
			else
			param.put("PACIENTE", "" +citaC.getCliente().getNombre()+" "+citaC.getCliente().getApellido());	
			param.put("ID_MEDICO", citaC.getMedico().getIdMedico());
			pr.crearReporte("/recursos/reportes/rptTicketCita.jasper", citaDAO, param);
			pr.showReport("Ticket");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void generarCliente() {
		if(clienteSeleccionado == null) {
			clienteSeleccionado = new Cliente();
		}
		clienteSeleccionado.setNombre(txtNombresCliente.getText());
		clienteSeleccionado.setApellido(txtApellidosCliente.getText());
		clienteSeleccionado.setCedula(txtCedulaCliente.getText());
		clienteSeleccionado.setDireccion(txtDireccionCliente.getText());
		clienteSeleccionado.setEmail(txtEmailCliente.getText());
		clienteSeleccionado.setEstado("A");
		clienteSeleccionado.setFechaCreacion(new Date());
		clienteDAO.getEntityManager().getTransaction().begin();
		if(clienteSeleccionado.getIdCliente() != null) {
			clienteDAO.getEntityManager().merge(clienteSeleccionado);
		}else {
			clienteDAO.getEntityManager().persist(clienteSeleccionado);
		}
		clienteDAO.getEntityManager().getTransaction().commit();
		System.out.println("Cliente grabado " + clienteSeleccionado.getIdCliente());
	}
	private boolean validarDatos() {
		try {
			boolean bandera = false;
			if(txtNoCita.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar número de la cita", Context.getInstance().getStage());
				return false;
			}
			if(txtCedulaCliente.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
				return false;
			}
			if(medicoSeleccionado == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un medico", Context.getInstance().getStage());
				return false;
			}
			if(dtpFecha.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Debe registrar la fecha de la cita", Context.getInstance().getStage());
				return false;
			}			
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	
	private void bloquearCamposNuevaCita() {
		cita = null;
		limpiarCliente();
		limpiarMedico();
		tvDatosDetalle.getItems().clear();
		txtNoCita.setText("0");
		dtpFecha.setValue(convertToLocalDate(new Date()));
		
		btnGenerarCita.setDisable(false);
		dtpFecha.setDisable(false);
		txtCedulaMedico.setDisable(false);
		txtNombresMedico.setDisable(false);
		txtApellidosMedico.setDisable(false);
		txtTituloMedico.setDisable(false);
		txtEspecialidadMedico.setDisable(false);
		btnBuscarMedico.setDisable(false);
		bloquear();
	}
	public void nuevo() {
		if(cita != null) {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea cancelar la cita?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				cita.setEstado("I");
				cita.setEstadoNoCita(0);
				citaDAO.getEntityManager().getTransaction().begin();
				citaDAO.getEntityManager().merge(cita);
				citaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Cita cancelada", Context.getInstance().getStage());
				bloquearCamposNuevaCita();
			}
		}
	}
	public void buscarCita() {
		
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

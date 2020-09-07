package com.fismedica.controlador;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.fismedica.modelo.Empresa;
import com.fismedica.modelo.EmpresaDAO;
import com.fismedica.modelo.FacturaCabecera;
import com.fismedica.modelo.FacturaCabeceraDAO;
import com.fismedica.modelo.FacturaDetalle;
import com.fismedica.modelo.Medico;
import com.fismedica.modelo.Precio;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.PrintReport;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.paint.Color;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class FFacturaC {
	Tooltip toolTip;
	@FXML private TextField txtNombresMedico;
	@FXML private TextField txtEspecialidadMedico;
	@FXML private TextField txtEmailCliente;
	@FXML private Button btnNuevo;
	@FXML private TextField txtIva;
	@FXML private TextField txtSubtotal;
	@FXML private Button btnBuscarCliente;
	@FXML private TextField txtCedulaCliente;
	@FXML private TextField txtTelefonoCliente;
	@FXML private Button btnAgregar;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtTotal;
	@FXML private TextField txtTituloMedico;
	@FXML private TextField txtCedulaMedico;
	@FXML private TextField txtNoFacuta;
	@FXML private Button btnBuscarMedico;
	@FXML private TableView<FacturaDetalle> tvDatosDetalle;
	@FXML private TextField txtNombresCliente;
	@FXML private TextField txtApellidosCliente;
	@FXML private Label lblIva;
	@FXML private TextField txtNoCita;
	@FXML private Button btnGrabar;
	@FXML private Button btnBuscarCita;
	@FXML private Button btnQuitar;
	CategoriaDAO categoriaDAO = new CategoriaDAO();
	ControllerHelper helper = new ControllerHelper();
	Cliente clienteSeleccionado = new Cliente();
	Medico medicoSeleccionado = new Medico();
	CitaCabecera citaSeleccionado = new CitaCabecera();
	FacturaCabecera factura;
	FacturaCabeceraDAO facturaDAO = new FacturaCabeceraDAO();
	DecimalFormat df = new DecimalFormat("#0.00");
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	CitaCabeceraDAO citaDAO = new CitaCabeceraDAO();
	ClienteDAO clienteDAO = new ClienteDAO();
	private String empresa = "";
	private String telefono = "";
	private String direccion = "";
	EmpresaDAO empresaDAO = new EmpresaDAO();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Grabar factura");
			btnGrabar.setStyle("-fx-graphic: url('/save.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Buscar cita");
			btnBuscarCita.setStyle("-fx-graphic: url('/icono_buscar.png');-fx-cursor: hand;");
			btnBuscarCita.setTooltip(toolTip);
			
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
			nuevo();
			cargarDatosEmpresa();
			//bloquear los botones necesarios cuando se factura desde el listado
			if(Context.getInstance().getBanderaFacturar() == true) {
				btnBuscarCita.setVisible(false);
				btnBuscarCliente.setVisible(false);
				btnBuscarMedico.setVisible(false);
				btnNuevo.setVisible(false);
				txtCedulaCliente.setDisable(true);
				txtCedulaMedico.setDisable(true);
				cargarDatos(Context.getInstance().getCita());			
			}
			if(Context.getInstance().getCita()!=null && Context.getInstance().getBanderaFacturar() !=true ) {
				cargarDatos(Context.getInstance().getCita());	
			}
			dtpFecha.setDayCellFactory(dayCellFactory);
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
	        if (item.isAfter(LocalDate.now())) {
	            this.setDisable(true);
	        }
	    }
	};
	private void cargarDatosEmpresa() {
		try {
			List<Empresa> lista = empresaDAO.getRecuperaDatosEmpresa();
			for(Empresa emp : lista) {
				empresa = emp.getRazonSocial();
				direccion = emp.getDireccion();
				telefono = emp.getTelefono();
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
			TableColumn<FacturaDetalle, String> servicioColum = new TableColumn<>("Servicio");
			servicioColum.setMinWidth(10);
			servicioColum.setPrefWidth(200);
			servicioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getServicio().getServicio());
				}
			});
			TableColumn<FacturaDetalle, String> categoriaColum = new TableColumn<>("Categoría");
			categoriaColum.setMinWidth(10);
			categoriaColum.setPrefWidth(200);
			categoriaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
					List<Categoria> listaC = categoriaDAO.getListaCategiaById(param.getValue().getIdCategoria());
					String categoria = "";
					if(listaC.size() > 0)
						categoria = listaC.get(0).getCategoria();
					return new SimpleObjectProperty<String>(categoria);
				}
			});
			TableColumn<FacturaDetalle, String> totalColum = new TableColumn<>("P. Total");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(100);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
				}
			});
			tvDatosDetalle.getColumns().addAll( servicioColum,categoriaColum,totalColum);			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarCita() {
		try {
			Context.getInstance().setCita(null);
			helper.abrirPantallaModal("/facturacion/FListaCitas.fxml","Listado de citas pendientes", Context.getInstance().getStage());
			if(Context.getInstance().getCita() != null) {
				cargarDatos(Context.getInstance().getCita());
			}else {
				limpiarCliente();
				limpiarMedico();
				limpiarDetalles();
			}
			Context.getInstance().setCita(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatos(CitaCabecera c) {
		try {
			citaSeleccionado = c;
			dtpFecha.setValue(convertToLocalDate(c.getFechaCita()));
			txtNoCita.setText(String.valueOf(c.getNumeroCita()));
			cargarCliente(c.getCliente());
			cargarMedico(c.getMedico());
			cargarDetalles(c.getCitaDetalles());
		}catch(Exception ex) {
			
		}
	}
	private void cargarDetalles(List<CitaDetalle> detalle) {
		for(CitaDetalle det : detalle) {
			if(det.getEstado().equals("A")) {
				FacturaDetalle fac = new FacturaDetalle();
				fac.setCantidad(1);
				fac.setEstado("A");
				fac.setIdCategoria(det.getIdCategoria());
				fac.setIdDetalle(null);
				fac.setPrecio(det.getPrecio());
				fac.setServicio(det.getServicio());
				fac.setTotal(det.getPrecio());
				tvDatosDetalle.getItems().add(fac);
			}
		}
		tvDatosDetalle.refresh();
		calcularTotales();
	}
	private void cargarCliente(Cliente cliente) {
		clienteSeleccionado = cliente;
		txtCedulaCliente.setText(cliente.getCedula());
		txtNombresCliente.setText(cliente.getNombre());
		txtApellidosCliente.setText(cliente.getApellido());
		txtEmailCliente.setText(cliente.getEmail());
		txtTelefonoCliente.setText(cliente.getTelefono());
	}
	private void cargarMedico(Medico medico) {
		medicoSeleccionado = medico;
		txtCedulaMedico.setText(medico.getCedula());
		txtNombresMedico.setText(medico.getNombre()+" "+medico.getApellido());
		txtTituloMedico.setText(medico.getTitulo());
		txtEspecialidadMedico.setText(medico.getEspecialidad());
	}
	private void limpiarCliente() {
		clienteSeleccionado = null;
		txtApellidosCliente.setText("");
		txtCedulaCliente.setText("");
		txtNombresCliente.setText("");
		txtEmailCliente.setText("");
		txtTelefonoCliente.setText("");
	}
	private void limpiarMedico() {
		medicoSeleccionado = null;
		txtCedulaMedico.setText("");
		txtNombresMedico.setText("");
		txtTituloMedico.setText("");
		txtEspecialidadMedico.setText("");
	}
	private void limpiarDetalles() {
		tvDatosDetalle.getItems().clear();
		txtSubtotal.setText("0.00");
		txtIva.setText("0.00");
		txtTotal.setText("0.00");
	}
	public void buscarCliente() {
		try {
			Context.getInstance().setClienteSeleccionado(null);
			helper.abrirPantallaModal("/terapias/TClientesLista.fxml","Listado de clientes", Context.getInstance().getStage());
			if(Context.getInstance().getClienteSeleccionado() != null) {
				cargarDatosCliente(Context.getInstance().getClienteSeleccionado());
			}else {
				limpiarCliente();
			}
			Context.getInstance().setClienteSeleccionado(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void cargarDatosCliente(Cliente cliente) {
		clienteSeleccionado = cliente;
		txtNombresCliente.setText(clienteSeleccionado.getNombre());
		txtApellidosCliente.setText(clienteSeleccionado.getApellido());
		txtEmailCliente.setText(clienteSeleccionado.getEmail());
		txtTelefonoCliente.setText(clienteSeleccionado.getTelefono());
		txtCedulaCliente.setText(clienteSeleccionado.getCedula());
	}
	public void agregar() {
		try {
			Context.getInstance().setPrecio(null);
			helper.abrirPantallaModal("/terapias/TServicios.fxml","Listado de servicios", Context.getInstance().getStage());
			if(Context.getInstance().getPrecio() != null) {
				Precio seleccionado = Context.getInstance().getPrecio();
				FacturaDetalle detalle = new FacturaDetalle();
				detalle.setPrecio(seleccionado.getPrecio());
				detalle.setIdDetalle(null);
				detalle.setEstado("A");
				detalle.setServicio(seleccionado.getServicio());
				detalle.setIdCategoria(seleccionado.getCategoria().getIdCategoria());
				tvDatosDetalle.getItems().add(detalle);
				calcularTotales();
				tvDatosDetalle.refresh();
				calcularTotales();
			}
			Context.getInstance().setPrecio(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void calcularTotales() {
		try {
			double subtotal = 0.0;
			double total = 0.0;
			double iva = 0.0;
			for(FacturaDetalle detalle : tvDatosDetalle.getItems()) {
				total = total + Double.parseDouble(String.valueOf(detalle.getPrecio()));
				//totalMinutos = totalMinutos + detalle.getServicio().getDuracionMinutos();
			}
			iva = total * 0.12;
			subtotal = total - iva;					
			txtSubtotal.setText(String.valueOf(df.format(subtotal).replace(",",".")));
			txtIva.setText(String.valueOf(df.format(iva).replace(",", ".")));
			txtTotal.setText(String.valueOf(df.format(total).replace(",", ".")));
		}catch(Exception ex) {
			System.out.println("error");
		}
	}
	
	public void quitar() {
		try {
			if(tvDatosDetalle.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un servicio a quitar", Context.getInstance().getStage());
				return;
			}
			FacturaDetalle detalle = new FacturaDetalle();
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
				generarCliente();
				
				factura.setCliente(clienteSeleccionado);
				factura.setMedico(medicoSeleccionado);
				factura.setFechaEmision(convertToDate(dtpFecha.getValue()));
				factura.setEstado("A");
				factura.setIdCita((Context.getInstance().getCita().getIdCita()));
				factura.setIva(BigDecimal.valueOf(Double.parseDouble(txtIva.getText())));
				factura.setSubtotal(BigDecimal.valueOf(Double.parseDouble(txtSubtotal.getText())));
				factura.setTotal(BigDecimal.valueOf(Double.parseDouble(txtTotal.getText())));
				factura.setNumFactura(txtNoFacuta.getText());
				
				if(factura.getFacturaDetalles() == null) {//no tiene datos de detalle
					List<FacturaDetalle> detalle = new ArrayList<>();
					for(FacturaDetalle det : tvDatosDetalle.getItems()) {
						det.setFacturaCabecera(factura);
						detalle.add(det);
					}
					factura.setFacturaDetalles(detalle);
				}else {// tiene datos de detalle
					boolean bandera = false;
					for(FacturaDetalle det : tvDatosDetalle.getItems()) {
						if(det.getIdDetalle() == null) {
							det.setFacturaCabecera(factura);
							factura.getFacturaDetalles().add(det);
						}
					}
					for(FacturaDetalle deta : factura.getFacturaDetalles()) {
						bandera = false;
						if(deta.getIdDetalle() != null) {
							for(FacturaDetalle det : tvDatosDetalle.getItems()) {
								if(det.getIdDetalle() != null) {
									if(det.getIdDetalle() == deta.getIdDetalle())
										bandera = true;//si esta en la lista de los que estan grabados
								}
							}
						}
						if(bandera == false) {//si la bandera sigue siendo falso.. es xq se elimino
							deta.setEstado("I");
						}
					}
				}
				
				if(factura.getIdFactura() != null) {//modifica
					facturaDAO.getEntityManager().getTransaction().begin();
					facturaDAO.getEntityManager().merge(factura);
					facturaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					System.out.println(factura.getIdFactura() + " modifica");
					imprimirFactura(factura.getIdCita());//visualiza primero
				}else {
					facturaDAO.getEntityManager().getTransaction().begin();
					facturaDAO.getEntityManager().persist(factura);
					facturaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					System.out.println(factura.getIdFactura()+ " inserta");
					imprimirFactura(factura.getIdFactura());//visualiza primero
					actualizarCita(factura.getIdCita());
				}
				Optional<ButtonType> resulta = helper.mostrarAlertaConfirmacion("Desea agendar la siguiente cita?",Context.getInstance().getStage());
				if(resulta.get() == ButtonType.OK){
					Context.getInstance().setBanderaNuevaCita(true);
					Context.getInstance().setClienteSeleccionado(clienteSeleccionado);
					helper.abrirPantallaModal("/terapias/TTerapias.fxml","Agendar próxima cita", Context.getInstance().getStage());
					Context.getInstance().setBanderaNuevaCita(false);
				}
				nuevo();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
		}
	}
	private void generarCliente() {
		if(clienteSeleccionado == null) {
			clienteSeleccionado = new Cliente();
		}
		clienteSeleccionado.setNombre(txtNombresCliente.getText());
		clienteSeleccionado.setApellido(txtApellidosCliente.getText());
		clienteSeleccionado.setCedula(txtCedulaCliente.getText());
		clienteSeleccionado.setTelefono(txtTelefonoCliente.getText());
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
	private void imprimirFactura(Integer idFactura) {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("NO_FACTURA", "" + txtNoFacuta.getText());
			param.put("TELEFONO", telefono);
			param.put("FECHA", "" + formateador.format(convertToDate(dtpFecha.getValue())));
			param.put("TOTAL", txtTotal.getText());
			param.put("SUBTOTAL", txtTotal.getText());
			param.put("IVA", txtTotal.getText());
			param.put("ID_FACTURA", idFactura);
			param.put("CEDULA", txtCedulaCliente.getText());
			param.put("CLIENTE", txtNombresCliente.getText() + " " + txtApellidosCliente.getText());
			param.put("TELEFONO_CLIENTE", txtTelefonoCliente.getText());
			
			pr.crearReporte("/recursos/reportes/rptFactura.jasper", facturaDAO, param);
			pr.showReport("Factura");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void actualizarCita(Integer idCita) {
		try {
			List<CitaCabecera> lista = citaDAO.getCitaById(idCita);
			if(lista.size() > 0) {
				CitaCabecera citaSel = lista.get(0);
				citaSel.setEstadoCita("COBRADO");
				citaDAO.getEntityManager().getTransaction().begin();
				citaDAO.getEntityManager().merge(citaSel);
				citaDAO.getEntityManager().getTransaction().commit();
			}
		}catch(Exception ex) {

		}
	}
	private boolean validarDatos() {
		try {
			boolean bandera = false;
			if(txtNoFacuta.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar número de factura", Context.getInstance().getStage());
				return false;
			}
			if(clienteSeleccionado == null) {
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
	
	public void nuevo() {
		try {
			citaSeleccionado = null;
			clienteSeleccionado = null;
			medicoSeleccionado = null;
			factura = new FacturaCabecera();
			factura.setIdFactura(null);
			limpiarCliente();
			limpiarDetalles();
			limpiarMedico();
			txtNoFacuta.setText("");
			txtNoCita.setText("0");
			dtpFecha.setValue(convertToLocalDate(new Date()));
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
			}else {
				limpiarMedico();
			}
			Context.getInstance().setClienteSeleccionado(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatosMedico(Medico medico) {
		medicoSeleccionado = medico;
		txtCedulaMedico.setText(medico.getCedula());
		txtNombresMedico.setText(medico.getNombre() + " " + medico.getApellido());
		txtTituloMedico.setText(medico.getTitulo());
		txtEspecialidadMedico.setText(medico.getEspecialidad());
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

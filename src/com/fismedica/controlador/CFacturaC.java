package com.fismedica.controlador;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fismedica.modelo.Empresa;
import com.fismedica.modelo.EmpresaDAO;
import com.fismedica.modelo.FacturaCabecera;
import com.fismedica.modelo.FacturaCabeceraDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.PrintReport;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.paint.Color;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class CFacturaC {
	@FXML private Button btnImprimir;
	@FXML private TableView<FacturaCabecera> tvDatos;
	@FXML private Button btnAnular;
	@FXML private DatePicker dtpFecha;
	@FXML private Button btnBuscar;

	FacturaCabeceraDAO facturaDAO = new FacturaCabeceraDAO();
	ControllerHelper helper = new ControllerHelper();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

	private String empresa = "";
	private String telefono = "";
	private String direccion = "";
	EmpresaDAO empresaDAO = new EmpresaDAO();
	
	public void initialize() {
		try {
			btnImprimir.setStyle("-fx-cursor: hand;");
			btnBuscar.setStyle("-fx-cursor: hand;");
			dtpFecha.setValue(convertToLocalDate(new Date()));
			dtpFecha.setDayCellFactory(dayCellFactory);
			cargarDatosEmpresa();
			bucarFacturaFecha();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
	    @Override
	    public void updateItem(LocalDate item, boolean empty) {

	        super.updateItem(item, empty);
	        
	        this.setDisable(false);
	        this.setBackground(null);
	        this.setTextFill(Color.BLACK);

	        if (item.isAfter(LocalDate.now())) {
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
	
    public void imprimirFactura() {
    	try {
    		if(tvDatos.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar cita", Context.getInstance().getStage());
    			return;
    		}
    		imprimirFactura(tvDatos.getSelectionModel().getSelectedItem());
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    private void imprimirFactura(FacturaCabecera fac) {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("NO_FACTURA", " " + fac.getNumFactura());
			param.put("TELEFONO", telefono);
			param.put("FECHA", "" + formateador.format(fac.getFechaEmision()));
			param.put("TOTAL", String.valueOf(fac.getTotal()));
			param.put("SUBTOTAL", String.valueOf(fac.getSubtotal()));
			param.put("IVA", String.valueOf(fac.getIva()));
			param.put("ID_FACTURA", fac.getIdFactura());
			param.put("CEDULA", fac.getCliente().getCedula());
			param.put("CLIENTE", fac.getCliente().getNombre() + " " + fac.getCliente().getApellido());
			param.put("TELEFONO_CLIENTE", fac.getCliente().getTelefono());
			pr.crearReporte("/recursos/reportes/rptFactura.jasper", facturaDAO, param);
			pr.showReport("Factura");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
    public void anularFactura() {
    	try {
    		if(tvDatos.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar cita", Context.getInstance().getStage());
    			return;
    		}
    		FacturaCabecera facturaCancelar = tvDatos.getSelectionModel().getSelectedItem();
    		facturaCancelar.setEstado("I");
    		facturaDAO.getEntityManager().getTransaction().begin();
    		facturaDAO.getEntityManager().merge(facturaCancelar);
    		facturaDAO.getEntityManager().getTransaction().commit();
    		helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
    		bucarFacturaFecha();
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }

    
    public void buscarFactura() {
    	bucarFacturaFecha();
    }
    @SuppressWarnings("unchecked")
	public void bucarFacturaFecha() {
    	try {
    		if(dtpFecha.getValue() == null) {
    			helper.mostrarAlertaAdvertencia("Debe seleccionar fecha", Context.getInstance().getStage());
    			return;
    		}
    		//cargar listado de facturas
    		tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<FacturaCabecera> lista;
			ObservableList<FacturaCabecera> datos = FXCollections.observableArrayList();
			lista = facturaDAO.getFacturaPorFecha(convertToDate(dtpFecha.getValue()));
			datos.setAll(lista);

			//llenar los datos en la tabla
			TableColumn<FacturaCabecera, String> noFacturaColum = new TableColumn<>("No. Factura");
			noFacturaColum.setMinWidth(10);
			noFacturaColum.setPrefWidth(90);
			noFacturaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaCabecera, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumFactura()));
				}
			});
			
			TableColumn<FacturaCabecera, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(220);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido());
				}
			});

			TableColumn<FacturaCabecera, String> medicoColum = new TableColumn<>("Médico");
			medicoColum.setMinWidth(10);
			medicoColum.setPrefWidth(220);
			medicoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaCabecera, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMedico().getNombre() + " " + param.getValue().getMedico().getApellido());
				}
			});

			TableColumn<FacturaCabecera, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaCabecera, String> param) {
					
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFechaEmision()));
				}
			});
			
			TableColumn<FacturaCabecera, String> horarioColum = new TableColumn<>("Total");
			horarioColum.setMinWidth(10);
			horarioColum.setPrefWidth(100);
			horarioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaCabecera,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaCabecera, String> param) {
					
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTotal()));
				}
			});
			
			tvDatos.getColumns().addAll(noFacturaColum, clienteColum,medicoColum,fechaColum,horarioColum);
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

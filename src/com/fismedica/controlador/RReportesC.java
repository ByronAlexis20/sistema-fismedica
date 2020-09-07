package com.fismedica.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fismedica.modelo.Empresa;
import com.fismedica.modelo.EmpresaDAO;
import com.fismedica.modelo.Medico;
import com.fismedica.modelo.ServicioDAO;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.PrintReport;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class RReportesC {
	@FXML private TextField txtNombresMedico;
	@FXML private Button btnRecaudacionServicio;
	@FXML private Button btnVisualizarIva;
	@FXML private TextField txtEspecialidadMedico;
	@FXML private DatePicker dtpDiariaInicio;
	@FXML private DatePicker dtpDiariaFin;
	@FXML private DatePicker dtpInicioIvaInicio;
	@FXML private DatePicker dtpInicioIvaFin;
	@FXML private DatePicker dtpRecaudacionServicioInicio;
	@FXML private DatePicker dtpRecaudacionServicioFin;
	@FXML private TextField txtTituloMedico;
	@FXML private TextField txtCedulaMedico;
	@FXML private DatePicker dtpFinComparativo;
	@FXML private Button btnBuscarMedico;
	@FXML private Button btnVisualizarComparativo;
	@FXML private DatePicker dtpInicioCliente;
	@FXML private Button btnRecaudacionMedico;
	@FXML private DatePicker dtpInicioComparativo;
	@FXML private Button btnVisualizarDiaria;
	@FXML private DatePicker dtpRecaudacionMedicoInicio;
	@FXML private DatePicker dtpRecaudacionMedicoFin;
	@FXML private DatePicker dtpInicioServiCat;
	@FXML private DatePicker dtpFinServiCat;
	

	ControllerHelper helper = new ControllerHelper();
	Medico medicoSeleccionado;
	private String empresa = "";
	private String telefono = "";
	private String direccion = "";
	EmpresaDAO empresaDAO = new EmpresaDAO();
	ServicioDAO servicioDAO = new ServicioDAO();
	
	public void initialize() {
		try {
			cargarDatosEmpresa();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
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
	public void buscarMedico() {
		try {
			Context.getInstance().setMedicoSeleccionado(null);
			helper.abrirPantallaModal("/terapias/TMedicoLista.fxml","Listado de medicos", Context.getInstance().getStage());
			if(Context.getInstance().getMedicoSeleccionado() != null) {
				cargarDatosMedico(Context.getInstance().getMedicoSeleccionado());
				//cargarTurnosHora();
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

	public void buscarServicio() {
		try {

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void visualizarRecaudacionMedico() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("TELEFONO", telefono);
			param.put("ID_MEDICO", medicoSeleccionado.getIdMedico());
			param.put("NOMBRE", medicoSeleccionado.getNombre()+" "+medicoSeleccionado.getApellido());
			param.put("FECHA_INICIO", convertToDate(dtpRecaudacionMedicoInicio.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpRecaudacionMedicoFin.getValue()));
			pr.crearReporte("/recursos/reportes/rptRecaudacionMedico.jasper", empresaDAO, param);
			pr.showReport("Recaudaciones por medico");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void visualizarRecaudacionServicio() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("TELEFONO", telefono);
			param.put("FECHA_INICIO", convertToDate(dtpRecaudacionServicioInicio.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpRecaudacionServicioFin.getValue()));
			pr.crearReporte("/recursos/reportes/rptServicioCategoria.jasper", empresaDAO, param);
			pr.showReport("Recaudaciones por servicio y categoria");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat format = new SimpleDateFormat("yyyy");
	@SuppressWarnings("deprecation")
	public void visualizarEstadisticoComparativo() {
		try {
			Date fechaInicio = convertToDate(dtpInicioComparativo.getValue());
			Date fechaFin = convertToDate(dtpFinComparativo.getValue());
			System.out.println("fecha inicio: " + formateador.format(fechaInicio));
			System.out.println("fecha fin: " + formateador.format(fechaFin));
			Date fechaInicioSiguienteAnio = sumarAnios(fechaInicio);
			Date fechaFinSiguienteAnio = sumarAnios(fechaFin);
			System.out.println("fecha inicio: " + formateador.format(fechaInicioSiguienteAnio));
			System.out.println("fecha fin: " + formateador.format(fechaFinSiguienteAnio));
			
			
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("TELEFONO", telefono);
			param.put("F_INICIO_ANT", formateador.format(fechaInicioSiguienteAnio));
			param.put("F_FIN_ANT", formateador.format(fechaFinSiguienteAnio));
			param.put("ANIO_ANT", format.format(fechaInicioSiguienteAnio));
			param.put("F_INICIO_ACT", formateador.format(fechaInicio));
			param.put("F_FIN_ACT", formateador.format(fechaFin));
			param.put("ANIO_ACT", format.format(fechaInicio));
			
			pr.crearReporte("/recursos/reportes/rptComparativo.jasper", empresaDAO, param);
			pr.showReport("Recaudaciones por meses Comraparativos");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public Date sumarAnios(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.YEAR, -1);
		return calendar.getTime();
	}
	
	public void VisualizarRecaudacionIva() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("TELEFONO", telefono);
			param.put("FECHA_INICIO", convertToDate(dtpInicioIvaInicio.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpInicioIvaFin.getValue()));
			pr.crearReporte("/recursos/reportes/rptRecaudacionIva.jasper", empresaDAO, param);
			pr.showReport("Recaudaciones por servicio y categoria");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void visualizarRecaudacionDiaria() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("TELEFONO", telefono);
			param.put("FECHA_INICIO", convertToDate(dtpDiariaFin.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpDiariaFin.getValue()));
			pr.crearReporte("/recursos/reportes/rptReporteDiario.jasper", empresaDAO, param);
			pr.showReport("Recaudaciones por servicio y categoria");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void visualizarEstadisticoServiciosCategrorias() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("TELEFONO", telefono);
			param.put("FECHA_INICIO", convertToDate(dtpInicioServiCat.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpFinServiCat.getValue()));
			pr.crearReporte("/recursos/reportes/rptEstadisticoServicioCategoria.jasper", empresaDAO, param);
			pr.showReport("Estadistico ServicioCategoria");
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

package com.fismedica.util;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import com.fismedica.modelo.ClaseDAO;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class PrintReport {
	public static final String FORMATO_PDF = "PDF";
	public static final String FORMATO_XLS = "XLS";

	private JasperReport report;
	private JasperPrint reportFilled;
	private JasperViewer viewer;
	ControllerHelper helper = new ControllerHelper();
	String applicationPath = System.getProperty("user.dir");
	String rutaArchivo = "c:\\factura.txt";
	
	
	public void crearReporte(String path, ClaseDAO claseDAO,Map<String, Object> param) {
		try {

			applicationPath = applicationPath + path;
			//helper.mostrarAlertaAdvertencia(applicationPath, Context.getInstance().getStage());
			Connection cn = claseDAO.abreConexion();
			report = (JasperReport) JRLoader.loadObjectFromFile(applicationPath);
			reportFilled = JasperFillManager.fillReport(report, param, cn);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void showReport(String titulo) {
		try {
			viewer = new JasperViewer(reportFilled,false);
			viewer.setTitle(titulo);
			viewer.setVisible(true);		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void imprimirFactura(String titulo) {
		try {
			viewer = new JasperViewer(reportFilled,false);
			viewer.setTitle(titulo);
			//viewer.setVisible(true);
			File file = new File(rutaArchivo);
			JRTextExporter exporter = new JRTextExporter();
			exporter.setParameter(JRTextExporterParameter.JASPER_PRINT, reportFilled);
			exporter.setParameter(JRTextExporterParameter.OUTPUT_FILE, file);
			exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 80);
			exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 40);
			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, Float.parseFloat("0"));
			exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, Float.parseFloat("0"));
			exporter.exportReport();

			java.awt.Desktop desktop = java.awt.Desktop.getDesktop(); 
			java.io.File fichero = new java.io.File(rutaArchivo); 
			if (desktop.isSupported(java.awt.Desktop.Action.PRINT)){ 
				try {
					desktop.print(fichero);
				} catch (Exception e){
					System.out.print("El sistema no permite imprimir usando la clase Desktop"); 
					e.printStackTrace();
				}
			}else{ 
				System.out.print("El sistema no permite imprimir usando la clase Desktop"); 
			} 


		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void exportToPDF(String destino) {
		try {
			JasperExportManager.exportReportToPdfFile(destino);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void imprimirReporte() {
		try {
			JasperPrintManager.printReport(reportFilled, false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

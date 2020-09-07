package com.fismedica.controlador;

import java.util.Date;

import com.fismedica.util.Constantes;
import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class CCitasObservacionC {
	 @FXML private TextArea txtAObservacion;
	 @FXML private Button btnObservacion;
	 
	 ControllerHelper helper = new ControllerHelper();
	
	 public void initialize() {
	    	try {
	    		btnObservacion.setStyle("-fx-cursor: hand;");		
	    	}catch(Exception ex) {
	    		System.out.println(ex.getMessage());
	    	}
	    }
	
	public void guardarObservacion() {
		try {
			if(txtAObservacion.getText().equals("")){
				helper.mostrarAlertaAdvertencia("Ingrese una observacion por favor ", Context.getInstance().getStage());
				return;
			}else {
				Context.getInstance().getCita().setObservaciones(txtAObservacion.getText());
				Context.getInstance().getStageModal().close();	
			}	
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}


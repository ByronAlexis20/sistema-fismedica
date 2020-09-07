package com.fismedica.main;

import java.util.Optional;

import com.fismedica.util.Context;
import com.fismedica.util.ControllerHelper;
import com.fismedica.util.Encriptado;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LaunchFismedica extends Application {
	ControllerHelper helper = new ControllerHelper();
	@Override
	public void start(Stage stage) {
		try {
			System.out.println(Encriptado.Encriptar("sa"));
			FXMLLoader root = new FXMLLoader();
			root.setLocation(getClass().getResource("/principal/FrmInicioSesion.fxml"));
			AnchorPane page = (AnchorPane) root.load();
			Scene scene = new Scene(page);
			stage.setScene(scene);
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("SISTEMA FISMEDICA - LOGIN");
			Context.getInstance().setStagePrincipal(stage);
			//stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea salir del sistema?",Context.getInstance().getStage());
					if(result.get() == ButtonType.OK)
						System.exit(0);
					else
						event.consume();
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

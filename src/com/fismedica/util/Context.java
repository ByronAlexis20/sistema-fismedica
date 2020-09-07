package com.fismedica.util;

import com.fismedica.modelo.CitaCabecera;
import com.fismedica.modelo.Cliente;
import com.fismedica.modelo.Medico;
import com.fismedica.modelo.Precio;
import com.fismedica.modelo.Servicio;
import com.fismedica.modelo.Usuario;

import javafx.stage.Stage;

public class Context {
	private final static Context instance = new Context();
	public static Context getInstance() {
		return instance;
	}
	
	private Stage stage;
	private Stage stageModal;
	private Stage stagePrincipal;
	private Usuario usuario;
	private Usuario usuarioSeleccionado;
	private Cliente clienteSeleccionado;
	private Medico medicoSeleccionado;
	private CitaCabecera cita;
	private Servicio servicio;
	private Precio precio;
	private Boolean banderaNuevaCita = false;
	private Boolean banderaFacturar = false;
	private Integer codigoTipoCliente;
	
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public Stage getStageModal() {
		return stageModal;
	}
	public void setStageModal(Stage stageModal) {
		this.stageModal = stageModal;
	}
	public Stage getStagePrincipal() {
		return stagePrincipal;
	}
	public void setStagePrincipal(Stage stagePrincipal) {
		this.stagePrincipal = stagePrincipal;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
	public Cliente getClienteSeleccionado() {
		return clienteSeleccionado;
	}
	public void setClienteSeleccionado(Cliente clienteSeleccionado) {
		this.clienteSeleccionado = clienteSeleccionado;
	}
	public Medico getMedicoSeleccionado() {
		return medicoSeleccionado;
	}
	public void setMedicoSeleccionado(Medico medicoSeleccionado) {
		this.medicoSeleccionado = medicoSeleccionado;
	}
	public CitaCabecera getCita() {
		return cita;
	}
	public void setCita(CitaCabecera cita) {
		this.cita = cita;
	}
	public Servicio getServicio() {
		return servicio;
	}
	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
	public Precio getPrecio() {
		return precio;
	}
	public void setPrecio(Precio precio) {
		this.precio = precio;
	}
	public Boolean getBanderaNuevaCita() {
		return banderaNuevaCita;
	}
	public void setBanderaNuevaCita(Boolean banderaNuevaCita) {
		this.banderaNuevaCita = banderaNuevaCita;
	}

	public Integer getCodigoTipoCliente() {
		return codigoTipoCliente;
	}
	public void setCodigoTipoCliente(Integer codigoTipoCliente) {
		this.codigoTipoCliente = codigoTipoCliente;
	}
	public Boolean getBanderaFacturar() {
		return banderaFacturar;
	}
	public void setBanderaFacturar(Boolean banderaFacturar) {
		this.banderaFacturar = banderaFacturar;
	}
	
}

package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the cita_detalle database table.
 * 
 */
@Entity
@Table(name="cita_detalle")
@NamedQuery(name="CitaDetalle.findAll", query="SELECT c FROM CitaDetalle c")
public class CitaDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_detalle")
	private Integer idDetalle;

	private String estado;

	@Column(name="id_categoria")
	private Integer idCategoria;

	private BigDecimal precio;

	//bi-directional many-to-one association to CitaCabecera
	@ManyToOne
	@JoinColumn(name="id_cita")
	private CitaCabecera citaCabecera;

	//bi-directional many-to-one association to Servicio
	@ManyToOne
	@JoinColumn(name="id_servicio")
	private Servicio servicio;

	public CitaDetalle() {
	}

	public Integer getIdDetalle() {
		return this.idDetalle;
	}

	public void setIdDetalle(Integer idDetalle) {
		this.idDetalle = idDetalle;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getIdCategoria() {
		return this.idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public BigDecimal getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public CitaCabecera getCitaCabecera() {
		return this.citaCabecera;
	}

	public void setCitaCabecera(CitaCabecera citaCabecera) {
		this.citaCabecera = citaCabecera;
	}

	public Servicio getServicio() {
		return this.servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

}
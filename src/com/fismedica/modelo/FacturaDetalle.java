package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the factura_detalle database table.
 * 
 */
@Entity
@Table(name="factura_detalle")
@NamedQuery(name="FacturaDetalle.findAll", query="SELECT f FROM FacturaDetalle f")
public class FacturaDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_detalle")
	private Integer idDetalle;

	private Integer cantidad;

	private String estado;

	@Column(name="id_categoria")
	private Integer idCategoria;

	private BigDecimal precio;

	private BigDecimal total;

	//bi-directional many-to-one association to FacturaCabecera
	@ManyToOne
	@JoinColumn(name="id_factura")
	private FacturaCabecera facturaCabecera;

	//bi-directional many-to-one association to Servicio
	@ManyToOne
	@JoinColumn(name="id_servicio")
	private Servicio servicio;

	public FacturaDetalle() {
	}

	public Integer getIdDetalle() {
		return this.idDetalle;
	}

	public void setIdDetalle(Integer idDetalle) {
		this.idDetalle = idDetalle;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
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

	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public FacturaCabecera getFacturaCabecera() {
		return this.facturaCabecera;
	}

	public void setFacturaCabecera(FacturaCabecera facturaCabecera) {
		this.facturaCabecera = facturaCabecera;
	}

	public Servicio getServicio() {
		return this.servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

}
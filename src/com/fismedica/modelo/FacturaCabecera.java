package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the factura_cabecera database table.
 * 
 */
@Entity
@Table(name="factura_cabecera")
@NamedQuery(name="FacturaCabecera.buscarPorFecha", query="SELECT f FROM FacturaCabecera f where f.fechaEmision = :fecha and f.estado = 'A'")
public class FacturaCabecera implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_factura")
	private Integer idFactura;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_emision")
	private Date fechaEmision;

	@Column(name="id_cita")
	private Integer idCita;

	private BigDecimal iva;

	@Column(name="num_factura")
	private String numFactura;

	private BigDecimal subtotal;

	private BigDecimal total;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	//bi-directional many-to-one association to Medico
	@ManyToOne
	@JoinColumn(name="id_medico")
	private Medico medico;

	//bi-directional many-to-one association to FacturaDetalle
	@OneToMany(mappedBy="facturaCabecera",cascade = CascadeType.ALL)
	private List<FacturaDetalle> facturaDetalles;

	public FacturaCabecera() {
	}

	public Integer getIdFactura() {
		return this.idFactura;
	}

	public void setIdFactura(Integer idFactura) {
		this.idFactura = idFactura;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Integer getIdCita() {
		return this.idCita;
	}

	public void setIdCita(Integer idCita) {
		this.idCita = idCita;
	}

	public BigDecimal getIva() {
		return this.iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public String getNumFactura() {
		return this.numFactura;
	}

	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}

	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Medico getMedico() {
		return this.medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public List<FacturaDetalle> getFacturaDetalles() {
		return this.facturaDetalles;
	}

	public void setFacturaDetalles(List<FacturaDetalle> facturaDetalles) {
		this.facturaDetalles = facturaDetalles;
	}

	public FacturaDetalle addFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().add(facturaDetalle);
		facturaDetalle.setFacturaCabecera(this);

		return facturaDetalle;
	}

	public FacturaDetalle removeFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().remove(facturaDetalle);
		facturaDetalle.setFacturaCabecera(null);

		return facturaDetalle;
	}

}
package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the servicio database table.
 * 
 */
@Entity
@Table(name="servicio")
@NamedQueries({
	@NamedQuery(name="Servicio.findAll", query="SELECT s FROM Servicio s"),
	@NamedQuery(name="Servicio.buscarActivos", query="SELECT s FROM Servicio s where s.estado = 'A'"),
	@NamedQuery(name="Servicio.buscarPatron", query="SELECT s FROM Servicio s where s.estado = 'A' and lower(s.servicio) like lower(:patron)")
})
public class Servicio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_servicio")
	private Integer idServicio;

	@Column(name="duracion_minutos")
	private Integer duracionMinutos;

	private String estado;

	private String servicio;

	//bi-directional many-to-one association to CitaDetalle
	@OneToMany(mappedBy="servicio")
	private List<CitaDetalle> citaDetalles;

	//bi-directional many-to-one association to FacturaDetalle
	@OneToMany(mappedBy="servicio")
	private List<FacturaDetalle> facturaDetalles;

	//bi-directional many-to-one association to Precio
	@OneToMany(mappedBy="servicio")
	private List<Precio> precios;

	public Servicio() {
	}

	public Integer getIdServicio() {
		return this.idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public Integer getDuracionMinutos() {
		return this.duracionMinutos;
	}

	public void setDuracionMinutos(Integer duracionMinutos) {
		this.duracionMinutos = duracionMinutos;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getServicio() {
		return this.servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public List<CitaDetalle> getCitaDetalles() {
		return this.citaDetalles;
	}

	public void setCitaDetalles(List<CitaDetalle> citaDetalles) {
		this.citaDetalles = citaDetalles;
	}

	public CitaDetalle addCitaDetalle(CitaDetalle citaDetalle) {
		getCitaDetalles().add(citaDetalle);
		citaDetalle.setServicio(this);

		return citaDetalle;
	}

	public CitaDetalle removeCitaDetalle(CitaDetalle citaDetalle) {
		getCitaDetalles().remove(citaDetalle);
		citaDetalle.setServicio(null);

		return citaDetalle;
	}

	public List<FacturaDetalle> getFacturaDetalles() {
		return this.facturaDetalles;
	}

	public void setFacturaDetalles(List<FacturaDetalle> facturaDetalles) {
		this.facturaDetalles = facturaDetalles;
	}

	public FacturaDetalle addFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().add(facturaDetalle);
		facturaDetalle.setServicio(this);

		return facturaDetalle;
	}

	public FacturaDetalle removeFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().remove(facturaDetalle);
		facturaDetalle.setServicio(null);

		return facturaDetalle;
	}

	public List<Precio> getPrecios() {
		return this.precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

	public Precio addPrecio(Precio precio) {
		getPrecios().add(precio);
		precio.setServicio(this);

		return precio;
	}

	public Precio removePrecio(Precio precio) {
		getPrecios().remove(precio);
		precio.setServicio(null);

		return precio;
	}

	@Override
	public String toString() {
		return this.servicio;
	}
	
}
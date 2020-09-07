package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the precio database table.
 * 
 */
@Entity
@Table(name="precio")
@NamedQueries({
	@NamedQuery(name="Precio.findAll", query="SELECT p FROM Precio p where p.estado = 'A'"),
	@NamedQuery(name="Precio.buscarCategoriaActiva", query="SELECT p FROM Precio p where p.estado = 'A' and p.servicio.idServicio = :idServicio and p.categoria.idCategoria = :idCategoria"),
	@NamedQuery(name="Precio.buscarPatron", query="SELECT p FROM Precio p where p.estado = 'A' and lower(p.servicio.servicio) like lower(:patron)"),
	@NamedQuery(name="Precio.buscarServicioCategoria", query="SELECT p FROM Precio p where p.categoria.idCategoria = :idCategoria and p.servicio.idServicio = :idServicio and p.estado = 'A'")
})
public class Precio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_precio")
	private Integer idPrecio;

	private String estado;

	private BigDecimal precio;

	@Column(name="tiempo_minutos")
	private Integer tiempoMinutos;
	
	//bi-directional many-to-one association to Categoria
	@ManyToOne
	@JoinColumn(name="id_categoria")
	private Categoria categoria;

	//bi-directional many-to-one association to Servicio
	@ManyToOne
	@JoinColumn(name="id_servicio")
	private Servicio servicio;

	public Precio() {
	}

	public Integer getIdPrecio() {
		return this.idPrecio;
	}

	public void setIdPrecio(Integer idPrecio) {
		this.idPrecio = idPrecio;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public BigDecimal getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Servicio getServicio() {
		return this.servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Integer getTiempoMinutos() {
		return tiempoMinutos;
	}

	public void setTiempoMinutos(Integer tiempoMinutos) {
		this.tiempoMinutos = tiempoMinutos;
	}

}
package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the categoria database table.
 * 
 */
@Entity
@Table(name="categoria")
@NamedQueries({
	@NamedQuery(name="Categoria.findAll", query="SELECT c FROM Categoria c"),
	@NamedQuery(name="Categoria.buscarActivos", query="SELECT c FROM Categoria c where c.estado = 'A'"),
	@NamedQuery(name="Categoria.buscarPorId", query="SELECT c FROM Categoria c where c.estado = 'A' and c.idCategoria = :idCategoria")
})
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_categoria")
	private Integer idCategoria;

	private String categoria;

	private String estado;

	//bi-directional many-to-one association to Precio
	@OneToMany(mappedBy="categoria")
	private List<Precio> precios;

	public Categoria() {
	}

	public Integer getIdCategoria() {
		return this.idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Precio> getPrecios() {
		return this.precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

	public Precio addPrecio(Precio precio) {
		getPrecios().add(precio);
		precio.setCategoria(this);

		return precio;
	}

	public Precio removePrecio(Precio precio) {
		getPrecios().remove(precio);
		precio.setCategoria(null);

		return precio;
	}

	@Override
	public String toString() {
		return this.categoria;
	}
	
}
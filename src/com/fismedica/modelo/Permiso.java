package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the permiso database table.
 * 
 */
@Entity
@Table(name="permiso")
@NamedQueries({
	@NamedQuery(name="Permiso.findAll", query="SELECT s FROM Permiso s"),
	@NamedQuery(name="Permiso.buscarPermisoPerfil", query="SELECT s FROM Permiso s "
			+ "where s.perfil.idPerfil = (:patron) and s.estado = 'A' and s.menu.estado = 'A' ORDER BY s.menu.posicion"),
	@NamedQuery(name="Permiso.buscarPermiso", query="SELECT s FROM Permiso s "
			+ "where s.perfil.idPerfil = (:patron) and s.estado = 'A' and s.menu.idMenuPadre <> 0")
})
public class Permiso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_permiso")
	private Integer idPermiso;

	private String estado;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="id_menu")
	private Menu menu;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	public Permiso() {
	}

	public Integer getIdPermiso() {
		return this.idPermiso;
	}

	public void setIdPermiso(Integer idPermiso) {
		this.idPermiso = idPermiso;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}
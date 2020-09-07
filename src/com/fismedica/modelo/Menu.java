package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name="menu")
@NamedQueries({
	@NamedQuery(name="Menu.findAll", query="SELECT s FROM Menu s ORDER BY s.posicion"),
	@NamedQuery(name="Menu.buscarMenu", query="SELECT s FROM Menu s where s.idMenuPadre <> 0 ORDER BY s.idMenuPadre"),
	@NamedQuery(name="Menu.BuscarPadre", query="SELECT s FROM Menu s where s.idMenuPadre = 0"),
	@NamedQuery(name="Menu.BuscarPadrePorId", query="SELECT s FROM Menu s where (s.idMenu = :patron) ORDER BY s.posicion")
})
public class Menu implements Serializable, Comparable<Menu> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_menu")
	private Integer idMenu;

	private String descripcion;

	private String estado;

	private String fxml;

	@Column(name="fxml_asociado")
	private String fxmlAsociado;

	private String icono;

	@Column(name="id_menu_padre")
	private Integer idMenuPadre;

	private Integer posicion;

	//bi-directional many-to-one association to Permiso
	@OneToMany(mappedBy="menu")
	private List<Permiso> permisos;

	public Menu() {
	}

	public Integer getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFxml() {
		return this.fxml;
	}

	public void setFxml(String fxml) {
		this.fxml = fxml;
	}

	public String getFxmlAsociado() {
		return this.fxmlAsociado;
	}

	public void setFxmlAsociado(String fxmlAsociado) {
		this.fxmlAsociado = fxmlAsociado;
	}

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public Integer getIdMenuPadre() {
		return this.idMenuPadre;
	}

	public void setIdMenuPadre(Integer idMenuPadre) {
		this.idMenuPadre = idMenuPadre;
	}

	public Integer getPosicion() {
		return this.posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public List<Permiso> getPermisos() {
		return this.permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

	public Permiso addPermiso(Permiso permiso) {
		getPermisos().add(permiso);
		permiso.setMenu(this);

		return permiso;
	}

	public Permiso removePermiso(Permiso permiso) {
		getPermisos().remove(permiso);
		permiso.setMenu(null);

		return permiso;
	}
	@Override
    public int compareTo(Menu o) {
        if (this.posicion < o.posicion) {
            return -1;
        }
        if (this.posicion  > o.posicion) {
            return 1;
        }
        return 0;
    }
}
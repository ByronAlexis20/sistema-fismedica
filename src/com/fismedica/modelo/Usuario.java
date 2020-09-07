package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u where u.perfil.idPerfil <> 3"),
	@NamedQuery(name="Usuario.buscarUsuario", query="SELECT u FROM Usuario u WHERE "
			+ "u.usuario = (:usu) and u.clave = (:cla) and u.estado = 'A'"),
	@NamedQuery(name="Usuario.validarUsuario", query="SELECT u FROM Usuario u "
			+ "WHERE u.usuario = (:usuario) AND u.idUsuario <> (:idUsuario) and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorRol", query="SELECT u FROM Usuario u WHERE u.perfil.idPerfil = :idRol and u.estado = 'A'"),
	@NamedQuery(name="Usuario.recuperaUsuario", query="SELECT u FROM Usuario u WHERE u.cedula = (:cedula) and u.estado = 'A'"),
	@NamedQuery(name="Usuario.recuperaUsuarioPorId", query="SELECT u FROM Usuario u WHERE u.idUsuario = (:idUsuario) and u.estado = 'A'"),
})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;

	private String apellido;

	private String cedula;

	private String clave;

	private String direccion;

	private String estado;

	private byte[] foto;

	private String nombre;

	private String telefono;

	private String usuario;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	public Usuario() {
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}
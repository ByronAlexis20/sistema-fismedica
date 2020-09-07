package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Table(name="cliente")
@NamedQueries({
	@NamedQuery(name="Cliente.buscarPatron", query="SELECT c FROM Cliente c "
			+ "WHERE (lower(c.nombre) like lower(:patron) or lower(c.apellido) like lower(:patron)) and c.estado = 'A'"),
	@NamedQuery(name="Cliente.buscarPorCedula", query="SELECT c FROM Cliente c WHERE c.cedula = (:cedula) and c.estado = 'A'")
})
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cliente")
	private Integer idCliente;

	private String apellido;

	private String cedula;

	private String direccion;

	private String email;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_modifica")
	private Date fechaModifica;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	private String nombre;

	private String telefono;

	@Column(name="usuario_crea")
	private String usuarioCrea;

	@Column(name="usuario_modifica")
	private String usuarioModifica;

	//bi-directional many-to-one association to FacturaCabecera
	@OneToMany(mappedBy="cliente")
	private List<FacturaCabecera> facturaCabeceras;

	//bi-directional many-to-one association to FacturaCabecera
	@OneToMany(mappedBy="cliente")
	private List<CitaCabecera> citaCabeceras;

	public Cliente() {
	}

	public Integer getIdCliente() {
		return this.idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
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

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
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

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public List<FacturaCabecera> getFacturaCabeceras() {
		return this.facturaCabeceras;
	}

	public void setFacturaCabeceras(List<FacturaCabecera> facturaCabeceras) {
		this.facturaCabeceras = facturaCabeceras;
	}

	public FacturaCabecera addFacturaCabecera(FacturaCabecera facturaCabecera) {
		getFacturaCabeceras().add(facturaCabecera);
		facturaCabecera.setCliente(this);

		return facturaCabecera;
	}

	public FacturaCabecera removeFacturaCabecera(FacturaCabecera facturaCabecera) {
		getFacturaCabeceras().remove(facturaCabecera);
		facturaCabecera.setCliente(null);

		return facturaCabecera;
	}

	public List<CitaCabecera> getCitaCabeceras() {
		return citaCabeceras;
	}

	public void setCitaCabeceras(List<CitaCabecera> citaCabeceras) {
		this.citaCabeceras = citaCabeceras;
	}

	public CitaCabecera addCitaCabecera(CitaCabecera facturaCabecera) {
		getCitaCabeceras().add(facturaCabecera);
		facturaCabecera.setCliente(this);

		return facturaCabecera;
	}

	public CitaCabecera removeCitaCabecera(CitaCabecera facturaCabecera) {
		getCitaCabeceras().remove(facturaCabecera);
		facturaCabecera.setCliente(null);

		return facturaCabecera;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

}
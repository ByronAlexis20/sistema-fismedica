package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the medico database table.
 * 
 */
@Entity
@Table(name="medico")
@NamedQueries({
	@NamedQuery(name="Medico.buscarPatron", query="SELECT m FROM Medico m "
			+ "WHERE (lower(m.nombre) like lower(:patron) or lower(m.apellido) like lower(:patron)) and m.estado = 'A'"),
	@NamedQuery(name="Medico.buscarPorCedula", query="SELECT m FROM Medico m WHERE m.cedula = (:cedula) and m.estado = 'A'"),
	@NamedQuery(name="Medico.buscarPorUsuario", query="SELECT m FROM Medico m "
			+ "WHERE (lower(m.nombre) like lower(:patron) or lower(m.apellido) like lower(:patron)) and m.estado = 'A' order by m.idUsuario"),
	@NamedQuery(name="Medico.buscarPorId", query="SELECT m FROM Medico m WHERE m.idMedico = :idMedico and m.estado = 'A'"),
	@NamedQuery(name="Medico.buscarPorIdUsuario", query="SELECT m FROM Medico m WHERE m.idUsuario = :idUsuario and m.estado = 'A'")
})
public class Medico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_medico")
	private Integer idMedico;

	private String apellido;

	
	@Column(name="id_usuario")
	private Integer idUsuario;
	
	
	private String cedula;

	private String especialidad;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	private String nombre;

	private String titulo;

	//bi-directional many-to-one association to CitaCabecera
	@OneToMany(mappedBy="medico")
	private List<CitaCabecera> citaCabeceras;

	//bi-directional many-to-one association to FacturaCabecera
	@OneToMany(mappedBy="medico")
	private List<FacturaCabecera> facturaCabeceras;

	public Medico() {
	}

	public Integer getIdMedico() {
		return this.idMedico;
	}

	public void setIdMedico(Integer idMedico) {
		this.idMedico = idMedico;
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

	public String getEspecialidad() {
		return this.especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<CitaCabecera> getCitaCabeceras() {
		return this.citaCabeceras;
	}

	public void setCitaCabeceras(List<CitaCabecera> citaCabeceras) {
		this.citaCabeceras = citaCabeceras;
	}

	public CitaCabecera addCitaCabecera(CitaCabecera citaCabecera) {
		getCitaCabeceras().add(citaCabecera);
		citaCabecera.setMedico(this);

		return citaCabecera;
	}

	public CitaCabecera removeCitaCabecera(CitaCabecera citaCabecera) {
		getCitaCabeceras().remove(citaCabecera);
		citaCabecera.setMedico(null);

		return citaCabecera;
	}

	public List<FacturaCabecera> getFacturaCabeceras() {
		return this.facturaCabeceras;
	}

	public void setFacturaCabeceras(List<FacturaCabecera> facturaCabeceras) {
		this.facturaCabeceras = facturaCabeceras;
	}

	public FacturaCabecera addFacturaCabecera(FacturaCabecera facturaCabecera) {
		getFacturaCabeceras().add(facturaCabecera);
		facturaCabecera.setMedico(this);

		return facturaCabecera;
	}

	public FacturaCabecera removeFacturaCabecera(FacturaCabecera facturaCabecera) {
		getFacturaCabeceras().remove(facturaCabecera);
		facturaCabecera.setMedico(null);

		return facturaCabecera;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
}
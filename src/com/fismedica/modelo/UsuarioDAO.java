package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class UsuarioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuario(String usuario,String clave) {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.buscarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usu", usuario);
		query.setParameter("cla", clave);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getValidarUsuario(String usuario,int idUsuario) {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.validarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usuario", usuario);
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuariosPorRol(Integer idRol) {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPorRol");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idRol", idRol);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	//para recuperar usuario
	@SuppressWarnings("unchecked")
	public List<Usuario> getRecuperaUsuario(String cedula){
		List<Usuario> resultado = new ArrayList<Usuario>();
		Query query = getEntityManager().createNamedQuery("Usuario.recuperaUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getRecuperaUsuarioPorId(Integer idUsuario){
		List<Usuario> resultado = new ArrayList<Usuario>();
		Query query = getEntityManager().createNamedQuery("Usuario.recuperaUsuarioPorId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
}

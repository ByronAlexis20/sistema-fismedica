package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;


public class PerfilDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Perfil> getListaPerfil(){
		List<Perfil> resultado = new ArrayList<Perfil>();
		Query query = getEntityManager().createNamedQuery("Perfil.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Perfil> getAllListaPerfil(){
		List<Perfil> resultado = new ArrayList<Perfil>();
		Query query = getEntityManager().createNamedQuery("Perfil.findAllPerfiles");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfilById(int codigo){
		List<Perfil> resultado = new ArrayList<Perfil>();
		Query query = getEntityManager().createNamedQuery("Perfil.findPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfil", codigo);
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Perfil> getUltimoPerfil(){
		List<Perfil> resultado = new ArrayList<Perfil>();
		Query query = getEntityManager().createNamedQuery("Perfil.BuscarUltimoPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
}

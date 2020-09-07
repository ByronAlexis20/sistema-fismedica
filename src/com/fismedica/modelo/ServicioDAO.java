package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ServicioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Servicio> getListaServiciosTodos(){
		List<Servicio> resultado = new ArrayList<Servicio>();
		Query query = getEntityManager().createNamedQuery("Servicio.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Servicio>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Servicio> getListaServiciosActivos(){
		List<Servicio> resultado = new ArrayList<Servicio>();
		Query query = getEntityManager().createNamedQuery("Servicio.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Servicio>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Servicio> getListaServicios(String patron){
		List<Servicio> resultado = new ArrayList<Servicio>();
		Query query = getEntityManager().createNamedQuery("Servicio.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Servicio>) query.getResultList();
		return resultado;
	}
}

package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class PrecioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Precio> getListaPrecios(){
		List<Precio> resultado = new ArrayList<Precio>();
		Query query = getEntityManager().createNamedQuery("Precio.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Precio>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Precio> getListaPreciosBusqueda(Integer idServicio, Integer idCategoria){
		List<Precio> resultado = new ArrayList<Precio>();
		Query query = getEntityManager().createNamedQuery("Precio.buscarServicioCategoria");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idServicio", idServicio);
		query.setParameter("idCategoria", idCategoria);
		resultado = (List<Precio>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Precio> getListaServicios(String patron){
		List<Precio> resultado = new ArrayList<Precio>();
		Query query = getEntityManager().createNamedQuery("Precio.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Precio>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Precio> gePrecioActivo(Integer idServicio, Integer idCategoria){
		List<Precio> resultado = new ArrayList<Precio>();
		Query query = getEntityManager().createNamedQuery("Precio.buscarCategoriaActiva");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idServicio", idServicio);
		query.setParameter("idCategoria", idCategoria);
		resultado = (List<Precio>) query.getResultList();
		return resultado;
	}
}

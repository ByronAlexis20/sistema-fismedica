package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class CategoriaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Categoria> getListaCategoriaTodos(){
		List<Categoria> resultado = new ArrayList<Categoria>();
		Query query = getEntityManager().createNamedQuery("Categoria.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Categoria> getListaCategoriaActivos(){
		List<Categoria> resultado = new ArrayList<Categoria>();
		Query query = getEntityManager().createNamedQuery("Categoria.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Categoria> getListaCategiaById(Integer idCategoria){
		List<Categoria> resultado = new ArrayList<Categoria>();
		Query query = getEntityManager().createNamedQuery("Categoria.buscarPorId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCategoria", idCategoria);
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
}

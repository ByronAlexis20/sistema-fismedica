package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MenuDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Menu> getListaMenu(){
		List<Menu> resultado = new ArrayList<Menu>();
		Query query = getEntityManager().createNamedQuery("Menu.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Menu>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getListaMenuAccesos(){
		List<Menu> resultado = new ArrayList<Menu>();
		Query query = getEntityManager().createNamedQuery("Menu.buscarMenu");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Menu>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getMenuPadre(){
		List<Menu> resultado = new ArrayList<Menu>();
		Query query = getEntityManager().createNamedQuery("Menu.BuscarPadre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Menu>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getMenuPadreByIdMenu(Integer idMenu){
		List<Menu> resultado = new ArrayList<Menu>();
		Query query = getEntityManager().createNamedQuery("Menu.BuscarPadrePorId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", idMenu);
		resultado = (List<Menu>) query.getResultList();
		return resultado;
	}

}

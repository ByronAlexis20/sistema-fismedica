package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class CitaCabeceraDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getListaCitas(String patron){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getCitaById(Integer idCita){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCita", idCita);
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getCitasPorFechaMedico(Date fecha,Integer idMedico){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarPorFecha");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		query.setParameter("idMedico", idMedico);
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getCitasPorNumero(Date fecha,Integer idMedico){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarCitasPorNumero");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		query.setParameter("idMedico", idMedico);
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getCitasPorNumeroHora(Date fecha,Integer idMedico){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarCitasPorNumeroHora");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		query.setParameter("idMedico", idMedico);
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getCitasPorFecha(Date fecha){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarPorFechaSeleccionado");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getCitasPorDia(Date fecha){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarPorDia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CitaCabecera> getCitasPorDiaMedico(Date fecha,Integer idMedico){
		List<CitaCabecera> resultado = new ArrayList<CitaCabecera>();
		Query query = getEntityManager().createNamedQuery("CitaCabecera.buscarPorDiaMedico");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		query.setParameter("idMedico", idMedico);
		resultado = (List<CitaCabecera>) query.getResultList();
		return resultado;
	}
}

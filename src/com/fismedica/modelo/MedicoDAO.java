package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MedicoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Medico> getListaMedicos(String patron){
		List<Medico> resultado = new ArrayList<Medico>();
		Query query = getEntityManager().createNamedQuery("Medico.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Medico>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Medico> getBuscarPorCedula(String cedula){
		List<Medico> resultado = new ArrayList<Medico>();
		Query query = getEntityManager().createNamedQuery("Medico.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Medico>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Medico> getListaMedicosPorUsuario(String patron){
		List<Medico> resultado = new ArrayList<Medico>();
		Query query = getEntityManager().createNamedQuery("Medico.buscarPorUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Medico>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Medico> getListaMedicosPorId(Integer idMedico){
		List<Medico> resultado = new ArrayList<Medico>();
		Query query = getEntityManager().createNamedQuery("Medico.buscarPorId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idMedico", idMedico);
		resultado = (List<Medico>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Medico> getListaMedicosPorIdUsuario(Integer idUsuario){
		List<Medico> resultado = new ArrayList<Medico>();
		Query query = getEntityManager().createNamedQuery("Medico.buscarPorIdUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<Medico>) query.getResultList();
		return resultado;
	}
}

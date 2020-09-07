package com.fismedica.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class FacturaCabeceraDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<FacturaCabecera> getFacturaPorFecha(Date fecha){
		List<FacturaCabecera> resultado = new ArrayList<FacturaCabecera>();
		Query query = getEntityManager().createNamedQuery("FacturaCabecera.buscarPorFecha");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		resultado = (List<FacturaCabecera>) query.getResultList();
		return resultado;
	}
}

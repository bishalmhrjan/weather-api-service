package com.weatherforcast.api.clientapp;

import com.weatherapi.forecast.common.ClientApp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientAppRepository extends CrudRepository<ClientApp, Integer> {

	@Query("SELECT c FROM ClientApp c WHERE c.clientId = ?1 AND c.enabled = true AND c.trashed = false")
	public Optional<ClientApp> findByClientId(String clientId);
}

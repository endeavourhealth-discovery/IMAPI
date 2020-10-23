package com.endavourhealth.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.endavourhealth.services.perspective.models.Perspective;

@Component
@ConfigurationProperties(prefix="info-model", ignoreUnknownFields = false)
public class InfoModelConfig {

	List<Perspective> perspectives = new ArrayList<Perspective>();
	
	@Bean
	public List<Perspective> getPerspectives() {
		return perspectives;
	}

	public void setPerspectives(List<Perspective> perspectives) {
		this.perspectives = perspectives;
	}
	
}

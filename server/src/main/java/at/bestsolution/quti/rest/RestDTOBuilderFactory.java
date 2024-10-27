package at.bestsolution.quti.rest;

import at.bestsolution.quti.service.DTOBuilderFactory;
import jakarta.inject.Singleton;

@Singleton
public class RestDTOBuilderFactory implements DTOBuilderFactory {

	@Override
	public <T extends at.bestsolution.quti.service.dto.BaseDTO.Builder> T builder(Class<T> type) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'builder'");
	}

}

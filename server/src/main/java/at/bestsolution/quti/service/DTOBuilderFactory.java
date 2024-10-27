package at.bestsolution.quti.service;

import at.bestsolution.quti.service.dto.BaseDTO;

public interface DTOBuilderFactory {
	public interface Builder {
		public BaseDTO build();
	}

	public <T extends BaseDTO.Builder> T builder(Class<T> type);
}

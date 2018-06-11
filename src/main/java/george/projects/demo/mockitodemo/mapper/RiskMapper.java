package george.projects.demo.mockitodemo.mapper;

import george.projects.demo.mockitodemo.dto.RiskDto;
import george.projects.demo.mockitodemo.model.RiskModel;
import george.projects.demo.mockitodemo.model.RiskType;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RiskMapper {

	public RiskDto modelToDto(RiskModel model) {
		RiskDto dto = new RiskDto(model.getRiskType(), model.getAssociatedTo());
		int riskSeverity = RiskType.MAJOR_RISK.equals(model.getRiskType()) ? 100 : 0;
		dto.setRiskSeverity(riskSeverity);
		return dto;
	}

	public RiskDto modelToDtoSimulatingMapperImplChanges(RiskModel model) {
		boolean isInitialImplementation = new Random().nextBoolean();
		RiskDto dto = new RiskDto(model.getRiskType(), model.getAssociatedTo());

		int riskSeverity;
		if (isInitialImplementation) {
			riskSeverity = RiskType.MAJOR_RISK.equals(model.getRiskType()) ? 100 : 0;
		} else {
			riskSeverity = RiskType.MAJOR_RISK.equals(model.getRiskType()) ? 45 : 1;
		}

		dto.setRiskSeverity(riskSeverity);
		return dto;
	}
}

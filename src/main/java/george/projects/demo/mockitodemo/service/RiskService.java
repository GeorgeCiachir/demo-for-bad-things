package george.projects.demo.mockitodemo.service;

import george.projects.demo.mockitodemo.dto.RiskDto;
import george.projects.demo.mockitodemo.mapper.RiskMapper;
import george.projects.demo.mockitodemo.model.RiskModel;
import george.projects.demo.mockitodemo.model.RiskType;
import george.projects.demo.mockitodemo.repository.RiskRepository;
import george.projects.demo.mockitodemo.someExternalApacheLibrary.AResult;
import george.projects.demo.mockitodemo.someExternalApacheLibrary.SomeExternalServiceFromApacheLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static george.projects.demo.mockitodemo.model.RiskType.MAJOR_RISK;
import static george.projects.demo.mockitodemo.model.RiskType.OTHER_RISK_TYPE;

@Service
public class RiskService {

	private List<String> cache = new ArrayList<>();
	private List<RiskType> events = new ArrayList<>();

	@Autowired
	private RiskMapper riskMapper;

	@Autowired
	private RiskRepository riskRepository;

	@Autowired
	private RiskCalculator riskCalculator;

	@Autowired
	private ServiceThatIsNotMockedInTests serviceThatIsNotMockedInTests;

	@Autowired
	private ServiceThatUsesAResult serviceThatUsesAResult;

	public RiskDto getRiskDtoById(long id) {
		RiskModel model = riskRepository.getById(id);
		return riskMapper.modelToDto(model);
	}

	/**
	 * Simulates changes in the Mapper implementation
	 */
	public RiskDto getRiskDtoByIdSimulateMapperImplChanges(long id) {
		RiskModel model = riskRepository.getById(id);
		return riskMapper.modelToDtoSimulatingMapperImplChanges(model);
	}

	public void doSomethingAndAddToCache() {
		cache.add(MAJOR_RISK.name());
	}

	public RiskType calculateRiskUsingExternalLibrary(int input) {
		AResult result = SomeExternalServiceFromApacheLibrary.getAResult(input);
		return riskCalculator.calculateRisk(result);
	}

	public RiskType calculateRisk(int riskFactor) {
		return calculateRiskInternally(riskFactor);
	}

	/**
	 * This method can be mocked in the tests if a @Spy is used for RiskService
	 */
	public RiskType calculateRiskInternally(int riskFactor) {
		return riskCalculator.calculateRisk(riskFactor);
	}

	public boolean performSomeLogicBasedOnAnInput(int input) {
		boolean actionPerformed;

		AResult result = serviceThatIsNotMockedInTests.performSomeLogic(input);
		RiskType riskType = serviceThatUsesAResult.performSomeLogic(result);

		if (MAJOR_RISK.equals(riskType)) {
			events.add(MAJOR_RISK);
			actionPerformed = true;
		} else if (RiskType.MINOR_RISK.equals(riskType)) {
			//do nothing
			actionPerformed = false;
		} else {
			events.add(OTHER_RISK_TYPE);
			actionPerformed = true;
		}
		return actionPerformed;
	}


	public List<String> getCache() {
		return cache;
	}

	public List<RiskType> getEvents() {
		return events;
	}
}
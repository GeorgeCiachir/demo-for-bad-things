package george.projects.demo.mockitodemo.service;

import george.projects.demo.mockitodemo.model.RiskType;
import george.projects.demo.mockitodemo.someExternalApacheLibrary.AResult;
import org.springframework.stereotype.Service;

import static george.projects.demo.mockitodemo.model.RiskType.MAJOR_RISK;
import static george.projects.demo.mockitodemo.model.RiskType.MINOR_RISK;

@Service
public class RiskCalculator {

	public RiskType calculateRisk(AResult aResult) {
		return aResult.getValue() == 0 ? MINOR_RISK : MAJOR_RISK;
	}

	public RiskType calculateRisk(int riskFactor) {
		return riskFactor == 0 ? MINOR_RISK : MAJOR_RISK;
	}
}

package george.projects.demo.mockitodemo.service;

import george.projects.demo.mockitodemo.model.RiskType;
import george.projects.demo.mockitodemo.someExternalApacheLibrary.AResult;
import org.springframework.stereotype.Service;

import static george.projects.demo.mockitodemo.model.RiskType.*;

@Service
public class ServiceThatUsesAResult {

	public RiskType performSomeLogic(AResult result) {
		if (result.getValue() == 0) {
			return MINOR_RISK;
		} else if (result.getValue() == 100) {
			return MAJOR_RISK;
		} else {
			return OTHER_RISK_TYPE;
		}
	}
}

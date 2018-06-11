package george.projects.demo.mockitodemo.controller;

import george.projects.demo.mockitodemo.dto.RiskDto;
import george.projects.demo.mockitodemo.model.RiskType;
import george.projects.demo.mockitodemo.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RiskController {

	@Autowired
	private RiskService riskService;

	@ResponseBody
	@RequestMapping("/getRiskDto/{id}")
	public RiskDto getRiskDtoById(@PathVariable long id) {
		return riskService.getRiskDtoById(id);
	}

	@ResponseBody
	@RequestMapping("/calculateRisk/{riskFactor}")
	public RiskType calculateRisk(@PathVariable int riskFactor) {
		return riskService.calculateRisk(riskFactor);
	}
}

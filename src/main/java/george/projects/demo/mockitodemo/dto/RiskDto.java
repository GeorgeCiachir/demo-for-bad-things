package george.projects.demo.mockitodemo.dto;

import george.projects.demo.mockitodemo.model.RiskType;

public class RiskDto {

	private RiskType riskType;
	private int riskSeverity;
	private String associatedTo;

	public RiskDto(RiskType riskType, String associatedTo) {
		this.riskType = riskType;
		this.associatedTo = associatedTo;
	}

	public RiskType getRiskType() {
		return riskType;
	}

	public void setRiskType(RiskType riskType) {
		this.riskType = riskType;
	}

	public int getRiskSeverity() {
		return riskSeverity;
	}

	public void setRiskSeverity(int riskSeverity) {
		this.riskSeverity = riskSeverity;
	}

	public String getAssociatedTo() {
		return associatedTo;
	}

	public void setAssociatedTo(String associatedTo) {
		this.associatedTo = associatedTo;
	}
}

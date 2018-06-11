package george.projects.demo.mockitodemo.model;

public class RiskModel {

	private RiskType riskType;
	private String associatedTo; //some object

	public RiskModel(RiskType riskType, String associatedTo) {
		this.riskType = riskType;
		this.associatedTo = associatedTo;
	}

	public RiskType getRiskType() {
		return riskType;
	}

	public void setRiskType(RiskType riskType) {
		this.riskType = riskType;
	}

	public String getAssociatedTo() {
		return associatedTo;
	}

	public void setAssociatedTo(String associatedTo) {
		this.associatedTo = associatedTo;
	}
}

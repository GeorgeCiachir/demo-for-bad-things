package george.projects.demo.mockitodemo.someExternalApacheLibrary;

public class AResult {

	private int value;
	private boolean isCreatedByTransformation;

	public AResult(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isCreatedByTransformation() {
		return isCreatedByTransformation;
	}

	public void setCreatedByTransformation(boolean createdByTransformation) {
		this.isCreatedByTransformation = createdByTransformation;
	}
}
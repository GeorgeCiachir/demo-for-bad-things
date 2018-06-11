package george.projects.demo.mockitodemo.someExternalApacheLibrary;

public class SomeExternalServiceFromApacheLibrary {

	public static AResult getAResult(int input) {
		return new AResult(input);
	}
}
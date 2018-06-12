package george.projects.demo.mockitodemo.service;

import george.projects.demo.mockitodemo.someExternalApacheLibrary.AResult;
import org.springframework.stereotype.Service;

@Service
public class ServiceThatIsNotMockedInTests {

	public AResult transform(int input) {
		// Ths isCreatedByTransformation should be set to true, but we might forget,
		// or at the point of creating this object, that attribute did not exist
		// It might be created when the new method below is created, as a new business requirement
		return input == 0 ? new AResult(0) : new AResult(100);
	}


	public AResult transformAgain(AResult received) {
		int oldValue = received.getValue();
		int newValue;

		if (received.isCreatedByTransformation()) {
			newValue = oldValue;
		} else {
			newValue = oldValue - 15;
		}

		// if the isCreatedByTransformation has not been previously set to true
		// this mow returns AResult(85)
		return new AResult(newValue);
	}
}

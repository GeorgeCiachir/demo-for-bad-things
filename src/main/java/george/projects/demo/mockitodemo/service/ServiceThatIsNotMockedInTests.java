package george.projects.demo.mockitodemo.service;

import george.projects.demo.mockitodemo.someExternalApacheLibrary.AResult;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ServiceThatIsNotMockedInTests {

	public AResult performSomeLogic(int input) {
		return simulateImplementationChange(input);
	}

	private AResult simulateImplementationChange(int value) {
		boolean isOriginalImplementation = new Random().nextBoolean();
		return isOriginalImplementation ? theOriginalImpl(value) : theNewImpl(value);
	}

	/**
	 * The original implementation was returning only results that contain 0 or 100
	 */
	private static AResult theOriginalImpl(int value) {
		return value == 0 ? new AResult(0) : new AResult(100);
	}

	/**
	 * The new implementation also produces other results
	 */
	private static AResult theNewImpl(int value) {
		if (value == 0) {
			return new AResult(0);
		} else if (value == 100) {
			return new AResult(100);
		} else {
			return new AResult(value); //this has changed.
		}
	}
}

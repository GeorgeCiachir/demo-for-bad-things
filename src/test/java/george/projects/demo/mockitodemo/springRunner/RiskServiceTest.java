package george.projects.demo.mockitodemo.springRunner;

import george.projects.demo.mockitodemo.dto.RiskDto;
import george.projects.demo.mockitodemo.mapper.RiskMapper;
import george.projects.demo.mockitodemo.model.RiskModel;
import george.projects.demo.mockitodemo.model.RiskType;
import george.projects.demo.mockitodemo.repository.RiskRepository;
import george.projects.demo.mockitodemo.service.RiskCalculator;
import george.projects.demo.mockitodemo.service.RiskService;
import george.projects.demo.mockitodemo.service.ServiceThatIsNotMockedInTests;
import george.projects.demo.mockitodemo.service.ServiceThatUsesAResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static george.projects.demo.mockitodemo.model.RiskType.MAJOR_RISK;
import static george.projects.demo.mockitodemo.model.RiskType.OTHER_RISK_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * The {@link RiskMapper} and {@link RiskCalculator} dependencies are
 * autowired by Spring into the RiskService
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RiskServiceTest {

	@Autowired
	private RiskService riskService;

	@Mock
	private RiskRepository riskRepository;

	@Mock
	private ServiceThatUsesAResult serviceThatUsesAResult;

	/*
	 ********************************************
	 * WHY WE SHOULD NOT USE THE SPRING CONTEXT *
	 ********************************************
	 */

	/**
	 * Evidently, this has to be updated each time the name of the attribute inside the RiskService
	 */
	@Before
	public void init() {
		initMocks(this);
		ReflectionTestUtils.setField(riskService, "riskRepository", riskRepository);
		ReflectionTestUtils.setField(riskService, "serviceThatUsesAResult", serviceThatUsesAResult);
	}

	/**
	 * Reason 1 -> happy case -> when the implementation is not yet changed
	 * <p>
	 * It uses the actual result of the RiskMapper, thus forcing tight coupling
	 */
	@Test
	public void shouldGetRiskDtoById() {
		//Given
		long id = 15L;
		RiskModel retrievedModel = new RiskModel(MAJOR_RISK, "some value");
		when(riskRepository.getById(id)).thenReturn(retrievedModel);
		int expectedRiskSeverity = 100;

		//When
		RiskDto result = riskService.getRiskDtoById(id);

		//Then
		assertThat(result.getRiskSeverity()).isEqualTo(expectedRiskSeverity);
	}

	/**
	 * Reason 1 -> bad case -> when the implementation might change
	 * <p>
	 * Depending on the implementation, the RiskMapper will produce a different value
	 *
	 * As a result, this test sometimes passes and sometimes fails, because it relies
	 * on a specific implementation of the RiskMapper
	 */
	@Test
	public void shouldGetRiskDtoByIdEvenIfTheMapperImplIsChanged() {
		//Given
		long id = 15L;
		RiskModel retrievedModel = new RiskModel(MAJOR_RISK, "some value");
		when(riskRepository.getById(id)).thenReturn(retrievedModel);
		int expectedRiskSeverity = 100;

		//When
		RiskDto result = riskService.getRiskDtoByIdSimulateMapperImplChanges(id);

		//Then
		assertThat(result.getRiskSeverity()).isEqualTo(expectedRiskSeverity);
	}

	/**
	 * Example for Reason 2 -> really bad case
	 *
	 * Do not abuse usage of {@link org.mockito.Mockito#any}
	 *
	 * The test will always pass, but only by accident and the side effects are problematic
	 *
	 * The problem is that the test relies on the original implementation of the {@link ServiceThatIsNotMockedInTests}
	 * In a newer version of this service's implementation, it will not return results that contain only 0 and 100
	 * but other values as well
	 *
	 * Another problem is that the changes in the {@link ServiceThatIsNotMockedInTests} will be reflected in its own tests,
	 * but as long as this one still passes, it will probably not be updated
	 * Imagine that the same any() matcher could be applied in the IT test.
	 * It could mask all the changes and the result of the application will be completely different than the one expected
	 *
	 */
	@Test
	public void willGiveAFalsePositiveAndTheTestWillPassEvenIfItShouldNot() {
		//Given
		int input = 15;
		// in the original implementation of the ServiceThatIsNotMockedInTests, if the
		// input was not 0, it always produced a result that was wrapping the value 100

		// based on the input, the new implementation returns a result that contains 15,
		// but the any() matcher masks the execution path in the tested method
		when(serviceThatUsesAResult.performSomeLogic(any())).thenReturn(MAJOR_RISK);

		//When
		boolean result = riskService.performSomeLogicBasedOnAnInput(input);

		//Then
		assertThat(result).isTrue();
		System.out.println(riskService.getEvents());
		assertThat(riskService.getEvents()).contains(MAJOR_RISK);
	}

	/**
	 * Example for Reason 3 - part 1
	 * By using the autowired RiskService from the SpringContext,
	 * the next test will be affected by this one
	 */
	@Test
	public void shouldModifyTheCache() {
		riskService.getCache().add(RiskType.MINOR_RISK.name());
	}

	/**
	 * Example for Reason 3 - part 2
	 * By using the autowired RiskService from the SpringContext,
	 * this test is affected by the previous one
	 *
	 * This test will always fail
	 */
	@Test
	public void shouldFailBecauseOfModificationsInPreviousTest() {
		//Given

		//When
		riskService.doSomethingAndAddToCache();

		//Then
		assertThat(riskService.getCache().size()).isEqualTo(1);
		assertThat(riskService.getCache().contains(RiskType.MINOR_RISK.name())).isFalse();
		assertThat(riskService.getCache().contains(MAJOR_RISK.name())).isTrue();
	}
}

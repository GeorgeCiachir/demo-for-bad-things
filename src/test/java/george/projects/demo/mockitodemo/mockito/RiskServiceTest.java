package george.projects.demo.mockitodemo.mockito;

import george.projects.demo.mockitodemo.dto.RiskDto;
import george.projects.demo.mockitodemo.mapper.RiskMapper;
import george.projects.demo.mockitodemo.model.RiskModel;
import george.projects.demo.mockitodemo.model.RiskType;
import george.projects.demo.mockitodemo.repository.RiskRepository;
import george.projects.demo.mockitodemo.service.RiskCalculator;
import george.projects.demo.mockitodemo.service.RiskService;
import george.projects.demo.mockitodemo.service.ServiceThatIsNotMockedInTests;
import george.projects.demo.mockitodemo.service.ServiceThatUsesAResult;
import george.projects.demo.mockitodemo.someExternalApacheLibrary.AResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static george.projects.demo.mockitodemo.model.RiskType.MAJOR_RISK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RiskServiceTest {

	@Spy
	@InjectMocks
	private RiskService riskService;

	@Mock
	private RiskRepository riskRepository;

	@Mock
	private RiskMapper riskMapper;

	@Mock
	private RiskCalculator riskCalculator;

	@Mock
	private ServiceThatIsNotMockedInTests thisIsNowMocked;

	@Mock
	private ServiceThatUsesAResult serviceThatUsesAResult;

	/**
	 * Example for calling the real method on a mock
	 */
	@Test
	public void shouldGetRiskDtoById() {
		//Given
		long id = 15L;
		RiskModel retrievedModel = new RiskModel(RiskType.MAJOR_RISK, "some value");
		when(riskRepository.getById(id)).thenReturn(retrievedModel);
		when(riskMapper.modelToDto(retrievedModel)).thenCallRealMethod();
		int expectedRiskSeverity = 100;

		//When
		RiskDto result = riskService.getRiskDtoById(id);

		//Then
		assertThat(result.getRiskSeverity()).isEqualTo(expectedRiskSeverity);
	}

	/**
	 * Example for using @Spy
	 */
	@Test
	public void shouldCalculateRisk() {
		//Given
		int riskFactor = 100;
		RiskType expected = RiskType.MAJOR_RISK;
		when(riskService.calculateRiskInternally(riskFactor)).thenReturn(expected);

		//When
		RiskType result = riskService.calculateRisk(riskFactor);

		//Then
		assertThat(result).isEqualTo(expected);
	}


	/*
	 **********************************
	 * WHY WE SHOULD USE JUST MOCKITO *
	 **********************************
	 */

	/**
	 * Example for Reason 1
	 * Even if the implementation of the {@link RiskMapper#modelToDtoSimulatingMapperImplChanges}
	 * is changed, the test is still consistent
	 */
	@Test
	public void shouldGetRiskDtoByIdEvenIfTheMapperImplIsChanged() {
		//Given
		long id = 15L;
		RiskType testedRiskCase = RiskType.MAJOR_RISK;
		String associatedObject = "some value";
		int expectedRiskSeverity = 100;

		RiskModel retrievedModel = new RiskModel(testedRiskCase, associatedObject);
		when(riskRepository.getById(id)).thenReturn(retrievedModel);
		RiskDto mappedRiskDto = new RiskDto(testedRiskCase, associatedObject);

		mappedRiskDto.setRiskSeverity(expectedRiskSeverity);
		when(riskMapper.modelToDtoSimulatingMapperImplChanges(retrievedModel)).thenReturn(mappedRiskDto);

		//When
		RiskDto result = riskService.getRiskDtoByIdSimulateMapperImplChanges(id);

		//Then
		assertThat(result.getRiskSeverity()).isEqualTo(expectedRiskSeverity);
	}

	/**
	 * Reason 2 -> avoid the bad case scenario of masking the second transformation
	 * and the new input
	 */
	@Test
	public void thisTestWillFailInTheNewImplementationOfTheMethod() {
		// Given
		int value = 15;

		AResult expectedResultInput = new AResult(100);
		when(thisIsNowMocked.transform(value)).thenReturn(expectedResultInput);

		// This condition will not produce the desired effect, because the input is now different
		// If we switch to any(), the test will pass
		when(serviceThatUsesAResult.performSomeLogic(expectedResultInput)).thenReturn(MAJOR_RISK);

		//When
		boolean result = riskService.performSomeLogicBasedOnAnInput(value);

		//Then
		assertThat(result).isTrue();
		assertThat(riskService.getEvents()).contains(MAJOR_RISK);

	}


	/**
	 * Example for Reason 2 -> good usage of the {@link org.mockito.Mockito#any}
	 * In this case - maybe even the only solution
	 */
	@Test
	public void shouldCalculateRiskUsingExternalLibrary() {
		//Given
		int input = 100;
		RiskType expected = RiskType.MAJOR_RISK;

		// This object cannot be used to mock the riskCalculator.calculateRisk(AResult)
		// because a new object of this type is always created
		AResult createdObjectInsideTheMethod = new AResult(input);

		when(riskCalculator.calculateRisk(any())).thenReturn(expected);

		//When
		RiskType result = riskService.calculateRiskUsingExternalLibrary(input);

		//Then
		assertThat(result).isEqualTo(expected);
	}

	/**
	 * Example for Reason 3 - part 1
	 * By not using the autowired RiskService from the SpringContext,
	 * The next test will not be affected by this one
	 */
	@Test
	public void shouldModifyTheCache() {
		riskService.getCache().add(RiskType.MINOR_RISK.name());
	}

	/**
	 * Example for Reason 3 - part 2
	 * By not using the autowired RiskService from the SpringContext,
	 * This test is not affected by the previous one
	 */
	@Test
	public void shouldBeOkEvenIfCacheIsModifiedInPreviousTest() {
		//Given

		//When
		riskService.doSomethingAndAddToCache();

		//Then
		assertThat(riskService.getCache().size()).isEqualTo(1);
		assertThat(riskService.getCache().contains(RiskType.MINOR_RISK.name())).isFalse();
		assertThat(riskService.getCache().contains(RiskType.MAJOR_RISK.name())).isTrue();
	}

	//No need to demonstrate Reason 4, as we do not use reflection


}

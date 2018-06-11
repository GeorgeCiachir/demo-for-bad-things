package george.projects.demo.mockitodemo.repository;

import george.projects.demo.mockitodemo.model.RiskModel;
import george.projects.demo.mockitodemo.model.RiskType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RiskRepository {

	private static final Map<Long, RiskModel> DB_SIMULATION = new HashMap<>();

	public RiskModel getById(long id) {
		return DB_SIMULATION.get(id);
	}

	static {
		RiskModel first = new RiskModel(RiskType.MAJOR_RISK, "some object");
		RiskModel second = new RiskModel(RiskType.MINOR_RISK, "some other object");
		DB_SIMULATION.put(1L, first);
		DB_SIMULATION.put(2L, second);
	}
}

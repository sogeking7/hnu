package com.hnu.api.goals;

import com.hnu.api.goals.operation.GoalSaveRequest;
import com.hnu.db.goals.GoalDao;
import com.hnu.db.goals.dto.GoalDto;
import com.hnu.db.jooq.model.tables.records.NuGoalRecord;
import common.util.Numbers;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Transactional
@RequestScoped
public class GoalServiceImpl implements GoalService {

	@Inject
	GoalDao goalDao;

	@Override
	public List<GoalDto> getGoals() {
		return goalDao.find();
	}

	@Override
	public GoalDto getGoalById(UUID id) {
		return goalDao.byId(id);
	}

	@Override
	public List<GoalDto> getGoalsByUserId(UUID userId) {
		return goalDao.findGoalsByUserId(userId);
	}

	@Override
	public UUID saveGoal(UUID userId, GoalSaveRequest req) {

		Consumer<NuGoalRecord> fn = record -> {
			record.setUserId(userId);
			record.setName(req.name());
			record.setDurationMonth(req.durationMonth());
			record.setMonthlyInvest(Numbers.toBigDecimal(req.monthlyInvest()));
			record.setTarget(Numbers.toBigDecimal(req.target()));
			record.setEstimatedDate(req.estimatedDate());
			record.setBalance(BigDecimal.ZERO);
			record.setCompleted(false);
		};
		var saved = req.id() == null ? goalDao.insert(fn) : goalDao.update(fn, req.id());
		return saved.id();
	}

	@Override
	public void remove(UUID id) {
		goalDao.remove(id);
	}
}

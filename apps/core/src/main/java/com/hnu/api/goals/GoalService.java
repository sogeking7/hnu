package com.hnu.api.goals;

import com.hnu.api.goals.operation.GoalSaveRequest;
import com.hnu.db.goals.dto.GoalDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface GoalService {
	List<GoalDto> getGoals();

	GoalDto getGoalById(@NotNull UUID id);

	List<GoalDto> getGoalsByUserId(@NotNull UUID userId);

	UUID saveGoal(@NotNull UUID userId, GoalSaveRequest goal);

	void remove(@NotNull UUID id);
}

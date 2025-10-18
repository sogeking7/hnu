package com.hnu.db.goals;

import com.hnu.db.goals.dto.GoalDto;
import com.hnu.db.jooq.model.tables.records.NuGoalRecord;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface GoalDao {
	List<GoalDto> findGoalsByUserId(UUID userId);

	GoalDto byId(@NotNull UUID id);

	List<GoalDto> find();

	GoalDto insert(Consumer<NuGoalRecord> fn);

	GoalDto update(Consumer<NuGoalRecord> fn, @NotNull UUID id);

	void remove(UUID id);
}

package com.hnu.db.goals;

import com.hnu.db.goals.dto.GoalDto;
import com.hnu.db.jooq.model.tables.NuGoals;
import com.hnu.db.jooq.model.tables.records.NuGoalRecord;
import com.hnu.db.util.JooqDb;
import jakarta.enterprise.context.Dependent;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Dependent
public class GoalDaoImpl extends JooqDb implements GoalDao {
	private final NuGoals g = NuGoals.NU_GOALS.as("g");

	public GoalDaoImpl(DSLContext dsl) {
		super(dsl);
	}

	@Override
	public List<GoalDto> findGoalsByUserId(UUID userId) {
		return db.selectFrom(g).where(g.USER_ID.eq(userId))
			.fetch(GoalDto::of);
	}

	@Override
	public GoalDto byId(UUID id) {
		return db.selectFrom(g)
			.where(g.ID.eq(id).and(g.REMOVED.isFalse())).fetchSingle(GoalDto::of);
	}

	@Override
	public List<GoalDto> find() {
		return db.selectFrom(g)
			.where(g.REMOVED.isFalse())
			.fetch(GoalDto::of);
	}

	@Override
	public @NotNull GoalDto insert(Consumer<NuGoalRecord> fn) {
		var record = new NuGoalRecord();
		fn.accept(record);
		return db.insertInto(g).set(record)
			.returning()
			.fetchSingle(GoalDto::of);
	}

	@Override
	public @NotNull GoalDto update(Consumer<NuGoalRecord> fn, @NotNull UUID id) {
		var record = new NuGoalRecord();
		fn.accept(record);
		return db.update(g)
			.set(record)
			.where(g.ID.eq(id))
			.returning()
			.fetchSingle(GoalDto::of);
	}

	@Override
	public void remove(UUID id) {
		db.update(g)
			.set(g.REMOVED, true)
			.where(g.ID.eq(id))
			.execute();
	}
}

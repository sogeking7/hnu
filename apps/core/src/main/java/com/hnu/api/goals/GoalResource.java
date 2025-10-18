package com.hnu.api.goals;

import com.hnu.api.goals.model.GoalModel;
import com.hnu.api.goals.operation.GoalSaveRequest;
import com.hnu.auth.HxIdentity;
import com.hnu.db.goals.dto.GoalDto;
import common.dto.SaveResult;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;
import java.util.UUID;

@Path("/api/hnu/goals")
public class GoalResource {
	@Inject
	GoalService goalService;

	@Inject
	HxIdentity identity;

	@GET
	@Path("/")
	public List<GoalModel> getGoals() {
		UUID userId = identity.userId();
		return goalService.getGoalsByUserId(userId).stream().map(GoalModel::of).toList();
	}

	@GET
	@Path("/{id}")
	public GoalModel getGoalById(@NotNull UUID id) {
		GoalDto goal = goalService.getGoalById(id);
		return GoalModel.of(goal);
	}


	@GET
	@Path("/user/{userId}")
	public List<GoalModel> getUserGoals(@NotNull UUID userId) {
		return goalService.getGoalsByUserId(userId).stream().map(GoalModel::of).toList();
	}

	@POST
	@Path("/")
	public SaveResult saveGoal(@Valid @NotNull @RequestBody GoalSaveRequest req) {
		UUID userId = identity.userId();
		UUID id = goalService.saveGoal(userId, req);
		return SaveResult.of(id);
	}

	@DELETE
	@Path("/{id}")
	public void removeGoal(@NotNull UUID id) {
		goalService.remove(id);
	}
}

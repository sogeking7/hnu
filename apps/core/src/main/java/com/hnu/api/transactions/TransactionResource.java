package com.hnu.api.transactions;

import com.hnu.api.transactions.model.TransactionModel;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;
import java.util.UUID;

@Path("/api/hnu/transactions")
public class TransactionResource {
	@Inject
	TransactionService transactionService;

	@GET
	@Path("/user/{userId}")
	public List<TransactionModel> getUserTransactions(@NotNull UUID userId) {
		return transactionService.getTransactionsByUserId(userId).stream().map(TransactionModel::of).toList();
	}

}

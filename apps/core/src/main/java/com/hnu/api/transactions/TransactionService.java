package com.hnu.api.transactions;

import com.hnu.db.transactions.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
	List<TransactionDto> getTransactionsByUserId(UUID userId);
}

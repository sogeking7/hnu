package com.hnu.db.transactions;

import com.hnu.db.transactions.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionDao {
	public List<TransactionDto> findTransactionsByUserId(UUID userId);
}

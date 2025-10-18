package com.hnu.api.transactions;

import com.hnu.db.transactions.TransactionDao;
import com.hnu.db.transactions.dto.TransactionDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@RequestScoped
public class TransactionServiceImpl implements TransactionService {

	@Inject
	TransactionDao transactionDao;

	public List<TransactionDto> getTransactionsByUserId(UUID userId) {
		return transactionDao.findTransactionsByUserId(userId);
	}
}

package com.hnu.db.transactions;

import com.hnu.db.jooq.model.tables.NuTransactions;
import com.hnu.db.transactions.dto.TransactionDto;
import com.hnu.db.util.JooqDb;
import jakarta.enterprise.context.Dependent;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

@Dependent
public class TransactionDaoImpl extends JooqDb implements TransactionDao {
	private final NuTransactions t = NuTransactions.NU_TRANSACTIONS.as("t");

	public TransactionDaoImpl(DSLContext dsl) {
		super(dsl);
	}

	public List<TransactionDto> findTransactionsByUserId(UUID userId) {
		return db.selectFrom(t).where(t.USER_ID.eq(userId))
			.fetch(TransactionDto::of);
	}

	public List<TransactionDto> findTransactions() {
		return db.selectFrom(t).where(t.REMOVED.isFalse())
			.fetch(TransactionDto::of);
	}
}

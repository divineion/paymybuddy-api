package com.paymybuddy.api.config.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class TransactionRollbackAspect {
	private static final Logger logger = LogManager.getLogger(TransactionRollbackAspect.class);
	
	@Around(value = "@annotation(org.springframework.transaction.annotation.Transactional)")
	public Object createTransactionListener(ProceedingJoinPoint pjp) throws Throwable {
		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronization() {
					public void afterCompletion(int status) {
						if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
							logger.error("Rollback a transaction");
						}
					}
		});
		
		return pjp.proceed();
	}
}

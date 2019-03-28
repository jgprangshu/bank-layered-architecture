package com.capgemini.bankapp.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.dao.impl.BankAccountDaoImpl;
import com.capgemini.bankapp.exception.BankAccountNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.util.DbUtil;

public class BankAccountServiceImpl implements BankAccountService {

	Logger logger = Logger.getLogger(BankAccountServiceImpl.class);

	private BankAccountDao bankAccountDao;

	public BankAccountServiceImpl() {
		bankAccountDao = new BankAccountDaoImpl();
	}

	@Override
	public double checkBalance(long accountId) throws BankAccountNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance >= 0)
			return balance;
		throw new BankAccountNotFoundException("Bank account doesnot exsist");
	}

	@Override
	public double withdraw(long accountId, double amount) throws LowBalanceException, BankAccountNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountNotFoundException("Bank account doesnt exsist");
		else if (balance - amount >= 0) {
			balance = balance - amount;
			bankAccountDao.updateBalance(accountId, balance);
			DbUtil.commit();
			System.out.println("New Updated balance after withdraw is Rs " + balance);
			return balance;

		} else
			throw new LowBalanceException("Low Balance unable to process withdraw");
	}

	public double withdrawForFundTransfer(long accountId, double amount)
			throws LowBalanceException, BankAccountNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountNotFoundException("Bank account doesnt exsist");
		else if (balance - amount >= 0) {
			balance = balance - amount;
			bankAccountDao.updateBalance(accountId, balance);
			System.out.println("New Updated balance after withdraw is Rs " + balance);
			return balance;

		} else
			throw new LowBalanceException("Low Balance unable to process withdraw");
	}

	@Override
	public double deposit(long accountId, double amount) throws BankAccountNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountNotFoundException("Bank account doesnot exsist");
		balance = balance + amount;
		bankAccountDao.updateBalance(accountId, balance);
		DbUtil.commit();
		System.out.println("New Updated balance after deposit is Rs " + balance);
		return balance;
	}

	@Override
	public boolean deleteBankAccount(long accountId) throws BankAccountNotFoundException {
		boolean result = bankAccountDao.deleteBankAccount(accountId);
		if(result) {
			DbUtil.commit();
			return result;
		}
		throw new BankAccountNotFoundException("Bank acaoount exsist");
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {
		boolean result = bankAccountDao.addNewBankAccount(account);
		if (result)
			DbUtil.commit();
		return result;
	}

	@Override
	public List<BankAccount> findAllBankAccounts() {
		return bankAccountDao.findAllBankAccounts();
	}

	@Override
	public double fundTransfer(long fromAccount, long toAccount, double amount)
			throws BankAccountNotFoundException, LowBalanceException {
		try {
			double newBalance = withdrawForFundTransfer(fromAccount, amount);
			deposit(toAccount, amount);
			DbUtil.commit();
			return newBalance;
		} catch (LowBalanceException | BankAccountNotFoundException e) {
			logger.error("Execption", e);
			DbUtil.rollback();
			throw e;
		}
	}

	@Override
	public BankAccount searchBankAccount(long accountId) throws BankAccountNotFoundException {
		 BankAccount result = bankAccountDao.searchBankAccount(accountId);
		if(result== null)
			throw new BankAccountNotFoundException("Bank account doesnt exsist");
		else
			return result;
	}

	@Override
	public boolean updateBankAccountDetails(long accountId, String account_type, String customer_name) {
		boolean result= bankAccountDao.updateBankAccountDetails(accountId, account_type, customer_name);
		if(result) {
			DbUtil.commit();
		}
		return result;
	}

}

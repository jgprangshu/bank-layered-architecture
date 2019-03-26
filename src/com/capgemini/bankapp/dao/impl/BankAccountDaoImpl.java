package com.capgemini.bankapp.dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.util.DbUtil;

public class BankAccountDaoImpl implements BankAccountDao {

	@Override
	public double getBalance(long accountId) {
		String query = "SELECT account_balance FROM bankaccounts WHERE account_id=" + accountId;
		double balance = 0;

		try (Connection connection = DbUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet result = statement.executeQuery()) {
			while(result.next()) {
			balance = result.getDouble(1);
			System.out.println(balance);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;
	}

	@Override
	public boolean deleteBankAccount(long accountId) {
		String query = "DELETE FROM bankaccounts WHERE account_id = ? ";
		int result;
		try (Connection connection = DbUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setLong(1, accountId);
			result = statement.executeUpdate();
			if (result == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void updateBalance(long accountId, double newBalance) {

		String query = "UPDATE bankaccounts SET account_balance =? WHERE account_id=?";

		try (Connection connection = DbUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setDouble(1, newBalance);
			statement.setLong(2, accountId);

			int result = statement.executeUpdate();
			//System.out.println("No. of rows updated :" + result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {
		String query = "INSERT INTO bankaccounts (account_type,customer_name,account_balance)VALUES(?,?,?)";

		try (Connection connection = DbUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			
			statement.setString(1, account.getAccountType());
			statement.setString(2, account.getAccountHolderName());
			statement.setDouble(3, account.getAccountBalance());
			int result = statement.executeUpdate();

			if (result == 1)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<BankAccount> findAllBankAccounts() {
		String query = "SELECT * FROM bankaccounts";
		List<BankAccount> bankaccounts = new ArrayList<>();

		try (Connection connection = DbUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet result = statement.executeQuery()) {

			while (result.next()) {
				long accountId = result.getLong(1);
				String accountType = result.getString(2);
				String accountHolderName = result.getString(3);
				double accountBalance = result.getDouble(4);
				BankAccount account = new BankAccount(accountId, accountType, accountHolderName, accountBalance);
				bankaccounts.add(account);
				System.out.println(bankaccounts);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bankaccounts;
	}

	@Override
	public BankAccount searchBankAccount(long accountId) {
		String query = "SELECT * FROM bankaccounts where account_id= " +accountId;
		BankAccount account = null;

		try (Connection connection = DbUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet result = statement.executeQuery()) {
			
			while(result.next())
			account = new BankAccount(result.getLong(1), result.getString(2), result.getString(3),
					result.getDouble(4));
			System.out.println(account);
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
		
		
	}


}







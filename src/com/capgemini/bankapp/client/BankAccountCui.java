package com.capgemini.bankapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;

public class BankAccountCui {
	public static void main(String[] args) {

		int choice;
		long accountId;
		long accountIdPayee;
		double amount;
		String accountHolderName;
		String accountType;
		double accountBalance;
		BankAccountService bankService = new BankAccountServiceImpl();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			while (true) {
				System.out.println("1. Add New BankAccount\n2. Withdraw\n3. Deposit\n4. Fund Transfer");
				System.out.println("5. Delete BankAccount\n6. Display All BankAccount Details");
				System.out.println("7. Search BankAccount\n8. Check Balance\n9. Exit\n");

				System.out.print("Please enter your choice = ");
				choice = Integer.parseInt(reader.readLine());

				switch (choice) {

				case 1:
					System.out.println("Enter account holder name: ");
					accountHolderName = reader.readLine();
					System.out.println("Enter account type: ");
					accountType = reader.readLine();
					System.out.println("Enter account balance: ");
					accountBalance = Double.parseDouble(reader.readLine());
					BankAccount account = new BankAccount(accountHolderName, accountType, accountBalance);
					if (bankService.addNewBankAccount(account))
						System.out.println("Account created successfully...\n");
					else
						System.out.println("failed to create new account...\n");
					break;

				// For Withdraw operation
				case 2:
					System.out.println("Enter the account ID");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter the amount to be withdrawn");
					amount = Double.parseDouble(reader.readLine());
					try {
						bankService.withdraw(accountId, amount);
					} catch (LowBalanceException e) {
						System.out.println("Insufficient Fund");
					}

					break;

				case 3:
					System.out.println("Enter the account ID");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("Enter the amount to be deposited");
					amount = Double.parseDouble(reader.readLine());
					bankService.deposit(accountId, amount);
					break;

				case 4:
					System.out.println("Enter the payee's account ID");
					accountId = Long.parseLong(reader.readLine());
					System.out.println("enter the reciepent's account ID");
					accountIdPayee = Long.parseLong(reader.readLine());
					System.out.println("Enter the amount to be deposited");
					amount = Double.parseDouble(reader.readLine());
					try {
						bankService.fundTransfer(accountId, accountIdPayee, amount);
					} catch (LowBalanceException e) {
						System.out.println("Insufficient Fund");
					}

				case 5:
					System.out.println("Enter the bank account ID of the account to be deleted");
					accountId = Long.parseLong(reader.readLine());
					bankService.deleteBankAccount(accountId);
					break;

				case 6:
					bankService.findAllBankAccounts();
					break;

				case 7:
					System.out.println("Enter the ID of the bank account to be searched");
					accountId = Long.parseLong(reader.readLine());
					bankService.searchBankAccount(accountId);
					break;

				case 8:
					System.out.println("Enter the bank account ID to check balance");
					accountId = Long.parseLong(reader.readLine());
					bankService.checkBalance(accountId);
					break;

				case 9:
					System.out.println("Thanks for banking with us.");
					System.exit(0);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

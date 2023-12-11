package com.banking.app;

import com.banking.app.DTO.CustomerDTO;
import com.banking.app.entities.*;
import com.banking.app.enums.AccountStatus;
import com.banking.app.enums.OperationType;
import com.banking.app.exceptions.AccountBalanceNotEnoughException;
import com.banking.app.exceptions.AccountNotFoundException;
import com.banking.app.exceptions.CustomerNotFoundException;
import com.banking.app.repository.AccountRepository;
import com.banking.app.repository.CustomerRepository;
import com.banking.app.repository.OperationRepository;
import com.banking.app.services.BankingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class AppApplication {

	// L'utilisation des entitéx directement dans la couche UI est déconseillé
	// Parmi les priblèmes qui pourraient se poser : Les dépendances cycliques durant la serialisation entité - json
	// => Utiliser DTOs

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankingService bankingService) {
		return args -> {
			Stream.of("Lamiss","Carlo","Doe").forEach( name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@googlemail.com");
				bankingService.saveCustomer(customer);
			});
			bankingService.getCustomers().forEach( customer -> {
				try {
					bankingService.saveCurrentAcc(2000+Math.random()*10000,9000, customer.getId());
					bankingService.saveSavingAcc(2000+Math.random()*10000,5.5, customer.getId());

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});
			bankingService.getAccounts().forEach( account -> {
				for(int i = 0; i <10;i++) {
					try {
						bankingService.credit(account.getId(),1000+Math.random()*10000);
						bankingService.debit(account.getId(),1000+Math.random()*1000);
					} catch (AccountNotFoundException | AccountBalanceNotEnoughException e) {
						e.printStackTrace();
					}
                }
			});


		};
	}


	//@Bean
	CommandLineRunner start(AccountRepository accountRepository,
							OperationRepository operationRepository,
							CustomerRepository customerRepository) {
		return (args) -> {

			Stream.of("Amine","Oulaya","Sanae").forEach( name ->{
						Customer customer = new Customer();
						customer.setEmail(name+"@gmail.com");
						customer.setName(name);
						customerRepository.save(customer);
					});

			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAcc = new CurrentAccount();
				currentAcc.setBalance(new Random().nextDouble(1)*9000);
				currentAcc.setStatus(AccountStatus.CREATED);
				currentAcc.setCustomer(cust);
				currentAcc.setCreatedAt(new Date());
				currentAcc.setOverDraft(8000);
				currentAcc.setId(UUID.randomUUID().toString());
				accountRepository.save(currentAcc);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setBalance(new Random().nextDouble(1)*9000);
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setInterestRate(5.5);
				savingAccount.setId(UUID.randomUUID().toString());
				accountRepository.save(savingAccount);
			});

			accountRepository.findAll().forEach(acc -> {
				for(int i = 0; i < 10 ; i++) {
					Operations op = new Operations();
					op.setType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
					op.setAmount(Math.random()*9000);
					op.setOperationDate(new Date());
					op.setAccount(acc);
					operationRepository.save(op);
				}
			});


		};
	}

}

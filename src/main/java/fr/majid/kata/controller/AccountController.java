package fr.majid.kata.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.majid.kata.exception.AccountNotFoundException;
import fr.majid.kata.exception.SoldeInsuffisantException;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.OperationType;
import fr.majid.kata.services.AccountService;

/**
 * 
 * @author mortada majid
 * @email majid.mortada@gmail.com
 *
 */
@RestController
public class AccountController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	@Autowired
	private AccountService accountService;	

	@RequestMapping(method = RequestMethod.PUT, path = "/kata/accounts/{accountNumero}/operations/{operationType}/")
	public Account handleUpdate(@PathVariable String accountNumero, @PathVariable OperationType operationType,
			@RequestBody Amount amount) throws AccountNotFoundException, SoldeInsuffisantException {
		MDC.put("operation", operationType.name());
		MDC.put("accountNumero", accountNumero);
		Account account = accountService.findAccountByNumero(accountNumero);

		switch (operationType) {
		case DEPOSIT:
			LOGGER.info("send reponse for deposit operation");
			MDC.clear();
			return accountService.depose(amount, account);
		case WITHDRAWL:
			LOGGER.info("send reponse for withdrawl operation");
			MDC.clear();
			return accountService.withdraw(amount, account);
		}
		MDC.clear();
		throw new IllegalArgumentException("operation given does not correspond to any type");
	}
	
	@ExceptionHandler({ AccountNotFoundException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)	
	public void  handleAccountNotFound(Exception e) {
		LOGGER.warn("Account not found, please check your account numero");
	}

	@ExceptionHandler({ SoldeInsuffisantException.class })
	@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
	public void  handleSoldeInsuffisantException(Exception e) {
		LOGGER.warn("Solde Insuffisant,operation canceled");
	}
}
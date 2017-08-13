package fr.majid.kata.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.majid.kata.AccountService;

@RestController
@RequestMapping("/kata")
public class AccountController {

	private AccountService accountService;
    
    public AccountController(AccountService accountService) {		
		this.accountService = accountService;
	}


    
}

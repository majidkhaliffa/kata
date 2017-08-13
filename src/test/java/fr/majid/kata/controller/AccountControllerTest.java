package fr.majid.kata.controller;

import static fr.majid.kata.builder.GenericBuilder.of;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.majid.kata.AccountService;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Customer;


@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void should_make_deposit_by_account_numero() throws Exception {
		Customer customer = of(Customer::new)
				.with(Customer::setNom, "guest")
				.with(Customer::setPrenom, "guest")
				.build();
        Account account = of(Account::new)
                .with(Account::setNumero,"1000236")
                .with(Account::setSolde,530L)
                .with(Account::setCustomer,customer)
                .build();
        doReturn(account).when(accountService).depose(530L, "1000236");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/kata/accounts/1000236")
                .content(mapper.writeValueAsString(530L))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(
                                "{'customer': {'nom':guest,'prenom':guest}, 'solde': 530, 'numero': '1000236'}"
                        )
                );

        verify(accountService, times(1)).depose(530L, "1000236");
    }
}

package fr.majid.kata.controller;


import static fr.majid.kata.builder.GenericBuilder.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import fr.majid.kata.KataApplication;
import fr.majid.kata.builder.GenericBuilder;
import fr.majid.kata.exception.AccountNotFoundException;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.Customer;
import fr.majid.kata.services.AccountService;

/**
 *
 * @author mortada majid
 * @email majid.mortada@gmail.com
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KataApplication.class)
@WebAppConfiguration
public class AccountControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private String ACOUNT_ENDPOINT = "/kata/accounts";
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Account account;
    
    @MockBean
    private AccountService accountService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        Customer customer = of(Customer::new)
				.with(Customer::setNom, "guest")
				.with(Customer::setPrenom, "guest")
				.build();
         account = GenericBuilder.of(Account::new)
                .with(Account::setNumero,"1000236")
                .with(Account::setSolde,50l)
                .with(Account::setCustomer,customer)
                .build();

    }

    @Test
    public void that_deposit_by_account_numero_should_Return_Success() throws Exception { 
    	doReturn(account).when(accountService).depose(Mockito.anyObject(), Mockito.any()); 
    	int expectedSold = (int) (this.account.getSolde());
    	
        mockMvc.perform(put(ACOUNT_ENDPOINT+"/1000236/operations/deposit/")
        		.content(this.json(new Amount(1000L)))
        		.contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.solde", is(expectedSold)))
                .andExpect(jsonPath("$.numero", is(this.account.getNumero())))                
                .andExpect(jsonPath("$.customer.nom", is(this.account.getCustomer().getNom())))
                ;
    }

    @Test
    public void that_deposit_by_account_numero_should_Return_fail_with_404() throws Exception { 
    	doThrow(new AccountNotFoundException("acount not found")).when(accountService).depose(new Amount(1000L), "1000236");
    	
        mockMvc.perform(put(ACOUNT_ENDPOINT+"/1000236/operations/deposit/")
        		.content(this.json(new Amount(1000L)))
        		.contentType(contentType))
                .andExpect(status().isNotFound());
    }

    protected String json(Object o) throws Exception {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage(); 
			this.mappingJackson2HttpMessageConverter.write(
			        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);		
        return mockHttpOutputMessage.getBodyAsString();
    }
}
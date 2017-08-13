package fr.majid.kata.controller;


import static fr.majid.kata.builder.GenericBuilder.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import fr.majid.kata.KataApplication;
import fr.majid.kata.builder.GenericBuilder;
import fr.majid.kata.model.Account;
import fr.majid.kata.model.Amount;
import fr.majid.kata.model.Customer;
import fr.majid.kata.repository.AccountRepository;

/**
 * 
 * @author  mortada majid
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KataApplication.class)
@WebAppConfiguration
public class AccountControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private String ACOUNT_ENDPOINT ="/kata/accounts/";
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Account account;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;
    
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
    public void readSingleBookmark() throws Exception {    	
    	int expectedSold = (int) (this.account.getSolde()+1000);
    	
        mockMvc.perform(put(ACOUNT_ENDPOINT+"1000236")
        		.content(this.json(new Amount(1000L)))
        		.contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.solde", is(expectedSold)))
                .andExpect(jsonPath("$.numero", is(this.account.getNumero())))                
                .andExpect(jsonPath("$.customer.nom", is(this.account.getCustomer().getNom())))
                ;
    }

    protected String json(Object o)  {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        try {
			this.mappingJackson2HttpMessageConverter.write(
			        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		} catch (HttpMessageNotWritableException | IOException ex) {
		      System.out.println("Error: " + ex);
		    
			ex.printStackTrace();
		}
        return mockHttpOutputMessage.getBodyAsString();
    }
}
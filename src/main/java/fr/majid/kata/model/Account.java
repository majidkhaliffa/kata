package fr.majid.kata.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author mortada majid
 *
 */

@Entity
public class Account{

	@Id
    @GeneratedValue
    private  Long id;

    @NotNull    
    private  String numero;

    private  long solde;

	@OneToOne(fetch = FetchType.LAZY)
    private  Customer customer;
    
    public void setNumero(String numero) {
    	this.numero = numero;
    }
    
    public void setSolde(long solde) {
    	this.solde = solde;
    }
    public long getSolde() {
    	return solde;
    }
    
    public void setCustomer(Customer customer) {
    	this.customer = customer;
    }
}

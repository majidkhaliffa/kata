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
public class Account {
	
	@Id
	@GeneratedValue
	private Long id;


	@NotNull
	private String numero;

	private long solde;

	@OneToOne(fetch = FetchType.LAZY)
	private Customer customer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getNumero() {
		return numero;
	}

	public Customer getCustomer() {
		return customer;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
}

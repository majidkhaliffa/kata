package fr.majid.kata.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.majid.kata.model.Account;
import fr.majid.kata.model.Customer;

/**
 *
 * @author mortada majid
 * @email majid.mortada@gmail.com
 *
 */

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByNumero(String numero);

	Account findByCustomer(Customer customer);

}

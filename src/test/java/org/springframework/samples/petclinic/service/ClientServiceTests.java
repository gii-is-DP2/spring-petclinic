
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClientServiceTests {

	@Autowired
	private ClientService clientService;


	@Test
	public void testCountWithInitialData() {
		int count = this.clientService.clientCount();
		Assertions.assertEquals(count, 1);
	}

}

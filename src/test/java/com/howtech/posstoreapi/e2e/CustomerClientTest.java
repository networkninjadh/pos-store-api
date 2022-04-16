package com.howtech.posstoreapi.e2e;

import com.howtech.posstoreapi.DTOs.CustomerDto;
import com.howtech.posstoreapi.clients.CustomerClient;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(SpringRunner.class)
@RestClientTest(CustomerClient.class)
public class CustomerClientTest {

    private final CustomerClient customerClient = new CustomerClient(new RestTemplate());


    @Test
    public void whenGetCustomerByIdThenReturnCustomer() throws Exception {

        CustomerDto customer = this.customerClient.getById(1L);

        assertThat("Response has a return type of CustomerDto", customer, instanceOf(CustomerDto.class));
    }
}

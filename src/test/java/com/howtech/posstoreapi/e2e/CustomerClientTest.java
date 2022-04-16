package com.howtech.posstoreapi.e2e;

import com.howtech.posstoreapi.DTOs.CustomerDto;
import com.howtech.posstoreapi.clients.CustomerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SpringRunner.class)
@RestClientTest(CustomerClient.class)
public class CustomerClientTest {

    private final CustomerClient customerClient = new CustomerClient(new RestTemplate());


    @Test
    public void whenGetCustomerByIdThenReturnCustomer() throws Exception {

        CustomerDto customer = this.customerClient.getById(1L);

        assertThat("Response has a return type of CustomerDto", customer, instanceOf(CustomerDto.class));
        assertThat("Customer ID matches", customer.getCustomerId(), equalTo(1L));
        assertThat("User Email is correct", customer.getEmail(), equalTo("networkninjadh@gmail.com"));
        assertThat("Username is correct", customer.getUsername(), equalTo("networkninjadh"));
    }
}

package com.howtech.posstoreapi.integration.services;

import com.howtech.posstoreapi.PosStoreApiApplication;
import com.howtech.posstoreapi.services.ValidateMembershipService;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PosStoreApiApplication.class)
public class ValidateMembershipServiceIntegration {

    private final Logger LOGGER = LoggerFactory.getLogger(ValidateMembershipServiceIntegration.class);

    @Autowired
    private ValidateMembershipService validateMembershipService;

    @Test
    @DisplayName("Validate that membership codes are validated properly")
    public void validateMembershipCode() {

        assertThat("Verify Bronze membership type", validateMembershipService.verifyMembershipBronze("BZ1234"), equalTo(true));
        assertThat("Verify GOLD membership type", validateMembershipService.verifyMembershipGold("GD1234"), equalTo(true));
        assertThat("Verify Platinum membership type", validateMembershipService.verifyMembershipPlatinum("PL1234"), equalTo(true));
    }
}

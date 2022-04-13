package com.howtech.posstoreapi.services;

import org.springframework.stereotype.Service;

@Service
public class ValidateMembershipService {

    public boolean verifyMembershipBronze(String membershipCode) {
        return false;
    }

    public boolean verifyMembershipGold(String membershipCode) {
        return false;
    }

    public boolean verifyMembershipPlatinum(String membershipCode) {
        return false;
    }

}

package com.howtech.posstoreapi.services;

import org.springframework.stereotype.Service;

@Service
public class ValidateMembershipService {
    /* TODO come back and implement these methods */

    public boolean verifyMembershipBronze(String membershipCode) {
        return membershipCode.contains("BZ");
    }

    public boolean verifyMembershipGold(String membershipCode) {
        return membershipCode.contains("GD");
    }

    public boolean verifyMembershipPlatinum(String membershipCode) {
        return membershipCode.contains("PL");
    }

}

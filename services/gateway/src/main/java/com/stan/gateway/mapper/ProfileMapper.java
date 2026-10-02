package com.stan.gateway.mapper;

import com.stan.gateway.dto.request.CreateProfileRequest;
import com.stan.gateway.dto.response.AuthenticationData;
import com.stan.gateway.dto.response.CreateProfileResponse;
import com.stan.gateway.entity.ProfileInfo;

import java.util.Date;

public class ProfileMapper {
    public static ProfileInfo mapRequestToProfileInfo(CreateProfileRequest createProfileRequest,
                                                      AuthenticationData authenticationData) {
        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setEmail(authenticationData.getEmail());
        profileInfo.setFirstName(authenticationData.getFirstName());
        profileInfo.setLastName(authenticationData.getLastName());
        profileInfo.setPassword(authenticationData.getPassword());
        profileInfo.setIPAddress(createProfileRequest.getIPAddress());
        profileInfo.setCreatedAt(new Date());
        return profileInfo;
    }

    public static CreateProfileResponse mapProfileInfoToCreateProfileInfoResponse(ProfileInfo profileInfo) {
        CreateProfileResponse profileInfoResponse = new CreateProfileResponse();
        profileInfoResponse.setEmail(profileInfo.getEmail());
        profileInfoResponse.setFirstName(profileInfo.getFirstName());
        profileInfoResponse.setLastName(profileInfo.getLastName());
        profileInfoResponse.setPassword(profileInfo.getPassword());
        return profileInfoResponse;
    }
}

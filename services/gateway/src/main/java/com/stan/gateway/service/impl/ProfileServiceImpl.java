package com.stan.gateway.service.impl;

import com.stan.gateway.dto.request.CreateProfileRequest;
import com.stan.gateway.dto.response.AuthenticationData;
import com.stan.gateway.dto.response.AuthenticationResponse;
import com.stan.gateway.dto.response.CreateProfileResponse;
import com.stan.gateway.entity.ProfileInfo;
import com.stan.gateway.mapper.ProfileMapper;
import com.stan.gateway.repository.ProfileInfoRepository;
import com.stan.gateway.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileInfoRepository profileInfoRepository;

    public CreateProfileResponse createProfile(CreateProfileRequest createProfileRequest, AuthenticationData authenticationData) {
        log.info("Inside Create Profile with authenticationData: {}", authenticationData);
        ProfileInfo profileInfo = ProfileMapper.mapRequestToProfileInfo(createProfileRequest, authenticationData);
        try {
            profileInfo = profileInfoRepository.save(profileInfo);
            log.info("Saved Profile: {}", profileInfo);
            return ProfileMapper.mapProfileInfoToCreateProfileInfoResponse(profileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExistingProfile(String email) {
        return profileInfoRepository.existsByEmail(email);
    }

}

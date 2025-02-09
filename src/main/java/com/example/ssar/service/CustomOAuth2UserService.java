package com.example.ssar.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.ssar.dto.CustomOAuth2User;
import com.example.ssar.dto.GoogleReponse;
import com.example.ssar.dto.NaverResponse;
import com.example.ssar.dto.OAuth2Response;
import com.example.ssar.entity.UserEntity;
import com.example.ssar.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	// DefaultOAuth2UserService OAuth2UserService의 구현체

	private final UserRepository userRepository;

	public CustomOAuth2UserService(UserRepository userRepository) {

		this.userRepository = userRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println(oAuth2User.getAttributes());

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		if (registrationId.equals("naver")) {

			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("google")) {

			oAuth2Response = new GoogleReponse(oAuth2User.getAttributes());
		} else {

			return null;
		}

		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		UserEntity existData = userRepository.findByUsername(username);

		String role = "ROLE_USER";
		if (existData == null) {

			UserEntity userEntity = new UserEntity();
			userEntity.setUsername(username);
			userEntity.setEmail(oAuth2Response.getEmail());
			userEntity.setRole(role);
			userEntity.setName(oAuth2Response.getName());
			userEntity.setNickname(oAuth2Response.getNickName());

			userRepository.save(userEntity);
		} else {

			existData.setUsername(username);
			existData.setEmail(oAuth2Response.getEmail());

			role = existData.getRole();

			userRepository.save(existData);
		}

		return new CustomOAuth2User(oAuth2Response, role);
	}
}

package com.devsuperior.bds02.components;

import com.devsuperior.bds02.entities.User;
import com.devsuperior.bds02.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    // JwtTokenEnhancer = adicionar informações ao token
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()); // authentication.getName() = email
        Map<String, Object> map = new HashMap<>();
        map.put("userFirstName", user.getFirstName()); // inserir no token
        map.put("userId", user.getId()); // inserir no token
        map.put("userRole", user.getRoles()); // inserir roles no token

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        token.setAdditionalInformation(map); // inserindo inf. no token

        return accessToken;
    }
}
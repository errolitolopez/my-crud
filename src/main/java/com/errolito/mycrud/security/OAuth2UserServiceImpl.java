package com.errolito.mycrud.security;

import com.errolito.mycrud.entity.Role;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.entity.UserAuthProvider;
import com.errolito.mycrud.entity.UserProfile;
import com.errolito.mycrud.enums.AuthProvider;
import com.errolito.mycrud.repository.RoleRepository;
import com.errolito.mycrud.repository.UserAuthProviderRepository;
import com.errolito.mycrud.repository.UserRepository;
import com.errolito.mycrud.utils.OAuth2Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static io.github.uncaughterrol.commons.exception.ExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    private final UserRepository userRepository;
    private final UserAuthProviderRepository userAuthProviderRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
            OAuth2User oauth2User = delegate.loadUser(request);

            String registrationId = request.getClientRegistration().getRegistrationId();
            AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());

            Map<String, Object> attributes = oauth2User.getAttributes();

            String providerId = OAuth2Utils.extractProviderId(attributes, registrationId);
            String email = OAuth2Utils.extractEmail(attributes);
            String name = OAuth2Utils.extractName(attributes);

            resolveUser(email, name, provider, providerId);

            return oauth2User;
    }

    private void resolveUser(String email, String name, AuthProvider provider, String providerId) {
        userAuthProviderRepository
                .findByProviderAndProviderId(provider, providerId)
                .map(UserAuthProvider::getUser)
                .orElseGet(() -> userRepository.findByUsername(email)
                        .map(user -> linkExistingUser(user, email, provider, providerId))
                        .orElseGet(() -> createNewUser(email, name, provider, providerId)));
    }

    private User linkExistingUser(User user, String email, AuthProvider provider, String providerId) {
        boolean alreadyLinked = user.getUserAuthProviders().stream()
                .anyMatch(p -> p.getProvider() == provider);

        if (!alreadyLinked) {
            UserAuthProvider auth = new UserAuthProvider();
            auth.setProvider(provider);
            auth.setProviderId(providerId);
            auth.setEmail(email);
            auth.setUser(user);

            user.getUserAuthProviders().add(auth);
        }

        return userRepository.save(user);
    }

    private User createNewUser(String email, String name, AuthProvider provider, String providerId) {
        Role role = roleRepository.findByName("user").orElseThrow(() -> notFound("Role not found"));

        UserProfile profile = new UserProfile();
        profile.setFullName(name);

        User user = new User();
        user.setUsername(email);
        user.setUserProfile(profile);
        user.getRoles().add(role);

        UserAuthProvider userAuthProvider = new UserAuthProvider();
        userAuthProvider.setProvider(provider);
        userAuthProvider.setProviderId(providerId);
        userAuthProvider.setEmail(email);
        userAuthProvider.setUser(user);

        user.getUserAuthProviders().add(userAuthProvider);

        return userRepository.save(user);
    }
}
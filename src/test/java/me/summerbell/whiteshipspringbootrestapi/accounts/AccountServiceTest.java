package me.summerbell.whiteshipspringbootrestapi.accounts;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void findByUsername(){
        // Given
        String username = "keesun@email.com";
        String password = "keesun";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountService.saveAccount(account);

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
        System.out.println("+=====");
        userDetails.getAuthorities().forEach(System.out::println);
        System.out.println("+=====");
    }

    @Test
    void findByUsernameFail(){
        String username = "random@email.com";
        Assertions.assertThrows(UsernameNotFoundException.class, ()->{
            accountService.loadUserByUsername(username);
        });

    }


}
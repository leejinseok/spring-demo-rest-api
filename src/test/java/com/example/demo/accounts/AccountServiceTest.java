package com.example.demo.accounts;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  AccountService accountService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  public void findByUsername() {
    String username = "keesun@email.com";
    String password = "keesun";
    Account account = Account.builder()
      .email(username)
      .password(password)
      .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
      .build();
    this.accountService.saveAccount(account);

    UserDetailsService userDetailsService = accountService;
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    Assertions.assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
  }

  @Test
  public void findByUsernameFail() {
    String username = "random@email.com";
    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage(Matchers.containsString(username));
    accountService.loadUserByUsername(username);
  }
}

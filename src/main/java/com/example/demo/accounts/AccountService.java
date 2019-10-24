package com.example.demo.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  public Account saveAccount(Account account) {
    account.setPassword(this.passwordEncoder.encode(account.getPassword()));
    return this.accountRepository.save(account);
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    return null;
  }
}
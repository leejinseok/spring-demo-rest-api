package com.example.demo.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter @Setter
@ToString
public class Account {

  @Id @GeneratedValue
  private Integer id;
  @Column(unique = true)
  private String email;
  private String password;
  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Set<AccountRole> roles;
}

package com.example.demo.events;

import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter @Setter
@ToString
public class Event {

  @Id @GeneratedValue
  private Integer id;
  private String name;
  private String description;
  private LocalDateTime beginEnrollmentDateTime;
  private LocalDateTime closeEnrollmentDateTime;
  private LocalDateTime beginEventDateTime;
  private LocalDateTime endEventDateTime;
  private String location;
  private int basePrice;
  private int maxPrice;
  private int limitOffEnrollment;
  private boolean offline;
  private boolean free;
  @Enumerated(EnumType.STRING)
  private EventStatus eventStatus = EventStatus.DRAFT;
  @ManyToOne
  @JsonSerialize(using = AccountSerializer.class)
  private Account manager;

  public void update() {
    if (this.basePrice == 0 && this.maxPrice == 0) {
      this.free = true;
    } else {
      this.free = false;
    }

    if (this.location == null || this.location.isBlank()) {
      this.offline = false;
    } else {
      this.offline = true;
    }
  }
}

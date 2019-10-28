package com.example.demo.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

  @Test
  public void builder() {
    Event event = Event.builder()
      .name("Inflearn Spring REST API")
      .description("REST API development with Spring")
      .build();

    Assertions.assertThat(event).isNotNull();
  }

  @Test
  public void javaBean() {
    String name = "Event";
    String description = "Spring";

    Event event = new Event();
    event.setName(name);
    event.setDescription(description);

    Assertions.assertThat(event.getName()).isEqualTo(name);
    Assertions.assertThat(event.getDescription()).isEqualTo(description);
  }

  @Test
  @Parameters(method = "parametersForTestFree")
  public void testFree(int basePrice, int maxPrice, boolean isFree) {
    Event event = Event.builder()
      .basePrice(basePrice)
      .maxPrice(maxPrice)
      .build();

    event.update();
    Assertions.assertThat(event.isFree()).isEqualTo(isFree);
  }

  private Object[] parametersForTestFree() {
    return new Object[] {
      new Object[] {0, 0, true},
      new Object[] {100, 0, false},
      new Object[] {0, 100, false},
      new Object[] {100, 200, false}
    };
  }
}

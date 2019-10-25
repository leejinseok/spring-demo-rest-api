package com.example.demo.accounts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

public class AccountSerializer extends JsonSerializer<Account> {

  @Override
  public void serialize(Account account, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("id", account.getId());
    gen.writeEndObject();
  }
}

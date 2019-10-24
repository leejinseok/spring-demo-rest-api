package com.example.demo.configs;


import com.example.demo.index.IndexController;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

public class ErrorsResource extends Resource<Errors> {
  public ErrorsResource(Errors content, Link... links) {
    super(content, links);
    add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
  }
}
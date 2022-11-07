package com.example.application.views.list;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginFormTest {
  private String username;
  private String password;

  @BeforeEach
  public void setupData() {
    username = "user";
    password = "userpass";
  }

  @Test
  public void loginFormTesting() {
    LoginForm formtest = new LoginForm();
    formtest.setAction("login");
    LoginEvent form = new LoginEvent(formtest, false, this.username, this.password);
    assertEquals("user", form.getUsername());
    assertEquals("userpass", form.getPassword());
  }
}
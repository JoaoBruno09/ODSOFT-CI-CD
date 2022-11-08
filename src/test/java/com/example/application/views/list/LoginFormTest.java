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
    // give values to username and password
    username = "user";
    password = "userpass";
  }

  @Test
  public void loginFormTesting() {
    // create new login form
    LoginForm formtest = new LoginForm();
    // say to form that is going to be a login action
    formtest.setAction("login");
    // create LoginEvent to pass to the form the username and password values
    LoginEvent form = new LoginEvent(formtest, false, this.username, this.password);
    // Verify the user and password values if they are correct
    assertEquals("user", form.getUsername());
    assertEquals("userpass", form.getPassword());
  }
}
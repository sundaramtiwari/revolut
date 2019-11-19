package com.revolut.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.domain.Account;
import com.revolut.domain.User;
import com.revolut.service.AccountService;
import com.revolut.service.UserService;
import com.revolut.util.RevolutContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static com.revolut.util.RevolutConstants.ACCOUNT_API;
import static com.revolut.util.RevolutConstants.API;
import static com.revolut.util.RevolutConstants.VERSION_1;
import static java.time.LocalDateTime.now;

@Path(API + VERSION_1 + ACCOUNT_API)
public class AccountAPI {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private AccountService accountService = RevolutContext.getAccountServiceInstance();
    private UserService userService = RevolutContext.getUserServiceInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
    public Response createTestAccount() throws JsonProcessingException {
        User user = userService.createUser("testUser_" + now(), "testUserEmail_" + now());
        Account account = accountService.createAccount(user, BigDecimal.valueOf(100));
        return Response.status(200).entity(MAPPER.writeValueAsString(account)).build();
    }
}
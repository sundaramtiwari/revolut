package com.revolut.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.domain.Transaction;
import com.revolut.service.TransactionService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.revolut.util.RevolutConstants.API;
import static com.revolut.util.RevolutConstants.TRANSFER_API;
import static com.revolut.util.RevolutConstants.VERSION_1;

@Path(API + VERSION_1 + TRANSFER_API)
public class TransferAPI {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private TransactionService transactionService = new TransactionService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(String payload) throws JsonProcessingException {
        JsonNode jsonNode = MAPPER.readTree(payload);
        Transaction transaction = transactionService.initiateTransfer(jsonNode.get("senderAccount").asText(),
                jsonNode.get("recipientAccount").asText(),
                jsonNode.get("amount").asText());

        return Response.status(200).entity(MAPPER.writeValueAsString(transaction)).build();
    }

}
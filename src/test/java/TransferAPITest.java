import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.domain.Account;
import com.revolut.domain.Transaction;
import com.revolut.rest.TransferAPI;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static com.revolut.domain.Transaction.TransactionStatus.SUCCESS;
import static com.revolut.rest.RevolutApplication.shutdown;
import static com.revolut.rest.RevolutApplication.start;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TransferAPITest {

    private static final Logger logger = LoggerFactory.getLogger(TransferAPI.class);

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(17.5);
    private static final String API_ENDPOINT = "http://localhost:8080/api/v1/";
    private static final String ACCOUNT_URI = "account";
    private static final String TRANSFER_URI = "transfer";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RequestSpecification request;

    @Before
    public void setup() {
        getDeamonThread().start();

        RestAssured.baseURI = API_ENDPOINT;
        request = RestAssured.given();
        request.contentType(ContentType.JSON);
    }

    @Test
    public void testAccountTransfer() throws Exception {
        Response response = request.get(ACCOUNT_URI);
        String accountString = response.getBody().asString();
        logger.info("Account Created: " + accountString);

        Account a1 = MAPPER.readValue(accountString, Account.class);
        BigDecimal initialSenderAccountBalance = a1.getBalance();

        response = request.get(ACCOUNT_URI);
        accountString = response.getBody().asString();
        logger.info("Account Created: " + accountString);

        Account a2 = MAPPER.readValue(accountString, Account.class);
        BigDecimal initialRecipientAccountBalance = a2.getBalance();

        String payload = "{\"senderAccount\":\"" + a1.getId() + "\", \"recipientAccount\":\"" + a2.getId() + "\", \"amount\":" + AMOUNT + " }";

        request.body(payload);
        response = request.post(TRANSFER_URI);
        String responseString = response.getBody().asString();
        logger.info("Transaction Created: " + responseString);

        Transaction transaction = MAPPER.readValue(responseString, Transaction.class);

        assertThat(transaction.getFromAccount(), is(a1));
        assertThat(transaction.getFromAccount().getBalance(), is(initialSenderAccountBalance.subtract(AMOUNT)));

        assertThat(transaction.getToAccount(), is(a2));
        assertThat(transaction.getToAccount().getBalance(), is(initialRecipientAccountBalance.add(AMOUNT)));

        assertThat(transaction.getCurrency(), is(a1.getCurrency()));
        assertThat(transaction.getStatus(), is(SUCCESS));

        shutdown();
    }

    private Thread getDeamonThread() {
        Runnable startRunnable = () -> {
            try {
                start();
            } catch (Exception e) {
                logger.error("Unable to start jetty.", e);
            }
        };

        return new Thread(startRunnable);
    }
}
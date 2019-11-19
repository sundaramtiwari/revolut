import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revolut.domain.Account;
import com.revolut.rest.RevolutApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.revolut.domain.Transaction.TransactionStatus.SUCCESS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TransferAPITest {

    private static final String AMOUNT = "17.5";
    private static final String API_ENDPOINT = "http://localhost:8080/api/v1/";
    private static final String ACCOUNT_URI = "account";
    private static final String TRANSFER_URI = "transfer";

    @Test
    public void testAccountTransfer() throws Exception {
        Thread startThread = getStartThread();
        startThread.start();

        Gson gson = new GsonBuilder().create();

        RestAssured.baseURI = API_ENDPOINT;
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);

        Response response = request.get(ACCOUNT_URI);
        String accountString = response.getBody().asString();

        Account a1 = gson.fromJson(accountString, Account.class);

        response = request.get(ACCOUNT_URI);
        accountString = response.getBody().asString();

        Account a2 = gson.fromJson(accountString, Account.class);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderAccount", a1.getId().toString());
        jsonObject.put("recipientAccount", a2.getId().toString());
        jsonObject.put("amount", AMOUNT);

        RequestSpecification postRequest = RestAssured.given();
        postRequest.contentType(ContentType.JSON);
        postRequest.body(jsonObject.toString());
        response = postRequest.post(TRANSFER_URI);
        String transactionString = response.getBody().asString();

        JSONObject jsonResponse = new JSONObject(transactionString);
        JSONObject fromAccountJson = jsonResponse.getJSONObject("fromAccount");
        JSONObject toAccountJson = jsonResponse.getJSONObject("toAccount");
        BigDecimal amount = jsonResponse.getBigDecimal("amount");
        String status = jsonResponse.getString("status");

        assertThat(fromAccountJson.getString("id"), is(a1.getId().toString()));
        assertThat(toAccountJson.getString("id"), is(a2.getId().toString()));
        assertThat(status, is(SUCCESS.name()));
        assertThat(amount, is(BigDecimal.valueOf(Double.parseDouble(AMOUNT))));

        RevolutApplication.shutdown();
    }

    private Thread getStartThread() {
        Runnable startRunnable = () -> {
            try {
                RevolutApplication.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        return new Thread(startRunnable);
    }

}
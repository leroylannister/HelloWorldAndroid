import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import static org.junit.Assert.*;

public class ApiServiceTest {
    private MockWebServer mockWebServer; // Variable for MockWebServer
    private ApiService apiService; // Variable for ApiService

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer(); // Initialize MockWebServer
        mockWebServer.start(); // Start the server

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/")) // Use MockWebServer's URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class); // Create ApiService instance
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown(); // Shut down the server after tests
    }

    @Test
    public void testGetItems() throws Exception {
        String mockResponse = "[{\"id\":1,\"name\":\"Item 1\"}]"; // Sample JSON response
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).setResponseCode(200)); // Enqueue response

        Call<List<Item>> call = apiService.getItems(); // Call API method
        Response<List<Item>> response = call.execute(); // Execute synchronously

        assertTrue(response.isSuccessful()); // Check if response is successful
        assertNotNull(response.body()); // Ensure body is not null
        assertEquals(1, response.body().size()); // Check number of items returned
        assertEquals("Item 1", response.body().get(0).getName()); // Validate item name
    }
}
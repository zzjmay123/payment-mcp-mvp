import java.io.*;
import java.net.*;

public class TestPayment {
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/mcp?session=test123";
        String json = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/call\",\"params\":{\"name\":\"create_payment\",\"arguments\":{\"amount\":49.00,\"order_id\":\"669649649\",\"user_id\":\"user_test\",\"payment_method\":\"alipay\",\"currency\":\"CNY\"}}}";
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        int code = con.getResponseCode();
        System.out.println("HTTP Response Code: " + code);
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response: " + response.toString());
        }
    }
}

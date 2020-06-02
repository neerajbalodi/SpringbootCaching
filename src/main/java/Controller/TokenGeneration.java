package Controller;

import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController

public class TokenGeneration {

    @GetMapping(value="/api/token", produces = "application/json")
    @Cacheable(value="token", key = "#username")
    public String generateToken(@RequestParam String url, @RequestParam String username, @RequestParam String password){

        String j_username;
        String j_password;
        int postDataLength = 2;
        String token = " ";

        try{
            URL apiv1url = new URL(url+"api/v1/check"+"?"+"j_username="+username+"&j_password="+password);
            HttpURLConnection conn = (HttpURLConnection) apiv1url.openConnection();
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.addRequestProperty("User-Agent", "SirionMobile");
            conn.addRequestProperty( "charset", "utf-8");
            conn.addRequestProperty( "Content-Length", Integer.toString(postDataLength));

            if(conn.getResponseCode()!=200){
                throw new RuntimeException("Failed : Http Error Code : " + conn.getResponseCode());
            }

            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            String jsonString = "";

            while((output = br.readLine()) != null){
                jsonString=output;
            }
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(jsonString);
            token = jsonObject.getString("refreshToken");



        }catch(Exception e){
            System.out.println("Failed to generate the token : " +e.getMessage());

        }


        return token;
    }
}

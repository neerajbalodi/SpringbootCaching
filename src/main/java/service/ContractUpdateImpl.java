package service;

import ConstantClients.ClientMapping;
import Controller.TokenGeneration;
import model.Contract;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ContractUpdateImpl {

    TokenGeneration tokenGeneration = new TokenGeneration();

    String url = "";
    String username = "";
    String password = "";
    String tokenTobeUsed="";
    String output;



    public String contractProcessed(String Contract, String Client, String Title){

        int ContractDBId = fetchContractDBId(Contract, Client);
        System.out.println("Start Calling Update API..." + ContractDBId);
        JSONObject postJsonObject = getJson(Title);
        postJsonObjectAPICall(postJsonObject);
        return Contract + " processed Successfully.";
    }

    public int fetchContractDBId(String contractId, String clientName){



        if(ClientMapping.clientMapping.containsKey(clientName)){

            String[] mapping = ClientMapping.clientMapping.get(clientName);
            url = mapping[0];
            username = mapping[1];
            password = mapping[2];

        }

        tokenTobeUsed = tokenGeneration.generateToken(url, username, password);
        System.out.println("Token Generated Successfully. " + tokenTobeUsed);

        // Sirion API Call
        int dbID = 0;
        try {

            URL geturl = new URL(url+"tblcontracts/getEntityId/"+contractId.substring(2)+"?am=true&entityTypeId=61");
            HttpURLConnection conn = (HttpURLConnection) geturl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", tokenTobeUsed);

            if(conn.getResponseCode()!=200){
                throw new RuntimeException("Failed : Http Error Code : " + conn.getResponseCode());
            }

            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            output = br.readLine();

            dbID= Integer.parseInt(output);



        } catch(Exception e) {
            System.out.println(e);
        }




        Map<String, String> hm1 = new HashMap<String, String>();

        return dbID;
    }

    public JSONObject getJson( String title) {
        JSONObject jsonObject = null;
        JSONObject j2 = null;
        try{
            URL getJsonurl = new URL(url +"contracts/edit/" + output);
            HttpURLConnection conn = (HttpURLConnection) getJsonurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", tokenTobeUsed);
            if(conn.getResponseCode()!=200){
                throw new RuntimeException("Failed : Http Error Code : " + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String getJsonoutput;
            String jsonString = "";
            while((getJsonoutput = br.readLine()) != null){
                jsonString=getJsonoutput;
            }
            jsonObject = new JSONObject(jsonString);
            jsonObject.getJSONObject("body").getJSONObject("data").getJSONObject("title").put("values", title);
            conn.disconnect();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return jsonObject;

    }


    public String postJsonObjectAPICall(JSONObject jsonObject){

        String output2 = "";

        try {
            URL url1 = new URL(url + "contracts/edit");
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
            conn1.setDoOutput(true);
            conn1.setRequestMethod("POST");
            conn1.setRequestProperty("Authorization", tokenTobeUsed);
            conn1.setRequestProperty("Content-Type", "application/json");

            // String input = jsonObject.toString();


            OutputStream os = conn1.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();

            if(conn1.getResponseCode()!=200){
                throw new RuntimeException("Failed : Http Error Code : " + conn1.getResponseCode());
            }

            BufferedReader broutput = new BufferedReader(new InputStreamReader(
                    (conn1.getInputStream())));

            System.out.println("Output from Server .... \n");
            while ((output2 = broutput.readLine()) != null) {
                System.out.println(output2);
            }

        } catch (Exception e){
            System.out.println(e);
        }
        return output2;

    }

}

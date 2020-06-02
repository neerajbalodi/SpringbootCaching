package Controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import model.*;
import service.*;

@RestController

public class ContractUpdation {

    @Autowired
    private TokenGeneration tokenGeneration;

    ContractUpdateImpl contractUpdateImpl = new ContractUpdateImpl();


    @PostMapping(value="/api/UpdateContract", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String updateContract(@RequestBody Contract contract){

        return contractUpdateImpl.contractProcessed(contract.getContract(), contract.getClient(), contract.getTitle());

       // return JSONObject.quote("Request Processed Successfully");
    }

}

package ConstantClients;

import java.util.HashMap;
import java.util.Map;

public class ClientMapping {

    public static Map<String, String[]> clientMapping;

static {
    clientMapping = new HashMap<>();
    String[] vfMapping = {"https://test.com/", "test.user", "7da12b"};
    clientMapping.put("vf", vfMapping);
}

    public static Map<String, String[]> getclientMapping() {
        return clientMapping;
    }



}

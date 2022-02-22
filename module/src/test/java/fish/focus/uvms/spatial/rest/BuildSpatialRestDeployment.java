package fish.focus.uvms.spatial.rest;

import java.util.Arrays;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import fish.focus.uvms.commons.date.JsonBConfigurator;
import fish.focus.uvms.rest.security.UnionVMSFeature;
import fish.focus.uvms.spatial.service.bean.BuildServiceDeployment;
import fish.focus.uvms.usm.jwt.JwtTokenHandler;

public abstract class BuildSpatialRestDeployment extends BuildServiceDeployment {

    @Inject
    private JwtTokenHandler tokenHandler;

    private String token;

    protected WebTarget getWebTarget() {
        Client client = ClientBuilder.newClient();
        client.register(JsonBConfigurator.class);
        return client.target("http://localhost:8080/test/spatialnonsecure");
    }
    
    protected WebTarget getSecuredWebTarget() {
        Client client = ClientBuilder.newClient();
        client.register(JsonBConfigurator.class);
        return client.target("http://localhost:8080/test/rest");
    }

    protected String getToken() {
        if (token == null) {
            token = tokenHandler.createToken("user",
                    Arrays.asList(UnionVMSFeature.manageManualMovements.getFeatureId(),
                            UnionVMSFeature.viewMovements.getFeatureId(),
                            UnionVMSFeature.viewManualMovements.getFeatureId(),
                            UnionVMSFeature.manageAlarmsHoldingTable.getFeatureId(),
                            UnionVMSFeature.viewAlarmsHoldingTable.getFeatureId()));
        }
        return token;
    }
}

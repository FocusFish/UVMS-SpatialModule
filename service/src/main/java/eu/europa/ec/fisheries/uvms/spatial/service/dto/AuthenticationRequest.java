package eu.europa.ec.fisheries.uvms.spatial.service.dto;

import java.io.Serializable;

/**
 * //TODO create test
 */
public class AuthenticationRequest implements Serializable{

    private static final long serialVersionUID = -8088379939241999781L;

    private String userName, password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

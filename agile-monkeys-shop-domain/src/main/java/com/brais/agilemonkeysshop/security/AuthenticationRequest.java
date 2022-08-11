package com.brais.agilemonkeysshop.security;

import java.io.Serializable;

public record AuthenticationRequest(String username, String password) implements Serializable {

}

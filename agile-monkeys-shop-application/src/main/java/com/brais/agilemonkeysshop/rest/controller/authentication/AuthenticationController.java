package com.brais.agilemonkeysshop.rest.controller.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brais.agilemonkeysshop.security.AuthenticationRequest;
import com.brais.agilemonkeysshop.security.TokenInfo;
import com.brais.agilemonkeysshop.security.service.JwtUtilService;
import com.brais.agilemonkeysshop.security.service.UserSecurityService;

@RestController
@RequestMapping("${openapi.agilemonkeysshop.base-path:/agile-monkeys/v1}")
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserSecurityService userSecurityService;

  @Autowired
  private JwtUtilService jwtUtilService;

  @PostMapping("/public/authenticate")
  public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password()));

    final UserDetails userDetails = userSecurityService.loadUserByUsername(authenticationRequest.username());

    final String jwt = jwtUtilService.generateToken(userDetails);

    return ResponseEntity.ok(new TokenInfo(jwt));
  }

}

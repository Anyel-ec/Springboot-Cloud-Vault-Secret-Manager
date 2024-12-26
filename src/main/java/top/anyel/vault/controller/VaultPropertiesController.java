package top.anyel.vault.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
 * Author: Anyel EC
 * Github: https://github.com/Anyel-ec
 * Creation date: 26/12/2024
 */
@RestController
public class VaultPropertiesController {

    @Value("${username:defaultUser}")
    private String username;

    @Value("${password:defaultPassword}")
    private String password;

    @GetMapping("/vault/properties")
    public Map<String, String> getVaultProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("password", password);
        return properties;
    }
}

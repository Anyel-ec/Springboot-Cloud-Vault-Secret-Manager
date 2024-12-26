package top.anyel.vault.controller;

/*
 * Author: Anyel EC
 * Github: https://github.com/Anyel-ec
 * Creation date: 26/12/2024
 */
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;


@Component
public class VaultConnectionTest {

    @Autowired
    private VaultTemplate vaultTemplate;

    @PostConstruct
    public void testConnection() {
        try {
            VaultResponse response = vaultTemplate.read("secret/data/myapp");
            if (response != null && response.getData() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) response.getData().get("data");
                if (data != null) {
                    System.out.println("Vault Connection Successful!");
                    System.out.println("Data: " + data);
                } else {
                    System.out.println("Vault Connection Failed: No data found.");
                }
            } else {
                System.out.println("Vault Connection Failed: Response is null.");
            }
        } catch (Exception e) {
            System.err.println("Error connecting to Vault: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

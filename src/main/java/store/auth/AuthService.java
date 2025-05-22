package store.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import store.account.AccountController;
import store.account.AccountIn;
import store.account.AccountOut;

@Service
public class AuthService {

    @Autowired
    private AccountController accountController;

    @Autowired
    private JwtService jwtService;

    public String register(Register register) {
        AccountIn accountIn = AccountIn.builder()
            .email(register.email())
            .name(register.name())
            .password(register.password())
            .build();

        // registrar no account
        // aqui estou substituindo o RestTemplate
        ResponseEntity<AccountOut> response = accountController.create(accountIn);
        AccountOut accountOut = response.getBody();

        // gerar o token
        // regra de geracao de token
        return generateToken(accountOut.id());
    }

    public String login(String email, String password) {

        ResponseEntity<AccountOut> response = accountController.findByEmailAndPassword(
            AccountIn.builder()
                .email(email)
                .password(password)
                .build()
        );
        AccountOut accountOut = response.getBody();

        return generateToken(accountOut.id());
    }
    private String generateToken(String id) {
        Date notBefore = new Date();
        int daysToExpire = 30;
        Date expiration = new Date(notBefore.getTime() + 1000L * 60 * 60 * 24 * daysToExpire);
        String token = jwtService.create(id, notBefore, expiration);
        return token;
    }

    public SolveOut solve(String token) {
        return SolveOut.builder()
            .idAccount(jwtService.getId(token))
            .build();
    }

}

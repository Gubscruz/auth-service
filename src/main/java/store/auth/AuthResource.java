package store.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthResource implements AuthController {

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<TokenOut> register(RegisterIn registerIn) {
        String token = authService.register(AuthParser.to(registerIn));
        return ResponseEntity.ok().body(AuthParser.to(token));
    }

    @Override
    public ResponseEntity<TokenOut> login(LoginIn loginIn) {
        String token = authService.login(loginIn.email(), loginIn.password());
        return ResponseEntity.ok().body(AuthParser.to(token));
    }

    @Override
    public ResponseEntity<SolveOut> solve(TokenOut token) {
        return ResponseEntity.ok().body(
            authService.solve(token.token())
        );
    }

}

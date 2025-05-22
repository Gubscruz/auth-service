package store.auth;

public class AuthParser {

    public static Register to(RegisterIn in) {
        return in == null ? null :
            Register.builder()
                .name(in.name())
                .email(in.email())
                .password(in.password())
                .build();
    }

    public static TokenOut to(String token) {
        return token == null ? null :
            TokenOut.builder()
                .token(token)
                .build();
    }
    
}

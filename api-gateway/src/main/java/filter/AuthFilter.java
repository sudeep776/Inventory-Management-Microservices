package filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements WebFilter {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Allow /register and /authenticate calls without auth
        if (path.contains("/register/user") || path.contains("/authenticate")) {
            return chain.filter(exchange);
        }

        // Extract Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Call Auth Service /verify
        return webClientBuilder.build()
                .post()
                .uri("http://authservice/api/auth/verify") // verify endpoint in authservice
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .onStatus(
                        org.springframework.http.HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("Auth Service Error"))
                )
                .bodyToMono(String.class)
                .flatMap(response -> {
                    // Log the response (for debugging)
                    System.out.println("Auth Service verify response: " + response);

                    if (response == null || response.isEmpty()) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // Verified, continue to the downstream service
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}

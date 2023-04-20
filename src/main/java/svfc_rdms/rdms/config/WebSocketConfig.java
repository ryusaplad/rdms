package svfc_rdms.rdms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String endpointPath = "/websocket-server";

        registry.addEndpoint(endpointPath)
        .setAllowedOriginPatterns("*")
        .withSockJS()
        .setInterceptors(httpSessionIdHandshakeInterceptor())
        .setStreamBytesLimit(512 * 1024)
        .setHttpMessageCacheSize(1000)
        .setDisconnectDelay(30_000)
        .setWebSocketEnabled(false); // disable credential support
    }

    @Bean
    public HttpSessionHandshakeInterceptor httpSessionIdHandshakeInterceptor() {
        return new HttpSessionHandshakeInterceptor();
    }

    
}





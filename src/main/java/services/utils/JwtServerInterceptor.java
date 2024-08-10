package services.utils;

import io.grpc.*;

import io.jsonwebtoken.Claims;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import services.constants.Constants;

public class JwtServerInterceptor implements ServerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JwtServerInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(Constants.AUTHORIZATION_METADATA_KEY);

        if (authHeader == null || !authHeader.startsWith(Constants.BEARER_TYPE + " ")) {
            logger.warn("Request missing or with invalid Authorization header");
            call.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid Authorization header"), headers);
            return new ServerCall.Listener<>() {
            };
        }

        String token = authHeader.substring(Constants.BEARER_TYPE.length()).trim();

        try {
            Claims claims = JwtUtility.validateToken(token);
            Context ctx = Context.current().withValue(Constants.CLIENT_ID_CONTEXT_KEY, claims.getSubject());
            logger.info("Authenticated request from client: {}", claims.getSubject());
            return Contexts.interceptCall(ctx, call, headers, next);
        } catch (Exception e) {
            logger.warn("Authentication failed: {}", e.getMessage());
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid token"), headers);
            return new ServerCall.Listener<>() {
            };
        }
    }
}

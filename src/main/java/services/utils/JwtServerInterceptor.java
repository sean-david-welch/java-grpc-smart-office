package services.utils;

import io.grpc.*;
import io.jsonwebtoken.Claims;
import services.constants.Constants;

public class JwtServerInterceptor implements ServerInterceptor {

    private static final System.Logger logger = System.getLogger(JwtServerInterceptor.class.getName());

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(Constants.AUTHORIZATION_METADATA_KEY);

        if (authHeader == null || !authHeader.startsWith(Constants.BEARER_TYPE + " ")) {
            logger.log(System.Logger.Level.WARNING, "Request missing or with invalid Authorization header");
            call.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid Authorization header"), headers);
            return new ServerCall.Listener<>() {
            };
        }

        String token = authHeader.substring(Constants.BEARER_TYPE.length()).trim();

        try {
            Claims claims = JwtUtility.validateToken(token);
            Context ctx = Context.current().withValue(Constants.CLIENT_ID_CONTEXT_KEY, claims.getSubject());
            logger.log(System.Logger.Level.INFO, "Authenticated request from client: {0}", claims.getSubject());
            return Contexts.interceptCall(ctx, call, headers, next);
        } catch (Exception e) {
            logger.log(System.Logger.Level.WARNING, "Authentication failed: {0}", e.getMessage());
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid token"), headers);
            return new ServerCall.Listener<>() {
            };
        }
    }
}

package example;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.web.*.*(..))")

    public void logBefore(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Anonymous";
        log.info("User: {} called endpoint: {}", username,
                joinPoint.getSignature().toShortString());

        // logger.info("Endpoint called: {}", joinPoint.getSignature().toShortString());
    }
}

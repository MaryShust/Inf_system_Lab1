package infs.lab.aop;

import infs.lab.config.AppConfig;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.stat.spi.StatisticsImplementor;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CacheLoggingAspect {

    private final StatisticsImplementor statistics;
    AppConfig appConfig;

    public CacheLoggingAspect(EntityManagerFactory entityManagerFactory, AppConfig appConfig) {
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        this.statistics = sessionFactory.getStatistics();
        this.statistics.setStatisticsEnabled(true);
        this.appConfig = appConfig;
    }

    @Around("@annotation(infs.lab.aop.CacheLogging)")
    public Object logCacheStatistics(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!appConfig.isEnabledLogCache()) {
            return joinPoint.proceed();
        }
        long hitsBefore = statistics.getSecondLevelCacheHitCount();
        long missesBefore = statistics.getSecondLevelCacheMissCount();
        Object result = joinPoint.proceed();
        long hitDelta = statistics.getSecondLevelCacheHitCount() - hitsBefore;
        long missDelta = statistics.getSecondLevelCacheMissCount() - missesBefore;
        log.info(
                "L2 cache stats for {} -> hits: {}, misses: {}",
                joinPoint.getSignature().toShortString(),
                hitDelta,
                missDelta);
        return result;
    }
}
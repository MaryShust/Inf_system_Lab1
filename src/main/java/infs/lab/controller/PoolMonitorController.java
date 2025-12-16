package infs.lab.controller;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PoolMonitorController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/api/pool-info")
    public Map<String, Object> getPoolInfo() throws SQLException {
        Map<String, Object> info = new HashMap<>();

        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;

            info.put("poolName", hikariDataSource.getPoolName());
            info.put("maximumPoolSize", hikariDataSource.getMaximumPoolSize());
            info.put("minimumIdle", hikariDataSource.getMinimumIdle());
            info.put("activeConnections", hikariDataSource.getHikariPoolMXBean().getActiveConnections());
            info.put("idleConnections", hikariDataSource.getHikariPoolMXBean().getIdleConnections());
            info.put("totalConnections", hikariDataSource.getHikariPoolMXBean().getTotalConnections());
            info.put("threadsAwaitingConnection", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }

        return info;
    }
}
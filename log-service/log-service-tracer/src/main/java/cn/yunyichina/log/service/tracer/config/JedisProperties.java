package cn.yunyichina.log.service.tracer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/5/1 22:35
 * @Description:
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jedis")
public class JedisProperties {

    private String ip;
    private int port;
    private String password;

    private Cluster cluster;

    /**
     * Cluster properties.
     */
    @Getter
    @Setter
    public static class Cluster {

        private List<HostAndPort> nodes;

        /**
         * Maximum number of redirects to follow when executing commands across the
         * cluster.
         */
        private Integer maxRedirects;

        public Set<redis.clients.jedis.HostAndPort> getJedisHostAndPort() {
            if (nodes == null || nodes.isEmpty()) {
                return new HashSet<>();
            } else {
                Set<redis.clients.jedis.HostAndPort> hapSet = new HashSet<>(nodes.size());
                for (HostAndPort node : nodes) {
                    hapSet.add(node.convert());
                }
                return hapSet;
            }
        }

    }

    @Setter
    @Getter
    public static class HostAndPort {
        private String ip;
        private int port;

        public redis.clients.jedis.HostAndPort convert() {
            return new redis.clients.jedis.HostAndPort(ip, port);
        }
    }

}

package pl.agol.openshiftinfinispan.infrastructure;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.agol.openshiftinfinispan.OpenshiftInfinispanApplication;

@Configuration
public class CacheClusterConfiguration {

    public static final String WEATHER_CACHE_MANAGER_NAME = "TestCacheManager";
    private static final String CACHE_CLUSTER_NAME = "test-service-cluster";
    private final String cacheClusterConfigFile;

    @Autowired
    CacheClusterConfiguration(
            @Value("${service.cache.clusterConfigFile:jgroups-configuration.xml}") String cacheClusterConfigFile
    ) {
        this.cacheClusterConfigFile = cacheClusterConfigFile;
    }

    @Bean
    public EmbeddedCacheManager cacheContainer() {
        return new DefaultCacheManager(withGlobalConfigurationThat());
    }

    private GlobalConfiguration withGlobalConfigurationThat() {
        return new GlobalConfigurationBuilder()
                .transport().defaultTransport()
                .clusterName(CACHE_CLUSTER_NAME)
                .addProperty("configurationFile", cacheClusterConfigFile)
                .globalJmxStatistics()
                .cacheManagerName(WEATHER_CACHE_MANAGER_NAME)
                .jmxDomain(OpenshiftInfinispanApplication.class.getCanonicalName())
                .build();
    }
}


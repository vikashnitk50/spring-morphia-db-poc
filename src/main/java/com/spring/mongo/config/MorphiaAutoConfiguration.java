package com.spring.mongo.config;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.spring.mongo.core.MultitenantDatastoreFactory;

@Configuration
@PropertySource("classpath:/com/spring/mongo/properties/mongodb.properties")
public class MorphiaAutoConfiguration {

	private static final Logger LOG = LoggerFactory
			.getLogger(MultitenantDatastoreFactory.class);

	@Value("${mongodb.host}")
	private String host;

	@Value("#{new Integer('${mongodb.port}')}")
	private Integer port;

	@Value("${mongodb.dbName}")
	private String defaultDBName;

	@Value("${mongodb.base.package}")
	private String basePackage;

	@Value("#{new Integer('${mongodb.maxConnectionsPerHost}')}")
	private Integer maxConnectionsPerHost;

	@Value("#{new Integer('${mongodb.minConnectionsPerHost}')}")
	private Integer minConnectionsPerHost;

	public MorphiaAutoConfiguration() {
	}

	@Bean
	public MongoClient mongoClient() {
		return createMongoClient();
	}

	public MongoClient createMongoClient() {
		MongoClientOptions mongoOptions = MongoClientOptions.builder()
				.minConnectionsPerHost(minConnectionsPerHost)
				.connectionsPerHost(maxConnectionsPerHost).socketTimeout(60000)
				.connectTimeout(1200000).build();
		MongoClient mongoClient = new MongoClient(
				new ServerAddress(host, port), mongoOptions);
		mongoClient.setWriteConcern(WriteConcern.MAJORITY);
		return mongoClient;
	}

	@Bean
	public Datastore datastore() throws Exception {
		return createDataStore(mongoClient(), defaultDBName, Boolean.TRUE);
	}

	public Datastore createDataStore(MongoClient mongoClient, String dbName,
			boolean indexRequired) {
		Morphia morphia = new Morphia();
		ClassPathScanningCandidateComponentProvider entityScanner = new ClassPathScanningCandidateComponentProvider(
				true);
		entityScanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		for (BeanDefinition candidate : entityScanner
				.findCandidateComponents(basePackage)) {
			try {
				morphia.map(Class.forName(candidate.getBeanClassName()));
			} catch (ClassNotFoundException ex) {
				LOG.info("Unable to load the class:" + ex.getMessage());
			}
		}
		Datastore datastore = morphia.createDatastore(mongoClient, dbName);
		if (indexRequired) {
			datastore.ensureIndexes();
		}
		return datastore;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();

	}

	public String getDefaultDBName() {
		return defaultDBName;
	}

}
package com.wallet.wallet_discovery_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class WalletDiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletDiscoveryServerApplication.class, args);
	}
}

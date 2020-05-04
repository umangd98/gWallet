package com.example.gWallet;

import com.example.gWallet.Repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EntityScan(
//		basePackageClasses = {GWalletApplication.class, Jsr310JpaConverters.class}
//)
//(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class GWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(GWalletApplication.class, args);
	}

}

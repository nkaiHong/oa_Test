package org.fkjava.identity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan("org.fkjava")
@EnableJpaRepositories // 激活JPA的自动DAO扫描
public class IdentityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		//单独的
		// PasswordEncoder encoder = new BCryptPasswordEncoder();
		// PasswordEncoder encoder = new SCryptPasswordEncoder();
		
		//用代理实现的，对于后期的修改比较方便
		Map<String,PasswordEncoder> map = new HashMap<>();
		map.put("B", new BCryptPasswordEncoder());
		map.put("S", new SCryptPasswordEncoder());
		
		// 第一个参数表示默认的编码器
		DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("S", map);
		// 在没有找到密码的加密器的时候，使用BCryptPasswordEncoder
		encoder.setDefaultPasswordEncoderForMatches(map.get("B"));
		return encoder;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(IdentityConfig.class, args);
	}
}

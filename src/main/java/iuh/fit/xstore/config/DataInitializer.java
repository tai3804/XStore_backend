package iuh.fit.xstore.config;

import iuh.fit.xstore.model.*;
import iuh.fit.xstore.repository.AccountRepository;
import iuh.fit.xstore.repository.UserRepository;
import iuh.fit.xstore.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AccountRepository accountRepo,
                               UserRepository userRepo,
                               PasswordEncoder passwordEncoder) {
        return args -> {

            // --- Tạo admin nếu chưa tồn tại ---
            if (!userRepo.existsByAccountUsername("admin")) {
                User adminUser = User.builder()
                        .firstName("admin")
                        .lastName("default")
                        .account(
                                Account.builder()
                                        .username("admin")
                                        .password(passwordEncoder.encode("admin"))
                                        .role(Role.ADMIN)
                                        .build()
                        )
                        .cart(
                                Cart.builder()
                                        .total(0)
                                        .build()
                        )
                        .build();

                userRepo.save(adminUser);
                log.info("Default admin user created: username=admin, password=admin");
            }

            // --- Tạo customer 'hien' nếu chưa tồn tại ---
            if (!userRepo.existsByAccountUsername("hien")) {
                User customerUser = User.builder()
                        .firstName("Hien")
                        .lastName("Nguyen")
                        .account(
                                Account.builder()
                                        .username("hien")
                                        .password(passwordEncoder.encode("hien"))
                                        .role(Role.CUSTOMER)
                                        .build()
                        )
                        .cart(
                                Cart.builder()
                                        .total(0)
                                        .build()
                        )
                        .build();

                userRepo.save(customerUser);
                log.info("Default customer user created: username=hien, password=hien");
            }

            if (!userRepo.existsByAccountUsername("loc")) {
                User customerUser = User.builder()
                        .firstName("Loc")
                        .lastName("Nguyen")
                        .account(
                                Account.builder()
                                        .username("loc")
                                        .password(passwordEncoder.encode("loc"))
                                        .role(Role.CUSTOMER)
                                        .build()
                        )
                        .cart(
                                Cart.builder()
                                        .total(0)
                                        .build()
                        )
                        .build();

                userRepo.save(customerUser);
                log.info("Default customer user created: username=hien, password=hien");
            }

            if (!userRepo.existsByAccountUsername("ly")) {
                User customerUser = User.builder()
                        .firstName("Ly")
                        .lastName("Nguyen")
                        .account(
                                Account.builder()
                                        .username("ly")
                                        .password(passwordEncoder.encode("ly"))
                                        .role(Role.CUSTOMER)
                                        .build()
                        )
                        .cart(
                                Cart.builder()
                                        .total(0)
                                        .build()
                        )
                        .build();

                userRepo.save(customerUser);
                log.info("Default customer user created: username=hien, password=hien");
            }

            if (!userRepo.existsByAccountUsername("thuy")) {
                User customerUser = User.builder()
                        .firstName("Thuy")
                        .lastName("Nguyen")
                        .account(
                                Account.builder()
                                        .username("thuy")
                                        .password(passwordEncoder.encode("thuy"))
                                        .role(Role.CUSTOMER)
                                        .build()
                        )
                        .cart(
                                Cart.builder()
                                        .total(0)
                                        .build()
                        )
                        .build();

                userRepo.save(customerUser);
                log.info("Default customer user created: username=hien, password=hien");
            }

            if (!userRepo.existsByAccountUsername("thanh")) {
                User customerUser = User.builder()
                        .firstName("Thanh")
                        .lastName("Nguyen")
                        .account(
                                Account.builder()
                                        .username("thanh")
                                        .password(passwordEncoder.encode("thanh"))
                                        .role(Role.CUSTOMER)
                                        .build()
                        )
                        .cart(
                                Cart.builder()
                                        .total(0)
                                        .build()
                        )
                        .build();

                userRepo.save(customerUser);
                log.info("Default customer user created: username=hien, password=hien");
            }

            if (!userRepo.existsByAccountUsername("tai")) {
                User customerUser = User.builder()
                        .firstName("Tai")
                        .lastName("Tran")
                        .account(
                                Account.builder()
                                        .username("tai")
                                        .password(passwordEncoder.encode("tai"))
                                        .role(Role.CUSTOMER)
                                        .build()
                        )
                        .cart(
                                Cart.builder()
                                        .total(0)
                                        .build()
                        )
                        .build();

                userRepo.save(customerUser);
                log.info("Default customer user created: username=tai, password=tai");
            }

        };
    }

    // Chay data.sql chi lan dau tien khi database duoc tao
    @Bean
    CommandLineRunner initializeData(DataSource dataSource, ProductRepository productRepo) {
        return args -> {
            // Kiểm tra xem dữ liệu đã được tải chưa bằng cách kiểm tra products table
            if (productRepo.count() == 0) {
                try (Connection connection = dataSource.getConnection()) {
                    log.info("Database is empty. Running data.sql...");
                    ScriptUtils.executeSqlScript(connection, new ClassPathResource("data.sql"));
                    log.info("data.sql executed successfully!");
                } catch (Exception e) {
                    log.error("Error executing data.sql: {}", e.getMessage());
                }
            } else {
                log.info("Database already has data. Skipping data.sql initialization.");
            }
        };


    }
}

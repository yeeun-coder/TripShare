package com.inhatc.tripshare.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class MysqlPasswordValidator implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String password = environment.getProperty("spring.datasource.password");
        if (password != null && !password.isBlank()
                && !password.contains("YOUR_MYSQL")) {
            return;
        }

        System.err.println("""
                
                ========================================
                MySQL 비밀번호가 설정되지 않았습니다!
                ========================================
                
                아래 중 하나를 실행하세요:
                
                [방법 1] 로컬 설정 파일 (권장)
                  cd server/src/main/resources
                  cp application-local.yml.example application-local.yml
                  # application-local.yml 에 MySQL root 비밀번호 입력
                
                [방법 2] 환경 변수
                  export MYSQL_PASSWORD=본인_MySQL_비밀번호
                  ./gradlew bootRun
                
                DB 생성 (최초 1회):
                  mysql -u root -p < database/tripshare.sql
                
                ========================================
                """);

        throw new IllegalStateException("MySQL password not configured. See instructions above.");
    }
}

package com.example.demo.Checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseChecker implements CommandLineRunner {

    // Gọi công cụ chọc database của Spring Boot
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("⏳ Đang kiểm tra kết nối tới Oracle Database...");
        
        try {
            // Câu lệnh "SELECT 1 FROM dual" là cách chuẩn để test mạng của Oracle
            jdbcTemplate.execute("SELECT 1 FROM dual");
            
            System.out.println("=====================================================");
            System.out.println("✅ THÀNH CÔNG: ĐÃ KẾT NỐI VÀ ĐĂNG NHẬP ORACLE DATABASE!");
            System.out.println("=====================================================");
            
        } catch (Exception e) {
            System.out.println("=====================================================");
            System.out.println("❌ THẤT BẠI: KHÔNG THỂ KẾT NỐI DATABASE. LỖI Ở BÊN DƯỚI:");
            System.out.println(e.getMessage());
            System.out.println("=====================================================");
        }
    }
}
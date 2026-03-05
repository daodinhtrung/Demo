package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FtpOptimizedService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${ftp.server}") private String server;
    @Value("${ftp.port}") private int port;
    @Value("${ftp.username}") private String username;
    @Value("${ftp.password}") private String password;
    @Value("${ftp.filepath}") private String filePath;

    public String syncDataToOracle() {
        FTPClient ftpClient = new FTPClient();
        int totalSaved = 0;

        String sql = "INSERT INTO NEIF_DUMP_TEMP (MESSAGE_REFERENCE, IMSI, MSISDN, NEIF_INFORMATION, " +
                     "ACCOUNT_PROFILE, TIME_STAMP, REFILL_COUNT, MAIN_BONUS, BONUS_AMOUNT, " +
                     "SCRATCH_CARD_NUMBER, SCRATCH_CARD_PROFILE, TAC) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            InputStream inputStream = ftpClient.retrieveFileStream(filePath);
            if (inputStream == null) return "Không tìm thấy file: " + filePath;

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<Object[]> batchArgs = new ArrayList<>();
            int BATCH_SIZE = 1000; 

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                if (columns.length >= 12) {
                    Double bonusAmount = 0.0;
                    try { bonusAmount = Double.parseDouble(columns[8].trim()); } catch (Exception e) {}

                    // Gắn 12 giá trị tương ứng với 12 dấu chấm hỏi (?)
                    Object[] singleRow = new Object[] {
                        columns[0].trim(), columns[1].trim(), columns[2].trim(), columns[3].trim(),
                        columns[4].trim(), columns[5].trim(), columns[6].trim(), columns[7].trim(),
                        bonusAmount, columns[9].trim(), columns[10].trim(), columns[11].trim()
                    };
                    
                    batchArgs.add(singleRow);

                    if (batchArgs.size() >= BATCH_SIZE) {
                        jdbcTemplate.batchUpdate(sql, batchArgs);
                        totalSaved += batchArgs.size();
                        batchArgs.clear(); 
                        System.out.println("Đã insert thành công " + totalSaved + " dòng...");
                    }
                }
            }

            if (!batchArgs.isEmpty()) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
                totalSaved += batchArgs.size();
            }

            reader.close();
            inputStream.close();
            ftpClient.disconnect();

            return "Tuyệt vời! Đã đẩy thành công " + totalSaved + " bản ghi vào Oracle.";

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Lỗi: " + ex.getMessage();
        }
    }
}
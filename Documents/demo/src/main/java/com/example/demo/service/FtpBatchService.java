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
import org.springframework.stereotype.Service;

import com.example.demo.entity.RefillDataEntity;
import com.example.demo.repository.RefillDataRepository;

@Service
public class FtpBatchService {

    @Autowired
    private RefillDataRepository repository;

    @Value("${ftp.server}") private String server;
    @Value("${ftp.port}") private int port;
    @Value("${ftp.username}") private String username;
    @Value("${ftp.password}") private String password;
    @Value("${ftp.filepath}") private String filePath;

    public String syncLargeData() {
        FTPClient ftpClient = new FTPClient();
        int totalSaved = 0;

        try {
            // 1. Kết nối FTP
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 2. Tải file stream
            InputStream inputStream = ftpClient.retrieveFileStream(filePath);
            if (inputStream == null) return "Không tìm thấy file: " + filePath;

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            
            List<RefillDataEntity> batchList = new ArrayList<>();
            int BATCH_SIZE = 1000; // Cứ đủ 1000 dòng thì lưu 1 lần

            // 3. Đọc từng dòng và xử lý Batch
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(","); // Tách bằng dấu phẩy

                if (columns.length >= 12) {
                    RefillDataEntity entity = new RefillDataEntity();
                    entity.setMessageReference(columns[0].trim());
                    entity.setImsi(columns[1].trim());
                    entity.setMsisdn(columns[2].trim());
                    entity.setNeifInformation(columns[3].trim());
                    entity.setAccountProfile(columns[4].trim());
                    entity.setTimeStamp(columns[5].trim());
                    entity.setRefillCount(columns[6].trim());
                    entity.setMainBonus(columns[7].trim());
                    
                    try {
                        entity.setBonusAmount(Double.parseDouble(columns[8].trim()));
                    } catch (Exception e) {
                        entity.setBonusAmount(0.0);
                    }
                    
                    entity.setScratchCardNumber(columns[9].trim());
                    entity.setScratchCardProfile(columns[10].trim());
                    entity.setTac(columns[11].trim());

                    batchList.add(entity);

                    // Khi danh sách đạt 1000 dòng -> Lưu vào Database -> Xóa danh sách để chứa đợt mới
                    if (batchList.size() >= BATCH_SIZE) {
                        repository.saveAll(batchList);
                        totalSaved += batchList.size();
                        batchList.clear(); // Dọn dẹp RAM
                        System.out.println("Đã lưu đợt: " + totalSaved + " bản ghi...");
                    }
                }
            }

            // 4. Lưu nốt những dòng còn lẻ (nếu tổng số dòng không chia hết cho 1000)
            if (!batchList.isEmpty()) {
                repository.saveAll(batchList);
                totalSaved += batchList.size();
            }

            // 5. Đóng kết nối
            reader.close();
            inputStream.close();
            ftpClient.completePendingCommand();
            ftpClient.disconnect();

            return "Thành công! Đã đồng bộ tổng cộng " + totalSaved + " bản ghi vào Oracle.";

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Lỗi trong quá trình đồng bộ: " + ex.getMessage();
        }
    }
}

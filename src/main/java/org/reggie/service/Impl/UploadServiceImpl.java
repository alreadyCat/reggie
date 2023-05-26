package org.reggie.service.Impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.reggie.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

  @Value("${qcloud.oss.secretId}")
  private String secretId;

  @Value("${qcloud.oss.secretKey}")
  private String secretKey;

  @Value("${qcloud.oss.bucketName}")
  private String bucketName;

  @Value("${qcloud.oss.regionStr}")
  private String regionStr;

  @Value("${qcloud.oss.bucketPath}")
  private String bucketPath;

  private String Local_Path = "D:\\COSUploadTempFile\\";

  private COSClient initCosClient() {

    BasicCOSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
    Region region = new Region(regionStr);
    ClientConfig clientConfig = new ClientConfig(region);
    COSClient client = new COSClient(cred, clientConfig);
    return client;
  }

//  没有解决文件直接上传到云，现在是存储在本地后再上传到云
  private void deleteLocalFile(String path){
    File file = new File(path);
    file.delete();
  }

  @Override
  public String uploadFile(MultipartFile file) {
    log.info("COS文件上传:{}", file);
    String originalFilename = file.getOriginalFilename();
    int index = originalFilename.lastIndexOf(".");
    String suffixName = originalFilename.substring(index);
    String uniqueFileName = UUID.randomUUID().toString() + suffixName;

    String savedLocalPath = Local_Path + uniqueFileName;
    try {
      file.transferTo(new File(savedLocalPath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String url = "";
    try {

      COSClient cosClient = initCosClient();

      File localFile = new File(savedLocalPath);
      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, originalFilename, localFile);
      cosClient.putObject(putObjectRequest);

      url = bucketPath + originalFilename;
    } catch (CosServiceException serverException) {
      serverException.printStackTrace();
    }
    deleteLocalFile(savedLocalPath);
    return url;
  }
}

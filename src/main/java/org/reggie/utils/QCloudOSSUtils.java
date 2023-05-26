package org.reggie.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QCloudOSSUtils {
    @Value("${qcloud.oss.secretId}")
    private static String secretId;
    @Value("${qcloud.oss.secretKey}")
    private static String secretKey ;
    @Value("${qcloud.oss.bucketName}")
    private static String bucketName;
    @Value("${qcloud.oss.regionStr}")
    private static String regionStr; // 区域北京则  beijing

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("C:\\Users\\cat\\Desktop\\Snipaste_2023-05-17_17-13-28.png");
        updateList(list);
    }
    public static List<String> updateList(List<String> list) {
        COSClient client = null;
        if (client != null) {
            client.shutdown();
        }
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(regionStr);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        client = new COSClient(cred, clientConfig);

        List<String> fileUrlList = new ArrayList<>();
        try {
            for (String path : list) {
                // 指定要上传的文件
                File localFile = new File(path);
                // 指定要上传到的存储桶

                // 指定要上传到 COS 上对象键
                String key = localFile.getName();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                client.putObject(putObjectRequest);

                int index = path.lastIndexOf("\\") + 1;
                String originFileName = path.substring(index);
                String url = "https://" + bucketName + ".cos." + regionStr + ".myqcloud.com/" + originFileName;
                fileUrlList.add(url);
            }

        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }

        return fileUrlList;
    }

}

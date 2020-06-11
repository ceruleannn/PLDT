package com.hyhello.priceless.module.cos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hyhello.priceless.config.TokenConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class COSCore {

    private final COSClient cosClient;

    private final TokenConfig tokenConfig;

    @Autowired
    public COSCore(COSClient cosClient, TokenConfig tokenConfig) {
        this.cosClient = cosClient;
        this.tokenConfig = tokenConfig;
    }

    public Bucket createBucket(String bucketName) {
        //存储桶名称，格式：BucketName-APPID
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // 设置 bucket 的权限为 Private(私有读写), 其他可选有公有读私有写, 公有读写
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);

       return cosClient.createBucket(createBucketRequest);
    }

    public List<Bucket> listBucket(String bucketName) {
        return cosClient.listBuckets();
    }

    /**
     *     同步上传对象
     */
    public PutObjectResult uploadObject(String bucketName, File file) {
        // 指定要上传到 COS 上对象键
        String key = file.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        return cosClient.putObject(putObjectRequest);
    }

    
    public COSObjectInputStream downloadObject(String bucketName, String key) {
        // 方法1 获取下载输入流
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObject cosObject = cosClient.getObject(getObjectRequest);

        return cosObject.getObjectContent();
    }
    public File downloadObject(String bucketName, String key, File downFile) {
        // 方法2 下载文件到本地
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        getObjectRequest = new GetObjectRequest(bucketName, key);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
        return downFile;
    }

    public List<COSObjectSummary> listObject(String bucketName, boolean iterDir) {
        return listObject(bucketName, "", iterDir);
    }


    /**
     *   文件的路径key
     *   String key = cosObjectSummary.getKey();
     *   文件的etag
     *   String etag = cosObjectSummary.getETag();
     *   文件的长度
     *   long fileSize = cosObjectSummary.getSize();
     *   文件的存储类型
     *   String storageClasses = cosObjectSummary.getStorageClass();
     */
    public List<COSObjectSummary> listObject(String bucketName, String prefix, boolean iterDir){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix(prefix);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter(iterDir ? "" : "/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;

        List<COSObjectSummary> results = new ArrayList<>();
        do {
            objectListing = cosClient.listObjects(listObjectsRequest);
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            results.addAll(cosObjectSummaries);

            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());

        return results;
     }
    
    public void deleteObject(String bucketName, String key) {
        cosClient.deleteObject(bucketName, key);
    }
   
}

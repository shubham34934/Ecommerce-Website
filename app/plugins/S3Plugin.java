package plugins;

/**
 * Created by manjeet on 10/07/15.
 */

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import javax.inject.Inject;
import play.Application;
import play.Logger;
import play.Plugin;

import java.io.File;

public class S3Plugin extends Plugin {

    public static final String AWS_S3_BUCKET = "aws.s3.bucket";
    public static final String AWS_ACCESS_KEY = "aws.access.key";
    public static final String AWS_SECRET_KEY = "aws.secret.key";
    public static final String AWS_URL = "aws.s3.url";
    public static final String AWS_DIR = "aws.s3.dir";

    private final Application application;

    public static AmazonS3 amazonS3;
    public  static String awsBaseURL;
    public static  String PublicURL;
    public static  String awsDir;
    public static String s3Bucket;

    @Inject
    public S3Plugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {
        String accessKey = application.configuration().getString(AWS_ACCESS_KEY);
        String secretKey = application.configuration().getString(AWS_SECRET_KEY);
        awsBaseURL = application.configuration().getString(AWS_URL);
        s3Bucket = application.configuration().getString(AWS_S3_BUCKET);
        awsDir =  application.configuration().getString(AWS_DIR);
        PublicURL = awsBaseURL + "/" + s3Bucket + "/" + awsDir;
        //System.out.print("on s3 plugin start"+accessKey+secretKey+s3Bucket);
        try {
            if ((accessKey != null) && (secretKey != null)) {
                AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
                amazonS3 = new AmazonS3Client(awsCredentials);
                if(!amazonS3.doesBucketExist(s3Bucket))
                    amazonS3.createBucket(s3Bucket);
                Logger.info("Using S3 Bucket: " + s3Bucket);
            }
        } catch (Exception e) {
            Logger.info("S3Plugin crash :" + e.getMessage());
        }
    }

    @Override
    public boolean enabled() {
        return (application.configuration().keys().contains(AWS_ACCESS_KEY) &&
                application.configuration().keys().contains(AWS_SECRET_KEY) &&
                application.configuration().keys().contains(AWS_S3_BUCKET));
    }

    public static void UploadToS3( String fileName, File file) {
        UploadToS3(s3Bucket,fileName,file);
    }

    public static void UploadToS3(String bucket, String fileName, File file) {
        Logger.info("full url "+awsBaseURL+"/"+bucket+"/"+fileName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
        S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
    }

    public static void DeleteFromS3(String fileName) {
        DeleteFromS3(s3Bucket,fileName);
    }

    public static void DeleteFromS3(String bucket, String fileName) {
        S3Plugin.amazonS3.deleteObject(bucket, fileName);
    }
}
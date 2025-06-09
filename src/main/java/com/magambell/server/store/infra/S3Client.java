package com.magambell.server.store.infra;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.magambell.server.common.utility.DateUtility;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class S3Client {

    private final AmazonS3Client amazonS3Client;

    @Value("${spring.aws.bucket}")
    private String S3_BUCKET_NAME;

    public String getPreSignedUrl(String fileName) {
        return amazonS3Client.generatePresignedUrl(
                S3_BUCKET_NAME,
                fileName,
                DateUtility.getPresignedExpireDate(),
                HttpMethod.PUT
        ).toString();
    }

    public void deleteObjectS3(String fileName) {
        amazonS3Client.deleteObject(S3_BUCKET_NAME, fileName);
    }

    public List<String> listObjectKeys(String prefix) {
        ObjectListing objectListing = amazonS3Client.listObjects(S3_BUCKET_NAME, prefix);
        return objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
}

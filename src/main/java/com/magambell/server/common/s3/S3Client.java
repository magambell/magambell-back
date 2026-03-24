package com.magambell.server.common.s3;

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

    private static final int MIN_PRESIGNED_EXPIRE_MINUTES = 1;
    private static final int MAX_PRESIGNED_EXPIRE_MINUTES = 10080; // SigV4 max: 7 days

    private final AmazonS3Client amazonS3Client;

    @Value("${spring.aws.bucket}")
    private String S3_BUCKET_NAME;

    @Value("${spring.aws.read-presigned-expire-minutes:10080}")
    private Integer readPresignedExpireMinutes;

    public String getPreSignedUrl(String fileName) {
        return amazonS3Client.generatePresignedUrl(
                S3_BUCKET_NAME,
                fileName,
                DateUtility.getPresignedExpireDate(),
                HttpMethod.PUT
        ).toString();
    }

    public String getPreSignedGetUrl(final String fileName) {
        int expireMinutes = normalizeReadExpireMinutes(readPresignedExpireMinutes);
        return amazonS3Client.generatePresignedUrl(
                S3_BUCKET_NAME,
                fileName,
                DateUtility.getPresignedExpireDate(expireMinutes),
                HttpMethod.GET
        ).toString();
    }

    private int normalizeReadExpireMinutes(final Integer expireMinutes) {
        if (expireMinutes == null) {
            return MAX_PRESIGNED_EXPIRE_MINUTES;
        }
        return Math.min(Math.max(expireMinutes, MIN_PRESIGNED_EXPIRE_MINUTES), MAX_PRESIGNED_EXPIRE_MINUTES);
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

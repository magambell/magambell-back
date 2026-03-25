package com.magambell.server.common.s3;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
import com.magambell.server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Adapter
public class S3Adapter implements S3InputPort {

    private static final String SSL = "https://";
    private final S3Client s3Client;

    @Value("${spring.aws.cf}")
    private String AWS_CF_DISTRIBUTION;

    @Override
    public List<TransformedImageDTO> saveImages(final String imagePrefix,
                                                final List<ImageRegister> imageRegisters, final User user) {
        return imageRegisters.stream()
                .map(image -> {
                    String getImagePrefix = getImagePrefix(imagePrefix, image, user);

                    return new TransformedImageDTO(
                            image.id(),
                            getCloudFrontSignedUrl(getImagePrefix),
                            s3Client.getPreSignedUrl(getImagePrefix)
                    );
                })
                .toList();
    }

    @Override
    public List<TransformedImageDTO> saveImages(final String imagePrefix,
                                                final List<ImageRegister> imageRegisters, final Long id) {
        return imageRegisters.stream()
                .map(image -> {
                    String getImagePrefix = getImagePrefix(imagePrefix, image, id);

                    return new TransformedImageDTO(
                            image.id(),
                            getCloudFrontSignedUrl(getImagePrefix),
                            s3Client.getPreSignedUrl(getImagePrefix)
                    );
                })
                .toList();
    }

    @Override
    public TransformedImageDTO saveImage(final String imagePrefix,
                                         final ImageRegister imageRegister,
                                         final Long id) {

        String getImagePrefix = getImagePrefix(imagePrefix, imageRegister, id);

        return new TransformedImageDTO(
                imageRegister.id(),
                getCloudFrontSignedUrl(getImagePrefix),
                s3Client.getPreSignedUrl(getImagePrefix)
        );
    }

    @Override
    public void deleteS3Objects(final String imagePrefix, final User user) {
        s3Client.listObjectKeys(
                        imagePrefix + "/" + user.getUserRole() + "/" + user.getId()
                )
                .forEach(s3Client::deleteObjectS3);
    }

    @Override
    public void deleteS3Objects(final String imagePrefix, Long id) {
        s3Client.listObjectKeys(
                        imagePrefix + "/" + id
                )
                .forEach(s3Client::deleteObjectS3);
    }

    @Override
    public String getImagePrefix(final String imagePrefix, final ImageRegister imageRegisters,
                                 final User user) {
        return imagePrefix + "/" + user.getUserRole() + "/" + user.getId() + "/" + imageRegisters.id() + "_"
                + imageRegisters.key();
    }

    @Override
    public String getImagePrefix(final String imagePrefix, final ImageRegister imageRegisters, final Long id) {
        return imagePrefix + "/" + id + "/" + imageRegisters.id() + "_"
                + imageRegisters.key();
    }

    @Override
    public String getReadUrl(final String imagePathOrKey) {
        if (imagePathOrKey == null || imagePathOrKey.isBlank()) {
            return imagePathOrKey;
        }

        try {
            String objectKey = extractObjectKey(imagePathOrKey);
            return s3Client.getPreSignedGetUrl(objectKey);
        } catch (RuntimeException ignored) {
            // Do not fail the whole API when URL re-signing fails.
            return imagePathOrKey;
        }
    }

    private String getCloudFrontSignedUrl(final String imageKey) {
        return SSL + AWS_CF_DISTRIBUTION + "/" + imageKey;
    }

    private String extractObjectKey(final String imagePathOrKey) {
        if (imagePathOrKey.startsWith("http://") || imagePathOrKey.startsWith("https://")) {
            try {
                URI uri = URI.create(imagePathOrKey);
                String path = uri.getPath();
                if (path != null && !path.isBlank()) {
                    return path.startsWith("/") ? path.substring(1) : path;
                }
            } catch (IllegalArgumentException ignored) {
                return imagePathOrKey;
            }
        }
        return imagePathOrKey;
    }
}

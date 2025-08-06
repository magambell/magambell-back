package com.magambell.server.common.s3;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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
    public void deleteS3Objects(final User user) {
        s3Client.listObjectKeys(
                        user.getUserRole() + "/" + user.getId()
                )
                .forEach(s3Client::deleteObjectS3);
    }

    @Override
    public String getImagePrefix(final String imagePrefix, final ImageRegister imageRegisters,
                                 final User user) {
        return imagePrefix + "/" + user.getUserRole() + "/" + user.getId() + "/" + imageRegisters.id() + "_"
                + imageRegisters.key();
    }

    private String getCloudFrontSignedUrl(final String imageKey) {
        return SSL + AWS_CF_DISTRIBUTION + "/" + imageKey;
    }
}

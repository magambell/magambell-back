package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.in.S3InputPort;
import com.magambell.server.store.app.port.in.dto.TransformedImageDTO;
import com.magambell.server.store.infra.S3Client;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Adapter
public class S3Adapter implements S3InputPort {

    private final S3Client s3Client;

    @Value("${spring.aws.cf}")
    private String AWS_CF_DISTRIBUTION;

    @Override
    public List<TransformedImageDTO> saveImages(final List<StoreImagesRegister> storeImagesRegisters, final User user) {
        return storeImagesRegisters.stream()
                .map(image -> {
                    String imagePrefix = getImagePrefix(image, user);

                    return new TransformedImageDTO(
                            image.id(),
                            getCloudFrontSignedUrl(imagePrefix),
                            s3Client.getPreSignedUrl(imagePrefix)
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
    public String getImagePrefix(final StoreImagesRegister storeImagesRegister, final User user) {
        return user.getUserRole() + "/" + user.getId() + "/" + storeImagesRegister.id() + "_"
                + storeImagesRegister.key();
    }

    private String getCloudFrontSignedUrl(final String imageKey) {
        return AWS_CF_DISTRIBUTION + "/" + imageKey;
    }
}

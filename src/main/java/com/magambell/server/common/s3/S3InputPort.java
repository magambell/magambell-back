package com.magambell.server.common.s3;

import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
import com.magambell.server.user.domain.entity.User;

import java.util.List;

public interface S3InputPort {

    List<TransformedImageDTO> saveImages(String imagePrefix, List<ImageRegister> imageRegisters, User user);

    List<TransformedImageDTO> saveImages(String imagePrefix, List<ImageRegister> imageRegister, Long id);

    TransformedImageDTO saveImage(String imagePrefix, ImageRegister imageRegister, Long id);

    String getImagePrefix(String imagePrefix, ImageRegister imageRegister, User user);

    String getImagePrefix(String imagePrefix, ImageRegister imageRegister, Long id);

    void deleteS3Objects(String imagePrefix, User user);

    void deleteS3Objects(String imagePrefix, Long id);

}

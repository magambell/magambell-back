package com.magambell.server.common.s3;

import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
import com.magambell.server.user.domain.model.User;
import java.util.List;

public interface S3InputPort {

    List<TransformedImageDTO> saveImages(String imagePrefix, List<ImageRegister> imageRegisters, User user);

    String getImagePrefix(String imagePrefix, ImageRegister imageRegister, User user);

    void deleteS3Objects(User user);

}

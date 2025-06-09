package com.magambell.server.store.app.port.in;

import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.in.dto.TransformedImageDTO;
import com.magambell.server.user.domain.model.User;
import java.util.List;

public interface S3InputPort {

    List<TransformedImageDTO> saveImages(List<StoreImagesRegister> storeImagesRegisters, User user);

    String getImagePrefix(StoreImagesRegister storeImagesRegister, User user);

    void deleteS3Objects(User user);

}

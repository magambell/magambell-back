package com.magambell.server.store.app.port.out;

import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.out.response.PreSignedUrlImage;
import java.util.List;

public interface StoreCommandPort {
    List<PreSignedUrlImage> registerStore(RegisterStoreDTO dto);
}

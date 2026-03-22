package com.magambell.server.admin.adapter.out.persistence;

import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.goods.app.port.out.response.GoodsPreSignedUrlImage;
import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "관리자 매장 수정 응답")
public class AdminEditStoreResponse extends BaseResponse {

    @Schema(description = "매장 이미지 업로드용 Pre-signed URL 목록")
    private final List<StorePreSignedUrlImage> storePreSignedUrlImages;

    @Schema(description = "상품 이미지 업로드용 Pre-signed URL 목록")
    private final List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages;

    public AdminEditStoreResponse(final List<StorePreSignedUrlImage> storePreSignedUrlImages,
                                  final List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages) {
        this.storePreSignedUrlImages = storePreSignedUrlImages;
        this.goodsPreSignedUrlImages = goodsPreSignedUrlImages;
    }
}

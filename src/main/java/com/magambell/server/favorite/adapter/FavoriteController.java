package com.magambell.server.favorite.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.favorite.adapter.in.web.FavoriteStoreListRequest;
import com.magambell.server.favorite.adapter.out.persistence.FavoriteStoreListResponse;
import com.magambell.server.favorite.app.port.in.FavoriteUseCase;
import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Favorite", description = "Favorite API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
@RestController
public class FavoriteController {

    private final FavoriteUseCase favoriteUseCase;

    @Operation(summary = "매장 즐겨찾기 삭제")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/{storeId}")
    public Response<BaseResponse> registerFavorite(@PathVariable final Long storeId,
                                                   @AuthenticationPrincipal final CustomUserDetails customUserDetails) {
        favoriteUseCase.registerFavorite(storeId, customUserDetails.userId());
        return new Response<>();
    }

    @Operation(summary = "매장 즐겨찾기 삭제")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @DeleteMapping("/{storeId}")
    public Response<BaseResponse> deleteFavorite(@PathVariable final Long storeId,
                                                 @AuthenticationPrincipal final CustomUserDetails customUserDetails) {
        favoriteUseCase.deleteFavorite(storeId, customUserDetails.userId());
        return new Response<>();
    }

    @Operation(summary = "매장 즐겨찾기 리스트")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = FavoriteStoreListResponse.class))})
    @GetMapping("")
    public Response<FavoriteStoreListResponse> getFavoriteStoreList(
            @ModelAttribute @Validated final FavoriteStoreListRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails) {
        List<FavoriteStoreListDTOResponse> favoriteStoreList = favoriteUseCase.getFavoriteStoreList(
                request.toService(customUserDetails.userId()));
        return new Response<>(new FavoriteStoreListResponse(favoriteStoreList));
    }
}

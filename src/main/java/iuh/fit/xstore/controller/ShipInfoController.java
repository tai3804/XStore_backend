package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ShipInfoRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ShipInfoResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.ShipInfo;
import iuh.fit.xstore.service.ShipInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ship-infos")
@AllArgsConstructor
@Slf4j
public class ShipInfoController {
    private final ShipInfoService shipInfoService;

    /**
     * GET /api/ship-infos/user/{userId}: Lấy tất cả ship info của user
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<ShipInfoResponse>> getUserShipInfos(@PathVariable("userId") int userId) {
        log.info("Getting ship infos for user: {}", userId);
        List<ShipInfo> shipInfos = shipInfoService.findByUserId(userId);
        List<ShipInfoResponse> responses = shipInfos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(), "Fetch ship infos successfully", responses);
    }

    /**
     * GET /api/ship-infos/user/{userId}/default: Lấy ship info mặc định của user
     */
    @GetMapping("/user/{userId}/default")
    public ApiResponse<ShipInfoResponse> getDefaultShipInfo(@PathVariable("userId") int userId) {
        log.info("Getting default ship info for user: {}", userId);
        ShipInfo shipInfo = shipInfoService.findDefaultByUserId(userId);
        ShipInfoResponse response = shipInfo != null ? convertToResponse(shipInfo) : null;
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(), "Fetch default ship info successfully", response);
    }

    /**
     * GET /api/ship-infos: Lấy tất cả ship info
     */
    @GetMapping
    public ApiResponse<List<ShipInfoResponse>> getShipInfos() {
        List<ShipInfo> shipInfos = shipInfoService.findAll();
        List<ShipInfoResponse> responses = shipInfos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(), "Fetch ship infos successfully", responses);
    }

    /**
     * GET /api/ship-infos/{id}: Lấy ship info theo ID
     */
    @GetMapping("/{id}")
    public ApiResponse<ShipInfoResponse> getShipInfo(@PathVariable("id") int id) {
        ShipInfo shipInfo = shipInfoService.findById(id);
        ShipInfoResponse response = convertToResponse(shipInfo);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(), "Fetch ship info successfully", response);
    }

    /**
     * POST /api/ship-infos: Tạo ship info mới
     */
    @PostMapping
    public ApiResponse<ShipInfoResponse> createShipInfo(
            @RequestBody ShipInfoRequest request,
            @RequestParam("userId") int userId) {
        log.info("Creating new ship info for user: {}", userId);
        ShipInfo shipInfo = ShipInfo.builder()
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .streetNumber(request.getStreetNumber())
                .streetName(request.getStreetName())
                .ward(request.getWard())
                .district(request.getDistrict())
                .city(request.getCity())
                .isDefault(request.isDefault())
                .build();
        ShipInfo createdShipInfo = shipInfoService.createShipInfoForUser(shipInfo, userId);
        ShipInfoResponse response = convertToResponse(createdShipInfo);
        return new ApiResponse<>(SuccessCode.SHIPINFO_CREATED.getCode(), "Ship info created successfully", response);
    }

    /**
     * PUT /api/ship-infos/{id}: Cập nhật ship info
     */
    @PutMapping("/{id}")
    public ApiResponse<ShipInfoResponse> updateShipInfo(@PathVariable("id") int id, @RequestBody ShipInfoRequest request) {
        log.info("📦 Updating ship info: {}", id);
        ShipInfo existedShipInfo = shipInfoService.findById(id);
        
        ShipInfo shipInfo = ShipInfo.builder()
                .id(id)
                .user(existedShipInfo.getUser())
                .recipientName(request.getRecipientName() != null ? request.getRecipientName() : existedShipInfo.getRecipientName())
                .recipientPhone(request.getRecipientPhone() != null ? request.getRecipientPhone() : existedShipInfo.getRecipientPhone())
                .streetNumber(request.getStreetNumber() != null ? request.getStreetNumber() : existedShipInfo.getStreetNumber())
                .streetName(request.getStreetName() != null ? request.getStreetName() : existedShipInfo.getStreetName())
                .ward(request.getWard() != null ? request.getWard() : existedShipInfo.getWard())
                .district(request.getDistrict() != null ? request.getDistrict() : existedShipInfo.getDistrict())
                .city(request.getCity() != null ? request.getCity() : existedShipInfo.getCity())
                .isDefault(request.isDefault())
                .build();
        ShipInfo updatedShipInfo = shipInfoService.updateShipInfo(shipInfo);
        ShipInfoResponse response = convertToResponse(updatedShipInfo);
        return new ApiResponse<>(SuccessCode.SHIPINFO_UPDATED.getCode(), "Ship info updated successfully", response);
    }

    /**
     * DELETE /api/ship-infos/{id}: Xóa ship info
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteShipInfo(@PathVariable("id") int id) {
        log.info("📦 Deleting ship info: {}", id);
        shipInfoService.deleteShipInfo(id);
        return new ApiResponse<>(SuccessCode.SHIPINFO_DELETED.getCode(), "Ship info deleted successfully", "Ship info " + id + " deleted");
    }

    /**
     * PUT /api/ship-infos/{id}/set-default: Đặt ship info làm mặc định
     */
    @PutMapping("/{id}/set-default")
    public ApiResponse<ShipInfoResponse> setDefaultShipInfo(@PathVariable("id") int id, @RequestParam("userId") int userId) {
        log.info("📦 Setting default ship info: {} for user: {}", id, userId);
        ShipInfo shipInfo = shipInfoService.setDefaultShipInfo(id, userId);
        ShipInfoResponse response = convertToResponse(shipInfo);
        return new ApiResponse<>(SuccessCode.SHIPINFO_UPDATED.getCode(), "Default ship info set successfully", response);
    }

    /**
     * Helper method để convert ShipInfo entity sang ShipInfoResponse DTO
     */
    private ShipInfoResponse convertToResponse(ShipInfo shipInfo) {
        return ShipInfoResponse.builder()
                .id(shipInfo.getId())
                .recipientName(shipInfo.getRecipientName())
                .recipientPhone(shipInfo.getRecipientPhone())
                .streetNumber(shipInfo.getStreetNumber())
                .streetName(shipInfo.getStreetName())
                .ward(shipInfo.getWard())
                .district(shipInfo.getDistrict())
                .city(shipInfo.getCity())
                .isDefault(shipInfo.isDefault())
                .build();
    }
}

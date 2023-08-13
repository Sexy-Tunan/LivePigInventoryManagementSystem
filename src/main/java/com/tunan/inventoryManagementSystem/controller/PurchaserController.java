package com.tunan.inventoryManagementSystem.controller;

import com.tunan.inventoryManagementSystem.domin.ErrorCode;
import com.tunan.inventoryManagementSystem.domin.NullData;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.VO.PurchaserVO;
import com.tunan.inventoryManagementSystem.service.WorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: Purchaser采购员管理类
 * @Author: CaiGou
 * @Date: 2023/4/16 17:00
 **/
@RestController
@RequestMapping("/purchaser")
@Api(tags = "采购员相关接口")
public class PurchaserController {


    private final WorkerService workerService;

    public PurchaserController(WorkerService workerService) {
        this.workerService = workerService;
    }

    /**
     * @Description: 分页查询Purchaser
     * @Author: CaiGou
     * @Date: 2023/4/16 17:19
     * @Param:
     * @Return:
     **/
    @GetMapping("/pageQuery")
    @ApiOperation(value = "分页查询采购员")
    @PreAuthorize("hasAuthority('采购员查询')")
    public Result<List<PurchaserVO>> getPurchasers(
        @RequestParam("pageNum") Integer pageNum,
        @RequestParam("pageSize") Integer pageSize
    ){
        List<PurchaserVO> purchaserVOS = workerService.pageQueryPurchasers(pageNum, pageSize);

        int count = workerService.getPurchaserCounts();

        if (purchaserVOS != null){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,purchaserVOS);
        }else {
//            return new Result<>(ErrorCode.RESOURCE_NOT_FOUND,ErrorCode.RESOURCE_NOT_FOUND_MSG);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,count,null);
        }
    }


    @GetMapping("/pageQueryConditionalPurchasers")
    @ApiOperation(value = "条件查询采购员并分页")
    @PreAuthorize("hasAuthority('采购员查询')")
    public Result<List<PurchaserVO>> getPurchaserMsg(
            @RequestBody PurchaserVO purchaserVO,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        List<PurchaserVO> conditionalPurchasers = workerService.getConditionalPurchasers(purchaserVO, pageNum, pageSize);

        int count = workerService.getPurchaserCounts();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS, count, conditionalPurchasers);
    }



    /**
     * @Description: 添加采购员
     * @Author: CaiGou
     * @Date: 2023/4/16 17:23
     * @Param:
     * @Return:
     **/
    @PostMapping("/add")
    @ApiOperation(value = "添加采购员")
    @PreAuthorize("hasAuthority('采购员添加')")
    public Result<NullData> addPurchaser(
            @RequestBody PurchaserVO purchaserVO
    ){

        boolean isSuccess = workerService.addPurchaser(purchaserVO);

        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,null);
        }else {
//            return new Result<>(ErrorCode.COMMON_ADD_FAILURE,ErrorCode.COMMON_ADD_FAILURE_MSG,null);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,null);
        }

    }


    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 17:23
     * @Param:
     * @Return:
     **/
    @PutMapping("/modify")
    @ApiOperation(value = "修改采购员信息")
    @PreAuthorize("hasAuthority('采购员修改')")
    public Result<NullData> modifyPurchaser(
            @RequestBody @Valid PurchaserVO purchaserVO
    ){
        boolean isSuccess = workerService.updatePurchaser(purchaserVO);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS, null);
        }else {
//            return new Result<>(ErrorCode.COMMON_MODIFY_FAILURE,ErrorCode.COMMON_MODIFY_FAILURE_MSG, null);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS, null);
        }
    }


    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 17:24
     * @Param:  id---->数据库采购员表的唯一标识（主键）
     * @Return:
     **/
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除采购员")
    @PreAuthorize("hasAuthority('采购员删除')")
    public Result<NullData> deletePurchaser(
            @RequestParam("id") Long id
    ){
        boolean isSuccess = workerService.deletePurchaser(id);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,new NullData());
        }else {
//            return new Result<>(ErrorCode.COMMON_DELETE_FAILURE,ErrorCode.COMMON_DELETE_FAILURE_MSG,new NullData());
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,new NullData());
        }
    }
}

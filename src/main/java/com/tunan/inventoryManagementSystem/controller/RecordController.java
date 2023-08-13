package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.domin.*;
import com.tunan.inventoryManagementSystem.entity.VO.PurchaseRecordVO;
import com.tunan.inventoryManagementSystem.entity.VO.SellingRecordVO;
import com.tunan.inventoryManagementSystem.service.PurchaseRecordService;
import com.tunan.inventoryManagementSystem.service.SellingRecordService;
import com.tunan.inventoryManagementSystem.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/record")
@Api(tags = "订单的相关接口")
public class RecordController {

    private final SellingRecordService sellingRecordService;

    private final PurchaseRecordService purchaseRecordService;

    private final UserService userService;

    public RecordController(SellingRecordService sellingRecordService, PurchaseRecordService purchaseRecordService, UserService userService) {
        this.sellingRecordService = sellingRecordService;
        this.purchaseRecordService = purchaseRecordService;
        this.userService = userService;
    }

    /**
     * @Description: 分页查询订单信息，根据传入的pageNum和pageSize将对应的订单列传给前端渲染(默认按照时间顺序排列)
     * @Author: CaiGou
     * @Date: 2023/4/15 21:58
     * @Param: 
     * @Return: 
     **/
    @GetMapping("/pageQuerySellingRecords")
    @ApiOperation(value = "分页查询销售订单", notes = "分页查询销售订单")
    @PreAuthorize("hasAnyAuthority('销售订单查询')")
    public Result<List<SellingRecordVO>> getSellingRecords(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){

        List<SellingRecordVO> sellingRecordVOS = sellingRecordService.pageQueryForSellingRecord(pageNum, pageSize,userService.getLoginUser());

        int count = sellingRecordService.getCounts();

        if(sellingRecordVOS != null){
            //如果不为空，说明有数据，进行R类的封装
            //TODO
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,sellingRecordVOS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,count,null);
        }
    }

    /**
     * @Description: 根据条件查询订单信息
     * @Author: CaiGou
     * @Date: 2023/4/16 14:27
     * @Param:
     * @Return:
     **/
    @GetMapping("/pageQueryConditionalSellingRecords")
    @ApiOperation(value = "条件查询销售记录并分页")
    @PreAuthorize("hasAuthority('销售订单查询')")
    public Result<List<SellingRecordVO>> getConditionalSellingRecords(
            @RequestParam(value = "recordNumber",required = false) String recordNumber,
            @RequestParam(value = "pigFunction",required = false) String pigFunction,
            @RequestParam(value = "pigType",required = false) String pigType,
            @RequestParam(value = "enterpriseName", required = false) String enterpriseName,
            @RequestParam(value = "startDate",required = false) String startDate,
            @RequestParam(value = "deadTime",required = false) String deadTime,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNum") Integer pageNum
    ){

        List<SellingRecordVO> conditionalRecords = sellingRecordService.getConditionalRecords( recordNumber, enterpriseName,
                                                                                            pigFunction,  pigType,
                                                                                            startDate,    deadTime,
                                                                                            pageNum,      pageSize,
                                                                                            userService.getLoginUser());

        int count = sellingRecordService.getCounts();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,conditionalRecords);

    }


    @PostMapping("/addSellingRecord")
    @ApiOperation(value = "添加销售记录")
    @PreAuthorize("hasAuthority('销售订单添加')")
    public Result<NullData> addSellingRecord(
            @RequestBody SellingRecordVO sellingRecordVO
    ){

        boolean isSuccess = sellingRecordService.addSellingRecord(sellingRecordVO,userService.getLoginUser());
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }

    /**
     * @Description: 修改销售订单信息，根据前端传入的信息，进行修改
     * @Author: CaiGou
     * @Date: 2023/4/15 21:57
     * @Param: 
     * @Return: 
     **/
    @PutMapping("/modifySellingRecord")
    @ApiOperation(value = "修改销售记录" , notes = "里面有些字段你是不需要管的，只是我偷懒拿实体类来接收参数，所以你会看到很多字段")
    @PreAuthorize("hasAuthority('销售订单修改')")
    public Result<NullData> modifySellingRecord(
            @RequestBody @Valid SellingRecordVO sellingRecordVO
    ){
        boolean isSuccess = sellingRecordService.updateSellingRecord(sellingRecordVO,userService.getLoginUser());
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }

    /**
     * @Description: 删除销售订单
     * @Author: CaiGou
     * @Date: 2023/4/16 14:27
     * @Param:
     * @Return:
     **/
    @DeleteMapping("/deleteSellingRecord")
    @ApiOperation(value = "删除销售记录")
    @PreAuthorize("hasAuthority('销售订单删除')")
    public Result<NullData> deleteSellingRecord(@RequestParam("id") Long id){
        //TODO 删除订单，但在数据库层面并没有真正删除，而是标记为已删除罢了
        boolean isSuccess = sellingRecordService.delSellingRecord(id);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }


    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 14:34
     * @Param:
     * @Return:
     **/
    @GetMapping("/pageQueryPurchaseRecords")
    @ApiOperation(value = "分页查询采购记录")
    @PreAuthorize("hasAnyAuthority('采购订单查询')")
    public Result<List<PurchaseRecordVO>> getPurchaseRecords(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){

        List<PurchaseRecordVO> purchaseRecordVOS = purchaseRecordService.pageQueryForPurchaseRecord(pageNum, pageSize, userService.getLoginUser());

        int count = purchaseRecordService.getCounts();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,purchaseRecordVOS);
    }


    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 14:36
     * @Param:
     * @Return:
     **/
    @GetMapping("/pagequeryConditionalPurchaseRecords")
    @ApiOperation(value = "条件查询采购记录并分页")
    @PreAuthorize("hasAnyAuthority('采购订单查询')")
    public Result<List<PurchaseRecordVO>> getConditionalPurchaseRecord(
            @RequestParam(value = "recordNumber", required = false) String recordNumber,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "pigFunction",required = false) String pigFunction,
            @RequestParam(value = "pigType",required = false) String pigType,
            @RequestParam(value = "startDate",required = false) String startDate,
            @RequestParam(value = "deadTime",required = false) String deadTime,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){

        List<PurchaseRecordVO> conditionalRecords = purchaseRecordService.
                getConditionalRecords(recordNumber, customerName, pigFunction, pigType, startDate, deadTime, pageNum, pageSize, userService.getLoginUser());

        int count = purchaseRecordService.getCounts();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,conditionalRecords);

    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 14:41
     * @Param: 
     * @Return: 
     **/
    @PostMapping("/addPurchaseRecord")
    @ApiOperation(value = "添加采购记录")
    @PreAuthorize("hasAnyAuthority('采购订单添加')")
    public Result<NullData> addPurchaseRecord(
            @RequestBody PurchaseRecordVO purchaseRecordVO
    ){

        boolean isSuccess = purchaseRecordService.addPurchaseRecord(purchaseRecordVO, userService.getLoginUser());

        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }
    
    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 14:41
     * @Param: 
     * @Return: 
     **/
    @PutMapping("/modifyPurchaseRecord")
    @ApiOperation(value = "修改采购记录")
    @PreAuthorize("hasAnyAuthority('采购订单修改')")
    public Result<NullData> modifyPurchaseRecord(
            @RequestBody @Valid PurchaseRecordVO purchaseRecordVO
    ){

        boolean isSuccess = purchaseRecordService.updatePurchaseRecord(purchaseRecordVO, userService.getLoginUser());

        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 14:44
     * @Param: 
     * @Return: 
     **/
    @DeleteMapping("/deletePurchaseRecord")
    @ApiOperation(value = "删除订单")
    @PreAuthorize("hasAnyAuthority('采购订单删除')")
    public Result<NullData> deletePurchaseRecord(
            @RequestParam("id") Long id
    ){

        boolean isSuccess = purchaseRecordService.delPurchaseRecord(id);

        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }

}

package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.domin.ErrorCode;
import com.tunan.inventoryManagementSystem.domin.NullData;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.VO.PigInventoryVO;
import com.tunan.inventoryManagementSystem.service.PigInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pigInventory")
@Api(tags = "生猪库存相关接口")
public class PigInventoryController {

    private final PigInventoryService pigInventoryService;

    public PigInventoryController(PigInventoryService pigInventoryService) {
        this.pigInventoryService = pigInventoryService;
    }

    /**
     * @Description: 获取猪的库存
     * @Author: CaiGou
     * @Date: 2023/4/16 17:37
     * @Param:
     * @Return:
     **/
    @GetMapping("/pageQuery")
    @ApiOperation(value = "分页查询生猪库存")
    @PreAuthorize("hasAnyAuthority('库存查询')")
    public Result<List<PigInventoryVO>> pageQuery(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        List<PigInventoryVO> pigInventoryVOS = pigInventoryService.pageQuery(pageNum, pageSize);

        int count = pigInventoryService.getCounts();

        if (pigInventoryVOS == null || pigInventoryVOS.size() == 0){
//            return new Result<>(ErrorCode.RESOURCE_NOT_FOUND,ErrorCode.RESOURCE_NOT_FOUND_MSG,null);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,count,null);
        }
        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,pigInventoryVOS);
    }


    @GetMapping("/pageQueryConditionalInventory")
    @ApiOperation(value = "条件查询生猪库存信息并分页")
    @PreAuthorize("hasAnyAuthority('库存查询')")
    public Result<List<PigInventoryVO>> getConditionalInventory(
            @RequestParam(value = "function", required = false) String function,
            @RequestParam(value = "type",required = false) String type,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        //TODO
        List<PigInventoryVO> pigInventoryVOS = pigInventoryService.pageQueryByCondition(function, type, pageNum, pageSize);

        int count = pigInventoryService.getCounts();

        if (pigInventoryVOS == null || pigInventoryVOS.size() == 0){
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,count,null);
//            return new Result<>(ErrorCode.RESOURCE_NOT_FOUND,ErrorCode.RESOURCE_NOT_FOUND_MSG,null);
        }
        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,pigInventoryVOS);
    }


    @PutMapping("modifyInventoryWarningLine")
    @ApiOperation(value = "修改库存的库存警告线", notes = "至于为什么不能修改其他信息，因为库存信息很重要不能随便改，不提供接口")
    @PreAuthorize("hasAnyAuthority('库存修改')")
    public Result<NullData> modifyInventoryWarningLine(
            @RequestBody PigInventoryVO pigInventoryVO
    ){
        boolean isSuccess = pigInventoryService.modifyInventoryWarningLine(pigInventoryVO.getId(), pigInventoryVO.getWarningInventoryLine());
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,null);
        }else {
//            return new Result<>(ErrorCode.COMMON_DELETE_FAILURE,ErrorCode.COMMON_DELETE_FAILURE_MSG);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,null);
        }
    }



    @DeleteMapping("delete")
    @ApiOperation(value = "删除生猪库存信息")
    @PreAuthorize("hasAnyAuthority('库存删除')")
    public Result<NullData> delete(
            @RequestParam("id") Integer id
    ){
        boolean isSuccess = pigInventoryService.deleteInventory(id);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,null);
        }else {
//            return new Result<>(ErrorCode.COMMON_DELETE_FAILURE,ErrorCode.COMMON_DELETE_FAILURE_MSG);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }

    @PostMapping("add")
    @ApiOperation(value = "添加某种猪的库存信息", notes = "只需要给出猪的种类和功能，而至于猪的库存数量，则应由添加的订单信息来增加或者修改")
    @PreAuthorize("hasAnyAuthority('库存种类添加')")
    public Result<NullData> add(
            @RequestBody PigInventoryVO pigInventoryVO
    ){
        boolean isSuccess = pigInventoryService.addInventoryPattern(pigInventoryVO.getFunction(), pigInventoryVO.getType());
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,null);
        }else {
//            return new Result<>(ErrorCode.COMMON_ADD_FAILURE,ErrorCode.COMMON_ADD_FAILURE_MSG);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,null);
        }
    }




}

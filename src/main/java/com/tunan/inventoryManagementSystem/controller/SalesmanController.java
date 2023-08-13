package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.domin.ErrorCode;
import com.tunan.inventoryManagementSystem.domin.NullData;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.VO.SalesmanVO;
import com.tunan.inventoryManagementSystem.service.WorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description: 销售员管理处理类
 * @Author: CaiGou
 * @Date: 2023/4/16 17:26
 **/
@RestController
@RequestMapping("/salesman")
@Api(tags = "销售员管理相关接口")
public class SalesmanController {


    private final WorkerService workerService;

    public SalesmanController(WorkerService workerService) {
        this.workerService = workerService;
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 17:27
     * @Param:
     * @Return:
     **/
    @GetMapping("/pageQuery")
    @ApiOperation(value = "分页查询销售员")
    @PreAuthorize("hasAuthority('销售员查询')")
    public Result<List<SalesmanVO>> getSalesmen(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        List<SalesmanVO> salesmanVOS = workerService.pageQuerySalesman(pageNum, pageSize);

        int count = workerService.getSalesmanCounts();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS, count, salesmanVOS);
    }


    @GetMapping("/pageQueryConditionalSalesmen")
    @ApiOperation(value = "条件查询销售员并分页")
    @PreAuthorize("hasAuthority('销售员查询')")
    public Result<List<SalesmanVO>> getSalesmen(
            @RequestBody SalesmanVO salesmanVO,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        List<SalesmanVO> conditionalSalesman = workerService.getConditionalSalesman(salesmanVO, pageNum, pageSize);

        int count = workerService.getSalesmanCounts();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS, count, conditionalSalesman);
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 17:29
     * @Param:
     * @Return:
     **/
    @PostMapping("/add")
    @ApiOperation(value = "添加销售员")
    @PreAuthorize("hasAuthority('销售员添加')")
    public Result<NullData> addSalesman(
            @RequestBody SalesmanVO salesmanVO
    ){

        boolean isSuccess = workerService.addSalesman(salesmanVO);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }
    }


    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 17:30
     * @Param:
     * @Return:
     **/
    @PutMapping("/modify")
    @ApiOperation(value = "修改销售员信息")
    @PreAuthorize("hasAuthority('销售员修改')")
    public Result<NullData> modifySalesman(
            @RequestBody SalesmanVO salesmanVO
    ){

        boolean isSuccess = workerService.updateSalesman(salesmanVO);
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
     * @Date: 2023/4/16 17:31
     * @Param:
     * @Return:
     **/
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除销售员")
    @PreAuthorize("hasAuthority('销售员删除')")
    public Result<NullData> modifySalesman(
            @RequestParam("id") Long id
    ){

        boolean isSuccess = workerService.deleteSalesman(id);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,null);
        }else {
//            return new Result<>(ErrorCode.COMMON_DELETE_FAILURE,ErrorCode.COMMON_DELETE_FAILURE_MSG,new NullData());
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS, null);
        }
    }
}

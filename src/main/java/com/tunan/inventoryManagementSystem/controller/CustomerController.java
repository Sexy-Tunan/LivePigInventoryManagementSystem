package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.domin.ErrorCode;
import com.tunan.inventoryManagementSystem.domin.NullData;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.VO.CustomerVO;
import com.tunan.inventoryManagementSystem.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Api(value = "经销商管理相关接口",tags = "经销商管理相关接口")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * @Description: 获取所有供应商企业的名字
     * @Author: CaiGou
     * @Date: 2023/4/16 15:29
     * @Param:
     * @Return:
     */
    @GetMapping("/getCombobox")
    @ApiOperation(value = "获取所有经销商的名字",notes = "获取所有经销商的名字")
    @PreAuthorize("hasAnyAuthority('经销商查询')")
    public Result<List<String>> getCustomerName(){

        List<String> combobox = customerService.getCombobox();

        if (combobox != null){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,combobox);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }

    }



    /**
     * @Description: 分页查询经销商的信息
     * @Author: CaiGou
     * @Date: 2023/4/16 15:30
     * @Param:
     * @Return:
     **/
    @GetMapping("/pageQuery")
    @ApiOperation(value = "分页查询经销商",notes = "pageNumber和pageSize不能忽略")
    @PreAuthorize("hasAnyAuthority('经销商查询')")
    public Result<List<CustomerVO>> getCustomers(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){

        List<CustomerVO> customers = customerService.getCustomers(pageNum, pageSize);

        int count = customerService.getCounts();
        if (customers != null){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,customers);
        } else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }

    }

    @GetMapping("/pageQueryConditionalCustomers")
    @ApiOperation(value = "条件查询经销商并分页", notes = "pageNumber和pageSize不能忽略")
    public Result<List<CustomerVO>> getConditionalCustomers(
            @RequestParam(value = "id",required = false) Long id,
            @RequestParam(value = "enterpriseName",required = false) String enterpriseName,
            @RequestParam(value = "contact",required = false) String contact,
            @RequestParam(value = "address",required = false) String address,
            @RequestParam(value = "phoneNumber",required = false) String phoneNumber,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        List<CustomerVO> customerVOS
                = customerService.getConditionalCustomer(id, enterpriseName, address, contact,
                                                        phoneNumber, pageNum, pageSize);

        int count = customerService.getCounts();
        if (customerVOS != null){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,customerVOS);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
        }

    }



    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 15:34
     * @Param:
     * @Return:
     **/
    @PostMapping("/add")
    @ApiOperation(value = "添加经销商", notes = "填充信息时，不需要填充id和日期等信息，后端会处理")
    @PreAuthorize("hasAnyAuthority('经销商添加')")
    public Result<NullData> addCustomer(
            @RequestBody CustomerVO customerVO
    ){

        boolean isSuccess = customerService.addCustomer(customerVO);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,new NullData());
        } else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,null);
        }
    }



    @PutMapping("/modify")
    @ApiOperation(value = "修改经销商信息", notes = "注意只能修改基本信息，然后修改时需要将后端传给你的id填充")
    @PreAuthorize("hasAnyAuthority('经销商修改')")
    public Result<NullData> modifyCustomer(
            @RequestBody CustomerVO customerVO
    ){

        boolean isSuccess = customerService.updateCustomer(customerVO);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,null);
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,null);
        }
    }


    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 15:36
     * @Param: id-->数据库主键的唯一标识
     * @Return:
     **/
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除经销商", notes = "注意删除时只需要传入经销商的id 或者 经销商的企业名字")
    @PreAuthorize("hasAnyAuthority('经销商删除')")
    public Result<NullData> deleteCustomer(
            @RequestParam("id") Long id,
            @RequestParam(value = "enterpriseName",required = false) String enterpriseName
    ){
        boolean isSuccess = false;
        if (id != null){
            isSuccess = customerService.delCustomer(id);
        } else if (enterpriseName != null){
            isSuccess = customerService.delCustomer(enterpriseName);
        }

        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,new NullData());
        }else {
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,new NullData());
        }

    }
}

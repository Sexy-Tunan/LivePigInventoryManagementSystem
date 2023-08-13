package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.domin.ErrorCode;
import com.tunan.inventoryManagementSystem.domin.NullData;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.VO.ProviderVO;
import com.tunan.inventoryManagementSystem.service.ProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 供应商管理处理类（增删改查）
 * @Author: CaiGou
 * @Date: 2023/4/16 14:51
 * @Param:
 * @Return:
 **/
@RestController
@RequestMapping("/provider")
@Validated
@Api(tags = "供应商相关接口")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * @Description: 获取所有供应商的名字，以给前端动态生成下拉表框，供用户条件查询时使用
     * @Author: CaiGou
     * @Date: 2023/4/16 14:52
     * @Param:
     * @Return:
     **/
    @GetMapping("/getCombobox")
    @ApiOperation(value = "获取所有供应商的名字")
    @PreAuthorize("hasAuthority('供应商查询')")
    public Result<List<String>>getCombobox(){
        //TODO 返回所有的供应商的企业名字
        List<String> combobox = providerService.getCombobox();
        if (combobox != null){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,combobox);
        }
//        return new Result<>(ErrorCode.RESOURCE_NOT_FOUND,ErrorCode.RESOURCE_NOT_FOUND_MSG);
        return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS);
    }



    /**
     * @Description: 分页查询供应商信息
     * @Author: CaiGou
     * @Date: 2023/4/16 14:56
     * @Param:
     * @Return:
     */
    @GetMapping("/pageQuery")
    @ApiOperation(value = "分页查询供应商信息")
    @PreAuthorize("hasAuthority('供应商查询')")
    public Result<List<ProviderVO>> getProviders(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        //TODO 分页查询供应商信息
        List<ProviderVO> providers = providerService.getProviders(pageNum, pageSize);

        int count = providerService.getCounts();

        if (providers != null){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,count,providers);
        }else {
//            return new Result<>(ErrorCode.RESOURCE_NOT_FOUND,ErrorCode.RESOURCE_NOT_FOUND_MSG);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,count,null);
        }

    }

    /**
     * @Description: 条件查询供应商
     * @Author: CaiGou
     * @Date: 2023/4/22 18:07
     * @Param:
     * @Return:
     **/
    @GetMapping("/pageQueryConditionalProviders")
    @ApiOperation(value = "条件查询供应商并分页")
    @PreAuthorize("hasAuthority('供应商查询')")
    public Result<List<ProviderVO>> getConditionalProviders(
            @RequestBody ProviderVO providerVO,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
        List<ProviderVO> conditionalProvider
                = providerService.getConditionalProvider(providerVO.getId(), providerVO.getEnterpriseName(),
                                                        providerVO.getAddress(), providerVO.getContact(), providerVO.getPhoneNumber(),
                                                        pageNum, pageSize);

        int count = providerService.getCounts();

        if (conditionalProvider != null){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS, count, conditionalProvider);
        }else {
//            return new Result<>(ErrorCode.RESOURCE_NOT_FOUND,ErrorCode.RESOURCE_NOT_FOUND_MSG);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS, count, null);
        }
    }



    /**
     * @Description: 添加供应商
     * @Author: CaiGou
     * @Date: 2023/4/16 14:57
     * @Param:
     * @Return:
     **/
    @PostMapping("/add")
    @ApiOperation(value = "添加新的供应商")
    @PreAuthorize("hasAuthority('供应商添加')")
    public Result<NullData> addProvider(
            @RequestBody ProviderVO providerVO
    ){
        //TODO 在controller层检验前端传入的基本信息是否完整。providerVO的属性，除了id和createDatetime
        boolean isSuccess = providerService.addProvider(providerVO);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,new NullData());
        } else {
//          return new Result<>(ErrorCode.COMMON_ADD_FAILURE,ErrorCode.COMMON_ADD_FAILURE_MSG,new NullData());
          return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,new NullData());
        }
    }


    
    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 15:15
     * @Param: 
     * @Return: 
     **/
    @PutMapping("/modify")
    @ApiOperation(value = "修改已有的供应商信息")
    @PreAuthorize("hasAuthority('供应商修改')")
    public Result<NullData> modifyProvider(
        @RequestBody @Valid ProviderVO providerVO
    ){

        boolean isSuccess = providerService.updateProvider(providerVO);
        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,null);
        }else {
//            return new Result<>(ErrorCode.COMMON_MODIFY_FAILURE,ErrorCode.COMMON_MODIFY_FAILURE_MSG,null);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS,null);
        }
    }
    

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 15:25
     * @Param: id-->数据库主键的唯一标识。
     * @Return:
     **/
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除供应商")
    @PreAuthorize("hasAuthority('供应商删除')")
    public Result<NullData> deleteProvider(
            @RequestBody Long id,
            @RequestBody String enterpriseName
    ){
        boolean isSuccess = false;
        if (id != null){
            isSuccess = providerService.delProvider(id);
        } else if (enterpriseName != null){
            isSuccess = providerService.delProvider(enterpriseName);
        }

        if (isSuccess){
            return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS, null );
        }else {
//            return new Result<>(ErrorCode.COMMON_MODIFY_FAILURE,ErrorCode.COMMON_MODIFY_FAILURE_MSG, null);
            return new Result<>(ErrorCode.REQ_ERROR_CODE,ErrorCode.REQ_ERROR_MESS, null);
        }
    }

}

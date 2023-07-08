package cn.crbz.controller.permission;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.vo.StoreMenuVO;
import cn.crbz.modules.member.service.StoreMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 管理端,菜单管理接口
 *
 * @author Chopper
 * @since 2020/11/20 12:07
 */
@Slf4j
@RestController
@Api(tags = "店铺端,菜单管理接口")
@RequestMapping("/store/menu")
public class StoreMenuController {

    @Autowired
    private StoreMenuService storeMenuService;


    @ApiOperation(value = "获取所有菜单")
    @GetMapping("/tree")
    public ResultMessage<List<StoreMenuVO>> getAllMenuList() {
        return ResultUtil.data(storeMenuService.tree());
    }

    @ApiOperation(value = "获取所有菜单---根据当前用户角色")
    @GetMapping("/memberMenu")
    public ResultMessage<List<StoreMenuVO>> memberMenu() {
        return ResultUtil.data(storeMenuService.findUserTree());
    }
}

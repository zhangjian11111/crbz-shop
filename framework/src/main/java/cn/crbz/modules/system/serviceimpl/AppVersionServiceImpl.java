package cn.crbz.modules.system.serviceimpl;

import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.common.utils.StringUtils;
import cn.crbz.modules.system.entity.dos.AppVersion;
import cn.crbz.modules.system.mapper.AppVersionMapper;
import cn.crbz.modules.system.service.AppVersionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * APP版本控制业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 8:02 下午
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    @Override
    public AppVersion getAppVersion(String appType) {
        return this.baseMapper.getLatestVersion(appType);
    }

    @Override
    public boolean checkAppVersion(AppVersion appVersion) {
        if (null == appVersion) {
            throw new ServiceException(ResultCode.APP_VERSION_PARAM_ERROR);
        }
        if (StringUtils.isBlank(appVersion.getType())) {
            throw new ServiceException(ResultCode.APP_VERSION_TYPE_ERROR);
        }
        //检测版本是否存在（同类型APP下版本不允许重复）
        if (null != this.getOne(new LambdaQueryWrapper<AppVersion>()
                .eq(AppVersion::getVersion, appVersion.getVersion())
                .eq(AppVersion::getType, appVersion.getType())
                .ne(appVersion.getId() != null, AppVersion::getId, appVersion.getId()))) {
            throw new ServiceException(ResultCode.APP_VERSION_EXIST);
        }
        return true;
    }
}

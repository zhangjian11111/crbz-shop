package cn.crbz.modules.logistics;

import cn.hutool.json.JSONUtil;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.modules.logistics.entity.enums.LogisticsEnum;
import cn.crbz.modules.logistics.plugin.kdniao.KdniaoPlugin;
import cn.crbz.modules.logistics.plugin.kuaidi100.Kuaidi100Plugin;
import cn.crbz.modules.logistics.plugin.shunfeng.ShunfengPlugin;
import cn.crbz.modules.system.entity.dos.Setting;
import cn.crbz.modules.system.entity.dto.LogisticsSetting;
import cn.crbz.modules.system.entity.enums.SettingEnum;
import cn.crbz.modules.system.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件服务抽象工厂 直接返回操作类
 *
 * @author Bulbasaur
 * @version v1.0
 * 2022-06-06 11:35
 */

@Component
public class LogisticsPluginFactory {


    @Autowired
    private SettingService settingService;


    /**
     * 获取logistics client
     *
     * @return
     */
    public LogisticsPlugin filePlugin() {

        LogisticsSetting logisticsSetting = null;
        try {
            Setting setting = settingService.get(SettingEnum.LOGISTICS_SETTING.name());
            logisticsSetting = JSONUtil.toBean(setting.getSettingValue(), LogisticsSetting.class);
            switch (LogisticsEnum.valueOf(logisticsSetting.getType())) {
                case KDNIAO:
                    return new KdniaoPlugin(logisticsSetting);
                case KUAIDI100:
                    return new Kuaidi100Plugin(logisticsSetting);
                case SHUNFENG:
                    return new ShunfengPlugin(logisticsSetting);
                default:
                    throw new ServiceException();
            }
        } catch (Exception e) {
            throw new ServiceException();
        }
    }


}

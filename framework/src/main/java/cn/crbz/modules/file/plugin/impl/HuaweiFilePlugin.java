package cn.crbz.modules.file.plugin.impl;

import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.modules.file.entity.enums.OssEnum;
import cn.crbz.modules.file.plugin.FilePlugin;
import cn.crbz.modules.system.entity.dto.OssSetting;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import lombok.extern.slf4j.Slf4j;
import com.obs.services.model.DeleteObjectsRequest;
import com.obs.services.model.DeleteObjectsResult;
import com.obs.services.model.ListVersionsResult;
import com.obs.services.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 华为obs 文件操作
 *
 * @author jianjian
 */

@Slf4j
public class HuaweiFilePlugin implements FilePlugin {

    private OssSetting ossSetting;

    public HuaweiFilePlugin(OssSetting ossSetting) {
        this.ossSetting = ossSetting;
    }

    @Override
    public OssEnum pluginName() {
        return OssEnum.HUAWEI_OBS;
    }

    /**
     * 获取oss client
     *
     * @return
     */
    private ObsClient getObsClient() {
        return new ObsClient(ossSetting.getHuaweicloudOBSAccessKey(), ossSetting.getHuaweicloudOBSSecretKey(), ossSetting.getHuaweicloudOBSEndPoint());
    }


    @Override
    public String pathUpload(String filePath, String key) {
        ObsClient obsClient = getObsClient();
        try {
            obsClient.putObject(ossSetting.getHuaweicloudOBSBucketName(), key, new File(filePath));
        } catch (ObsException ce) {
            log.error("Error Message: " + ce.getMessage());
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        } finally {
            try {
                // 关闭OBS连接
                obsClient.close();
            } catch (IOException e) {
                log.error("OBS关闭连接报错！" + e.getMessage());
                throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
            }
        }
        return getUrlPrefix() + key;
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String key) {
        ObsClient obsClient = getObsClient();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossSetting.getHuaweicloudOBSBucketName(), key, inputStream);
            obsClient.putObject(putObjectRequest);
        } catch (ObsException obsException) {
            obsException.printStackTrace();
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        } finally {
            try {
                // 关闭OBS连接
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("OBS关闭连接报错！" + e.getMessage());
                throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
            }
        }

        return getUrlPrefix() + key;
    }

    @Override
    public void deleteFile(List<String> keys) {
        ObsClient obsClient = getObsClient();
        ListVersionsResult result;
        try {
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(ossSetting.getHuaweicloudOBSBucketName());
            //deleteRequest.setQuiet(true); // 注意此demo默认是详细模式，如果要使用简单模式，请添加本行代码
            for (String key : keys) {
                deleteRequest.addKeyAndVersion(key);
            }
            DeleteObjectsResult deleteResult = obsClient.deleteObjects(deleteRequest);
            // 获取删除成功的对象
            log.info("删除成功：" + deleteResult.getDeletedObjectResults());
            // 获取删除失败的对象
            log.info("删除失败：" + deleteResult.getErrorResults());
        } catch (ObsException obsException) {
            throw new ServiceException(ResultCode.OSS_DELETE_ERROR);
        } finally {
            try {
                // 关闭OBS连接
                obsClient.close();
            } catch (IOException e) {
                log.error("OBS关闭连接报错！" + e.getMessage());
                throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
            }
        }
    }


    /**
     * 获取配置前缀
     *
     * @return
     */
    private String getUrlPrefix() {
        return "https://" + ossSetting.getHuaweicloudOBSBucketName() + "." + ossSetting.getHuaweicloudOBSEndPoint() + "/";
    }
}

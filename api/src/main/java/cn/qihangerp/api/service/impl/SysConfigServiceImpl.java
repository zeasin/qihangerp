package cn.qihangerp.api.service.impl;

import cn.qihangerp.core.redis.RedisCache;
import cn.qihangerp.api.mapper.SysConfigMapper;
import cn.qihangerp.common.annotation.DataSource;
import cn.qihangerp.common.constant.CacheConstants;
import cn.qihangerp.common.constant.UserConstants;
import cn.qihangerp.core.text.Convert;
import cn.qihangerp.common.enums.DataSourceType;
import cn.qihangerp.common.exception.ServiceException;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.domain.SysConfig;
import cn.qihangerp.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * 参数配置 服务层实现
 * 
 * @author qihang
 */

@Service
public class SysConfigServiceImpl implements ISysConfigService
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
        logger.info("初始化系统配置缓存！！");
        try {
            loadingConfigCache();
        }catch (Exception ex){
            logger.info("初始化系统配置缓存失败"+ex.getMessage());
        }
    }

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId)
    {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        try {
            String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
//            String configValue = Convert.toStr(CaffeineUtil.get(getCacheKey(configKey)));

            if (StringUtils.isNotEmpty(configValue)) {
                return configValue;
            }
            SysConfig config = new SysConfig();
            config.setConfigKey(configKey);
            SysConfig retConfig = configMapper.selectConfig(config);
            if (StringUtils.isNotNull(retConfig)) {
                redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
//                redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
//                CaffeineUtil.put(getCacheKey(configKey), retConfig.getConfigValue());
                return retConfig.getConfigValue();
            }
            return StringUtils.EMPTY;
        }catch (Exception e){
            logger.error("读取Redis出错，"+e.getMessage());
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled()
    {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled))
        {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config)
    {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config)
    {
        int row = configMapper.insertConfig(config);
        if (row > 0)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
//            CaffeineUtil.put(getCacheKey(config.getConfigKey()), config.getConfigValue());

        }
        return row;
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config)
    {
        SysConfig temp = configMapper.selectConfigById(config.getConfigId());
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey()))
        {
            redisCache.deleteObject(getCacheKey(temp.getConfigKey()));
//            CaffeineUtil.remove(getCacheKey(temp.getConfigKey()));
        }

        int row = configMapper.updateConfig(config);
        if (row > 0)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
//            CaffeineUtil.put(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     * 
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds)
    {
        for (Long configId : configIds)
        {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType()))
            {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configMapper.deleteConfigById(configId);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
//            CaffeineUtil.remove(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache()
    {

        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
//            CaffeineUtil.put(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache()
    {
//        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
//        redisCache.deleteObject(keys);

    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache()
    {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config)
    {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}

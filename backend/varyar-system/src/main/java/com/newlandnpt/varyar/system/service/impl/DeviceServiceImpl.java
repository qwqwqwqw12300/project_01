package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.constant.CacheConstants;
import com.newlandnpt.varyar.common.core.domain.entity.TOrg;
import com.newlandnpt.varyar.common.core.redis.RedisCache;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.core.device.settings.RadarDeviceSettingsDisposer;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.domain.TDeviceFence;
import com.newlandnpt.varyar.system.domain.TRoomZone;
import com.newlandnpt.varyar.system.domain.dto.org.OrgDeviceCountDto;
import com.newlandnpt.varyar.system.mapper.*;
import com.newlandnpt.varyar.system.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.newlandnpt.varyar.common.constant.DeviceConstants.*;
import static com.newlandnpt.varyar.common.utils.SecurityUtils.getLoginUserName;

/**
 * 设备Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class DeviceServiceImpl implements IDeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    private TDeviceMapper deviceMapper;
    @Autowired
    private IOrgService orgService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IRoomZoneService roomZoneService;
    @Autowired
    private IDeviceFenceService deviceFenceService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
        loadingDeviceCache();
    }

    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public TDevice selectDeviceByDeviceId(Long deviceId) {
        return deviceMapper.selectTDeviceByDeviceId(deviceId);
    }
    @Override
    public List<TDevice> selectDeviceByMember(Long memberId){
        return deviceMapper.selectTDeviceByMember(memberId);
    }
    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备
     */
    @DataScope(orgAlias = "d")
    @Override
    public List<TDevice> selectDeviceList(TDevice device) {
        return deviceMapper.selectTDeviceList(device);
    }
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public List<TDevice> selectDeviceByMemberId(Map map){
        return deviceMapper.selectByMemberId(map);
    }
    /**
     * 新增设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    public int insertDevice(TDevice device) {
        device.autoSetCreateByLoginUser();
        int effect = deviceMapper.insertTDevice(device);
        resetDeviceCache(device);
        return effect;
    }

    /**
     * 修改设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int updateDevice(TDevice device) {
        device.setUpdateTime(DateUtils.getNowDate());
        Long familyId = device.getFamilyId();
        Long roomId = device.getRoomId();
        setSettings(device.getDeviceId(),device.getParameter());
        device.setFamilyId(familyId);
        device.setRoomId(roomId);
        int effect = deviceMapper.updateTDevice(device);
        resetDeviceCache(device);
        return effect;
    }

    /**
     * 批量删除设备
     *
     * @param deviceIds 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceIds(Long[] deviceIds) {
        return deviceMapper.deleteTDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息
     *
     * @param deviceId 设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceId(Long deviceId) {
        return deviceMapper.deleteTDeviceByDeviceId(deviceId);
    }

    @Override
    public int associate(TDevice device) {

        TDevice target = deviceMapper.selectTDeviceByDeviceId(device.getDeviceId());
        if(target == null){
            throw new ServiceException("设备不存在");
        }
        if(target.getDevicegroupId()!=null){
            throw new ServiceException("设备已经分配设备组，需解除分配才能重新配对");
        }
        Long orgId = device.getOrgId();
        if (orgId != null) {
            TOrg org = orgService.selectOrgById(orgId);
            if (org == null) {
                throw new ServiceException("机构id: " + orgId + "的机构不存在");
            }
            target.setOrgId(orgId);
            target.setOrgName(org.getOrgName());
            target.setDistributeFlag(DISTRIBUTE_FLAG_YES);
        }
        target.setLocation(device.getLocation());
        // 确认配对设备这里的别名是否就是设备名称-- 已确认目前别名没作用，这里就是修改设备名称
        target.setName(device.getName());
        target.autoSetUpdateByLoginUser();
        return this.updateDevice(target);
    }

    @Override
    public int active(Long deviceId) {
        TDevice device = selectDeviceByDeviceId(deviceId);
        device.setDeviceId(deviceId);
        device.setStatus(STATUS_ACTIVATED);
        device.setUpdateTime(DateUtils.getNowDate());
        int effect= deviceMapper.updateTDevice(device);
        resetDeviceCache(device);
        return effect;
    }

    @Override
    public int offline(Long deviceId) {
        TDevice device = new TDevice();
        device.setDeviceId(deviceId);
        device.setStatus(STATUS_OFFLINE);
        return this.updateDevice(device);
    }

    @Override
    public int arrangeDeviceToGroup(Long[] deviceIds, Long deviceGroupId) {
        return deviceMapper.arrangeDeviceToGroup(deviceIds,deviceGroupId,getLoginUserName());
    }

    /**
     * 根据设备号查询设备
     * @param deviceNo
     * @return
     */
    @Override
    public TDevice selectByDeviceNo(String deviceNo){
        return deviceMapper.selectByDeviceNo(deviceNo);
    }

    @Override
    public String importDevice(List<TDevice> devices, Long orgId) {
        if (StringUtils.isNull(devices) || devices.size() == 0) {
            throw new ServiceException("导入设备数据不能为空！");
        }
        TOrg org = orgService.selectOrgById(orgId);
        if (org == null) {
            throw new ServiceException("机构id: "+orgId+"的机构不存在");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (TDevice device : devices) {
            try {
                //校验设备编号不为空
                if (StringUtils.isBlank(device.getNo())) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备编号不能为空");
                    continue;
                }
                //校验设备编号是否存在
                TDevice oldDevice = deviceMapper.selectByDeviceNo(device.getNo());
                if (oldDevice != null) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备编号 " + device.getNo() + " 已存在");
                    continue;
                }
                //设置机构id
                device.setOrgId(orgId);
                device.setOrgName(org.getOrgName());
                //默认使用设备编号作为名称
                device.setName(device.getNo());
                //默认未激活
                device.setStatus(STATUS_NOT_ACTIVE);
                //默认未分配
                device.setDistributeFlag(DISTRIBUTE_FLAG_NO);
                //获取当前登录用户名作为创建人
                device.autoSetCreateByLoginUser();
                this.insertDevice(device);
                successNum++;
//                successMsg.append("<br/>" + successNum + "、设备编号 " + device.getNo() + " 导入成功");
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备编号 " + device.getNo() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条");
        }
        return successMsg.toString();
    }

    @Override
    public long total(TDevice device) {
        return deviceMapper.total(device);
    }

    @Override
    @DataScope(orgAlias = "d")
    public long notAssociateDeviceCount(TDevice device) {
        return deviceMapper.notAssociateDeviceCount(device);
    }

    @Override
    @DataScope(orgAlias = "d")
    public long notArrangeDeviceCount(TDevice device) {
        return deviceMapper.notArrangeDeviceCount(device);
    }

    @Override
    @DataScope(orgAlias = "d")
    public List<OrgDeviceCountDto> countGroupByOrgId(TDevice device) {
        return deviceMapper.countGroupByOrgId(device);
    }

    @Override
    public List<TDevice> selectBizCareDeviceList(Long userId) {
        return deviceMapper.selectBizCareDeviceList(userId);
    }

    @Override
    public void loadingDeviceCache() {
        List<TDevice> devices = deviceMapper.selectTDeviceList(new TDevice());
        for (TDevice device : devices)
        {
            redisCache.setCacheObject(getCacheKey(device.getNo()), device);
        }
    }

    @Override
    public TDevice loadDeviceFromCacheByNo(String deviceNo) {
        return redisCache.getCacheObject(getCacheKey(deviceNo));
    }

    @Override
    public TDevice.DeviceParameter loadSettings(Long deviceId) {
        TDevice device = deviceMapper.selectTDeviceByDeviceId(deviceId);
        if(device == null){
            return null;
        }
        TDevice.DeviceParameter settings;
        if(TYPE_READER_WAVE.equals(device.getType())){
            settings = device.getParameter();
            if(settings == null){
                settings = new TDevice.RadarWaveDeviceSettings();
            }
            TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings) settings;
            if(device.getRoomId()!=null){
                //radarWaveDeviceSettings.setRoom(roomService.selectTRoomByRoomId(device.getRoomId()));
                TRoomZone roomZone = new TRoomZone();
                roomZone.setRoomId(device.getRoomId());
                radarWaveDeviceSettings.setRoomZones(roomZoneService.selectTRoomZoneList(roomZone));
            }
        }else if(TYPE_WATCH.equals(device.getType())){
            settings = new TDevice.WatchSettings();
            TDevice.WatchSettings watchSettings = (TDevice.WatchSettings) settings;
            if(device.getParameter()!=null){
                watchSettings.setList(((TDevice.WatchSettings)device.getParameter()).getList());
            }
            List<TDeviceFence> fences = deviceFenceService.selectTDeviceFenceByDeviceId(deviceId);
            watchSettings.setFence(Optional.ofNullable(fences)
                    .map(list->list.stream().findFirst().orElse(null))
                    .orElse(null));
        }else {
            settings = null;
        }
        return settings;
    }

    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int setSettings(Long deviceId, TDevice.DeviceParameter settings) {
        TDevice device = deviceMapper.selectTDeviceByDeviceId(deviceId);
        if(device == null){
            throw new ServiceException("设备不存在");
        }
        if(TYPE_READER_WAVE.equals(device.getType())){
            TDevice.RadarWaveDeviceSettings radarWaveDeviceSettings = (TDevice.RadarWaveDeviceSettings) settings;
            if(device.getParameter()==null){
                device.setParameter(new TDevice.RadarWaveDeviceSettings());
            }
            device.setParameter(radarWaveDeviceSettings);
            //更新设备参数信息
            deviceMapper.updateTDevice(device);
            TRoomZone roomZone = new TRoomZone();
            roomZone.setDeviceId(device.getDeviceId());
            List<TRoomZone> roomZones = roomZoneService.selectTRoomZoneList(roomZone);
            List<Long> removeZones = new ArrayList<>();
            if(CollectionUtils.isEmpty(radarWaveDeviceSettings.getRoomZones())){
                removeZones.addAll(roomZones.stream()
                        .map(p->p.getRoomZoneId())
                        .collect(Collectors.toList()));
            }else{
                removeZones.addAll(roomZones.stream()
                        .filter(p->radarWaveDeviceSettings.getRoomZones().stream().noneMatch(q->q.getRoomZoneId()!=null&&p.getRoomZoneId()== q.getRoomZoneId()))
                        .map(p->p.getRoomZoneId())
                        .collect(Collectors.toList()));
                radarWaveDeviceSettings.getRoomZones().forEach(zone->{
                    if(zone.getRoomZoneId()!=null){
                        zone.autoSetUpdateByLoginUser();
                        roomZoneService.updateTRoomZone(zone);
                    }else{
                        zone.autoSetCreateByLoginUser();
                        roomZoneService.insertTRoomZone(zone);
                    }
                });
            }

            if(removeZones.size()>0){
                roomZoneService.deleteTRoomZoneByRoomZoneIds(removeZones.stream()
                        .toArray(Long[]::new));
            }

        }else if(TYPE_WATCH.equals(device.getType())){
            TDevice.WatchSettings watchSettings = (TDevice.WatchSettings) settings;
            if(device.getParameter()==null){
                device.setParameter(new TDevice.DeviceParameter());
            }
            ((TDevice.WatchSettings)device.getParameter()).setList(watchSettings.getList());
            deviceMapper.updateTDevice(device);
            if(watchSettings.getFence()!=null){
                if(watchSettings.getFence().getDeviceFenceId()!=null){
                    deviceFenceService.updateTDeviceFence(watchSettings.getFence());
                }else{
                    deviceFenceService.insertTDeviceFence(watchSettings.getFence());
                }
            }
        }
        //下发设备参数
        RadarDeviceSettingsDisposer.dispose(device.getNo(),settings);
        return 1;
    }



    @Override
    public List<TDevice> loadingDeviceStauts(List<TDevice> devices) {
        //模拟测试数据
        //redisCache.setCacheObject(CacheConstants.DEVICE_ONLINE_FLAG_KEY + "12305","online:1");
        //redisCache.setCacheObject(CacheConstants.DEVICE_ONLINE_FLAG_KEY + "12306", "online:1");
        //redisCache.setCacheObject(CacheConstants.DEVICE_ONLINE_FLAG_KEY + "201", "{"deviceNo":"203","online":"1"}");

        for (TDevice device : devices)
        {
            //Redis验证设备断网情况，无记录则确为断网
            String deviceNo = device.getNo();
            if (!redisCache.hasKey(CacheConstants.DEVICE_STATE_KEY + deviceNo)) {
                device.setOnlineFlag("0");
            }else {
                device.setOnlineFlag("1");
            }

        }
        return  devices;
    }

    @Override
    public TDevice loadingDeviceStauts(TDevice device) {
            //Redis验证设备在线离线状态
            String deviceNo = device.getNo();
            if (!redisCache.hasKey(CacheConstants.DEVICE_STATE_KEY + deviceNo)) {
                device.setOnlineFlag("0");
            }else {
                device.setOnlineFlag("1");
            }
        return  device;
    }

    /**
     * 重置单个设备缓存
     */
    protected void resetDeviceCache(TDevice device){
        if(device!=null)
            redisCache.setCacheObject(getCacheKey(device.getNo()), device);
    }

    /**
     * 设置cache key
     *
     * @param deviceNo 设备编号
     * @return 缓存键key
     */
    private String getCacheKey(String deviceNo)
    {
        return CacheConstants.T_DEVICE_KEY + deviceNo;
    }
}

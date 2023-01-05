package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.core.domain.entity.Org;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.Device;
import com.newlandnpt.varyar.system.domain.dto.org.OrgDeviceCountDto;
import com.newlandnpt.varyar.system.mapper.DeviceMapper;
import com.newlandnpt.varyar.system.mapper.OrgMapper;
import com.newlandnpt.varyar.system.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    private DeviceMapper deviceMapper;
    @Autowired
    private OrgMapper orgMapper;

    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public Device selectDeviceByDeviceId(Long deviceId) {
        return deviceMapper.selectTDeviceByDeviceId(deviceId);
    }

    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备
     */
    @DataScope(orgAlias = "d")
    @Override
    public List<Device> selectDeviceList(Device device) {
        return deviceMapper.selectTDeviceList(device);
    }
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public List<Device> selectDeviceByMemberId(Map map){
        return deviceMapper.selectByMemberId(map);
    }
    /**
     * 新增设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    public int insertDevice(Device device) {
        device.autoSetCreateByLoginUser();
        return deviceMapper.insertTDevice(device);
    }

    /**
     * 修改设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    public int updateDevice(Device device) {
        device.setUpdateTime(DateUtils.getNowDate());
        return deviceMapper.updateTDevice(device);
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
    public int associate(Device device) {
        Device target = deviceMapper.selectTDeviceByDeviceId(device.getDeviceId());
        if(target == null){
            throw new ServiceException("设备不存在");
        }
        Long orgId = device.getOrgId();
        if(orgId!=null){
            Org org = orgMapper.selectOrgById(orgId);
            if (org == null) {
                throw new ServiceException("机构id: "+orgId+"的机构不存在");
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
        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setStatus(STATUS_ACTIVATED);
        return this.updateDevice(device);
    }

    @Override
    public int offline(Long deviceId) {
        Device device = new Device();
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
    public Device selectByDeviceNo(String deviceNo){
        return deviceMapper.selectByDeviceNo(deviceNo);
    }

    @Override
    public String importDevice(List<Device> devices, Long orgId) {
        if (StringUtils.isNull(devices) || devices.size() == 0) {
            throw new ServiceException("导入设备数据不能为空！");
        }
        Org org = orgMapper.selectOrgById(orgId);
        if (org == null) {
            throw new ServiceException("机构id: "+orgId+"的机构不存在");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (Device device : devices) {
            try {
                //校验设备编号不为空
                if (StringUtils.isBlank(device.getNo())) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备编号不能为空");
                    continue;
                }
                //校验设备编号是否存在
                Device oldDevice = deviceMapper.selectByDeviceNo(device.getNo());
                if (oldDevice != null) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备编号 " + device.getNo() + " 已存在");
                    continue;
                }
                //设置机构id
                device.setOrgId(orgId);
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
    public long total(Device device) {
        return deviceMapper.total(device);
    }

    @Override
    @DataScope(orgAlias = "d")
    public long notAssociateDeviceCount(Device device) {
        return deviceMapper.notAssociateDeviceCount(device);
    }

    @Override
    @DataScope(orgAlias = "d")
    public long notArrangeDeviceCount(Device device) {
        return deviceMapper.notArrangeDeviceCount(device);
    }

    @Override
    @DataScope(orgAlias = "d")
    public List<OrgDeviceCountDto> countGroupByOrgId(Device device) {
        return deviceMapper.countGroupByOrgId(device);
    }
}

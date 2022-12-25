package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.core.domain.entity.TOrg;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.system.domain.TDevice;
import com.newlandnpt.varyar.system.mapper.TDeviceMapper;
import com.newlandnpt.varyar.system.mapper.TOrgMapper;
import com.newlandnpt.varyar.system.service.ITDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.newlandnpt.varyar.common.constant.DeviceConstants.*;
import static com.newlandnpt.varyar.common.utils.SecurityUtils.getLoginUserName;

/**
 * 设备Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TDeviceServiceImpl implements ITDeviceService {

    private static final Logger log = LoggerFactory.getLogger(TDeviceServiceImpl.class);

    @Autowired
    private TDeviceMapper tDeviceMapper;
    @Autowired
    private TOrgMapper orgMapper;

    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public TDevice selectTDeviceByDeviceId(Long deviceId) {
        return tDeviceMapper.selectTDeviceByDeviceId(deviceId);
    }

    /**
     * 查询设备列表
     *
     * @param tDevice 设备
     * @return 设备
     */
    @DataScope(orgAlias = "d")
    @Override
    public List<TDevice> selectTDeviceList(TDevice tDevice) {
        return tDeviceMapper.selectTDeviceList(tDevice);
    }

    /**
     * 新增设备
     *
     * @param tDevice 设备
     * @return 结果
     */
    @Override
    public int insertTDevice(TDevice tDevice) {
        tDevice.setCreateTime(DateUtils.getNowDate());
        return tDeviceMapper.insertTDevice(tDevice);
    }

    /**
     * 修改设备
     *
     * @param tDevice 设备
     * @return 结果
     */
    @Override
    public int updateTDevice(TDevice tDevice) {
        tDevice.setUpdateTime(DateUtils.getNowDate());
        return tDeviceMapper.updateTDevice(tDevice);
    }

    /**
     * 批量删除设备
     *
     * @param deviceIds 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceByDeviceIds(Long[] deviceIds) {
        return tDeviceMapper.deleteTDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息
     *
     * @param deviceId 设备主键
     * @return 结果
     */
    @Override
    public int deleteTDeviceByDeviceId(Long deviceId) {
        return tDeviceMapper.deleteTDeviceByDeviceId(deviceId);
    }

    @Override
    public int associate(TDevice tDevice) {
        TDevice target = tDeviceMapper.selectTDeviceByDeviceId(tDevice.getDeviceId());
        if(target == null){
            throw new ServiceException("设备不存在");
        }
        Long orgId = tDevice.getOrgId();
        if(orgId!=null){
            TOrg org = orgMapper.selectOrgById(orgId);
            if (org == null) {
                throw new ServiceException("机构id: "+orgId+"的机构不存在");
            }
            target.setOrgId(orgId);
            target.setOrgName(org.getOrgName());
            target.setDistributeFlag(DISTRIBUTE_FLAG_YES);
        }
        target.setLocation(tDevice.getLocation());
        // todo 确认配对设备这里的别名是否就是设备名称
        target.setName(tDevice.getName());

        return this.updateTDevice(target);
    }

    @Override
    public int active(Long deviceId) {
        TDevice tDevice = new TDevice();
        tDevice.setDeviceId(deviceId);
        tDevice.setStatus(STATUS_ACTIVATED);
        return this.updateTDevice(tDevice);
    }

    @Override
    public int offline(Long deviceId) {
        TDevice tDevice = new TDevice();
        tDevice.setDeviceId(deviceId);
        tDevice.setStatus(STATUS_OFFLINE);
        return this.updateTDevice(tDevice);
    }

    @Override
    public int arrangeDeviceToGroup(Long[] deviceIds, Long deviceGroupId) {
        return tDeviceMapper.arrangeDeviceToGroup(deviceIds,deviceGroupId,getLoginUserName());
    }

    @Override
    public String importDevice(List<TDevice> devices, Long orgId) {
        if (StringUtils.isNull(devices) || devices.size() == 0) {
            throw new ServiceException("导入设备数据不能为空！");
        }
        TOrg org = orgMapper.selectOrgById(orgId);
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
                TDevice oldDevice = tDeviceMapper.selectByDeviceNo(device.getNo());
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
                this.insertTDevice(device);
                successNum++;
                successMsg.append("<br/>" + successNum + "、设备编号 " + device.getNo() + " 导入成功");
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
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}

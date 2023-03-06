package com.newlandnpt.varyar.common.core.redis;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

/**
 * spring redis 工具类
 *
 * @author ruoyi
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key)
    {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key)
    {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public boolean deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey)
    {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }

    /**
     * 获取原子级自增数
     * @param key
     * @param liveTime
     * @return
     */
    public long getAtomicIncrementLong(String key,long liveTime){
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        return increment;
    }


    /**
     * 获取原子级自增数
     * @param key
     * @param liveTime
     * @return
     */
    public String getAtomicIncrementLongWithDecimalFormat(String key,long liveTime,int bit){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<bit;i++){
            stringBuffer.append("0");
        }
        long num = getAtomicIncrementLong(key,liveTime);

        DecimalFormat df = new DecimalFormat(stringBuffer.toString());
        return df.format(num);
    }

    /**
     * 设置24小时时间轮的值
     * 时间轮单位秒，记录24小时 每一刻度的具体时间（年月日时分秒）
     * @param key
     * @param liveTime 存活时间 单位秒
     * @param time 设置的值
     * @return
     */
    public void set24HourTimeWheelValue(String key,long liveTime, LocalTime graduation,Date time){
        redisTemplate.opsForZSet().removeRangeByScore(key,graduation.toSecondOfDay(),graduation.toSecondOfDay());
        redisTemplate.opsForZSet().add(key,time,graduation.toSecondOfDay());
        redisTemplate.expire(key,liveTime,TimeUnit.SECONDS);
    }

    /**
     * 获取24小时时间轮的值
     * 时间轮
     * @param key
     * @param graduation 刻度值 精确到秒
     * @return
     */
    public Date get24HourTimeWheelValue(String key, LocalTime graduation){
        Set<Date> result = redisTemplate.opsForZSet().rangeByScore(key,graduation.toSecondOfDay(),graduation.toSecondOfDay());
        return Optional
                .ofNullable(result)
                .map(p->p.stream().sorted(Comparator.comparing(t->-t.getTime())).findFirst().orElse(null))
                .orElse(null);
    }

    /**
     * 时间轮
     */
    public static class TimeWheel {

        /**
         * 时间轮的key，唯一标识
         */
        private String key;

        /**
         * 时间轮的单位
         */
        private TimeWheelUnit unit;

        /**
         * 当前刻度
         */
        private LocalTime currentGraduation;

        public TimeWheel(String key, TimeWheelUnit unit){
            this.key = key;
            this.unit = unit;
            this.currentGraduation = convertToLocalTime(new Date());
        }

        public void setCurrentGraduation(Date time){
            this.currentGraduation = convertToLocalTime(time);
        }

        public LocalTime getCurrentGraduation(){
            return LocalTime.from(this.currentGraduation);
        }

        public void setCurrentGraduationValueWithFix(Date time){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            switch (unit){
                case SECOND:
                    calendar.set(Calendar.SECOND,currentGraduation.getSecond());
                case MINUTES:
                    calendar.set(Calendar.MINUTE,currentGraduation.getMinute());
                case HOUR:
                    calendar.set(Calendar.HOUR_OF_DAY,currentGraduation.getHour());
            }
            setCurrentGraduation(calendar.getTime());
        }

        public void setCurrentGraduationValue(Date time){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            switch (unit){
                case SECOND:
                    if(currentGraduation.getSecond()!=calendar.get(Calendar.SECOND)){
                        throw new ServiceException("设置时间轮的秒数的值与当前刻度不符");
                    }
                case MINUTES:
                    if(currentGraduation.getMinute()!=calendar.get(Calendar.MINUTE)){
                        throw new ServiceException("设置时间轮的秒数的值与当前刻度不符");
                    }
                case HOUR:
                    if(currentGraduation.getHour()!=calendar.get(Calendar.HOUR_OF_DAY)){
                        throw new ServiceException("设置时间轮的秒数的值与当前刻度不符");
                    }
            }

            SpringUtils.getBean(RedisCache.class).set24HourTimeWheelValue(key,2*24*60*60,currentGraduation,time);
        }

        /**
         * 时针归位，回到最开始的地方
         * @param referenceDay 参考日期，将结合当前刻度往前推到未设置正确值的初始位
         */
        public void homing(LocalDate referenceDay){
            // 先默认为当前刻度
            LocalTime graduation = LocalTime.from(this.currentGraduation);

            Calendar calendar = Calendar.getInstance();
            do {
                // 回退一个刻度
                LocalTime preGraduation = minus(graduation);
                Date date = SpringUtils.getBean(RedisCache.class).get24HourTimeWheelValue(key,preGraduation);
                if(date!=null){
                    calendar.setTime(date);
                    if(referenceDay.getYear() == calendar.get(Calendar.YEAR)&&
                            referenceDay.getMonthValue() == calendar.get(Calendar.MONTH)+1&&
                            referenceDay.getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH)){
                        // 年月日相符说明当前刻度无误结束
                        break;
                    }
                }
                graduation = preGraduation;
            }while (graduation.compareTo(this.currentGraduation)!=0);
            this.currentGraduation = graduation;
        }

        public void next(){
            switch (unit){
                case HOUR:
                    this.currentGraduation = this.currentGraduation.plusHours(1);
                    break;
                case MINUTES:
                    this.currentGraduation = this.currentGraduation.plusMinutes(1);
                    break;
                case SECOND:
                    this.currentGraduation = this.currentGraduation.plusSeconds(1);
                    break;
                default:
                    throw new ServiceException("不支持的时间轮单位值："+unit);
            }
        }

        public void previous(){
            this.currentGraduation = minus(this.currentGraduation);
        }

        private LocalTime minus(LocalTime time){
            switch (unit){
                case HOUR:
                    return time.minusHours(1);
                case MINUTES:
                    return time.minusMinutes(1);
                case SECOND:
                    return time.minusSeconds(1);
                default:
                    throw new ServiceException("不支持的时间轮单位值："+unit);
            }
        }

        public Date getCurrentGraduationValue(){
            return SpringUtils.getBean(RedisCache.class).get24HourTimeWheelValue(key,currentGraduation);
        }

        private LocalTime convertToLocalTime(Date time){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            switch (unit){
                case HOUR:
                    return LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY),0);
                case MINUTES:
                    return LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE));
                case SECOND:
                    return LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            calendar.get(Calendar.SECOND));
                default:
                    throw new ServiceException("不支持的时间轮单位值："+unit);
            }
        }

    }

    public enum TimeWheelUnit{
        HOUR,
        MINUTES,
        SECOND
    }

}

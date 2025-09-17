package com.baitabei.service;

import com.baitabei.vo.NotificationVo;
import com.baitabei.common.result.PageResult;

/**
 * 通知服务接口
 */
public interface NotificationService {
    
    /**
     * 发送通知
     */
    void sendNotification(Long userId, String title, String content, String type);
    
    /**
     * 批量发送通知
     */
    void batchSendNotification(String userIds, String title, String content, String type);
    
    /**
     * 获取用户通知列表
     */
    PageResult<NotificationVo> getUserNotifications(Long userId, Integer page, Integer size);
    
    /**
     * 标记通知为已读
     */
    void markAsRead(Long notificationId);
    
    /**
     * 批量标记通知为已读
     */
    void batchMarkAsRead(String notificationIds);
    
    /**
     * 获取未读通知数量
     */
    Integer getUnreadCount(Long userId);
    
    /**
     * 删除通知
     */
    void deleteNotification(Long notificationId);
}

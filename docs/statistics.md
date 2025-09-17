# StatisticsController 接口文档

**Base URL**：`https://api.baitabei.com/api/statistics`  
**统一返回格式**
```json
{
  "code": 0,
  "message": "success",
  "data": { ... }
}
```

---

## 1. 概览统计
**GET** `/overview`  
**描述**：一次性返回总用户数、总项目数、总评审数、今日新增、在线人数等关键指标  
**响应 200**
```json
{
  "code": 0,
  "data": {
    "totalUsers": 1200,
    "totalProjects": 300,
    "totalReviews": 500,
    "activeUsers": 800,
    "todayNewUsers": 25,
    "todayNewProjects": 10,
    "onlineUsers": 56,
    "submittedProjects": 280,
    "completedReviews": 450,
    "pendingReviews": 50
  }
}
```

---

## 2. 用户统计
**GET** `/users`  
**描述**：用户总量、活跃、新增、在线 + 7 日增长趋势 + 状态分布  
**响应 200**
```json
{
  "code": 0,
  "data": {
    "totalUsers": 1200,
    "activeUsers": 800,
    "newUsers": 25,
    "onlineUsers": 56,
    "userGrowthTrend": {
      "2025-09-01": 12,
      "2025-09-02": 18,
      ...
      "2025-09-07": 25
    },
    "userStatusDistribution": {
      "active": 800,
      "inactive": 400
    }
  }
}
```

---

## 3. 项目统计
**GET** `/projects`  
**描述**：项目总量、草稿/已提交、今日新增、按赛道与状态分布、7 日提交趋势  
**响应 200**
```json
{
  "code": 0,
  "data": {
    "totalProjects": 300,
    "submittedProjects": 280,
    "todayNewProjects": 10,
    "draftProjects": 20,
    "projectsByTrack": [
      { "trackId": 1, "trackName": "文创设计", "count": 120 },
      { "trackId": 2, "trackName": "数字内容", "count": 90 }
    ],
    "projectsByStatus": [
      { "status": 2, "statusName": "草稿", "count": 20 },
      { "status": 4, "statusName": "初审通过", "count": 200 }
    ],
    "submissionTrend": {
      "2025-09-01": 8,
      ...
      "2025-09-07": 10
    }
  }
}
```

---

## 4. 评审统计
**GET** `/reviews`  
**描述**：评审总量、已完成、待评审、平均得分、完成率、7 日评审趋势  
**响应 200**
```json
{
  "code": 0,
  "data": {
    "totalReviews": 500,
    "completedReviews": 450,
    "pendingReviews": 50,
    "avgReviewScore": 85.5,
    "completionRate": 90.0,
    "reviewTrend": {
      "2025-09-01": 12,
      ...
      "2025-09-07": 15
    }
  }
}
```

---

## 5. 赛道统计
**GET** `/tracks`  
**描述**：各赛道项目数量、最热门赛道、赛道总数  
**响应 200**
```json
{
  "code": 0,
  "data": {
    "projectsByTrack": [ ... ],
    "mostPopularTrack": { "trackId": 1, "trackName": "文创设计", "count": 120 },
    "totalTracks": 8,
    "activeTracks": 8
  }
}
```

---

## 6. 时间范围统计
**GET** `/timerange?startDate=2025-09-01&endDate=2025-09-07&granularity=day`  
**描述**：按天/周/月粒度返回用户、项目、评审三条时间序列  
**参数**
- `startDate`：yyyy-MM-dd
- `endDate`：yyyy-MM-dd
- `granularity`：day | week | month，默认 day

**响应 200**
```json
{
  "code": 0,
  "data": {
    "userStats": { "2025-09-01": 12, ... },
    "projectStats": { "2025-09-01": 8, ... },
    "reviewStats": { "2025-09-01": 5, ... },
    "granularity": "day",
    "dateRange": { "start": "2025-09-01", "end": "2025-09-07" }
  }
}
```

---

## 7. 导出报表
**GET** `/export?type=overview&startDate=2025-09-01&endDate=2025-09-07`  
**描述**：生成 Excel 报表并返回下载链接（演示阶段仅返回模拟地址）  
**参数**
- `type`：overview | user | project | review
- `startDate/endDate`：可选，限定数据区间

**响应 200**
```json
{
  "code": 0,
  "data": "/downloads/overview_report_2025-09-07T14-23-00.xlsx"
}
```

---

## 公共错误码
| 码值 | 说明               |
|------|--------------------|
| 0    | 成功               |
| 500  | 服务器内部错误     |
# 白塔杯管理端接口文档（仅 AdminController）

**Base URL**：`https://api.baitabei.com/api/admin`  
**认证方式**：登录后返回 `token`，置于请求头 `Authorization: Bearer <token>`  
**统一返回包装**
```json
{
  "code": 0,
  "message": "success",
  "data": { ... }
}
```

---

## 1. 获取系统概览数据
**GET** `/dashboard`  
**描述**：返回首页仪表盘统计数据  
**请求参数**：无  
**响应 200**
```json
{
  "code": 0,
  "data": {
    "totalUsers": 1200,
    "activeUsers": 800,
    "todayNewUsers": 25,
    "onlineUsers": 56,
    "totalProjects": 300,
    "submittedProjects": 280,
    "todayNewProjects": 10,
    "pendingProjects": 20,
    "totalReviews": 500,
    "completedReviews": 450,
    "pendingReviews": 50,
    "avgReviewScore": 85.5,
    "systemStatus": "正常",
    "lastUpdateTime": "2025-09-07T14:23:00"
  }
}
```

---

## 2. 管理员登录
**POST** `/login`  
**描述**：账号密码登录，成功返回 token 及管理员信息  
**请求体**
```json
{
  "username": "admin",
  "password": "admin123"
}
```
**响应 200**
```json
{
  "code": 0,
  "data": {
    "token": "admin_token_1694081234567",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "realName": "系统管理员",
      "role": "admin",
      "permissions": ["admin:*"]
    },
    "loginTime": "2025-09-07T14:23:00"
  }
}
```
**错误 401**
```json
{ "code": 10001, "message": "用户名或密码错误" }
```

---

## 3. 获取系统配置
**GET** `/config`  
**描述**：读取当前系统运行参数  
**响应 200**
```json
{
  "code": 0,
  "data": {
    "siteName": "白塔杯文化创意大赛",
    "siteDescription": "2025年白塔杯文化创意大赛管理系统",
    "version": "1.0.0",
    "registrationEnabled": true,
    "submissionEnabled": true,
    "reviewEnabled": true,
    "maxProjectsPerUser": 5,
    "maxFileSize": 10485760,
    "allowedFileTypes": [".pdf", ".doc", ".docx", ".jpg", ".png"],
    "reviewRounds": 3,
    "maxReviewersPerProject": 5
  }
}
```

---

## 4. 更新系统配置
**PUT** `/config`  
**描述**：全量覆盖式修改系统配置（演示阶段仅记录日志，不持久化）  
**请求体**
```json
{
  "siteName": "白塔杯·2025",
  "registrationEnabled": false
}
```
**响应 200**
```json
{ "code": 0, "message": "success" }
```

---

## 5. 修改用户状态
**PUT** `/users/{userId}/status?status=<value>`  
**描述**：启用/禁用/待审核用户  
**路径参数**
- `userId`：Long，用户主键

**查询参数**
- `status`：String，可选值 `active|1` `disabled|0` `pending|2`

**响应 200**
```json
{ "code": 0, "message": "success" }
```
**错误 400**
```json
{ "code": 400, "message": "无效的用户状态" }
```

---

## 6. 项目审核
**PUT** `/projects/{projectId}/audit?status=<value>`  
**描述**：对提交项目进行初审  
**路径参数**
- `projectId`：Long，项目主键

**查询参数**
- `status`：String，可选值
    - `approved/pass` → 初审通过
    - `rejected/fail` → 初审不通过
    - `reviewing` → 退回初审中

**响应 200**
```json
{ "code": 0, "message": "success" }
```
**错误 400**
```json
{ "code": 400, "message": "无效的审核状态" }
```

---

## 状态码对照
| 状态码 | 说明               |
|--------|--------------------|
| 0      | 成功               |
| 10001  | 登录失败           |
| 400    | 参数错误           |
| 500    | 服务器内部错误     |

---

**文档版本**：v1.0  
**更新日期**：2025-09-07
# 用户管理模块接口文档（OpenAPI 3.0）

> 文档对应代码版本：2025-09-06  
> 基础路径：`/api/user`  
> 认证方式：Bearer Token（JWT），需放在 Header：`Authorization: Bearer <token>`

---

## 1. 当前登录用户

### 1.1 获取当前用户信息
```
GET /api/user/profile
```
**权限**：登录即可  
**请求参数**：无（由 Token 解析）  
**返回示例**：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "phone": "13800138000",
    "realName": "管理员",
    "gender": 1,
    "birthDate": "1990-01-01",
    "avatar": "https://xxx.com/avatar.jpg",
    "institution": "XX医院",
    "profession": "医生",
    "bio": "个人简介",
    "status": 1,
    "emailVerified": 1,
    "phoneVerified": 1,
    "lastLoginTime": "2025-09-06T12:00:00",
    "lastLoginIp": "192.168.1.100",
    "createdTime": "2025-01-01T08:00:00",
    "updatedTime": "2025-09-06T12:00:00"
  }
}
```

### 1.2 更新当前用户信息
```
PUT /api/user/update
```
**权限**：登录即可  
**请求体**：
```json
{
  "realName": "张三",
  "gender": 1,
  "birthDate": "1992-02-02",
  "avatar": "https://xxx.com/new.jpg",
  "institution": "YY医院",
  "profession": "护士长",
  "bio": "更新后的简介"
}
```
> 注：username/email/phone/status 等字段会被忽略，仅更新允许字段。  
**返回示例**：
```json
{ "code": 200, "msg": "更新成功", "data": null }
```

---

## 2. 单点查询（无需管理员权限）

### 2.1 根据 ID 获取用户
```
GET /api/user/{userId}
```

### 2.2 根据用户名获取用户
```
GET /api/user/username/{username}
```

**返回字段**：同 1.1 的 `data` 结构。

---

## 3. 管理员接口（需 ROLE_ADMIN）

### 3.1 分页查询用户
```
GET /api/user/page
```
**Query 参数**：
| 字段       | 类型   | 说明                  | 示例   |
|------------|--------|-----------------------|--------|
| current    | int    | 当前页，默认 1        | 1      |
| size       | int    | 每页条数，默认 10     | 10     |
| username   | string | 模糊查询用户名        | ad     |
| email      | string | 模糊查询邮箱          | @qq    |
| phone      | string | 精确查询手机号        | 138    |
| status     | int    | 状态 0/1/2            | 1      |
| gender     | int    | 性别 0/1/2            | 1      |
| startTime  | date   | 创建时间 ≥            | 2025-08-01 |
| endTime    | date   | 创建时间 ≤            | 2025-09-06 |

**返回示例**：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "records": [ /* 数组，元素同 UserVo */ ],
    "total": 120,
    "size": 10,
    "current": 1
  }
}
```

### 3.2 更新用户状态
```
PUT /api/user/{userId}/status?status=0
```
**status 枚举**：0-禁用，1-正常，2-待审核  
**返回**：`{ "code": 200, "msg": "状态更新成功" }`

### 3.3 批量更新状态
```
PUT /api/user/batch-status?status=0
```
**Body**：
```json
[1,2,3]
```

### 3.4 删除用户
```
DELETE /api/user/delete/{userId}
```

### 3.5 批量删除用户
```
DELETE /api/user/delete/batch
```
**Body**：
```json
[1,2,3]
```

---

## 4. 存在性校验（无需登录）

### 4.1 用户名是否存在
```
GET /api/user/exists/username/{username}
```

### 4.2 邮箱是否存在
```
GET /api/user/exists/email/{email}
```

### 4.3 手机号是否存在
```
GET /api/user/exists/phone/{phone}
```

**返回**：
```json
{ "code": 200, "msg": "success", "data": true }
```

---

## 5. 统计指标（需 ROLE_ADMIN）

| 接口                         | 说明               |
|------------------------------|--------------------|
| `GET /api/user/count`        | 总用户数           |
| `GET /api/user/count/active` | 激活用户数（status=1） |
| `GET /api/user/count/today-new` | 今日新增（createdTime ≥ 今日 00:00） |
| `GET /api/user/count/online` | 在线用户数（最后登录时间在 30 分钟内） |

**返回示例**：
```json
{ "code": 200, "msg": "success", "data": 12345 }
```

---

## 6. 当前登录用户的角色 & 权限

### 6.1 获取角色
```
GET /api/user/roles
```
**返回**：
```json
{ "code": 200, "msg": "success", "data": ["ADMIN","DOCTOR"] }
```

### 6.2 获取权限
```
GET /api/user/permissions
```
**返回**：
```json
{ "code": 200, "msg": "success", "data": ["user:add","user:delete"] }
```

---

## 公共返回格式

| 字段  | 类型   | 说明             |
|-------|--------|------------------|
| code  | int    | 200 成功，其他见错误码表 |
| msg   | string | 提示信息           |
| data  | any    | 业务数据           |

---

## 常见错误码

| HTTP | code | 含义               |
|------|------|--------------------|
| 200  | 200  | 成功               |
| 400  | 400  | 参数校验失败         |
| 401  | 401  | 未登录或 Token 失效 |
| 403  | 403  | 无权限（非 ADMIN）  |
| 404  | 404  | 用户不存在          |
| 500  | 500  | 系统异常            |

---

## 附录：UserVo 字段说明

与 `User` 实体基本一致，但 **不含** `password`、`deleted` 敏感字段。
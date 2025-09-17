# 白塔杯文化创意大赛 · 后端接口总文档
**Base URL**：`https://api.baitabei.com`  
**认证方式**：登录后返回 `token`，置于请求头 `Authorization: Bearer <token>`  
**统一返回包装**
```json
{
  "code": 0,
  "message": "success",
  "data": ...
}
```

---

## 一、Admin 管理端（/api/admin）

| 接口 | 方法 | 说明 | 主要参数 | 返回数据 |
|----|------|------|----------|----------|
| `/login` | POST | 管理员登录 | `{username, password}` | `{token, userInfo, loginTime}` |
| `/dashboard` | GET | 仪表盘概览 | 无 | 总用户数、项目数、评审数、今日新增、在线人数等 |
| `/config` | GET | 读取系统配置 | 无 | 站点信息、功能开关、上限值、评审规则等 |
| `/config` | PUT | 更新系统配置 | JSON 对象 | 无 |
| `/users/{userId}/status` | PUT | 修改用户状态 | 查询参数 `status=active&#124;disabled&#124;pending` | 无 |
| `/projects/{projectId}/audit` | PUT | 项目初审 | 查询参数 `status=approved&#124;rejected&#124;reviewing` | 无 |

---

## 二、Statistics 统计（/api/statistics）

| 接口 | 方法 | 说明 | 主要参数 | 返回数据 |
|----|------|------|----------|----------|
| `/overview` | GET | 关键指标 | 无 | 总用户数、项目数、评审数、今日新增、在线人数 |
| `/users` | GET | 用户统计 | 无 | 总量、活跃、新增、7 日趋势、状态分布 |
| `/projects` | GET | 项目统计 | 无 | 总量、草稿/已提交、赛道分布、状态分布、7 日提交趋势 |
| `/reviews` | GET | 评审统计 | 无 | 总量、完成/待评、平均得分、完成率、7 日趋势 |
| `/tracks` | GET | 赛道统计 | 无 | 各赛道项目数、最热门赛道 |
| `/timerange` | GET | 时间序列 | `startDate, endDate, granularity=day&#124;week&#124;month` | 用户、项目、评审三条时间序列 |
| `/export` | GET | 导出报表 | `type, startDate, endDate` | 文件下载地址 |

---

## 三、Review 评审（/api/reviews）

| 接口 | 方法 | 说明 | 主要参数 | 返回数据 |
|----|------|------|----------|----------|
| `/` | POST | 创建评审 | `ReviewDto` | 新生成 `reviewId` |
| `/{reviewId}` | PUT | 更新评审 | `ReviewDto`（全量） | 无 |
| `/{reviewId}` | DELETE | 删除评审 | 路径 `reviewId` | 无 |
| `/{reviewId}` | GET | 评审详情 | 路径 `reviewId` | 评审完整 VO |
| `/` | GET | 分页列表 | `keyword, page=1, size=10` | `PageResult<ReviewVo>` |
| `/{reviewId}/result` | POST | 提交结果 | 查询参数 `result=pass&#124;reject&#124;revise, comment` | 无 |
| `/project/{projectId}` | GET | 某项目全部评审 | 路径 `projectId, page=1, size=10` | `PageResult<ReviewVo>` |

---

## 公共错误码
| 码值 | 说明 |
|----|------|
| 0 | 成功 |
| 10001 | 登录失败 |
| 400 | 参数非法 |
| 404 | 资源不存在 |
| 409 | 状态冲突 |
| 500 | 服务器内部错误 |

---

**文档版本**：v1.0  
**更新日期**：2025-09-07
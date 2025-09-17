# 2025年白塔杯文化创意大赛后端部署指南

## 环境要求

### 基本要求
- **操作系统**: Linux (Ubuntu 20.04+ / CentOS 7+) 或 Windows Server
- **JDK**: Oracle JDK 8+ 或 OpenJDK 8+
- **数据库**: MySQL 8.0+
- **构建工具**: Maven 3.6+
- **反向代理**: Nginx (可选)
- **缓存**: Redis 6.0+ (可选)

### 推荐配置
- **CPU**: 4核以上
- **内存**: 8GB以上
- **硬盘**: 100GB以上 SSD
- **网络**: 100Mbps以上

## 数据库部署

### 1. 安装MySQL

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install mysql-server-8.0
sudo mysql_secure_installation
```

#### CentOS/RHEL
```bash
sudo yum install mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
sudo mysql_secure_installation
```

### 2. 创建数据库
```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE baitabei_2025 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 创建用户（可选）
CREATE USER 'baitabei'@'%' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON baitabei_2025.* TO 'baitabei'@'%';
FLUSH PRIVILEGES;
```

### 3. 导入数据
```bash
# 导入表结构
mysql -u root -p baitabei_2025 < sql/schema.sql

# 导入初始数据
mysql -u root -p baitabei_2025 < sql/data.sql
```

## Java应用部署

### 1. 安装JDK

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install openjdk-8-jdk
```

#### CentOS/RHEL
```bash
sudo yum install java-1.8.0-openjdk-devel
```

### 2. 安装Maven
```bash
# 下载并安装Maven
wget https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
tar -xzf apache-maven-3.8.6-bin.tar.gz
sudo mv apache-maven-3.8.6 /opt/maven

# 配置环境变量
echo 'export PATH=/opt/maven/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### 3. 构建应用
```bash
# 进入项目目录
cd java-backend

# 清理并构建
mvn clean package -DskipTests

# 构建成功后，生成的JAR文件位于 target 目录
ls target/*.jar
```

### 4. 配置文件
```bash
# 复制配置模板
cp src/main/resources/application.yml.template src/main/resources/application.yml

# 编辑配置文件
vim src/main/resources/application.yml
```

修改以下配置项：
- 数据库连接信息
- 阿里云OSS配置
- JWT密钥
- Redis配置（如果使用）

### 5. 启动应用

#### 直接启动
```bash
java -jar target/competition-backend-1.0.0.jar
```

#### 后台运行
```bash
nohup java -jar target/competition-backend-1.0.0.jar > app.log 2>&1 &
```

#### 使用systemd管理
创建服务文件 `/etc/systemd/system/baitabei-backend.service`：

```ini
[Unit]
Description=BaitaBei Competition Backend
After=network.target

[Service]
Type=simple
User=baitabei
WorkingDirectory=/opt/baitabei
ExecStart=/usr/bin/java -jar /opt/baitabei/competition-backend-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务：
```bash
sudo systemctl daemon-reload
sudo systemctl enable baitabei-backend
sudo systemctl start baitabei-backend
sudo systemctl status baitabei-backend
```

## Nginx配置（可选）

### 1. 安装Nginx
```bash
# Ubuntu/Debian
sudo apt install nginx

# CentOS/RHEL
sudo yum install nginx
```

### 2. 配置反向代理
创建配置文件 `/etc/nginx/sites-available/baitabei-backend`：

```nginx
server {
    listen 80;
    server_name api.baitabei.com;  # 替换为你的域名

    # API路由
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 跨域配置
        add_header Access-Control-Allow-Origin *;
        add_header Access-Control-Allow-Methods 'GET, POST, PUT, DELETE, OPTIONS';
        add_header Access-Control-Allow-Headers 'Authorization, Content-Type';
        
        if ($request_method = 'OPTIONS') {
            return 204;
        }
    }

    # API文档
    location /doc.html {
        proxy_pass http://localhost:8080/doc.html;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态文件上传目录
    location /uploads/ {
        alias /data/upload/;
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
}
```

启用配置：
```bash
sudo ln -s /etc/nginx/sites-available/baitabei-backend /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## 阿里云OSS配置

### 1. 创建 OSS Bucket
1. 登录阿里云控制台
2. 进入对象存储 OSS 服务
3. 创建 Bucket，建议选择“公共读”权限
4. 记录 Bucket 名称和访问域名

### 2. 创建访问密钥
1. 进入 RAM 访问控制
2. 创建用户，授予 AliyunOSSFullAccess 权限
3. 创建访问密钥，记录 AccessKeyId 和 AccessKeySecret

### 3. 更新配置文件
```yaml
aliyun:
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com  # 你的地域节点
    access-key-id: your_access_key_id
    access-key-secret: your_access_key_secret
    bucket-name: your_bucket_name
    url-prefix: https://your_bucket_name.oss-cn-beijing.aliyuncs.com/
```

## 系统监控

### 1. 日志监控
应用日志位于 `logs/baitabei-competition.log`。

```bash
# 实时查看日志
tail -f logs/baitabei-competition.log

# 查看错误日志
grep ERROR logs/baitabei-competition.log
```

### 2. 健康检查
```bash
# 检查应用是否正常运行
curl http://localhost:8080/api/health

# 检查API文档是否可访问
curl http://localhost:8080/doc.html
```

### 3. 性能监控
可以使用以下工具进行性能监控：
- **Prometheus + Grafana**: 系统监控
- **ELK Stack**: 日志分析
- **APM工具**: 应用性能监控

## 备份与恢复

### 1. 数据库备份
```bash
# 全量备份
mysqldump -u root -p --single-transaction --routines --triggers baitabei_2025 > backup_$(date +%Y%m%d_%H%M%S).sql

# 定时备份（添加到crontab）
0 2 * * * mysqldump -u root -p --single-transaction --routines --triggers baitabei_2025 > /backup/db_$(date +\%Y\%m\%d_\%H\%M\%S).sql
```

### 2. 文件备份
```bash
# 备份上传文件
tar -czf uploads_backup_$(date +%Y%m%d_%H%M%S).tar.gz /data/upload/

# 同步到远程服务器
rsync -av /data/upload/ user@backup-server:/backup/uploads/
```

## 故障排查

### 常见问题

1. **应用启动失败**
   - 检查JDK版本
   - 检查数据库连接
   - 查看日志文件

2. **数据库连接失败**
   - 检查MySQL服务状态
   - 验证数据库用户名密码
   - 检查防火墙设置

3. **文件上传失败**
   - 检查阿里云OSS配置
   - 验证AccessKey权限
   - 检查网络连通性

4. **跨域问题**
   - 检查CORS配置
   - 验证Nginx配置
   - 检查前端域名设置

### 日志级别调整
在生产环境中，建议将日志级别调整为 INFO：

```yaml
logging:
  level:
    com.baitabei: info
    root: warn
```

## 安全建议

1. **修改默认密码**
   - 数据库root密码
   - 默认管理员密码
   - JWT密钥

2. **网络安全**
   - 配置防火墙
   - 使用HTTPS
   - 限制数据库访问权限

3. **应用安全**
   - 定期更新依赖
   - 配置访问控制
   - 启用API限流

## 性能优化

1. **JVM参数优化**
```bash
java -Xms2g -Xmx4g -XX:+UseG1GC -jar target/competition-backend-1.0.0.jar
```

2. **数据库优化**
   - 添加索引
   - 优化查询
   - 配置连接池

3. **缓存优化**
   - 启用Redis缓存
   - 配置静态资源缓存
   - 使用CDN加速

---

**作者**: MiniMax Agent  
**日期**: 2025-07-30  
**版本**: 1.0.0

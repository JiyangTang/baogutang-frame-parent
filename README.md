# baogutang frame parent

baogutang frame parent 是一个基于 Spring Boot 的项目框架，旨在提供高性能、可扩展的基础设施。

## JDK版本
**11**
## 项目结构

该项目的核心功能模块包括：

- **Spring Boot 基础框架**：提供项目的基础配置和初始化。
- **Redis 缓存与分布式锁**：使用 `RedisTemplate` 和 `Redisson` 实现缓存管理、分布式锁等功能。

## 特性

- **集成 Redis**：提供高效的缓存、分布式锁、限流等功能。
- **多数据源支持**：支持多个数据库连接池的配置，方便与不同数据库交互。
- **Spring Boot 集成**：快速构建 Spring Boot 应用，简化配置与开发。

## 技术栈

- **Spring Boot**：构建 Java 后端应用。
- **Redis**：缓存与分布式锁功能。
- **Redisson**：高级 Redis 客户端，用于实现分布式锁、限流器等功能。
- **MySQL/PostgreSQL**：关系型数据库。

## 安装与运行

### 克隆项目

```bash
git clone https://github.com/JiyangTang/baogutang-frame-parent.git
cd baogutang-frame-parent

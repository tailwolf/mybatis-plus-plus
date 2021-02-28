## 本框架定位
mybatis-complete，顾名思义，即是对mybatis的补全。  
mybatis-complete没有改变任何mybatis的原生功能和配置，它的核心功能支持无sql双表查询和单表增删改查。  
设计思想是，一两张表的简单操作封装成全自动orm，多表的复杂操作则应该发挥mybatis自身的优势。

## 功能
- [x] 单表
  - [x] 实体类增删改查接口
  - [x] 查询dsl
    - [x] select，groupBy，having，orderBy语法
    - [x] eq(=)，gt(>)，ge(>=)，lt(<)，le(<=)，ne(!=)，like，or，in，not in，is null，is not null语法
  - [x] 删除dsl
  - [x] 修改dsl
- [x] 双表查询(可自动返回嵌套结果，告别resultMap标签)
  - [x] innerJoin，leftJoin，rightJoin语法
  - [x] select，groupBy，having，orderBy语法
  - [x] eq(=)，gt(>)，ge(>=)，lt(<)，le(<=)，ne(!=)，like，or，in，not in，is null，is not null语法
- [x] 分页
- [x] EXISTS和NOT EXISTS的sql脚本
- [x] 逻辑删除
- [x] 填充指定的数据库字段
- [ ] 多租户(1.1版本开发)
- [x] 打印完整的sql语句和执行时间
- [x] 并发版本控制
- [x] 多数据源
  - [x] 基于注解使用的多数据源
  - [x] 基于dsl的动态多数据源
- [ ] 多种主键生成策略（1.2版本后开发，目前只支持自增）
- [ ] 多数据库适配（1.5版本后开发，目前只支持mysql）
- [ ] 代码生成器
  - [x] 逆向工程
  - [ ] 正向工程（1.5版本后开发）

## 特性预览
#### 单表操作：  
```java
EntityQuery<SysUser> entityQuery = new EntityQuery<>();
entityQuery.eq(SysUser::getAccount, "小明").eq(SysUser::getUserPwd, "ggg").asc(SysUser::getId);
List<SysUser> sysUserList = sysUserService.dslQuery(entityQuery);
```
#### 双表操作：  
```java
JoinQuery<SysUser, SysRole> joinQuery = new JoinQuery<>();
joinQuery.select(col -> col.column(SysUser::getAccount).column(SysRole::getRoleName))
        .from(SysUser::new).leftJoin(SysRole::new)
        .on(condition -> condition.eq(SysUser::getId, SysRole::getUserId))
        .eq(SysUser::getAccount, "xiaoming");
List<SysUserVo> sysUserVoList = joinOptService.joinQuery(joinQuery, SysUserVo.class);
```
#### 基于dsl的动态数据源：  
```java
EntityQuery<SysUser> entityQuery = new EntityQuery<>();
entityQuery.eq(SysUser::getAccount, "小明").eq(SysUser::getUserPwd, "ggg").asc(SysUser::getId);
entityQuery.setDataSource("数据源名称");
List<SysUser> sysUserList = sysUserService.dslQuery(entityQuery);
```

## 使用文档
使用文档可直接看mybatis-plus-plus-boot-starter-test下的[配置文件](https://github.com/tailwolf/mybatis-plus-plus/blob/0.x/mybatis-plus-plus-boot-starter-test/src/main/resources/application.yml)和[测试用例](https://github.com/tailwolf/mybatis-plus-plus/tree/0.x/mybatis-plus-plus-boot-starter-test/src/test/java/com/tailwolf/test/doc)

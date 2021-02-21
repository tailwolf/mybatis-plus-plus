## 本框架定位
mybatis++没有改变任何mybatis的原生功能和配置。  
mybatis++核心功能支持无sql双表查询和单表增删改查，设计思想是，对一两张表的操作封装了全自动orm，多表的复杂操作应该发挥mybatis的优势。

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


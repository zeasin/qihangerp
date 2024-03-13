# 启航电商ERP系统


启航电商ERP系统是一套为中小电商企业构建的一套简单、实用、覆盖全流程的电商系统，本项目采用Java SpringBoot+Vue2前后端分离开发。 

支持供应商一件代发和仓库发货两种发货方式，功能覆盖采购、网店订单处理、供应商一件代发、仓库发货、网店售后、网店商品管理、仓库出入库、采购结算、代发结算等功能，基本上覆盖了电商日常业务。 

可支持所有电商平台接口，已对接平台有：淘宝、拼多多、抖店，后续计划对接：小红书、快手、京东等。

![预览](preview.png)

## 项目介绍
**启航电商ERP可以说是我这五年以来的工作经验成果。**

公司从2019年踏入电商以来，一直都是由我组建和带领一帮技术人员从零开始建设了一套完全适应公司业务需要的电商ERP系统，包括WMS仓库系统、OMS订单处理系统、财务系统、直播运营系统等子系统组成。主要功能模块包括：采购模块、出入库模块、订单发货模块、网店订单管理模块、电子面单打印模块。公司ERP对接了批批网、1688、蘑菇街、淘宝天猫、拼多多、抖店、快手小店平台。

### 公司应用场景一：抖店直播


```mermaid
graph TD
A[早上7点开播] -->B(10点结束直播-一般是500单左右)
    B --> C[使用ERP系统打单-对接平台电子面单接口]
    C --> E(库存核对)
    E --> F[库存不足]
    F --> F1(采购补货)
    F1 --> G1
    E --> G(库存充足)
    G --> G1[打印订单]
    G1 --> G5(推送快递单号到平台)
    G1 --> G2(生成备货单)
    G2 --> G3(仓库人员备货出库)
    G3 --> H(完成)
    G5 --> H
```


## 一、功能模块
### 供应链管理
+ 供应商管理：管理供应商信息
+ 采购订单管理：管理采购流程，包括供应商选择、采购订单生成、采购合同管理等。
+ 采购退货管理
+ 采购物流管理：跟踪采购订单物流信息。
+ 供应商代发管理：管理一件代发订单。

**采购流程**

```mermaid
graph LR
A[创建采购订单] -->B(审核)
    B --> C[供应商确认]
    C --> E[供应商发货]
    E --> F1(生成物流信息)
    F1 --> G1[确认收货]
    G1 --> G3[生成入库单]
    G3 --> G4(入库)
    G1 --> G2[生成财务应付及明细]
    G4 --> H(完成)
    G2 --> H

```

### 订单管理
+ 创建订单：手动创建订单。
+ 店铺订单管理：处理和管理多平台订单的流程，包括订单录入、处理、发货等。
  + 支持淘宝、拼多多、抖店、快手小店、小红书平台订单接口；
  + 支持淘宝订单excel导入；
  + 支持手动添加订单；
  + 订单确认到仓库；
+ 订单查询：查询所有订单信息。
+ 店铺管理：管理店铺信息、店铺商品上下架信息等。

**订单处理流程**
```mermaid
graph LR
A[录入订单] -->B(确认订单)
    B --> C[供应商代发]
    B --> D[仓库发货]
    C --> E[供应商发货]
    E --> F1(生成应付)
    D --> H
    F1 --> H(完成)


```
### 发货管理
+ 订单备货：生成拣货单；
+ 拣货出库：拣货出库、生成出库单减库存；
+ 打包发货：记录包裹信息、物流发货、同步发货状态；
+ 物流跟踪：跟踪发货快递物流；

**发货流程**
```mermaid
graph TD
A[查询备货清单] -->B(生成拣货单)
    B --> C[拣货出库]
    C --> E(减库存)
    E --> F[打包发货]
    F --> F1(记录包裹信息)
    F1 --> G(填写物流信息)
    G --> G1[同步发货状态]
    G1 --> H(完成)
    G --> G2[生成订单应收款项]
    G2 --> H

```

### 售后管理
对退货、换货、维修等售后处理进行管理，包括退款审核、退货入库、退款处理等环节。
+ 店铺售后管理：处理和管理多平台售后包括录入售后数据、退货入库、换货处理等。
  + 支持拼多多、抖店、快手小店、小红书平台售后接口；
  + 支持手动录入、备注；
+ 退货处理：数据录入、仓库收货确认、库存处理等。
+ 换货处理：数据录入、仓库收货确认、仓库发货、库存处理等。

**退款退货流程**
```mermaid
graph LR
A[录入退款退货] -->B(仅退款)
    B --> C[生成销售应付]
    A --> D(退货退款)
    D --> E[仓库收货]
    E --> F1(生成销售应付)
    E --> F[退货入库处理]
    C --> H
    F --> H
    F1 --> H(完成)
```

### 店铺管理
+ 淘宝商品管理：同步淘宝店铺商品，关联到ERP商品（用于仓库发货处理）；
+ 多多商品管理：同步多多店铺商品，关联到ERP商品（用于仓库发货处理）；
+ 抖店商品管理：同步抖店店铺商品，关联到ERP商品（用于仓库发货处理）；
+ 店铺设置：网店管理、API参数设置；

### 库存管理

+ 入库管理
+ 出库管理
+ 库存查询：跟踪和管理库存，包括批次管理、库存盘点、库存调整、库存预警等。
+ 库位管理

### 财务管理
+ 应收管理
  + 商品销售收入
  + 退款款项
  + 平台服务费
+ 应付管理
  + 代发账单
  + 采购账单
  + 采购退货款项
  + 发货物流账单
  + 营销费用
  + 其他费用
+ 存货报表
  每日凌晨生成报表（日报表、月报表、季报表、年报表、查看存货明细）


### 商品管理
商品信息、分类信息、属性信息等管理。


## 二、技术栈
### 1、技术栈
+ vue2 + elementUI
+ SpringBoot2.x
+ Java 17

### 2、存储栈
+ MySQL8数据库
+ minio文件存储

### 3、中间件
+ Redis（缓存：在线用户、字典、系统配置）
+ Nacos配置中心
+ quartz定时任务


## 三、如何使用？
### 3.1、开发环境配置
+ MySQL数据库创建
  + 运行MySQL脚本`db\sql\qihang-erp.sql`导入数据到主库`qihang-erp`

  + 运行MySQL脚本`db\sql\nacos.sql`导入数据到nacos库`nacos`

+ 启动nacos
  + 修改Nacos数据库配置 `nacos\conf\application.properties`
  ```
  db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai
  db.user.0=root
  db.password.0=Andy_123
  ```
  + 启动Nacos（这里可以使用db文件夹下的nacos压缩包，其他都配置好了，数据库连接重新配置一下，双击`bin\startup.cmd`就可以运行了）
  
+ 启动Redis（这里可以使用db文件夹下的redis压缩包，直接双击`start.bat`就可以启动了）

  
+ 启动minio（以db文件夹下的minio压缩包为例）
  + 进入项目minio文件夹`db\minio`
  + 启动：CMD运行`minio.exe server data`
  
### 3.2、启动后端`ecerp-api`
+ 进入Nacos进行Mysql数据库连接配置`http://127.0.0.1:8848/nacos` 
  + 新建配置`ecerp-dev.yaml`
  + 添加配置内容（从db\ecerp-dev.yaml复制修改）
  + 发布配置
  
+ IDEA启动项目

### 3.3、启动前端 `ecerp-vue`
+ `npm install`
+ `npm run dev`
+ `npm run build:prod`
+ 访问web
  + 访问地址：`http://localhost`
  + 登录名：`admin`
  + 登录密码：`admin123`

## 恰饭

作者为兼职做开源,平时还需要工作，如果有一定的收入，作者更有动力把项目做好,如果项目对您有帮助，请记得恰饭

💖 如果觉得有用记得点 Star⭐

### 关注公众号

作者微信公众号：qihangerp168


<img src="公众号.jpg" width="300px" />



### 捐献作者
作者为兼职做开源,平时还需要工作,如果帮到了您可以请作者吃个盒饭
 

<img src="./db/weixinzhifu.jpg" width="300px" />
<img src="./db/zhifubao.jpg" width="300px" />





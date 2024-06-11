<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <!-- <el-input
          v-model="queryParams.shopId"
          placeholder="请输入店铺ID"
          clearable
          @keyup.enter.native="handleQuery"
        /> -->
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
         <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单日期" prop="orderCreatedTime">
        <el-date-picker clearable
                        v-model="queryParams.orderCreatedTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择下单日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="statusStr">
        <el-input
          v-model="queryParams.statusStr"
          placeholder="请输入订单状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['xhs:order:add']"-->
<!--        >手动添加</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-upload"-->
<!--          size="mini"-->
<!--          @click="handleImport"-->
<!--          v-hasPermi="['xhs:order:edit']"-->
<!--        >Execl导入</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-download"
          size="mini"

          @click="handlePull"
          v-hasPermi="['xhs:order:remove']"
        >API拉取订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button

          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          :disabled="single"
          @click="handleConfirm"
        >确认订单</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['xhs:order:export']"-->
<!--        >导出订单</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键id" align="center" prop="id" />-->
      <el-table-column label="订单号" align="center" prop="orderId" />
      <el-table-column label="店铺" align="center" prop="shopId" >
       <template slot-scope="scope">
         <span>{{ shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name :'' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品" width="350">
          <template slot-scope="scope">
            <el-row v-for="item in scope.row.xhsOrderItemList" :key="item.id" :gutter="20">

            <div style="float: left;display: flex;align-items: center;" >
              <el-image  style="width: 70px; height: 70px;" :src="item.itemImage"></el-image>
              <div style="margin-left:10px">
              <p>{{item.itemName}}</p>
              <p>{{item.itemSpec}}&nbsp;
                <el-tag size="small">x {{item.quantity}}</el-tag>
                </p>
              </div>
            </div>
            </el-row>
          </template>
      </el-table-column>
<!--      <el-table-column label="订单来源" align="center" prop="shopType" />-->
      <!-- <el-table-column label="店铺" align="center" prop="shopId" /> -->
<!--      <el-table-column label="订单类型" align="center" prop="orderType" />-->
      <el-table-column label="实付金额" align="center" prop="totalPayAmount" />
      <el-table-column label="运费" align="center" prop="totalShippingFree" />
      <el-table-column label="小红书订单状态" align="center" prop="orderStatus" >
        <!-- 小红书订单状态，1已下单待付款 2已支付处理中 3清关中 4待发货 5部分发货 6待收货 7已完成 8已关闭 9已取消 10换货申请中 -->
        <template slot-scope="scope">

          <el-tag size="small" v-if="scope.row.orderStatus === 1"> 待支付</el-tag>
          <el-tag size="small" v-if="scope.row.orderStatus === 2"> 已支付</el-tag>
          <el-tag size="small" v-if="scope.row.orderStatus === 3"> 清关中</el-tag>
          <el-tag size="small" v-if="scope.row.orderStatus === 4"> 待发货</el-tag>
          <el-tag size="small" v-if="scope.row.orderStatus === 5"> 部分发货</el-tag>
           <el-tag size="small" v-if="scope.row.orderStatus === 6"> 待收货</el-tag>
            <el-tag size="small" v-if="scope.row.orderStatus === 7"> 已完成</el-tag>
             <el-tag size="small" v-if="scope.row.orderStatus === 8"> 已关闭</el-tag>
              <el-tag size="small" v-if="scope.row.orderStatus === 9"> 已取消</el-tag>
               <el-tag size="small" v-if="scope.row.orderStatus === 10"> 换货申请中</el-tag>
          <span></span>

          <el-tag size="small" v-if="scope.row.auditStatus === 0" style="margin-top: 5px;"> 待确认</el-tag>
          <el-tag size="small" v-if="scope.row.auditStatus === 1" style="margin-top: 5px;"> 已确认</el-tag>
          <el-tag size="small" v-if="scope.row.auditStatus === 2" style="margin-top: 5px;"> 已拦截</el-tag>

        </template>
      </el-table-column>
      <!-- <el-table-column label="小红书售后状态" align="center" prop="afterSalesStatus" /> -->
      <!-- <el-table-column label="申请取消状态" align="center" prop="cancelStatus" /> -->
      <el-table-column label="下单时间" align="center" prop="orderCreatedTime" >
        <template slot-scope="scope">
          <span>{{ dateToString(scope.row.orderCreatedTime) }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="订单支付时间 单位ms" align="center" prop="orderPaidTime" />
      <el-table-column label="订单更新时间 单位ms" align="center" prop="orderUpdateTime" />
      <el-table-column label="订单发货时间 单位ms" align="center" prop="orderDeliveryTime" />
      <el-table-column label="订单取消时间 单位ms" align="center" prop="orderCancelTime" />
      <el-table-column label="订单完成时间 单位ms" align="center" prop="orderFinishTime" />
      <el-table-column label="承诺最晚发货时间 单位ms" align="center" prop="promiseLastDeliveryTime" /> -->
      <el-table-column label="用户备注" align="center" prop="customerRemark" />
      <el-table-column label="商家标记备注" align="center" prop="sellerRemark" />
      <el-table-column label="商家标记优先级" align="center" prop="sellerRemarkFlag" />
      <!-- <el-table-column label="预售最早发货时间 单位ms" align="center" prop="presaleDeliveryStartTime" />
      <el-table-column label="预售最晚发货时间 单位ms" align="center" prop="presaleDeliveryEndTime" /> -->
<!--      <el-table-column label="原始关联订单号(退换订单的原订单)" align="center" prop="originalPackageId" />-->

      <el-table-column label="快递单号" align="center" prop="expressTrackingNo" />
<!--      <el-table-column label="快递公司编码" align="center" prop="expressCompanyCode" />-->
<!--      <el-table-column label="收件人姓名+手机+地址等计算得出，用来查询收件人详情" align="center" prop="openAddressId" />-->
      <!-- <el-table-column label="省" align="center" prop="province" /> -->
      <!-- <el-table-column label="市" align="center" prop="city" /> -->
<!--      <el-table-column label="区县" align="center" prop="district" />-->
<!--      <el-table-column label="订单审核状态" align="center" prop="auditStatus" />-->
<!--      <el-table-column label="订单审核时间" align="center" prop="auditTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ // parseTime(scope.row.auditTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="结算状态0未结算1已结算" align="center" prop="settleStatus" />-->
<!--      <el-table-column label="结算金额" align="center" prop="settleAmount" />-->
<!--      <el-table-column label="ERP发货状态0待处理1出库中2已出库3已发货" align="center" prop="sendStatus" />-->
<!--      <el-table-column label="发货时间" align="center" prop="sendTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="创建时间" align="center" prop="createTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="创建人" align="center" prop="createBy" />-->
<!--      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="更新人" align="center" prop="updateBy" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.auditStatus === 0"
            size="mini"
            type="success"
            icon="el-icon-success"
            @click="handleConfirm(scope.row)"
            v-hasPermi="['xhs:order:edit']"
          >确认订单</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['xhs:order:remove']"
          >详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改小红书订单对话框 -->


    <!-- 确认订单、订单详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1100px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" inline>
        <el-descriptions title="订单信息">
          <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{form.orderId}}</el-descriptions-item>
          <el-descriptions-item label="店铺">
            {{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name:'' }}
          </el-descriptions-item>
          <el-descriptions-item label="订单类型">
            <el-tag size="small" v-if="form.orderType ===1 ">普通订单</el-tag>
            <el-tag size="small" v-if="form.orderType ===2 ">定金预售</el-tag>
            <el-tag size="small" v-if="form.orderType ===3 ">全款预售</el-tag>
            <el-tag size="small" v-if="form.orderType ===4 ">延迟发货</el-tag>
            <el-tag size="small" v-if="form.orderType ===5 ">换货补发</el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="买家备注">
            {{form.customerRemark}}
          </el-descriptions-item>
          <el-descriptions-item label="卖家备注">
            {{form.sellerRemark}}
          </el-descriptions-item>

          <el-descriptions-item label="创建时间">
            {{ dateToString(form.orderCreatedTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="最晚发货时间"> {{ dateToString(form.promiseLastDeliveryTime) }}</el-descriptions-item>

          <el-descriptions-item label="订单取消时间"> {{ dateToString(form.orderCancelTime) }}</el-descriptions-item>

          <el-descriptions-item label="订单状态">
            <!--    小红书订单状态，1已下单待付款 2已支付处理中 3清关中 4待发货 5部分发货 6待收货 7已完成 8已关闭 9已取消 10换货申请中-->
            <el-tag v-if="form.orderStatus === 1" style="margin-bottom: 6px;">待支付</el-tag>
            <el-tag v-if="form.orderStatus === 2" style="margin-bottom: 6px;">处理中</el-tag>
            <el-tag v-if="form.orderStatus === 3" style="margin-bottom: 6px;">清关中</el-tag>
            <el-tag v-if="form.orderStatus === 4" style="margin-bottom: 6px;">待发货</el-tag>
            <el-tag v-if="form.orderStatus === 5" style="margin-bottom: 6px;">部分发货</el-tag>
            <el-tag v-if="form.orderStatus === 6" style="margin-bottom: 6px;">待收货</el-tag>
            <el-tag v-if="form.orderStatus === 7" style="margin-bottom: 6px;">已完成</el-tag>
            <el-tag v-if="form.orderStatus === 8" style="margin-bottom: 6px;">已关闭</el-tag>
            <el-tag v-if="form.orderStatus === 9" style="margin-bottom: 6px;">已取消</el-tag>
            <el-tag v-if="form.orderStatus === 10" style="margin-bottom: 6px;">换货申请中</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="退款状态">
            <el-tag v-if="form.afterSalesStatus === 1">无售后</el-tag>
            <el-tag v-if="form.afterSalesStatus === 2">售后处理中</el-tag>
            <el-tag v-if="form.afterSalesStatus === 3">售后完成(含取消)</el-tag>
          </el-descriptions-item>

        </el-descriptions>
        <el-descriptions title="付款信息">

          <el-descriptions-item label="实际支付金额">{{form.totalPayAmount /100}}</el-descriptions-item>

          <el-descriptions-item label="运费">{{form.totalShippingFree / 100}}</el-descriptions-item>

          <el-descriptions-item label="支付时间"> {{ dateToString(form.orderPaidTime) }}</el-descriptions-item>

        </el-descriptions>


        <el-descriptions title="收货信息">
          <el-descriptions-item label="收件人姓名">{{form.receiver}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.phone}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.district}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>
        </el-descriptions>
        <el-descriptions title="发货信息">
          <!-- <el-descriptions-item label="发货方式">
            <el-tag v-if="form.shipType === 1"  type="danger">供应商代发</el-tag>
              <el-tag v-if="form.shipType === 0" type="danger">仓库发货</el-tag>
          </el-descriptions-item> -->
          <el-descriptions-item label="物流公司">{{form.expressCompanyCode}}</el-descriptions-item>
          <el-descriptions-item label="物流单号">{{form.expressTrackingNo}}</el-descriptions-item>
          <el-descriptions-item label="发货时间">{{dateToString(form.orderDeliveryTime)}}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="center">订单商品</el-divider>
        <el-table :data="goodsList"  style="margin-bottom: 10px;">
          <el-table-column label="序号" align="center" type="index" width="50"/>

          <el-table-column label="商品图片" width="80">
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.itemImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="itemName" ></el-table-column>
          <el-table-column label="SKU" prop="itemSpec" width="150"></el-table-column>
          <el-table-column label="sku编码" prop="itemSpecCode"></el-table-column>
          <el-table-column label="单价" prop="price"></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>
          <el-table-column label="商品金额" prop="totalPaidAmount"></el-table-column>
        </el-table>



        <el-divider content-position="center">订单商品</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddXhsOrderItem">添加赠品</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteXhsOrderItem">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="xhsOrderItemList" :row-class-name="rowXhsOrderItemIndex" @selection-change="handleXhsOrderItemSelectionChange" ref="xhsOrderItem" style="margin-bottom: 10px;">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品" prop="erpGoodsId" width="350" >
            <template slot-scope="scope">
              <el-select v-model="scope.row.erpGoodsSpecId" filterable remote reserve-keyword placeholder="搜索商品" style="width: 330px;"
                         :remote-method="searchSku" :loading="skuListLoading" @change="skuChanage(scope.row)">
                <el-option v-for="item in skuList" :key="item.id"
                           :label="item.name + ' - ' + item.colorValue + ' ' + item.sizeValue + ' ' + item.styleValue"
                           :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="商品图片" prop="itemImage" >
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.itemImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="SKU编码" prop="itemSpecCode" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemSpecCode" placeholder="请输入规格" />
            </template>
          </el-table-column>
          <el-table-column label="单价" prop="price">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price" placeholder="请输入单价" />
            </template>
          </el-table-column>
          <el-table-column label="数量" prop="quantity" >
            <template slot-scope="scope">
              <el-input v-model="scope.row.quantity" placeholder="请输入数量"  @input="qtyChange(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="总金额" prop="itemAmount">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemAmount" placeholder="请输入总金额" />
            </template>
          </el-table-column>
        </el-table>
        <el-form-item label="收件人姓名" prop="receiver" v-if="isAudit">
          <el-input v-model="form.receiver" placeholder="请输入收件人姓名" style="width:250px" />
        </el-form-item>
        <el-form-item label="收件人电话" prop="phone" v-if="isAudit">
          <el-input v-model="form.phone" placeholder="请输入收件人电话" style="width:250px" />
        </el-form-item>
        <el-form-item label="省市区" prop="provinces" v-if="isAudit">
          <el-cascader style="width:250px"
                       size="large"
                       :options="pcaTextArr"
                       v-model="form.provinces">
          </el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="address" v-if="isAudit">
          <el-input v-model="form.address" placeholder="请输入收件地址" style="width:250px" />
        </el-form-item>
        <el-form-item label="发货方式" prop="shipType" v-if="isAudit">
          <el-select v-model="form.shipType" placeholder="发货类型0仓库发货1供应商代发" style="width:250px">
            <el-option label="供应商代发" value="1"></el-option>
            <el-option label="仓库发货" value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitConfirmForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, confirmOrder } from "@/api/xhs/order";
import { listShop } from "@/api/shop/shop";
import { searchSku } from "@/api/goods/goods";
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
export default {
  name: "OrderXhs",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedXhsOrderItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 小红书订单表格数据
      orderList: [],
      // 小红书订单明细表格数据
      xhsOrderItemList: [],
      shopList:[],
      skuListLoading:false,
      skuList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        shopType: null,
        shopId: null,
        orderType: null,
        orderStatus: null,
        afterSalesStatus: null,
        cancelStatus: null,
        orderCreatedTime: null,
        orderPaidTime: null,
        orderUpdateTime: null,
        orderDeliveryTime: null,
        orderCancelTime: null,
        orderFinishTime: null,
        promiseLastDeliveryTime: null,
        customerRemark: null,
        sellerRemark: null,
        sellerRemarkFlag: null,
        presaleDeliveryStartTime: null,
        presaleDeliveryEndTime: null,
        originalPackageId: null,
        totalPayAmount: null,
        totalShippingFree: null,
        expressTrackingNo: null,
        expressCompanyCode: null,
        openAddressId: null,
        province: null,
        city: null,
        district: null,
        auditStatus: null,
        auditTime: null,
        settleStatus: null,
        settleAmount: null,
        sendStatus: null,
        sendTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      },
      // 表单参数
      // 表单参数
      form: {
        provinces: [],
        address:null,
        receiver:null,
        phone:null,
        province:null,
        city:null,
        town:null,
        goodsAmount:null
      },
      isAudit:false,
      detailOpen:false,
      detailTitle:'',
      goodsList:[],
      pcaTextArr,
      // 表单校验
      rules: {
        orderId: [{ required: true, message: "订单号不能为空", trigger: "blur" }],
        shopId: [{ required: true, message: "店铺ID不能为空", trigger: "blur" }],
        createTime: [{ required: true, message: "下单时间不能为空", trigger: "blur" }],
        receiver: [{ required: true, message: "收件人姓名不能为空", trigger: "blur" }],
        phone: [{ required: true, message: "收件人电话不能为空", trigger: "blur" }],
        provinces: [{ required: true, message: "请选择省市区", trigger: "blur" }],
        address: [{ required: true, message: "收件人详情不能为空", trigger: "blur" }],
        goodsAmount: [{ required: true, message: "总金额(不含运费) 不能为空", trigger: "blur" }],
        shippingFree: [{ required: true, message: "订单运费不能为空", trigger: "blur" }],
        shipType: [{ required: true, message: "请选择发货方式", trigger: "blur" }],
      }
    };
  },
  created() {
    listShop({platform:7}).then(response => {
        this.shopList = response.rows;
      });
    this.getList();
  },
  methods: {
    dateToString(timespan){
            var date = new Date(timespan);
            return date.toLocaleString();
        },
    /** 查询小红书订单列表 */
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.detailOpen = false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        orderId: null,
        shopType: null,
        shopId: null,
        orderType: null,
        orderStatus: null,
        afterSalesStatus: null,
        cancelStatus: null,
        orderCreatedTime: null,
        orderPaidTime: null,
        orderUpdateTime: null,
        orderDeliveryTime: null,
        orderCancelTime: null,
        orderFinishTime: null,
        promiseLastDeliveryTime: null,
        customerRemark: null,
        sellerRemark: null,
        sellerRemarkFlag: null,
        presaleDeliveryStartTime: null,
        presaleDeliveryEndTime: null,
        originalPackageId: null,
        totalPayAmount: null,
        shippingFree: 0,
        expressTrackingNo: null,
        expressCompanyCode: null,
        openAddressId: null,
        province: null,
        city: null,
        district: null,
        auditStatus: null,
        auditTime: null,
        settleStatus: null,
        settleAmount: null,
        sendStatus: null,
        sendTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      };
      this.xhsOrderItemList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加小红书订单";
    },
    handleImport(){

    },
    handlePull(){
      this.$modal.msgSuccess("请先配置API参数");
    },
    /** 修改按钮操作 */
    handleConfirm(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.goodsList = response.data.xhsOrderItemList;
        this.detailOpen = true;
        this.detailTitle = "确认订单";
        this.isAudit = true
      });
    },
    handleDetail(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.goodsList = response.data.xhsOrderItemList;
        this.detailOpen = true;
        this.detailTitle = "订单详情";
        this.isAudit = false
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.district = this.form.provinces[2]
          this.form.xhsOrderItemList = this.xhsOrderItemList;

            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });

            // confirmOrder(this.form).then(response => {
            //   this.$modal.msgSuccess("修改成功");
            //   this.open = false;
            //   this.getList();
            // });

        }
      });
    },
    submitConfirmForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.district = this.form.provinces[2]
          this.form.xhsOrderItemList = this.xhsOrderItemList;
          confirmOrder(this.form).then(response => {
            this.$modal.msgSuccess("确认成功");
            this.detailOpen = false;
            this.getList();
          });
        }
      })
    },
    /** 删除按钮操作 */

	/** 小红书订单明细序号 */
    rowXhsOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 小红书订单明细添加按钮操作 */
    handleAddXhsOrderItem() {
      let obj = {};
      obj.itemId = "";
      obj.itemName = "";
      obj.erpcode = "";
      obj.itemSpec = "";
      obj.itemImage = "";
      obj.quantity = "";
      obj.itemAmount = "";
      obj.totalMerchantDiscount = "";
      obj.totalRedDiscount = "";
      obj.itemTag = "";
      obj.erpSendStatus = "";
      this.xhsOrderItemList.push(obj);
    },
    /** 小红书订单明细删除按钮操作 */
    handleDeleteXhsOrderItem() {
      if (this.checkedXhsOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的小红书订单明细数据");
      } else {
        const xhsOrderItemList = this.xhsOrderItemList;
        const checkedXhsOrderItem = this.checkedXhsOrderItem;
        this.xhsOrderItemList = xhsOrderItemList.filter(function(item) {
          return checkedXhsOrderItem.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleXhsOrderItemSelectionChange(selection) {
      this.checkedXhsOrderItem = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xhs/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    },
    // 搜索SKU
    searchSku(query) {
      this.shopLoading = true;
      const qw = {
        keyword: query
      }
      searchSku(qw).then(res => {
        this.skuList = res.rows;
        this.skuListLoading = false;
      })
    },
    skuChanage(row) {
      console.log('=========',row)
      const spec = this.skuList.find(x => x.id === row.erpGoodsSpecId);
      if (spec) {
        console.log('=========', spec)
        row.erpGoodsId = spec.goodsId
        row.itemName = spec.name
        row.erpcode = spec.number
        row.itemSpec = spec.colorValue + ' ' + spec.sizeValue + ' ' + spec.styleValue
        row.itemImage = spec.colorImage
        row.price = spec.purPrice
        row.itemSpecCode = spec.specNum
        row.itemTag = '0'
        row.quantity = 1
        row.itemAmount = parseFloat(row.price) * row.quantity

        // 计算总金额
        let goodsAmount = this.form.goodsAmount ? parseFloat(this.form.goodsAmount):0.0
        goodsAmount += row.itemAmount
        this.form.goodsAmount = goodsAmount
      }
    },
    qtyChange(row) {
      console.log('======值变化=====', row)
      row.itemAmount = row.price * row.comboNum
      // 计算总金额
      let goodsAmountNew =0.0
      this.xhsOrderItemList.forEach(x=>{
        goodsAmountNew+= row.itemAmount
      })
      this.form.goodsAmount = goodsAmountNew

    },

  }
};
</script>

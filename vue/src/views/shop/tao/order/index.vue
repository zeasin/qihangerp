<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="tid">
        <el-input
          v-model="queryParams.tid"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <!-- <el-input
          v-model="queryParams.shopId"
          placeholder="请输入店铺"
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

      <el-form-item label="下单日期" prop="orderCreateTime">
        <el-date-picker clearable
          v-model="queryParams.orderCreateTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择订单创建时间">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="等待卖家发货" value="WAIT_SELLER_SEND_GOODS" ></el-option>
          <el-option label="等待买家确认收货" value="WAIT_BUYER_CONFIRM_GOODS"></el-option>
          <el-option label="交易成功" value="TRADE_FINISHED"> </el-option>
          <el-option label="交易自动关闭" value="TRADE_CLOSED"></el-option>
          <el-option label="卖家或买家主动关闭交易" value="TRADE_CLOSED_BY_TAOBAO"></el-option>
        </el-select>
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
<!--          v-hasPermi="['tao:order:add']"-->
<!--        >手动添加</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-upload"-->
<!--          size="mini"-->
<!--          @click="handleImportExcel"-->
<!--        >Execl导入淘宝子订单</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          :loading="pullLoading"
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-success"
          size="mini"
          :disabled="single"
          @click="handleConfirm"
        >确认订单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="left" prop="tid" width="180px">
        <template slot-scope="scope">
          <div>{{scope.row.tid}}</div>
          <el-tag size="small">{{ shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name :'' }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--       <template slot-scope="scope">-->
<!--         <span>{{ shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name :'' }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品" width="350">
        <template slot-scope="scope">
          <el-row v-for="item in scope.row.items" :key="item.id" :gutter="20">

            <div style="float: left;display: flex;align-items: center;" >
              <el-image  style="width: 70px; height: 70px;" :src="item.picPath"></el-image>
              <div style="margin-left:10px">
                <p>{{item.title}}</p>
                <p>{{item.skuPropertiesName}}&nbsp;
                  <el-tag size="small">x {{item.num}}</el-tag>
                </p>
                <p v-if="scope.row.refundStatus === 0">
                  <el-button type="text" size="mini" round @click="handleRefund(scope.row,item)">售后</el-button>
                </p>
              </div>
            </div>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center" prop="payment" :formatter="amountFormatter" />
      <el-table-column label="收货信息" align="left" prop="shippingFee" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.receiverName }}</span><br/>
          <span>{{ scope.row.receiverMobile }}</span><br/>
          <span>{{ scope.row.receiverAddress }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="left" prop="orderCreateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.created, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="status" >
         <template slot-scope="scope">
           <el-tag size="small" v-if="scope.row.status === 'WAIT_BUYER_PAY'"> 等待买家付款</el-tag>
           <el-tag size="small" v-if="scope.row.status === 'SELLER_CONSIGNED_PART'"> 卖家部分发货</el-tag>
           <el-tag size="small" v-if="scope.row.status === 'WAIT_SELLER_SEND_GOODS'"> 等待卖家发货</el-tag>
           <el-tag size="small" v-if="scope.row.status === 'WAIT_BUYER_CONFIRM_GOODS'"> 等待买家确认收货</el-tag>
           <el-tag size="small" v-if="scope.row.status === 'TRADE_FINISHED'"> 交易成功</el-tag>
           <el-tag size="small" v-if="scope.row.status === 'TRADE_CLOSED'"> 交易自动关闭</el-tag>
           <el-tag size="small" v-if="scope.row.status === 'TRADE_CLOSED_BY_TAOBAO'"> 卖家或买家主动关闭交易</el-tag>
           <el-tag size="small" v-if="scope.row.status === 'PAID_FORBID_CONSIGN'"> 禁止发货</el-tag>
          <span></span>
          <br />
          <el-tag size="small" v-if="!scope.row.auditStatus||scope.row.auditStatus === 0" style="margin-top: 5px;"> 待确认</el-tag>
          <el-tag size="small" v-if="scope.row.auditStatus === 1" style="margin-top: 5px;"> 已确认</el-tag>
            <el-tag size="small" v-if="scope.row.auditStatus === 2" style="margin-top: 5px;"> 已拦截</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['tao:order:remove']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            :loading="pullLoading"
            icon="el-icon-refresh"
            @click="handlePullUpdate(scope.row)"
          >更新订单</el-button>
          <el-row>
          <el-button
          v-if="!scope.row.auditStatus||scope.row.auditStatus === 0"
            size="mini"
            plain
            type="success"
            icon="el-icon-success"
            @click="handleConfirm(scope.row)"
            v-hasPermi="['tao:order:edit']"
          >确认订单</el-button>
          </el-row>

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



    <!-- 导入淘宝订单 -->
    <el-dialog title="导入淘宝主订单" :visible.sync="importOrderOpen" width="400px" append-to-body>
      <el-upload
        class="upload-demo"
        :headers="headers"
        drag
        action="/dev-api/tao/order/order_item_import"
        accept="xlsx"
        multiple >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
    </el-dialog>
    <!-- 导入淘宝子订单 -->
    <el-dialog title="导入淘宝子订单" :visible.sync="importOrderItemOpen" width="400px" append-to-body>
      <el-upload
        class="upload-demo"
        :headers="headers"
        drag
        :action="importOrderUrl"
        accept="xlsx"
        multiple >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
    </el-dialog>

    <!-- 订单详情对话框 -确认订单 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1100px" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="120px" inline>
        <el-descriptions title="订单信息">
            <el-descriptions-item label="订单号">{{form.id}}</el-descriptions-item>

            <el-descriptions-item label="店铺">
              <span >{{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name :'' }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="下单时间">
              {{ parseTime(form.created)}}
            </el-descriptions-item>

            <el-descriptions-item label="卖家备忘信息">
              {{form.sellerMemo}}
            </el-descriptions-item>
            <el-descriptions-item label="买家留言">
              {{form.buyerFeedback}}
            </el-descriptions-item>
            <el-descriptions-item label="备注">
              {{form.remark}}
            </el-descriptions-item>

            <el-descriptions-item label="订单状态">
              <el-tag size="small" v-if="form.status === 'WAIT_BUYER_PAY'"> 等待买家付款</el-tag>
              <el-tag size="small" v-if="form.status === 'SELLER_CONSIGNED_PART'"> 卖家部分发货</el-tag>
              <el-tag size="small" v-if="form.status === 'WAIT_SELLER_SEND_GOODS'"> 等待卖家发货</el-tag>
              <el-tag size="small" v-if="form.status === 'WAIT_BUYER_CONFIRM_GOODS'"> 等待买家确认收货</el-tag>
              <el-tag size="small" v-if="form.status === 'TRADE_FINISHED'"> 交易成功</el-tag>
              <el-tag size="small" v-if="form.status === 'TRADE_CLOSED'"> 交易自动关闭</el-tag>
              <el-tag size="small" v-if="form.status === 'TRADE_CLOSED_BY_TAOBAO'"> 卖家或买家主动关闭交易</el-tag>
              <el-tag size="small" v-if="form.status === 'PAID_FORBID_CONSIGN'"> 禁止发货</el-tag>
            </el-descriptions-item>

        </el-descriptions>
        <el-descriptions title="付款信息">
            <el-descriptions-item label="商品金额">{{ amountFormatter(null,null,parseFloat(form.totalFee),null)}}</el-descriptions-item>
            <el-descriptions-item label="卖家手工调整">{{amountFormatter(null,null,parseFloat(form.adjustFee),null)}}</el-descriptions-item>
            <el-descriptions-item label="优惠金额">{{amountFormatter(null,null,parseFloat(form.discountFee),null)}}</el-descriptions-item>
            <el-descriptions-item label="运费">{{amountFormatter(null,null,parseFloat(form.shippingFee),null)}}</el-descriptions-item>
            <el-descriptions-item label="实际支付金额">{{amountFormatter(null,null,parseFloat(form.payment),null)}}</el-descriptions-item>
        </el-descriptions>


         <el-descriptions title="收货信息">
          <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.receiverMobile}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.receiverState}}{{form.receiverCity}}{{form.receiverDistrict}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.receiverAddress}}</el-descriptions-item>
      </el-descriptions>

        <el-divider content-position="center">订单商品</el-divider>
        <el-table :data="goodsList"  style="margin-bottom: 10px;">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" type="index" width="50"/>

          <el-table-column label="商品图片" prop="picPath" width="80">
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.picPath"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="title" ></el-table-column>
          <el-table-column label="SKU" prop="skuPropertiesName"></el-table-column>
          <el-table-column label="SkuID" prop="skuId"></el-table-column>
          <el-table-column label="单价" prop="price"></el-table-column>
          <el-table-column label="数量" prop="num"></el-table-column>
          <el-table-column label="子订单金额" prop="totalFee" :formatter="amountFormatter"></el-table-column>
        </el-table>

        <el-row :gutter="10" class="mb8" v-if="isAudit">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddTaoOrderItem">添加赠品</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDeleteTaoOrderItem">删除</el-button>
          </el-col>
        </el-row>
        <el-table v-if="isAudit" :data="taoOrderItemList" :row-class-name="rowTaoOrderItemIndex" @selection-change="handleTaoOrderItemSelectionChange" ref="taoOrderItem"  style="margin-bottom: 10px;">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品" prop="erpGoodsId" width="350"  >
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
          <el-table-column label="商品图片" prop="productImgUrl" >
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.productImgUrl"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="sku编码" prop="specNumber" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.specNumber" placeholder="请输入单品货号，对应系统sku编码" disabled/>
            </template>
          </el-table-column>

          <el-table-column label="单价" prop="price">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price" placeholder="请输入单价" disabled/>
            </template>
          </el-table-column>
          <el-table-column label="数量" prop="quantity">
            <template slot-scope="scope">
              <el-input v-model="scope.row.quantity" placeholder="请输入数量"  @input="qtyChange(scope.row)" :disabled="isAudit" />
            </template>
          </el-table-column>
          <el-table-column label="总金额" prop="itemAmount">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemAmount" placeholder="请输入明细总金额" disabled/>
            </template>
          </el-table-column>

        </el-table>
        <el-form-item label="收件人姓名" prop="receiverName" v-if="isAudit">
          <el-input v-model="form.receiverName" placeholder="请输入收件人姓名" style="width:250px" />
        </el-form-item>
        <el-form-item label="收件人电话" prop="receiverMobile" v-if="isAudit">
          <el-input v-model="form.receiverMobile" placeholder="请输入收件人电话" style="width:250px" />
        </el-form-item>

        <el-form-item label="详细地址" prop="receiverAddress" v-if="isAudit">
          <el-input v-model="form.receiverAddress" placeholder="请输入收件地址" style="width:250px" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer" v-if="isAudit">
        <el-button type="primary" @click="submitConfirmForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 售后对话框 -->
    <el-dialog title="添加售后" :visible.sync="saleAfterOpen" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="refundForm" :model="saleAfterForm" :rules="saleAfterRules" label-width="80px" inline>
        <el-form-item label="订单ID" prop="tid">
          <el-input v-model.number="saleAfterForm.tid" placeholder="订单ID" style="width:250px" disabled />
        </el-form-item>
        <!-- <el-form-item label="子订单ID" prop="oid">
          <el-input v-model="saleAfterForm.oid" placeholder="" style="width:250px" disabled />
        </el-form-item> -->
        <el-form-item label="商品图片" prop="productImgUrl">
          <!-- <el-image style="width: 70px; height: 70px" :src="saleAfterForm.productImgUrl"></el-image> -->
          <div style="float: left;display: flex;align-items: center;" >
              <el-image  style="width: 70px; height: 70px;" :src="saleAfterForm.productImgUrl"></el-image>
              <div style="margin-left:10px">
              <p>{{saleAfterForm.goodsTitle}}</p>
              <p>{{saleAfterForm.skuInfo}} </p>
              </div>
            </div>
        </el-form-item>
        <!-- <el-form-item label="商品名称" prop="goodsTitle">
          <el-input v-model="saleAfterForm.goodsTitle" placeholder="" style="width:250px" disabled />
        </el-form-item> -->
        <el-form-item label="退款单号" prop="refundId">
          <el-input v-model.number="saleAfterForm.refundId" placeholder="" style="width:250px" />
        </el-form-item>
        <el-form-item label="购买数量" prop="quantity">
          <el-input v-model="saleAfterForm.quantity" placeholder="" style="width:250px" disabled />
        </el-form-item>
        <el-form-item label="退货数量" prop="num">
          <el-input v-model.number="saleAfterForm.num" placeholder="" style="width:250px" />
        </el-form-item>
        <el-form-item label="总金额" prop="itemAmount">
          <el-input v-model="saleAfterForm.itemAmount" placeholder="" style="width:250px" disabled/>
        </el-form-item>
        <el-form-item label="退款金额" prop="refundFee">
          <el-input type="number" v-model.number="saleAfterForm.refundFee" placeholder="请输入退款金额" style="width:250px"/>
        </el-form-item>
        <el-form-item label="退款类型" prop="afterSalesType">
          <el-select v-model="saleAfterForm.afterSalesType" placeholder="退款类型" style="width:250px" >
           <el-option value="1" label="退货"></el-option>
           <el-option value="3" label="换货"></el-option>
           <el-option value="9" label="仅退款"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="实际支付金额" prop="payAmount">
          <el-input v-model="form.payAmount" placeholder="请输入实际支付金额" style="width:250px"/>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRefundForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, pullOrder, addOrder, confirmOrder,pullOrderDetail } from "@/api/tao/order";
import { addTaoRefund } from "@/api/tao/taoRefund";
import { listShop } from "@/api/shop/shop";
import { searchSku } from "@/api/goods/goods";
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
import {MessageBox} from "element-ui";
import {isRelogin} from "../../../../utils/request";
import {getToken} from "@/utils/auth";

export default {
  name: "OrderTao",
  data() {
    return {
      headers: { 'Authorization': 'Bearer ' + getToken() },
      // headers: {
      //   "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjYyM2E0NDE3LWYxYjgtNGZkZi05NjIyLWExMTQxNDMzYjIwMiJ9.kAQDBzkdBEV6byqcufxm9hWvrAMXUJZm_lWiS6r_z5GnuRZrOMz_PnLQgObjQ3Ysem593JW3c38mVK5RnE4upA"
      // },
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedTaoOrderItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      pullLoading: false,
      // 总条数
      total: 0,
      // 淘宝订单表格数据
      orderList: [],
      // 淘宝订单明细表格数据
      taoOrderItemList: [],
      goodsList:[],
      shopList:[],
      isAudit:false,
      skuListLoading:false,
      skuList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      detailTitle:'订单详情',
      detailOpen:false,
      importOrderOpen:false,
      importOrderItemOpen:false,
      importOrderUrl:'',
      saleAfterOpen:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        orderSource: '1',
        totalAmount: null,
        shippingFee: null,
        discountAmount: null,
        payAmount: null,
        discountRemark: null,
        orderCreateTime: null,
        orderModifyTime: null,
        payTime: null,
        confirmedTime: null,
        deliveredTime: null,
        completeTime: null,
        sellerMemo: null,
        buyerFeedback: null,
        closeReason: null,
        receivingTime: null,
        statusStr: null,
        status: null,
        logisticsCompany: null,
        logisticsCompanyCode: null,
        logisticsCode: null,
        refundId: null,
        refundAmount: null,
        refundStatus: null,
        auditStatus: null,
        auditTime: null,
        sendStatus: null,
        sendTime: null,
        tag: null,
        isComment: null,
        isMerge: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      },
      // 表单参数
      form: {
        provinces: [],
        address:null,
        receiver:null,
        phone:null,
        province:null,
        city:null,
        town:null
      },
      // 售后表单
      saleAfterForm:{
        num:null,
        refundFee:null,
        afterSalesType:null,
        refundId:null
      },
      pcaTextArr,
      // 表单校验
      saleAfterRules:{
        refundId: [{ required: true, message: "不能为空", trigger: "blur" }],
        refundQty: [{ required: true, message: "不能为空", trigger: "blur" }],
        refundFee: [{ required: true, message: "不能为空", trigger: "blur" }],
        afterSalesType: [{ required: true, message: "不能为空", trigger: "change" }],
      },
      rules: {
        id: [{ required: true, message: "请输入淘宝订单ID", trigger: "blur" }],
        receiverName: [{ required: true, message: "请填写收件人姓名", trigger: "blur" }],
        receiverMobile: [{ required: true, message: "请填写收件人手机号", trigger: "blur" }],
        receiverAddress: [{ required: true, message: "请填写详细地址", trigger: "change" }],
      }
    };
  },
  created() {
    listShop({platform: 4}).then(response => {
      this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
    });
    // this.getList();
  },
  methods: {
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询淘宝订单列表 */
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
      this.detailOpen = false;
      this.saleAfterOpen = false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        shopId: null,
        orderSource: '1',
        totalAmount: null,
        shippingFee: 0,
        discountAmount: 0,
        payAmount: null,
        sellerMemo: null,
        buyerFeedback: null,
        status: null,
        auditStatus: null,
        auditTime: null,
        sendStatus: null,
        sendTime: null,
        tag: null,
        remark: null,
        isComment: null,
        isMerge: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      };
      this.taoOrderItemList = [];
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
      this.isAudit = false
      this.open = true;
      this.title = "添加淘宝订单";
    },
    handlePull() {
      if(this.queryParams.shopId){
        this.pullLoading =true
        pullOrder({shopId:this.queryParams.shopId,updType:0}).then(response => {
          console.log('拉取淘宝订单接口返回=====',response)
          if(response.code === 1401) {
              MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
                this.$router.push({path:"/shop/shop_list",query:{platform:4}})
                // isRelogin.show = false;
                // store.dispatch('LogOut').then(() => {
                // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
                // })
              }).catch(() => {
                isRelogin.show = false;
              });

            // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
          }else{
            this.$modal.msgSuccess(JSON.stringify(response));
            this.getList()
          }
            this.pullLoading =false
        })
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }

      // this.$modal.msgSuccess("请先配置API");
    },
    /** 修改按钮操作 */
    handleImportExcel(row) {
      if(this.queryParams.shopId){
        this.importOrderUrl = process.env.VUE_APP_BASE_API+'/tao/order/order_item_import?shopId='+this.queryParams.shopId
        this.importOrderItemOpen = true
      }else{
        this.$modal.msgSuccess("请选择店铺");
      }
      // this.importOrderOpen = true

      // this.reset();
      // const id = row.id || this.ids
      // getOrder(id).then(response => {
      //   this.form = response.data;
      //   this.goodsList = response.data.taoOrderItemList;
      //   this.detailOpen = true;
      //   this.detailTitle = "确认订单";
      // });
      // this.isAudit = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.town = this.form.provinces[2]
          this.form.taoOrderItemList = this.taoOrderItemList;
          // if (this.isAudit === true) {
          //   updateOrder(this.form).then(response => {
          //     this.$modal.msgSuccess("修改成功");
          //     this.open = false;
          //     this.getList();
          //   });
          // } else {
            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          // }
        }
      });
    },
    /** 确认订单按钮 */
    submitConfirmForm(){
      console.log('====确认订单=====',this.form,this.taoOrderItemList)
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.taoOrderItemList = this.taoOrderItemList;
          confirmOrder(this.form).then(response => {
            this.$modal.msgSuccess("确认成功");
            this.detailOpen = false;
            this.getList();
          });
        }
      })
    },
    /** 售后按钮 */
    handleRefund(row,item){
      this.saleAfterForm.tid = row.id
      this.saleAfterForm.oid = item.subItemId
      this.saleAfterForm.productImgUrl = item.productImgUrl
      this.saleAfterForm.goodsTitle = item.goodsTitle
      this.saleAfterForm.skuInfo = item.skuInfo
      this.saleAfterForm.num = item.quantity
      this.saleAfterForm.quantity = item.quantity
      this.saleAfterForm.itemAmount = item.itemAmount
      this.saleAfterForm.refundFee = item.itemAmount

      console.log('售后====',row)
      this.saleAfterOpen = true
    },
    submitRefundForm() {
      this.$refs["refundForm"].validate(valid => {
        if (valid) {
          addTaoRefund(this.saleAfterForm).then(resp =>{
            console.log('=====售后添加成功=====',resp)
            this.$modal.msgSuccess("退款添加成功");
            this.saleAfterOpen = false
          })
        }
      })
    },
    /** 详情 */
    handleDetail(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.goodsList = response.data.items;
        this.detailOpen = true;
        this.detailTitle = "订单详情";
      });
      this.isAudit = false
    },
	/** 淘宝订单明细序号 */
    rowTaoOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 淘宝订单明细添加按钮操作 */
    handleAddTaoOrderItem() {
      let obj = {};
      obj.subItemId = "";
      obj.itemAmount = "";
      obj.discountFee = "";
      obj.adjustFee = "";
      obj.goodsTitle = "";
      obj.goodsNumber = "";
      obj.productImgUrl = "";
      obj.productUrl = "";
      obj.productId = "";
      obj.skuId = "";
      obj.specNumber = "";
      obj.skuInfo = "";
      obj.price = "";
      obj.quantity = "";
      obj.status = "";
      obj.statusStr = "";
      obj.refundStatus = "";
      obj.refundStatusStr = "";
      obj.refundAmount = "";
      obj.refundId = "";
      obj.logisticsStatus = "";
      obj.newSpecId = "";
      obj.newSpecNumber = "";
      obj.afterSaleState = "";
      obj.erpGoodsId = "";
      obj.erpGoodsSpecId = "";
      obj.remark = "";
      obj.isGift = "";
      obj.isSwap = "";
      this.taoOrderItemList.push(obj);
    },
    /** 淘宝订单明细删除按钮操作 */
    handleDeleteTaoOrderItem() {
      if (this.checkedTaoOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的淘宝订单明细数据");
      } else {
        const taoOrderItemList = this.taoOrderItemList;
        const checkedTaoOrderItem = this.checkedTaoOrderItem;
        this.taoOrderItemList = taoOrderItemList.filter(function(item) {
          return checkedTaoOrderItem.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleTaoOrderItemSelectionChange(selection) {
      this.checkedTaoOrderItem = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('tao/order/export', {
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
        row.price = spec.purPrice
        row.skuInfo = spec.colorValue + ' ' + spec.sizeValue + ' ' + spec.styleValue
        row.productImgUrl = spec.colorImage
        row.goodsNumber = spec.number
        row.goodsTitle = spec.name
        row.specNumber = spec.specNum
        row.isGift = '0'
        row.quantity = 1
        row.itemAmount = row.price * row.quantity

        // 计算总金额
        let goodsAmount = this.form.totalAmount ? parseFloat(this.form.totalAmount):0.0
        goodsAmount += row.itemAmount
        this.form.totalAmount = goodsAmount
      }
    },
    qtyChange(row) {
      console.log('======值变化=====', row)
      row.itemAmount = row.price * row.comboNum
      // 计算总金额
      let goodsAmountNew =0.0
      this.taoOrderItemList.forEach(x=>{
        goodsAmountNew+= row.itemAmount
      })
      this.form.totalAmount = goodsAmountNew

    },
    handleConfirm(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.goodsList = response.data.items;
        this.form.provinces = []
        this.form.provinces.push(response.data.province)
        this.form.provinces.push(response.data.city)
        this.form.provinces.push(response.data.town)
        this.isAudit = true
        this.detailOpen = true;
        this.detailTitle = "确认订单";
      });
    },
    handlePullUpdate(row) {
      // 接口拉取订单并更新
      this.pullLoading = true
      pullOrderDetail({shopId:row.shopId,orderId:row.tid}).then(response => {
        console.log('拉取淘宝订单接口返回=====',response)
        if(response.result === 0){
          this.$modal.msgSuccess(JSON.stringify(response));
        }else {
          this.$modal.msgError(JSON.stringify(response.msg));
        }

        this.pullLoading = false
      })
    },
  }
};
</script>

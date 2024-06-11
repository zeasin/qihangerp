<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="退款单号" prop="refundId">
        <el-input
          v-model="queryParams.refundId"
          placeholder="请输入退款单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="tid">
        <el-input
          v-model="queryParams.tid"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

<!--      <el-form-item label="申请时间" prop="created">-->
<!--        &lt;!&ndash; <el-input-->
<!--          v-model="queryParams.created"-->
<!--          placeholder="请输入退款申请时间。"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        /> &ndash;&gt;-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.created"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择退款申请时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->


      <!-- <el-form-item label="退款原因" prop="reason">
        <el-input
          v-model="queryParams.reason"
          placeholder="请输入退款原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->

      <el-form-item label="物流单号" prop="logisticsCode">
        <el-input
          v-model="queryParams.logisticsCode"
          placeholder="请输入物流单号"
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

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          :loading="pullLoading"
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取退款</el-button>
      </el-col>
      <!--  <el-col :span="1.5">
         <el-button
           type="success"
           plain
           icon="el-icon-edit"
           size="mini"
           :disabled="single"
           @click="handleUpdate"
           v-hasPermi="['tao:taoRefund:edit']"
         >修改</el-button>
       </el-col>
       <el-col :span="1.5">
         <el-button
           type="danger"
           plain
           icon="el-icon-delete"
           size="mini"
           :disabled="multiple"
           @click="handleDelete"
           v-hasPermi="['tao:taoRefund:remove']"
         >删除</el-button>
       </el-col>
       <el-col :span="1.5">
         <el-button
           type="warning"
           plain
           icon="el-icon-download"
           size="mini"
           @click="handleExport"
           v-hasPermi="['tao:taoRefund:export']"
         >导出</el-button>
       </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taoRefundList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="${comment}" align="center" prop="id" /> -->
      <el-table-column label="退款单号" align="center" prop="refundId" />
      <el-table-column label="类型" align="center" prop="disputeType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.disputeType === 'REFUND'"> 仅退款</el-tag>
          <el-tag size="small" v-if="scope.row.disputeType === 'REFUND_AND_RETURN'"> 退货退款</el-tag>
          <el-tag size="small" v-if="scope.row.disputeType === 'TMALL_EXCHANGE'"> 天猫换货</el-tag>
          <el-tag size="small" v-if="scope.row.disputeType === 'TAOBAO_EXCHANGE'"> 淘宝换货</el-tag>
          <el-tag size="small" v-if="scope.row.disputeType === 'REPAIR'"> 维修</el-tag>
          <el-tag size="small" v-if="scope.row.disputeType === 'RESHIPPING'"> 补寄</el-tag>
          <el-tag size="small" v-if="scope.row.disputeType === 'OTHERS'"> 其他</el-tag>

        </template>
      </el-table-column>
      <el-table-column label="订单号" align="center" prop="tid" />
<!--      <el-table-column label="子订单号" align="center" prop="oid" />-->
      <el-table-column label="订单状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
        <el-tag size="small" v-if="scope.row.orderStatus === 'WAIT_SELLER_SEND_GOODS'">等待卖家发货</el-tag>
        <el-tag size="small" v-if="scope.row.orderStatus === 'WAIT_BUYER_CONFIRM_GOODS'">等待买家确认收货</el-tag>
        <el-tag size="small" v-if="scope.row.orderStatus === 'TRADE_FINISHED'">交易成功</el-tag>
        <el-tag size="small" v-if="scope.row.orderStatus === 'TRADE_CLOSED'">交易关闭</el-tag>
        <el-tag size="small" v-if="scope.row.orderStatus === 'TRADE_CLOSED_BY_TAOBAO'">交易被淘宝关闭</el-tag>
        <el-tag size="small" v-if="scope.row.orderStatus === 'ALL_CLOSED'">交易被淘宝关闭1</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品" align="center" prop="title" />
      <el-table-column label="SKU" align="center" prop="sku" />

      <el-table-column label="退货数量" align="center" prop="num" />
     <el-table-column label="退款金额" align="center" prop="refundFee" />
       <el-table-column label="退款申请时间" align="center" prop="created" />
      <!-- <el-table-column label="更新时间。" align="center" prop="modified" /> -->
      <!-- <el-table-column label="退款对应的订单交易状态。" align="center" prop="orderStatus" /> -->
      <!-- <el-table-column label="退款状态" align="center" prop="status" /> -->
       <el-table-column label="货物状态" align="center" prop="goodStatus" >
         <template slot-scope="scope">
           <el-tag size="small" v-if="scope.row.goodStatus === 'BUYER_NOT_RECEIVED'">买家未收到货</el-tag>
           <el-tag size="small" v-if="scope.row.goodStatus === 'BUYER_RECEIVED'">买家已收到货</el-tag>
           <el-tag size="small" v-if="scope.row.goodStatus === 'BUYER_RETURNED_GOODS'">买家已退货</el-tag>
         </template>
       </el-table-column>
      <el-table-column label="买家是否需要退货" align="center" prop="hasGoodReturn" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.hasGoodReturn">是</el-tag>
          <el-tag size="small" v-else>否</el-tag>

        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 'WAIT_SELLER_AGREE'">买家已经申请退款，等待卖家同意</el-tag>
          <el-tag size="small" v-if="scope.row.status === 'WAIT_BUYER_RETURN_GOODS'">卖家已经同意退款，等待买家退货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 'WAIT_SELLER_CONFIRM_GOODS'">买家已经退货，等待卖家确认收货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 'SELLER_REFUSE_BUYER'">卖家拒绝退款</el-tag>
          <el-tag size="small" v-if="scope.row.status === 'SUCCESS'">退款成功</el-tag>
          <el-tag size="small" v-if="scope.row.status === 'CLOSED'">退款关闭</el-tag>
        </template>
      </el-table-column>

<!--      <el-table-column label="退款原因" align="center" prop="reason" />-->
<!--      <el-table-column label="退货物流" align="center" prop="logisticsCode" />-->
<!--      <el-table-column label="处理状态" align="center" prop="auditStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.auditStatus === 0"> 未处理</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.auditStatus === 2"> 已签收</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.auditStatus === 9"> 供应商已退款</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="店铺" align="center" prop="shopId" >
        <template slot-scope="scope">
          <span v-if="scope.row.shopId==6">梦小妮牛仔裤</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="${comment}" align="center" prop="erpGoodsId" /> -->
      <!-- <el-table-column label="${comment}" align="center" prop="erpGoodsSpecId" /> -->
      <!-- <el-table-column label="sku编号" align="center" prop="specNumber" /> -->
      <!-- <el-table-column label="退款阶段，可选值：onsale/aftersale" align="center" prop="refundPhase" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
          v-if="scope.row.auditStatus === 0 && scope.row.afterSalesType === 1 "
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleConfirm(scope.row)"
            v-hasPermi="['tao:taoRefund:edit']"
          >退货确认</el-button>
          <el-button
            size="mini"
            type="text"
            :loading="pullLoading"
            icon="el-icon-refresh"
            @click="handlePullUpdate(scope.row)"
          >更新退款</el-button>
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

    <!-- 添加或修改淘宝退款订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="退款ID" prop="refundId">
          <el-input v-model="form.refundId" placeholder="请输入退款id" disabled/>
        </el-form-item>
        <el-form-item label="订单单号" prop="tid">
          <el-input v-model="form.tid" placeholder="请输入淘宝交易单号" disabled/>
        </el-form-item>
        <el-form-item label="退还金额" prop="refundFee">
          <el-input v-model="form.refundFee" placeholder="请输入退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分" disabled/>
        </el-form-item>
        <el-form-item label="sku编号" prop="specNumber">
          <el-input v-model="form.specNumber" placeholder="请输入sku编号" />
        </el-form-item>
        <el-form-item label="退货数量" prop="num">
          <el-input v-model="form.num" placeholder="请输入退货数量" disabled/>
        </el-form-item>
        <el-form-item label="物流公司" prop="logisticsCompany">
          <el-input v-model="form.logisticsCompany" placeholder="请输入物流公司" />
        </el-form-item>
        <el-form-item label="物流单号" prop="logisticsCode">
          <el-input v-model="form.logisticsCode" placeholder="请输入物流单号" />
        </el-form-item>
        <el-form-item label="发货时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择发货时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listTaoRefund, getTaoRefund, pullRefund, pullRefundDetail} from "@/api/tao/taoRefund";
import { listShop } from "@/api/shop/shop";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
export default {
  name: "TaoRefund",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      pullLoading: false,
      // 总条数
      total: 0,
      // 淘宝退款订单表格数据
      taoRefundList: [],
      shopList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundId: null,
        afterSalesType: null,
        tid: null,
        oid: null,
        buyerNick: null,
        totalFee: null,
        payment: null,
        refundFee: null,
        created: null,
        modified: null,
        orderStatus: null,
        status: null,
        goodStatus: null,
        num: null,
        hasGoodReturn: null,
        reason: null,
        desc: null,
        logisticsCompany: null,
        logisticsCode: null,
        sendTime: null,
        auditStatus: null,
        auditTime: null,
        receivedTime: null,
        address: null,
        createOn: null,
        shopId: null,
        erpGoodsId: null,
        erpGoodsSpecId: null,
        specNumber: null,
        refundPhase: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        num: [
          { required: true, message: "退货数量不能为空", trigger: "blur" }
        ],
        logisticsCompany: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        logisticsCode: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        sendTime: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
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
    /** 查询淘宝退款订单列表 */
    getList() {
      this.loading = true;
      listTaoRefund(this.queryParams).then(response => {
        this.taoRefundList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
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
    handlePull(){
      if(this.queryParams.shopId){
        pullRefund({shopId:this.queryParams.shopId,updType:0}).then(response => {
          console.log('拉取淘宝退款接口返回=====',response)
          this.pullLoading=true
          if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权', '系统提示', { confirmButtonText: '重新授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              isRelogin.show = false;
              // store.dispatch('LogOut').then(() => {
              location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
              // })
            }).catch(() => {
              isRelogin.show = false;
            });

            // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
          }else{
            this.$modal.msgSuccess(JSON.stringify(response));
            this.getList();
          }
            this.pullLoading = false;
        })
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }
    },
    handlePullUpdate(row) {
      // 接口拉取订单并更新
      this.pullLoading = true
      pullRefundDetail({shopId:row.shopId,refundId:row.refundId}).then(response => {
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

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="退货单号" prop="refundNum">
        <el-input
          v-model="queryParams.refundNum"
          placeholder="请输入退货单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原始订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入原始订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 1">1688</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 2">视频号小店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 3">京东</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 4">淘宝天猫</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 5">拼多多</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 6">抖店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 7">小红书</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 8">快手小店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 99">其他</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="待审核" value="10001" ></el-option>
          <el-option label="等待买家退货" value="10002"></el-option>
          <el-option label="等待卖家收货" value="10005"> </el-option>
          <el-option label="拒绝退款" value="14000"></el-option>
          <el-option label="退款关闭" value="10011"></el-option>
          <el-option label="退款完成" value="10010"></el-option>
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
          icon="el-icon-plus"
          size="mini"
          @click="handlePull"
          v-hasPermi="['api:returned:add']"
        >拉取店铺售后</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['api:returned:edit']"
        >订单拦截</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['api:returned:edit']"
        >订单补发</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['api:returned:remove']"
        >删除</el-button>
      </el-col> -->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['api:returned:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="returnedList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="退款单号" align="center" prop="refundNum" />
      <el-table-column label="类型" align="center" prop="refundType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.refundType === 10"> 退货</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 20"> 换货</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 30"> 维修</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 11"> 仅退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="原始订单号" align="center" prop="originalOrderId" />
      <!-- <el-table-column label="店铺id" align="center" prop="shopId" /> -->
      <!-- <el-table-column label="店铺类型" align="center" prop="shopType" /> -->
      <!-- <el-table-column label="订单id" align="center" prop="orderId" /> -->
      <!-- <el-table-column label="子订单id" align="center" prop="orderItemId" /> -->
      <!-- <el-table-column label="商品id" align="center" prop="goodsId" /> -->
      <!-- <el-table-column label="规格id" align="center" prop="specId" /> -->
      <!-- <el-table-column label="商品编码" align="center" prop="goodsNum" /> -->
      <el-table-column label="sku编码" align="center" prop="specNum" />
      <el-table-column label="商品名称" align="center" prop="goodsName" />
      <el-table-column label="SKU" align="center" prop="goodsSku" />
<!--      <el-table-column label="商品图片" align="center" prop="goodsImage" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <image-preview :src="scope.row.goodsImage" :width="50" :height="50"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="买家是否需要退货" align="center" prop="hasGoodReturn" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.hasGoodReturn === 1">是</el-tag>
          <el-tag size="small" v-else>否</el-tag>
        </template>
      </el-table-column>
      <!-- <el-table-column label="物流公司" align="center" prop="logisticsCompany" /> -->
<!--      <el-table-column label="物流单号" align="center" prop="logisticsCode" />-->
<!--      <el-table-column label="收货时间" align="center" prop="receiveTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.receiveTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 10001"> 待审核</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10002"> 等待买家退货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10005"> 等待卖家收货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 14000"> 拒绝退款</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10011"> 退款关闭</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10010"> 退款关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
           v-if="scope.row.status === 1"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['api:returned:edit']"
          >确认收货</el-button>
          <el-button
           v-if="scope.row.status === 2"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['api:returned:edit']"
          >入库</el-button>
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

    <!-- 添加或修改退换货对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="退货单号" prop="returnedNum">
          <el-input v-model="form.returnedNum" placeholder="请输入退货单号" />
        </el-form-item>
        <el-form-item label="源订单号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入源订单号" />
        </el-form-item>
        <el-form-item label="店铺id" prop="shopId">
          <el-input v-model="form.shopId" placeholder="请输入店铺id" />
        </el-form-item>
        <el-form-item label="订单id" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入订单id" />
        </el-form-item>
        <el-form-item label="子订单id" prop="orderTimeId">
          <el-input v-model="form.orderTimeId" placeholder="请输入子订单id" />
        </el-form-item>
        <el-form-item label="商品id" prop="goodsId">
          <el-input v-model="form.goodsId" placeholder="请输入商品id" />
        </el-form-item>
        <el-form-item label="规格id" prop="specId">
          <el-input v-model="form.specId" placeholder="请输入规格id" />
        </el-form-item>
        <el-form-item label="商品编码" prop="goodsNum">
          <el-input v-model="form.goodsNum" placeholder="请输入商品编码" />
        </el-form-item>
        <el-form-item label="规格编码" prop="specNum">
          <el-input v-model="form.specNum" placeholder="请输入规格编码" />
        </el-form-item>
        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品规格" prop="goodsSpec">
          <el-input v-model="form.goodsSpec" placeholder="请输入商品规格" />
        </el-form-item>
        <el-form-item label="商品图片" prop="goodsImage">
          <image-upload v-model="form.goodsImage"/>
        </el-form-item>
        <el-form-item label="退货数量" prop="quantity">
          <el-input v-model="form.quantity" placeholder="请输入退货数量" />
        </el-form-item>
        <el-form-item label="物流公司" prop="logisticsCompany">
          <el-input v-model="form.logisticsCompany" placeholder="请输入物流公司" />
        </el-form-item>
        <el-form-item label="物流单号" prop="logisticsCode">
          <el-input v-model="form.logisticsCode" placeholder="请输入物流单号" />
        </el-form-item>
        <el-form-item label="收货时间" prop="receiveTime">
          <el-date-picker clearable
            v-model="form.receiveTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择收货时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="发货人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入发货人" />
        </el-form-item>
        <el-form-item label="发货人手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入发货人手机号" />
        </el-form-item>
        <el-form-item label="发货地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入发货地址" />
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
import { listReturned, getReturned, delReturned, addReturned, updateReturned } from "@/api/api/returned";
import {listShop} from "@/api/shop/shop";

export default {
  name: "Returned",
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
      // 总条数
      total: 0,
      // 退换货表格数据
      returnedList: [],
      shopList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        returnedNum: null,
        returnedType: null,
        orderNum: null,
        shopId: null,
        shopType: null,
        orderId: null,
        orderTimeId: null,
        goodsId: null,
        specId: null,
        goodsNum: null,
        specNum: null,
        goodsName: null,
        goodsSpec: null,
        goodsImage: null,
        quantity: null,
        logisticsCompany: null,
        logisticsCode: null,
        receiveTime: null,
        contactPerson: null,
        mobile: null,
        address: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        returnedNum: [
          { required: true, message: "退货单号不能为空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "源订单号不能为空", trigger: "blur" }
        ],
        orderId: [
          { required: true, message: "订单id不能为空", trigger: "blur" }
        ],
        orderTimeId: [
          { required: true, message: "子订单id不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "订单创建时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    listShop({}).then(response => {
      this.shopList = response.rows;
    });
    this.getList();
  },
  methods: {
    /** 查询退换货列表 */
    getList() {
      this.loading = true;
      listReturned(this.queryParams).then(response => {
        this.returnedList = response.rows;
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
      this.form = {
        id: null,
        returnedNum: null,
        returnedType: null,
        orderNum: null,
        shopId: null,
        shopType: null,
        orderId: null,
        orderTimeId: null,
        goodsId: null,
        specId: null,
        goodsNum: null,
        specNum: null,
        goodsName: null,
        goodsSpec: null,
        goodsImage: null,
        quantity: null,
        logisticsCompany: null,
        logisticsCode: null,
        receiveTime: null,
        remark: null,
        contactPerson: null,
        mobile: null,
        address: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      };
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
      this.$router.push('/saleafter/shop_refund');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReturned(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改退换货";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReturned(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReturned(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('api/returned/export', {
        ...this.queryParams
      }, `returned_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

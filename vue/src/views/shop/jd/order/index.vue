<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="订单号" prop="id">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
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
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单日期" prop="orderStartTime">
        <el-date-picker clearable
                        v-model="queryParams.orderStartTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择订单创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderState">
        <el-select v-model="queryParams.orderState" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="等待出库" value="WAIT_SELLER_STOCK_OUT" ></el-option>
          <el-option label="等待确认收货" value="WAIT_GOODS_RECEIVE_CONFIRM"></el-option>
          <el-option label="等待发货" value="WAIT_SELLER_DELIVERY"> </el-option>
          <el-option label="POP暂停" value="POP_ORDER_PAUSE"></el-option>
          <el-option label="完成" value="FINISHED_L"></el-option>
          <el-option label="取消" value="TRADE_CANCELED"></el-option>
          <el-option label="已锁定" value="LOCKED"></el-option>
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
          :loading="pullLoading"
          type="primary"
          plain
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
          :disabled="multiple"
          @click="handlePushOms"
        >确认订单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lists" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />

        <el-table-column label="订单号" align="left" prop="orderId" width="180px">
          <template slot-scope="scope">
            <div>{{scope.row.orderId}}</div>
            <el-tag size="small">{{ shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name :'' }}</el-tag>
          </template>
        </el-table-column>

      <el-table-column label="商品" width="450">
        <template slot-scope="scope">
          <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">
              <div style="margin-left:10px">
                <p>{{item.skuName}}
                  （{{item.outerSkuId}}）

                <el-tag size="small">数量：x {{item.itemTotal}}</el-tag>
                </p>
                <p v-if="scope.row.refundStatus === 0">
                  <el-button type="text" size="mini" round @click="handleRefund(scope.row,item)">售后</el-button>
                </p>
              </div>

          </el-row>
        </template>
      </el-table-column>
      <el-table-column label="订单应付金额" align="center" prop="orderPayment"  :formatter="amountFormatter"/>
      <el-table-column label="收货信息" align="left" prop="fullname" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.fullname }}</span><br/>
          <span>{{ scope.row.fullAddress }}</span>
        </template>
      </el-table-column>

      <el-table-column label="商家备注" align="center" prop="venderRemark" />
      <el-table-column label="状态" align="center" prop="orderStateRemark" >
        <template slot-scope="scope">
          <el-tag size="small">{{scope.row.orderStateRemark}}</el-tag>
          <span></span>
          <br />
          <el-tag size="small" v-if="!scope.row.auditStatus||scope.row.auditStatus === 0" style="margin-top: 5px;"> 待确认</el-tag>
          <el-tag size="small" v-if="scope.row.auditStatus === 1" style="margin-top: 5px;"> 已确认</el-tag>
          <el-tag size="small" v-if="scope.row.auditStatus === 2" style="margin-top: 5px;"> 已拦截</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderStartTime" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            size="mini"-->
<!--            :loading="pullLoading"-->
<!--            icon="el-icon-refresh"-->
<!--            @click="handlePullUpdate(scope.row)"-->
<!--          >更新订单</el-button>-->
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

    <!-- 订单详情对话框 -确认订单 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1100px" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="120px" inline>
        <el-descriptions title="订单信息">
          <el-descriptions-item label="订单号">{{form.orderId}}</el-descriptions-item>

          <el-descriptions-item label="店铺">
            <span >{{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name :'' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="下单时间">
            {{ form.orderStartTime }}
          </el-descriptions-item>

          <el-descriptions-item label="商家备注">
            {{form.venderRemark}}
          </el-descriptions-item>
          <el-descriptions-item label="买家备注">
            {{form.orderRemark}}
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag size="small">{{form.orderStateRemark}}</el-tag>
          </el-descriptions-item>

        </el-descriptions>
        <el-descriptions title="付款信息">
          <el-descriptions-item label="订单总金额">{{ amountFormatter(null,null,parseFloat(form.orderTotalPrice),null)}}</el-descriptions-item>
          <el-descriptions-item label="订单货款金额">{{amountFormatter(null,null,parseFloat(form.orderSellerPrice),null)}}</el-descriptions-item>
          <el-descriptions-item label="商家优惠">{{amountFormatter(null,null,parseFloat(form.sellerDiscount),null)}}</el-descriptions-item>
          <el-descriptions-item label="运费">{{amountFormatter(null,null,parseFloat(form.freightPrice),null)}}</el-descriptions-item>
          <el-descriptions-item label="买家应付金额">{{amountFormatter(null,null,parseFloat(form.orderPayment),null)}}</el-descriptions-item>

        </el-descriptions>


        <el-descriptions title="收货信息">
          <el-descriptions-item label="收件人姓名">{{form.fullname}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.mobile}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.county}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.fullAddress}}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="center">订单商品</el-divider>
        <el-table :data="form.itemList"  style="margin-bottom: 10px;">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" type="index" width="50"/>

<!--          <el-table-column label="商品图片" prop="picPath" width="80">-->
<!--            <template slot-scope="scope">-->
<!--              <el-image style="width: 70px; height: 70px" :src="scope.row.picPath"></el-image>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="商品标题" prop="skuName" ></el-table-column>
          <el-table-column label="SKU编码" prop="outerSkuId"></el-table-column>
          <el-table-column label="SkuID" prop="skuId"></el-table-column>
          <el-table-column label="单价" prop="jdPrice"></el-table-column>
          <el-table-column label="数量" prop="itemTotal"></el-table-column>

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
        <el-form-item label="收件人姓名" prop="fullname" v-if="isAudit">
          <el-input v-model="form.fullname" placeholder="请输入收件人姓名" style="width:250px" />
        </el-form-item>
        <el-form-item label="收件人电话" prop="mobile" v-if="isAudit">
          <el-input v-model="form.mobile" placeholder="请输入收件人电话" style="width:250px" />
        </el-form-item>

        <el-form-item label="详细地址" prop="fullAddress" v-if="isAudit">
          <el-input v-model="form.fullAddress" placeholder="请输入收件地址" style="width:250px" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer" v-if="isAudit">
        <el-button type="primary" @click="submitConfirmForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import {listOrder, pullOrder, getOrder, confirmOrder, pullOrderDetail} from "@/api/jd/order";
import { listShop } from "@/api/shop/shop";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
export default {
  name: "OrderJd",
  data() {
    return {
      // 遮罩层
      loading: true,
      pullLoading: false,
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
      // 商品管理表格数据
      lists: [],
      // 弹出层标题
      title: "",
      isAudit:false,
      detailTitle:'订单详情',
      detailOpen:false,
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        image: null
      },
      // 表单参数
      form: {},
      shopList: [],
      // 表单校验
      rules: {
        fullname: [{ required: true, message: "请填写收件人姓名", trigger: "blur" }],
        mobile: [{ required: true, message: "请填写收件人手机号", trigger: "blur" }],
        fullAddress: [{ required: true, message: "请填写详细地址", trigger: "change" }],
      }
    };
  },
  created() {
    listShop({platform: 3}).then(response => {
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
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.lists = response.rows;
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
        name: null,
        image: null,
        number: null
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
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handlePushOms(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否手动推送到OMS？').then(function() {
        return pushOms({ids:ids});
      }).then(() => {
        // this.getList();
        this.$modal.msgSuccess("推送成功");
      }).catch(() => {});
    },
    handlePull(){
      if(this.queryParams.shopId){
        this.pullLoading = true
        pullOrder({shopId:this.queryParams.shopId,updType:0}).then(response => {
          console.log('拉取JD订单接口返回=====',response)
          if(response.code === 200){
            this.getList()
            this.$modal.msgSuccess(JSON.stringify(response));
          }
          else if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              this.$router.push({path:"/shop/shop_list",query:{platform:3}})
              // isRelogin.show = false;
              // store.dispatch('LogOut').then(() => {
              // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
              // })
            }).catch(() => {
              isRelogin.show = false;
            });

            // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
          }else{

            this.$modal.msgError(JSON.stringify(response));
          }
          this.pullLoading = false
        })
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }
    },
    handlePullUpdate(row) {
      // 接口拉取订单并更新
      this.pullLoading = true
      pullOrderDetail({shopId:row.shopId,orderId:row.orderId}).then(response => {
        console.log('拉取JD订单接口返回=====',response)
        this.$modal.msgSuccess(JSON.stringify(response));
        this.getList()
        this.pullLoading = false
      })
    },
    handleDetail(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        // this.goodsList = response.data.items;
        this.detailOpen = true;
        this.detailTitle = "订单详情";
      });
      this.isAudit = false
    },
    handleConfirm(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        // this.goodsList = response.data.items;
        this.form.provinces = []
        this.form.provinces.push(response.data.province)
        this.form.provinces.push(response.data.city)
        this.form.provinces.push(response.data.town)
        this.isAudit = true
        this.detailOpen = true;
        this.detailTitle = "确认订单";
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
  }
};
</script>

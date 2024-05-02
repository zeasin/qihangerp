<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="原始订单号" prop="sourceOrderNum">
        <el-input
          v-model="queryParams.sourceOrderNum"
          placeholder="请输入原始订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品ID" prop="goodsId">
        <el-input
          v-model="queryParams.goodsId"
          placeholder="请输入商品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格ID" prop="specId">
        <el-input
          v-model="queryParams.specId"
          placeholder="请输入规格ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格编码" prop="specNum">
        <el-input
          v-model="queryParams.specNum"
          placeholder="请输入规格编码"
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:stockOutEntry:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockOutEntryList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
<!--      <el-table-column label="出库单号" align="center" prop="stockOutNum" />-->
      <el-table-column label="源单号" align="center" prop="sourceOrderNum" />
      <el-table-column label="商品Id" align="center" prop="goodsId" />
      <el-table-column label="规格Id" align="center" prop="specId" />
      <el-table-column label="规格编码" align="center" prop="specNum" />
      <el-table-column label="总数量" align="center" prop="originalQuantity" />
      <el-table-column label="已出库数量" align="center" prop="outQuantity" />

      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0">待拣货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 1">拣货中</el-tag>
          <el-tag size="small" v-if="scope.row.status === 2">拣货完成</el-tag>
          <el-tag size="small" v-if="scope.row.status === 3">已出库</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="创建日期" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-d-arrow-right"
            @click="handleStockOut(scope.row)"
            v-hasPermi="['wms:stockOutEntry:edit']"
          >出库</el-button>
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

    <!-- 添加或修改出库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-descriptions title="出库单详情">-->
<!--          <el-descriptions-item label="单号">{{form.stockOutNum}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="来源">-->
<!--            <el-tag size="small" v-if="form.stockOutType === 1">订单拣货出库</el-tag>-->
<!--            <el-tag size="small" v-if="form.stockOutType === 2">采购退货出库</el-tag>-->
<!--            <el-tag size="small" v-if="form.stockOutType === 3">盘点出库</el-tag>-->
<!--            <el-tag size="small" v-if="form.stockOutType === 4">报损出库</el-tag>-->
<!--          </el-descriptions-item>-->
<!--          <el-descriptions-item label="备注">{{form.remark}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="商品数">{{form.goodsUnit}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="规格数">{{form.specUnit}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="总件数">{{form.specUnitTotal}}</el-descriptions-item>-->
<!--&lt;!&ndash;          <el-descriptions-item label="店铺">&ndash;&gt;-->
<!--&lt;!&ndash;            <span v-if="form.shopId==6">梦小妮牛仔裤</span>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-descriptions-item>&ndash;&gt;-->
<!--        </el-descriptions>-->


        <el-divider content-position="center">出库商品明细</el-divider>
        <el-table :data="items" :row-class-name="rowWmsStockOutEntryItemIndex" ref="wmsStockOutEntryItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品图片" prop="colorImage" >
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.colorImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="规格编码" prop="specNum"></el-table-column>
          <el-table-column label="规格"  >
            <template slot-scope="scope">
              <el-tag size="small">{{scope.row.colorValue}} {{scope.row.sizeValue}} {{scope.row.styleValue}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="数量" prop="originalQuantity"></el-table-column>
          <el-table-column label="已出库数量" prop="outQuantity"></el-table-column>

          <el-table-column label="出库仓位" prop="inventoryId" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.inventoryDetailId" placeholder="请选择出库仓位" v-if="scope.row.status < 2">
                <el-option v-for="item in scope.row.inventoryDetails" :key="item.id" :label="item.locationNum" :value="item.id">
                  <span style="float: left">{{ item.locationNum }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px"  >剩余库存：{{ item.currentQty }}</span>

                </el-option>

              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="出库数量" prop="outQty" width="100">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.outQty" placeholder="出库数量"  v-if="scope.row.status < 2" />
            </template>
          </el-table-column>
          <el-table-column label="出库操作" prop="outQuantity" width="100"  >
          <template slot-scope="scope">
          <el-button
            v-if="scope.row.status < 2"
            size="mini"
            plain
            type="danger"
            @click="stockOutSubmit(scope.row)"
            icon="el-icon-d-arrow-right"
          >出库</el-button>
        </template>
          </el-table-column>
        </el-table>
      </el-form>

    </el-dialog>
  </div>
</template>

<script>

import {listOrderStockOutEntryItem} from "@/api/wms/shipping";
import {getStockOutEntryItem} from "@/api/wms/stockOutEntry";
export default {
  name: "StockOutEntryItem",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedWmsStockOutEntryItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 出库单表格数据
      stockOutEntryList: [],
      // 出库单明细表格数据
      items: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stockOutNum: null,
        sourceNo: null,
        sourceId: null,
        stockOutType: null,
        status: null,
        printStatus: null,
        printTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        completeTime: null,
        stockOutOperatorId: null,
        stockOutOperatorName: null,
        stockOutTime: null,
      },
      // 表单参数
      form: {
      },
      // 表单校验
      rules: {
        inventoryId:[{ required: true, message: '请填写收货信息' }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询出库单列表 */
    getList() {
      this.loading = true;
      listOrderStockOutEntryItem(this.queryParams).then(response => {
        this.stockOutEntryList = response.rows;
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
        stockOutNum: null,
        sourceNo: null,
        sourceId: null,
        stockOutType: null,
        status: null,
        printStatus: null,
        printTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        completeTime: null,
        stockOutOperatorId: null,
        stockOutOperatorName: null,
        stockOutTime: null,
        remark: null,
        isDelete: null,
        goodsUnit: null,
        specUnit: null,
        specUnitTotal: null
      };
      this.items = [];
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
    /** 修改按钮操作 */
    handleStockOut(row) {
      this.reset();
      const id = row.id || this.ids
      getStockOutEntryItem(id).then(response => {
        // this.form = response.data;
        this.items.push(response. data);
        // this.wmsStockOutEntryItemList.forEach(x=>{
        //   x.inventoryId = null;
        //   x.outQty = null
        // })
        this.open = true;
        this.title = "出库操作";
      });
    },
    /** 提交按钮 */
    stockOutSubmit(row) {
      console.log("=====提交出库操作====",row)
      if(!row.outQty && row.outQty <= 0){
        this.$modal.msgError("请填写要出库的库存");
        return
      }
      if(!row.inventoryDetailId){
        this.$modal.msgError("请选择库存仓位");
        return
      }else{
        // 判断填写的数量是否小于等于当前仓位库存
        // if(row.inventoryId < row.outQty){
        //   this.$modal.msgError("仓位库存不足！");
        //   return
        // }
        // 判断输入的数量要小于等于需要出库的数量
        if(row.outQty > (row.originalQuantity - row.outQuantity)){
          this.$modal.msgError("出库数量不对！");
          return
        }
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          const subForm ={
            entryItemId:row.id,
            entryId:row.entryId,
            specId:row.specId,
            inventoryDetailId:row.inventoryDetailId,
            outQty:row.outQty
          }
          stockOut(subForm).then(response => {
              this.$modal.msgSuccess("出库成功");
              // this.open = false;
              getStockOutEntry(row.id).then(response => {
                this.form = response.data;
                this.wmsStockOutEntryItemList = response.data.wmsStockOutEntryItemList;
              });
            });

        }
      });
    },
	/** 出库单明细序号 */
    rowWmsStockOutEntryItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/stockOutEntry/export', {
        ...this.queryParams
      }, `stockOutEntry_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

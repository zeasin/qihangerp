<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="发货物流费用" name="stockShip">
        <ship-logistics-fee></ship-logistics-fee>

      </el-tab-pane>
      <el-tab-pane label="供应商代发货账单" name="supplierShip" lazy>
        <supplier-ship-fee></supplier-ship-fee>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import supplierShipFee  from "@/views/shipping/shipFee/supplier_ship_fee.vue";
import shipLogisticsFee  from "@/views/shipping/shipFee/ship_logistics_fee.vue";

export default {
  name: "shipFeeIndex",
  components:{supplierShipFee,shipLogisticsFee},
  data() {
    return {
      activeName: 'stockShip'
    };
  },
  created() {

  },
  mounted() {

  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>

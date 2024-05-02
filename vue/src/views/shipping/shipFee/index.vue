<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="发货物流费用" name="stockShip">
        <ship-logistics-fee></ship-logistics-fee>

      </el-tab-pane>
      <el-tab-pane label="供应商代发货账单" name="supplierShip" lazy>
        <agent-ship-fee></agent-ship-fee>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import agentShipFee  from "@/views/shipping/shipFee/angent_ship_fee.vue";
import shipLogisticsFee  from "@/views/shipping/shipFee/ship_logistics_fee.vue";
export default {
  name: "shipFeeIndex",
  components:{agentShipFee,shipLogisticsFee},
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

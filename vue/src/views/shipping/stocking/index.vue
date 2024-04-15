<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="仓库发货清单" name="stockShip">
        <wms-stocking></wms-stocking>

      </el-tab-pane>
      <el-tab-pane label="供应商代发货清单" name="supplierShip" lazy>
        <supplier-ship></supplier-ship>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import supplierShip  from "@/views/shipping/stocking/supplierShip.vue";
import wmsStocking  from "@/views/shipping/stocking/wmsStocking.vue";

export default {
  name: "stocking",
  components:{supplierShip,wmsStocking},
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

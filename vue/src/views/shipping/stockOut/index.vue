<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="待出库" name="stockEntry">
        <stock-out-entry></stock-out-entry>

      </el-tab-pane>
      <el-tab-pane label="待出库明细" name="stockEntryItem" lazy>
        <stock-out-entry-item></stock-out-entry-item>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import StockOutEntryItem  from "@/views/shipping/stockOut/stockOutEntryItem.vue";
import StockOutEntry  from "@/views/shipping/stockOut/stockOutEntry.vue";

export default {
  name: "stockOut",
  components:{StockOutEntryItem,StockOutEntry},
  data() {
    return {
      activeName: 'stockEntry'
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

<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="订单拦截管理" name="intercept">
        <intercept-after></intercept-after>
      </el-tab-pane>
      <el-tab-pane label="退货管理" name="returned" lazy>
        <returned-after></returned-after>
      </el-tab-pane>
      <el-tab-pane label="换货管理" name="exchange" lazy>
        <exchange-after></exchange-after>
      </el-tab-pane>
      <el-tab-pane label="补发管理" name="shipAgain" lazy>
        <ship-again-after></ship-again-after>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import InterceptAfter  from "@/views/afterSale/intercept/index.vue";
import ReturnedAfter  from "@/views/afterSale/returned/index.vue";
import ExchangeAfter  from "@/views/afterSale/exchange/index.vue";
import ShipAgainAfter  from "@/views/afterSale/shipAgain/index.vue";

export default {
  name: "afterSaleIndex",
  components:{InterceptAfter,ReturnedAfter,ExchangeAfter,ShipAgainAfter},
  data() {
    return {
      activeName: 'intercept'
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

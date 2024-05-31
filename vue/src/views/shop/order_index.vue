<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="淘宝天猫" name="taoOrder">
        <order-tao></order-tao>
      </el-tab-pane>
      <el-tab-pane label="京东POP" name="jdOrder" lazy>
        <order-jd></order-jd>
      </el-tab-pane>
      <el-tab-pane label="拼多多" name="pddOrder" lazy>
        <order-pdd></order-pdd>
      </el-tab-pane>
      <el-tab-pane label="抖店" name="douOrder" lazy>
        <order-dou></order-dou>
      </el-tab-pane>
      <el-tab-pane label="视频号小店" name="weiOrder" lazy>
        <order-wei></order-wei>
      </el-tab-pane>
<!--      <el-tab-pane label="快手小店" name="kwaiOrder" lazy>-->
<!--        &lt;!&ndash;        <order-jd></order-jd>&ndash;&gt;-->
<!--        暂不支持快手小店，因为作者没有快手appkey，如果你有appkey并且愿意提供测试，那么请联系作者！-->
<!--      </el-tab-pane>-->
<!--      <el-tab-pane label="小红书" name="xhsOrder" lazy>-->
<!--        <order-xhs></order-xhs>-->
<!--      </el-tab-pane>-->
    </el-tabs>

  </div>
</template>

<script>
import OrderTao  from "@/views/shop/tao/order/index.vue";
import OrderDou  from "@/views/shop/dou/order/index.vue";
import OrderPdd  from "@/views/shop/pdd/order/index.vue";
import OrderJd  from "@/views/shop/jd/order/index.vue";
import OrderWei  from "@/views/shop/wei/order/index.vue";
import OrderXhs  from "@/views/shop/xhs/order/index.vue";
export default {
  name: "Order",
  components:{OrderTao,OrderDou,OrderPdd,OrderJd,OrderWei,OrderXhs},
  data() {
    return {
      activeName: 'taoOrder'
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

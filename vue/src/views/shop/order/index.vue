<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="淘宝天猫" name="taoOrder">
        <order-tao></order-tao>
      </el-tab-pane>
      <el-tab-pane label="京东" name="jdOrder" lazy>
<!--        <order-jd></order-jd>-->
        开源版本不支持京东，需要京东接口请联系作者！
      </el-tab-pane>
      <el-tab-pane label="拼多多" name="pddOrder" lazy>
        <order-pdd></order-pdd>
      </el-tab-pane>
      <el-tab-pane label="抖店" name="douOrder" lazy>
        <order-dou></order-dou>
      </el-tab-pane>
      <el-tab-pane label="视频号" name="weiOrder" lazy>
        <order-wei></order-wei>
      </el-tab-pane>
      <el-tab-pane label="快手小店" name="kwaiOrder" lazy>
        <!--        <order-jd></order-jd>-->
        暂不支持快手小店，因为作者没有快手appkey，如果你有appkey并且愿意提供测试，那么请联系作者！
      </el-tab-pane>
      <el-tab-pane label="小红书" name="xhsOrder" lazy>
        <order-xhs></order-xhs>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import OrderTao  from "@/views/tao/order/index";
import OrderDou  from "@/views/dou/order/index";
import OrderPdd  from "@/views/pdd/order/index";
import OrderJd  from "@/views/jd/order/index";
import OrderWei  from "@/views/wei/order/index";
import OrderXhs  from "@/views/xhs/order/index";
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

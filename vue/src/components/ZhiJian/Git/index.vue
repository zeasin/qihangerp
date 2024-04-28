<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'QiHangERP',
  data() {
    return {
      url: 'https://gitee.com/qiliping/qihang-erp'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>

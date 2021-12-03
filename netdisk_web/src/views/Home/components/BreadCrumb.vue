<template>
  <el-breadcrumb class="bread-crumb" separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">全部</el-breadcrumb-item>
    <el-breadcrumb-item>文件夹1</el-breadcrumb-item>
    <el-breadcrumb-item>文件夹1-1</el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
export default {
  name: "BreadCrumb",
  data() {
    return {}
  }
}
</script>

<style lang="stylus" scoped>
.bread-crumb {
  height: 28px;
  line-height: 28px;
}
</style>
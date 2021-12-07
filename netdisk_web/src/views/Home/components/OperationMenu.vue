<template>
  <div class="operation-menu-wrapper">
    <!-- 按钮组 -->
    <el-button-group class="operate-group">
      <!-- disabled 只在全部类型页面 才可新建文件夹 -->
      <el-button
          size="mini"
          type="primary"
          icon="el-icon-plus"
          :disabled="fileType !== 0"
          @click="addFolderDialog.visible = true"
      >新建文件夹</el-button
      >
    </el-button-group>

    <!-- 对话框 - 新建文件夹 -->
    <!-- @closed 对话框关闭动画结束时 重置表单并清空所有表单校验 -->
    <el-dialog
        title="新建文件夹"
        width="600px"
        :visible.sync="addFolderDialog.visible"
        @closed="$refs.addFolderForm.resetFields()"
    >
      <el-form :model="addFolderForm" :rules="addFolderRules" label-position="top" ref="addFolderForm">
        <el-form-item label="文件夹名称" prop="name">
          <el-input v-model="addFolderForm.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addFolderDialog.visible = false">取 消</el-button>
        <el-button type="primary" :loading="addFolderDialog.loading" @click="handleAddFolderSubmit('addFolderForm')"
        >提 交</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { createFile } from '@/request/file.js'

export default {
  name: 'OperationMenu',
  props: {
    // 文件类型
    fileType: {
      type: Number,
      required: true
    },
    // 文件路径
    filePath: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      // 新建文件夹对话框数据
      addFolderDialog: {
        visible: false, //  对话框显隐状态
        loading: false
      },
      // 新建文件夹表单
      addFolderForm: {
        name: ''
      },
      // 新建文件夹表单校验规则
      addFolderRules: {
        name: [
          {
            required: true,
            message: '请输入文件夹名称',
            trigger: 'blur, change'
          }
        ]
      }
    }
  },
  methods: {
    // 新建文件夹对话框 - 提交按钮
    handleAddFolderSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addFolderDialog.loading = true //  对话框的确定按钮打开loading状态
          // 表单校验通过 - 调用新建文件夹接口
          createFile({
            fileName: this.addFolderForm.name,
            filePath: this.filePath, //  文件路径
            isDir: 1 //  是否为文件夹：1 表示文件夹 0 表示文件
          }).then(
              (res) => {
                this.addFolderDialog.loading = false //  对话框的确定按钮关闭loading状态
                if (res.success) {
                  this.$message.success('添加成功')
                  this.addFolderDialog.visible = false //  关闭对话框
                  this.$emit('getTableData') // 重新获取文件列表
                } else {
                  this.$message.warning(res.message)
                }
              },
              (error) => {
                this.addFolderDialog.loading = false //  对话框的确定按钮关闭loading状态
                console.log(error)
              }
          )
        } else {
          return false
        }
      })
    }
  }
}
</script>
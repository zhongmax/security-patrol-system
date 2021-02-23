<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          type="primary"
          @click="handleSearchList()"
          size="small"
        >
          查询搜索
        </el-button>
        <el-button
          style="float: right; margin-right: 15px"
          @click="handleResetSearch()"
          size="small"
        >
          重置
        </el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form
          :inline="true"
          :model="listQuery"
          size="small"
          label-width="140px"
        >
          <el-form-item label="权限名称：">
            <el-input
              v-model="listQuery.nameKeyword"
              class="input-width"
              placeholder="权限名称"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="权限路径：">
            <el-input
              v-model="listQuery.urlKeyword"
              class="input-width"
              placeholder="权限路径"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="权限分类：">
            <el-select
              v-model="listQuery.categoryId"
              placeholder="全部"
              clearable
              class="input-width"
            >
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        size="mini"
        class="btn-add"
        @click="handleAdd()"
        style="margin-left: 20px"
        >添加</el-button
      >
      <el-button size="mini" class="btn-add" @click="handleShowCategory()"
        >权限分类</el-button
      >
    </el-card>
    <div class="table-container">
      <el-table
        ref="permissionTable"
        :data="list"
        style="width: 100%"
        v-loading="listLoading"
        border
      >
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="权限名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="权限路径" align="center">
          <template slot-scope="scope">{{ scope.row.permission }}</template>
        </el-table-column>
        <el-table-column label="描述" align="center">
          <template slot-scope="scope">{{ scope.row.description }}</template>
        </el-table-column>
        <el-table-column label="添加时间" width="160" align="center">
          <template slot-scope="scope">{{
            scope.row.createTime | formatDateTime
          }}</template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handleUpdate(scope.$index, scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              @click="handleDelete(scope.$index, scope.row)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes,prev, pager, next,jumper"
        :current-page.sync="listQuery.pageNum"
        :page-size="listQuery.pageSize"
        :page-sizes="[10, 15, 20]"
        :total="total"
      >
      </el-pagination>
    </div>
    <el-dialog
      :title="isEdit ? '编辑权限' : '添加权限'"
      :visible.sync="dialogVisible"
      width="40%"
    >
      <el-form
        :model="permission"
        ref="permissionForm"
        label-width="150px"
        size="small"
      >
        <el-form-item label="权限名称：">
          <el-input v-model="permission.name" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="权限路径：">
          <el-input
            v-model="permission.permission"
            style="width: 250px"
          ></el-input>
        </el-form-item>
        <el-form-item label="权限分类：">
          <el-select
            v-model="permission.categoryId"
            placeholder="全部"
            clearable
            style="width: 250px"
          >
            <el-option
              v-for="item in categoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述：">
          <el-input
            v-model="permission.description"
            type="textarea"
            :rows="5"
            style="width: 250px"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { fetchList, createPermission, updatePermission, deletePermission } from '@/api/permission'
import { listAllCate } from '@/api/permissionCategory'
import { formatDate } from '@/utils/date'

const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  nameKeyword: null,
  urlKeyword: null,
  categoryId: null
}
const defaultPermission = {
  id: null,
  name: null,
  url: null,
  categoryId: null,
  description: ''
}
export default {
  name: 'permissionList',
  data() {
    return {
      listQuery: Object.assign({}, defaultListQuery),
      list: null,
      total: null,
      listLoading: false,
      dialogVisible: false,
      permission: Object.assign({}, defaultPermission),
      isEdit: false,
      categoryOptions: [],
      defaultCategoryId: null
    }
  },
  created() {
    this.getList()
    this.getCateList()
  },
  filters: {
    formatDateTime(time) {
      if (time == null || time === '') {
        return 'N/A'
      }
      let date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  methods: {
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery)
    },
    handleSearchList() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.pageNum = 1
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    handleAdd() {
      this.dialogVisible = true
      this.isEdit = false
      this.permission = Object.assign({}, defaultPermission)
      this.permission.categoryId = this.defaultCategoryId
    },
    handleDelete(index, row) {
      this.$confirm('是否要删除该权限?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePermission(row.id).then(response => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.getList()
        })
      })
    },
    handleUpdate(index, row) {
      this.dialogVisible = true
      this.isEdit = true
      this.permission = Object.assign({}, row)
    },
    handleDialogConfirm() {
      this.$confirm('是否要确认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.isEdit) {
          updatePermission(this.permission.id, this.permission).then(response => {
            this.$message({
              message: '修改成功！',
              type: 'success'
            })
            this.dialogVisible = false
            this.getList()
          })
        } else {
          createPermission(this.permission).then(response => {
            this.$message({
              message: '添加成功！',
              type: 'success'
            })
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    handleShowCategory() {
      this.$router.push({ path: '/ums/permissionCategory' })
    },
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.listLoading = false
        this.list = response.data.list
        this.total = response.data.total
      })
    },
    getCateList() {
      listAllCate().then(response => {
        let cateList = response.data
        for (let i = 0; i < cateList.length; i++) {
          let cate = cateList[i]
          this.categoryOptions.push({ label: cate.name, value: cate.id })
        }
        this.defaultCategoryId = cateList[0].id
      })
    }
  }
}
</script>
<style></style>



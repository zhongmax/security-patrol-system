<template>
  <el-card class="form-container" shadow="never">
    <div
      v-for="(cate, index) in allPermissionCate"
      :class="index === 0 ? 'top-line' : null"
      :key="'cate' + cate.id"
    >
      <el-row class="table-layout" style="background: #f2f6fc">
        <el-checkbox
          v-model="cate.checked"
          :indeterminate="isIndeterminate(cate.id)"
          @change="handleCheckAllChange(cate)"
        >
          {{ cate.name }}
        </el-checkbox>
      </el-row>
      <el-row class="table-layout">
        <el-col
          :span="8"
          v-for="permission in getPermissionByCate(cate.id)"
          :key="permission.id"
          style="padding: 4px 0"
        >
          <el-checkbox
            v-model="permission.checked"
            @change="handleCheckChange(permission)"
          >
            {{ permission.name }}
          </el-checkbox>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 20px" align="center">
      <el-button type="primary" @click="handleSave()">保存</el-button>
      <el-button @click="handleClear()">清空</el-button>
    </div>
  </el-card>
</template>

<script>
import { fetchAllPermissionList } from '@/api/permission'
import { listAllCate } from '@/api/permissionCategory'
import { allocPermission, listPermissionByRole } from '@/api/role'

export default {
  name: "allocPermission",
  data() {
    return {
      roleId: null,
      allPermission: null,
      allPermissionCate: null
    }
  },
  created() {
    this.roleId = this.$route.query.roleId
    this.getAllPermissionCateList()
  },
  methods: {
    getAllPermissionList() {
      fetchAllPermissionList().then(response => {
        this.allPermission = response.data
        for (let i = 0; i < this.allPermission.length; i++) {
          this.allPermission[i].checked = false
        }
        this.getPermissionByRole(this.roleId)
      })
    },
    getAllPermissionCateList() {
      listAllCate().then(response => {
        this.allPermissionCate = response.data
        for (let i = 0; i < this.allPermissionCate.length; i++) {
          this.allPermissionCate[i].checked = false
        }
        this.getAllPermissionList()
      })
    },
    getPermissionByCate(categoryId) {
      let catePermission = []
      if (this.allPermission == null) return null
      for (let i = 0; i < this.allPermission.length; i++) {
        let permission = this.allPermission[i]
        if (permission.categoryId === categoryId) {
          catePermission.push(permission)
        }
      }
      return catePermission
    },
    getPermissionByRole(roleId) {
      listPermissionByRole(roleId).then(response => {
        let allocPermission = response.data
        this.allPermission.forEach(item => {
          item.checked = this.getPermissionChecked(item.id, allocPermission)
        })
        this.allPermissionCate.forEach(item => {
          item.checked = this.isAllChecked(item.id)
        })
        this.$forceUpdate()
      })
    },
    getPermissionChecked(permissionId, allocPermission) {
      if (allocPermission == null || allocPermission.length === 0) return false
      for (let i = 0; i < allocPermission.length; i++) {
        if (allocPermission[i].id === permissionId) {
          return true
        }
      }
      return false
    },
    isIndeterminate(categoryId) {
      let catePermissions = this.getPermissionByCate(categoryId)
      if (catePermissions == null) return false
      let checkedCount = 0
      for (let i = 0; i < catePermissions.length; i++) {
        if (catePermissions[i].checked === true) {
          checkedCount++
        }
      }
      return !(checkedCount === 0 || checkedCount === catePermissions.length)
    },
    isAllChecked(categoryId) {
      let catePermissions = this.getPermissionByCate(categoryId)
      if (catePermissions == null) return false
      let checkedCount = 0
      for (let i = 0; i < catePermissions.length; i++) {
        if (catePermissions[i].checked === true) {
          checkedCount++
        }
      }
      if (checkedCount === 0) {
        return false
      }
      return checkedCount === catePermissions.length
    },
    handleSave() {
      this.$confirm('是否分配资源？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let checkedPermissionIds = new Set()
        if (this.allPermission != null && this.allPermission.length > 0) {
          this.allPermission.forEach(item => {
            if (item.checked) {
              checkedPermissionIds.add(item.id)
            }
          })
        }
        let params = new URLSearchParams()
        params.append("roleId", this.roleId)
        params.append("permissionIds", Array.from(checkedPermissionIds))
        allocPermission(params).then(response => {
          this.$message({
            message: '分配成功',
            type: 'success',
            duration: 1000
          })
          this.$router.back()
        })
      })
    },
    handleClear() {
      this.allPermissionCate.forEach(item => {
        item.checked = false
      })
      this.allPermission.forEach(item => {
        item.checked = false
      })
      this.$forceUpdate()
    },
    handleCheckAllChange(cate) {
      let catePermissions = this.getPermissionByCate(cate.id)
      for (let i = 0; i < catePermissions.length; i++) {
        catePermissions[i].checked = cate.checked
      }
      this.$forceUpdate()
    },
    handleCheckChange(permission) {
      this.allPermissionCate.forEach(item => {
        if (item.id === permission.categoryId) {
          item.checked = this.isAllChecked(permission.categoryId)
        }
      })
      this.$forceUpdate()
    }
  }
}
</script>

<style scoped>
.table-layout {
  padding: 20px;
  border-left: 1px solid #dcdfe6;
  border-right: 1px solid #dcdfe6;
  border-bottom: 1px solid #dcdfe6;
}

.top-line {
  border-top: 1px solid #dcdfe6;
}
</style>

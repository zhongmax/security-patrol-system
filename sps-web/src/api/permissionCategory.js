import request from '@/utils/request'

export function listAllCate() {
  return request({
    url: '/permCategory/listAll',
    method: 'get'
  })
}

export function createPermissionCategory(data) {
  return request({
    url: '/permCategory/create',
    method: 'post',
    data: data
  })
}

export function updatePermissionCategory(id, data) {
  return request({
    url: '/permCategory/update/' + id,
    method: 'post',
    data: data
  })
}

export function deletePermissionCategory(id) {
  return request({
    url: '/permCategory/delete/' + id,
    method: 'post'
  })
}

import request from '@/utils/request'

// 查询淘宝订单列表
export function listOrder(query) {
  return request({
    url: '/tao-api/order/list',
    method: 'get',
    params: query
  })
}

// 查询淘宝订单详细
export function getOrder(id) {
  return request({
    url: '/tao-api/order/' + id,
    method: 'get'
  })
}

// 新增淘宝订单
export function addOrder(data) {
  return request({
    url: '/tao-api/order',
    method: 'post',
    data: data
  })
}

// 确认淘宝订单
export function confirmOrder(data) {
  return request({
    url: '/tao-api/order/confirmOrder',
    method: 'post',
    data: data
  })
}


// 接口拉取淘宝订单
export function pullOrder(query) {
  return request({
    url: '/tao-api/order/pull_order',
    method: 'get',
    params: query
  })
}
export function pullOrderDetail(data) {
  return request({
    url: '/tao-api/order/pull_order_detail',
    method: 'post',
    data: data
  })
}

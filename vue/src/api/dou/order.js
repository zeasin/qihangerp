import request from '@/utils/request'

// 查询抖店订单列表
export function listOrder(query) {
  return request({
    url: '/dou-api/order/list',
    method: 'get',
    params: query
  })
}

// 查询抖店订单详细
export function getOrder(id) {
  return request({
    url: '/dou-api/order/' + id,
    method: 'get'
  })
}

// 确认抖店订单
export function confirmOrder(data) {
  return request({
    url: '/dou-api/order/confirm',
    method: 'post',
    data: data
  })
}



// 接口拉取抖店订单
export function pullOrder(data) {
  return request({
    url: '/dou-api/order/pull_order',
    method: 'post',
    data: data
  })
}

import request from '@/utils/request'

// 查询仓库订单发货列表
export function listShipping(query) {
  return request({
    url: '/shipping/stock_ship_list',
    method: 'get',
    params: query
  })
}

// 查询仓库订单发货详细
export function getShipping(id) {
  return request({
    url: '/wms/shipping/' + id,
    method: 'get'
  })
}

// 生成出库单
export function generateStockOutEntry(data) {
  return request({
    url: '/shipping/generate_stock_out_entry',
    method: 'post',
    data: data
  })
}

// 修改仓库订单发货
export function updateShipping(data) {
  return request({
    url: '/wms/shipping',
    method: 'put',
    data: data
  })
}

// 删除仓库订单发货
export function delShipping(id) {
  return request({
    url: '/wms/shipping/' + id,
    method: 'delete'
  })
}

// 查询供应商订单发货列表
export function listShippingSupplier(query) {
  return request({
    url: '/shipping/supplier_ship_list',
    method: 'get',
    params: query
  })
}


// 订单待出库列表
export function listOrderStockOutEntry(query) {
  return request({
    url: '/shipping/order_stock_out_entry_list',
    method: 'get',
    params: query
  })
}

// 订单待出库明细列表
export function listOrderStockOutEntryItem(query) {
  return request({
    url: '/shipping/order_stock_out_entry_item_list',
    method: 'get',
    params: query
  })
}

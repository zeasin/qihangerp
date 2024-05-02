package cn.qihangerp.api.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class ShipOrderSupplierShipBo {
    private Long erpOrderId;
    private String logisticsCompany;
    private String logisticsCode;
    private Float logisticsFee;
    private String shipTime;
    private String remark;
    private List<ShipOrderSupplierShipItemBo> itemList;
}

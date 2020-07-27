package com.kakarote.crm9.erp.crm.common;

public enum WorkOrderEnum {

    WorkOrder_NULL("NULL",0,"NULL")
    ;

    private final String name;
    private final int  type;
    private final String remarks;

    WorkOrderEnum(String name, int type, String remarks) {
        this.name = name;
        this.type = type;
        this.remarks = remarks;
    }
    public static WorkOrderEnum parse(int type) {
        for (WorkOrderEnum Enum : WorkOrderEnum.values()) {
            if (Enum.getType()==type) {
                return Enum;
            }
        }
        return WorkOrder_NULL;
    }

    public static WorkOrderEnum parse(String name) {
        for (WorkOrderEnum Enum : WorkOrderEnum.values()) {
            if (Enum.getName().equals(name)) {
                return Enum;
            }
        }
        return WorkOrder_NULL;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

}

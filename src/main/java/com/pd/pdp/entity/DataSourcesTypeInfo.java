package com.pd.pdp.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataSourcesTypeInfo implements Serializable {

    private static final long serialVersionUID = -2126718447287158588L;


    private int id;
    private String sourcesType;


}

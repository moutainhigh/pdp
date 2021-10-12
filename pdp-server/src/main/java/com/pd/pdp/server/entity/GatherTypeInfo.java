package com.pd.pdp.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/9
 */

@Data
public class GatherTypeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private int id;
    private String gatherType;

}

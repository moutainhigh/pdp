package com.pd.pdp.server.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Description:
 *
 * @author ckw
 * @date 2021/10/27
 */

@Data
public class QualityRulesInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private int id;
    private String ruleName;

}

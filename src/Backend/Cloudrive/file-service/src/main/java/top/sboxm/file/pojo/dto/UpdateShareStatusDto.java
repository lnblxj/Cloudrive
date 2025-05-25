package top.sboxm.file.pojo.dto;

import lombok.Data;

@Data
public class UpdateShareStatusDto {
    /**
     * 分享ID
     */
    private String shareId;

    /**
     * 状态：0=审核中 1=正常 2=下架
     */
    private Integer status;
}
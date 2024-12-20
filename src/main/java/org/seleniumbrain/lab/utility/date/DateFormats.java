package org.seleniumbrain.lab.utility.date;

import lombok.Getter;
import org.seleniumbrain.lab.utils.date.DateFormatEnum;

@Getter
public enum DateFormats {
    YYYY_MM_DD("yyyy-MM-dd"),
    YYYY_MMM_DD("yyyy-MMM-dd"),
    YYYY_MMMM_DD("yyyy-MMMM-dd"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD__HH_MM_SS("yyyy-MM-dd - HH:mm:ss"),
    YYYY_MM_DD_T_HH_MM_SS("yyyy-MM-dd'T'HH:mm:ss"),
    YYYY_MM_DD_T_HH_MM_SS_SSS("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    YYYY_MM_DD_T_HH_MM_SS_SSS_X("yyyy-MM-dd'T'HH:mm:ss.SSSX"),
    YYYY_MM_DD_HH_MM_SS_SSS_X("yyyy-MM-dd HH:mm:ss.SSSX"),
    YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX("yyyy-MM-dd'T'HH:mm:ss.SSSSXXX"),
    YYYY_MM_DD_HH_MM_SS_SSSS_XXX("yyyy-MM-dd HH:mm:ss.SSSSXXX"),
    YYYY_MM_DD_SLASH("yyyy/MM/dd"),
    DD_MM_YYYY("dd-MM-yyyy"),
    DD_MM_YYYY_SLASH("dd/MM/yyyy"),
    DDMMYYYY("ddMMyyyy"),
    DD_MMM_YYYY("dd-MMM-yyyy"),
    DD_MMM_YYYY_HH_MM_SS("dd-MMM-yyyy - HH:mm:ss"),
    DD_MMMM_YYYY("dd-MMMM-yyyy"),
    DD_MMM_COMMA_YYYY("dd MMM, yyyy"),
    DD_MON_COMMA_YYYY("dd MMMM, yyyy"),
    MMM_DD_COMMA_YYYY("MMM dd, yyyy"),
    MMMM_DD_COMMA_YYYY("MMMM dd, yyyy");

    private final String format;

    DateFormats(String format) {
        this.format = format;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author deepak2310
 */
public class Naration extends Common {

    
    private String narationType;
    private String narationDetail;

    public String getNarationType() {
        return narationType;
    }

    public void setNarationType(String narationType) {
        this.narationType = narationType;
    }

    public String getNarationDetail() {
        return narationDetail;
    }

    public void setNarationDetail(String narationDetail) {
        this.narationDetail = narationDetail;
    }

}

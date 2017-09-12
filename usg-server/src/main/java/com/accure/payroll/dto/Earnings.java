package com.accure.payroll.dto;

import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.leave.dto.LeaveEncashment;
import com.accure.usg.common.dto.Common;
import java.util.ArrayList;

/**
 *
 * @author chaitu
 */
public class Earnings extends Common {

    private boolean isEarningHeads;
    private ArrayList<EarningHeadsDetails> earningHeads;
    private boolean isLeaveEncashment;
    private LeaveEncashment leaveEncashment;

    public boolean isIsEarningHeads() {
        return isEarningHeads;
    }

    public void setIsEarningHeads(boolean isEarningHeads) {
        this.isEarningHeads = isEarningHeads;
    }

    public ArrayList<EarningHeadsDetails> getEarningHeads() {
        return earningHeads;
    }

    public void setEarningHeads(ArrayList<EarningHeadsDetails> earningHeads) {
        this.earningHeads = earningHeads;
    }

    public boolean isIsLeaveEncashment() {
        return isLeaveEncashment;
    }

    public void setIsLeaveEncashment(boolean isLeaveEncashment) {
        this.isLeaveEncashment = isLeaveEncashment;
    }

    public LeaveEncashment getLeaveEncashment() {
        return leaveEncashment;
    }

    public void setLeaveEncashment(LeaveEncashment leaveEncashment) {
        this.leaveEncashment = leaveEncashment;
    }


}

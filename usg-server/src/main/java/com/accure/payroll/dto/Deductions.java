package com.accure.payroll.dto;

import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.usg.common.dto.Common;
import java.util.ArrayList;

/**
 *
 * @author chaitu
 */
public class Deductions extends Common {

    private boolean isDeductionHeads;
    private ArrayList<EarningHeadsDetails> deductionHeads;
    private boolean isLoan;
    private ArrayList<LoanPayment> loan;
    private boolean isInsurance;
    private ArrayList<InsuranceTransactions> insurance;
    private boolean isIncomeTax;
    private IncomeTax incomeTax;

    public boolean isIsDeductionHeads() {
        return isDeductionHeads;
    }

    public void setIsDeductionHeads(boolean isDeductionHeads) {
        this.isDeductionHeads = isDeductionHeads;
    }

    public ArrayList<EarningHeadsDetails> getDeductionHeads() {
        return deductionHeads;
    }

    public void setDeductionHeads(ArrayList<EarningHeadsDetails> deductionHeads) {
        this.deductionHeads = deductionHeads;
    }

    public boolean isIsLoan() {
        return isLoan;
    }

    public void setIsLoan(boolean isLoan) {
        this.isLoan = isLoan;
    }

    public ArrayList<LoanPayment> getLoan() {
        return loan;
    }

    public void setLoan(ArrayList<LoanPayment> loan) {
        this.loan = loan;
    }

    public boolean isIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(boolean isInsurance) {
        this.isInsurance = isInsurance;
    }

    public ArrayList<InsuranceTransactions> getInsurance() {
        return insurance;
    }

    public void setInsurance(ArrayList<InsuranceTransactions> insurance) {
        this.insurance = insurance;
    }

    public boolean isIsIncomeTax() {
        return isIncomeTax;
    }

    public void setIsIncomeTax(boolean isIncomeTax) {
        this.isIncomeTax = isIncomeTax;
    }

    public IncomeTax getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(IncomeTax incomeTax) {
        this.incomeTax = incomeTax;
    }

}

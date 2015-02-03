package gh.com.zenith.ZPROMPT;

import java.util.List;

/**
 * Created by Robby on 1/27/2015.
 */
public class HelperClass
{

    //login classes

    public class RimInfo
    {
        public int rimNo ;
        public String rimName ;
        public String rsmName ;
    }
    public class Account
    {
        public String acctType ;
        public String acctNo ;
        public String acctDesc ;
        public String isoCurrency ;
    }
    public class Profile
    {
        public String username ;
        public long accesscode ;
        public RimInfo rimInfo ;
        public List<Account> accounts ;
        public boolean changePassword ;
        // public String AuthCode { get; set;}
    }


    public class RootObject
    {
        public Profile profile ;
        public String AuthCode ;
        public AccountActivity accountActivity ;
        public SingleBalance balance ;
        public TransferProduct transferProduct ;
        public Transfer transfer ;
        public BillProduct billProduct ;
        public BillInformation billInformation ;
        public BillPayment billPayment ;
        public AccountBalance accountBalance ;

    }

    //account balances classes
    public class AccountBalance
    {
        public String acctType ;
        public String acctNo ;
        public String acctDesc ;
        public String isoCurrency ;
        public double curBal ;
        public double acctAvail ;
        public int holdBal ;
        public String title1 ;
        public int memoFloat ;
    }
    //account activities classes
    public class AccountActivity
    {
        public int amt ;
        public int checkNo ;
        public String createDt ;
        public String drCr ;
        public String effectiveDt ;
        public String historyDesc ;
        public String isoCurrency ;
        public int itemType ;
        public int ptid ;
        public boolean reversal ;
        public int runningBal ;
        public String tranCodeDesc ;
        public String transactionDt ;
    }
    //single account balance classes
    public class SingleBalance
    {
        public double availableBal ;
        public double curBal ;
        public int memoCR ;
        public int memoDR ;
        public int memoFloat ;
    }
    //account activity seacrch results classes
    public class singleAccountActivity
    {
        public int amt ;
        public int checkNo ;
        public String createDt ;
        public String drCr ;
        public String effectiveDt ;
        public String historyDesc ;
        public String isoCurrency ;
        public int itemType ;
        public int ptid ;
        public boolean reversal ;
        public int runningBal ;
        public String tranCodeDesc ;
        public String transactionDt ;
    }
    //transfer products classes
    public class Validation
    {
        public int id ;
        public int fieldNum ;
        public boolean required ;
        public String validatorMessage ;
        public String extAutoComplete ;
        public String regEx ;
    }
    public class TransferProduct
    {
        public int id ;
        public String productCode ;
        public String productName ;
        public String field1 ;
        public String field2 ;
        public String field3 ;
        public String field4 ;
        public String field5 ;
        public boolean valid ;
        public List<Validation> validations ;
    }
    //trans beneficiary classes
    public class TransferBeneficiary
    {
        public String id ;
    }
    public class Schedule
    {
        public String firstScheduleDt ;
        public int frid ;
        public boolean valid ;
        public String id ;
    }
    public class Transfer
    {
        public int id ;
        public TransferBeneficiary transferBeneficiary ;
        public Schedule schedule ;
        public String tfrAcctNo ;
        public int amount ;
    }
    //Bill Products classes
    public class Validations
    {
        public int id ;
        public int fieldNum ;
        public boolean required ;
        public String regEx ;
        public String validatorMessage ;
    }
    public class BillProduct
    {
        public int id ;
        public String productName ;
        public String field1 ;
        public Validations validations ;
    }
    public class BillInformation
    {
        public int id ;
        public BillProduct billProduct ;
        public int field1 ;
        public Schedule schedule;
    }
    //bill Payment classes
    public class BillPayment
    {
        public int id ;
        public BillInformation billInformation ;
        public long pmtAcctNo ;
        public int amount ;
        public Schedule schedule ;
    }


}

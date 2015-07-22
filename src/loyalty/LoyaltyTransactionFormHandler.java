package loyalty;

import java.sql.Date;

import javax.transaction.SystemException;

import atg.nucleus.Nucleus;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.droplet.DropletException;
import atg.droplet.GenericFormHandler;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.security.SecurityException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

public class LoyaltyTransactionFormHandler extends GenericFormHandler {
	
	private static final String ITEM_DESCRIPTOR_USER = "user";
	private LoyaltyManager mLM=null;
    private Integer mAmount;
    private String mDescription=null;
    private Date mTransactionDate=null;
    private String mUser=null;   
    private String mAddLoyaltyTransactionErrorURL = null;
    private String mLoyaltyTransactionId = null;
    private String mUserId;
    private String mRepositoryId;
    private String mSuccessURL;
    private String mProfile;
    
    public LoyaltyManager getLoyaltyManager() {
    	return mLM;
    }
    
    public void setLoyaltyManager(LoyaltyManager pLM) { 
    	mLM = pLM;
    }

    public Integer getAmount() { 
    	return mAmount;
   	}
    
    public void setAmount(Integer pAmount) {
    	mAmount = pAmount;
   	}
    
    public String getDescription() {
    	return mDescription; 
    }
    public void setDescription(String pDescription) {
    	mDescription = pDescription; 
    }
    
    public Date getTransactionDate() {
    	return mTransactionDate; 
   	}
    public void setTransactionDate(Date pTransactionDate) {
    	mTransactionDate = pTransactionDate;
   	}
    
    public String getUser() {
    	return mUser;
    }
    public void setUser(String pUser) {
    	mUser = pUser;
    }
                             
    public String getUserId() { 
    	return mUserId;
    }
    public void setUserId(String pUserId) {
    	mUserId = pUserId; 
    }                    
    
    public String getProfile() { 
    	return mProfile;
   	}
    
    public void setProfile(String pProfile) {
    	mProfile = pProfile;
   	}
                             
    public String getAddLoyaltyTransactionErrorURL() {
    	return mAddLoyaltyTransactionErrorURL;
    }
    public void setAddLoyaltyTransactionErrorURL(String pAddLoyaltyTransactionErrorURL) {
    	mAddLoyaltyTransactionErrorURL = pAddLoyaltyTransactionErrorURL;
    }
    
    public String getSuccessURL() {
    	return mSuccessURL;
    }
    public void setSuccessURL(String pSuccessURL) {
    	mSuccessURL = pSuccessURL;
    }
    
    
    public String getRepositoryId() {
    	return mRepositoryId; 
    }
    public void setRepositoryId(String pRepositoryId) {
    	mRepositoryId = pRepositoryId;
    }   	 
    
    
    
    private void validateLoyaltyInput(RepositoryItem mUser){
    	
    	LoyaltyManager lm = getLoyaltyManager();
//    	LoyaltyManager lm = (LoyaltyManager) Nucleus.getGlobalNucleus().resolveName("/loyalty/LoyaltyTransactionFormHandler");
        
    	if ((getAmount() == null) || !(getAmount() instanceof Integer)) {
        		if (isLoggingDebug()) 
        			logDebug("amount is null");
            addFormException(new DropletException("The amount field can't be empty or not integer number"));
           
    	} 
        	
        	if (mUser == null) {
          	  
                if (isLoggingDebug()) 
                    logDebug("user is null");
                addFormException(new DropletException("The user field can't be empty"));
                
        	  } 
        	if (mProfile == null) {
            	  
                if (isLoggingDebug()) 
                    logDebug("Profile is null");
                addFormException(new DropletException("The profile can't be empty"));
                
        	  } 
    }
    
    public boolean handleAddLoyalty (DynamoHttpServletRequest request, DynamoHttpServletResponse response)
      throws java.io.IOException, RepositoryException, SecurityException
    {       
    	LoyaltyManager lm = getLoyaltyManager();
    	
   	              
   	 
    	if (isLoggingDebug())
           logDebug("handleAddLoyalty called");        
        
   	 	validateLoyaltyInput(lm.getUserRepository().getItem(getUserId(), ITEM_DESCRIPTOR_USER));

   	 	
   	 	if(!(getFormError())){
   	 	
   	 	try{	
   	 		TransactionDemarcation td = new TransactionDemarcation();
   	 		td.begin(lm.getTransactionManager(), td.REQUIRED);
   	 		
   	 		try {             
   	 			mLoyaltyTransactionId = lm.createLoyaltyTransaction(getUserId(), getAmount(), getDescription());
          
   	 			if (isLoggingDebug())
   	 				logDebug("Adding loyalty transaction " +  mLoyaltyTransactionId + " to user " + mUserId);         
          
   	 			lm.addLoyaltyTransactionToUser(mLoyaltyTransactionId, getUserId());      	  
            }
            catch (RepositoryException re) {
        	 
              if (isLoggingError())
                  logError(re);
               addFormException(new DropletException("Cannot create loyalty transaction",re));
               
               try {
                   lm.getTransactionManager().setRollbackOnly();
               }
               catch (Exception se) {
                   if (isLoggingError())
                       logError("Unable to set rollback for transaction", se);
               }
           }
   	 	
        finally {
            td.end();
        }    
      }
      catch (TransactionDemarcationException e) {
        if (isLoggingError()) 
           logError("creating transaction demarcation failed, no loyalty created", e);
      }
   	 	
    if (getSuccessURL() != null) {
     	response.sendLocalRedirect(getSuccessURL(), request); 
       return false;  
    } 
 
  } 		
   	 	return true;  
  }
}    
package loyalty;

import atg.repository.Repository;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryException;
import javax.transaction.TransactionManager;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;

public class LoyaltyManager extends atg.nucleus.GenericService {
	
	  private Repository mRepository = null;
	  private Repository mUserRepository = null;	  
	  private TransactionManager mTransactionManager = null;	  
	  
	  public LoyaltyManager() {}
	  
	  public void setTransactionManager(TransactionManager pTransactionManager) {
		    mTransactionManager = pTransactionManager;
	  }		  
		 
	  public TransactionManager getTransactionManager() {
		    return mTransactionManager;
	  }
	  
	  public void setRepository(Repository pRepository) {
		    mRepository = pRepository;
	  }		  
		 
	  public Repository getRepository() {
		    return mRepository;
	  }
	  
	  public void setUserRepository(Repository pUserRepository) {
		    mUserRepository = pUserRepository;
	  }		  
		 
	  public Repository getUserRepository() {
		    return mUserRepository;
	  }	  
	  public String createLoyaltyTransaction(String userId, Integer amount, String description) throws RepositoryException {
		  return createLoyaltyTransaction(userId, amount, description, Calendar.getInstance().getTime());
	  }
	  public String createLoyaltyTransaction(String userId, Integer amount, String description, Date transactionDate) throws RepositoryException {
		   
	      if (isLoggingDebug()) 
	          logDebug("Creating new loyalty transaction " );

	      MutableRepository mutRepos = (MutableRepository) getRepository();	      
	      RepositoryItem loyaltyTransactionItem = null;
	      
	       try {
	         MutableRepositoryItem mutLoyaltyTransactionItem = mutRepos.createItem("loyaltyTransaction");
	         mutLoyaltyTransactionItem.setPropertyValue("userId", userId);
	         mutLoyaltyTransactionItem.setPropertyValue("amount", amount);
	         mutLoyaltyTransactionItem.setPropertyValue("description", description);
	         mutLoyaltyTransactionItem.setPropertyValue("transactionDate", transactionDate);	           
	         mutRepos.addItem(mutLoyaltyTransactionItem);
	         loyaltyTransactionItem = mutLoyaltyTransactionItem;
	         
	         if (isLoggingDebug()) 
	             logDebug("new loyalty transaction " + loyaltyTransactionItem + " created");
	         }
	       catch (RepositoryException e) {
	         if (isLoggingError()) {
	            logError(e);
	         }
	         throw e;
	       }
	       
	      return loyaltyTransactionItem.getRepositoryId();	    	    
	  }
 
	  public void addLoyaltyTransactionToUser(String pLoyaltyTransactionId, String pUserId) throws RepositoryException {
	      if (isLoggingDebug()) 
	          logDebug("adding loyalty transaction " + pLoyaltyTransactionId + " to user " + pUserId);

	      MutableRepository mutRepos = (MutableRepository)getUserRepository();
	      Repository repos = getRepository();
	      
	          MutableRepositoryItem user = mutRepos.getItemForUpdate(pUserId, "user");  
	          RepositoryItem loyaltyTransaction = repos.getItem(pLoyaltyTransactionId, "loyaltyTransaction");
	          
	          if (isLoggingDebug()) 
	             logDebug("loyalty transaction is adding " + loyaltyTransaction + " to user " + user);
	          
	          Collection loyaltyTransactionList = (Collection)user.getPropertyValue("loyaltyTransactions");
	          loyaltyTransactionList.add(loyaltyTransaction);
	          mutRepos.updateItem(user);	      
     
	 }     
}
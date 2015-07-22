package loyalty;

import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemImpl;
import atg.repository.RepositoryPropertyDescriptor;

import java.util.Set;
import java.util.Iterator;

public class LoyaltyCalc extends RepositoryPropertyDescriptor {

	String mTotalLoyaltyAmount;

	public Object getPropertyValue(RepositoryItemImpl pItem, Object pValue) {

		int totalLoyaltyAmount = 0;
		Set loyaltyTransactions = (Set) pItem
				.getPropertyValue("loyaltyTransactions");
		Iterator i = loyaltyTransactions.iterator();

		while (i.hasNext()) {
			RepositoryItem currentLoyaltyTransaction = (RepositoryItem) i
					.next();
			Integer amount = (Integer) currentLoyaltyTransaction
					.getPropertyValue("amount");
			totalLoyaltyAmount += amount;
		}

		return totalLoyaltyAmount;
	}

	public Class getPropertyType() {
		return String.class;
	}

	public LoyaltyCalc() {
		super();
	}

	public boolean isQueryable() {
		return false;
	}

	public boolean isWritable() {
		return false;
	}
}
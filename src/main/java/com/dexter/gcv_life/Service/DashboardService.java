package com.dexter.gcv_life.Service;

import com.dexter.gcv_life.Entity.GrowthRequest;
import com.dexter.gcv_life.Entity.StoreReachRequest;

public interface DashboardService {

	public double getGrowth(GrowthRequest growthRequest);
	
	public Long getStoreReach(StoreReachRequest storeReachRequest);
	
}

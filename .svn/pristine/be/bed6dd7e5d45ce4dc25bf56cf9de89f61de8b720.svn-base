package com.comviva.api.j4u.utils;

import com.comviva.api.j4u.dao.PEDLookUPDAO;

import com.comviva.api.j4u.model.PEDRandomPrizeInfo;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PEDRandomPrizesCache {
	private static final Logger LOGGER = Logger.getLogger(PEDRandomPrizesCache.class);
	private final List<PEDRandomPrizeInfo> randomPrizesCache;
	private LocalDateTime cacheReloadTime;

	private PEDRandomPrizesCache() {
		randomPrizesCache = new ArrayList<>();
		try {
			init();
		} catch (Exception ex) {
			LOGGER.error("Error occured at ==> ", ex);
		}
	}

	public String getRandomPrize(int randomNum) {
		String prizeId = PEDCONSTANT.NO_PRIZE;

		for (PEDRandomPrizeInfo priceInfo : randomPrizesCache) {
			if (randomNum >= priceInfo.getMinRange() && randomNum <= priceInfo.getMaxRange()) {
				prizeId = priceInfo.getPrizeID();
				break;
			}
		}
		return prizeId;

	}

	public void reloadCache() {
		try {
			init();
		} catch (Exception ex) {
			LOGGER.error("Error occured at ==> ", ex);
		}
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Cache reloaded at - ");
		stringBuilder.append(cacheReloadTime.toString());
		stringBuilder.append(" | Total records - ");
		stringBuilder.append(randomPrizesCache.size());

		return stringBuilder.toString();
	}

	public static PEDRandomPrizesCache instance() {
		return InstanceHolder.instance;
	}

	public void init() throws Exception {
		LOGGER.info("PEDRandomPrizesCache - Caching START");
		cacheReloadTime = LocalDateTime.now();
		PEDLookUPDAO dao = new PEDLookUPDAO();
		List<PEDRandomPrizeInfo> randomPrizeInfoList = dao.getRandomPrize();
		if (null != randomPrizeInfoList) {
			if (null != randomPrizesCache) {
				randomPrizesCache.clear();
				randomPrizesCache.addAll(randomPrizeInfoList);
			}
		} else {
			throw new Exception("Could not find any record in Random Prize table");
		}
		LOGGER.info("PEDRandomPrizesCache - Caching END - Total records in Cache -" +(randomPrizesCache != null  ? randomPrizesCache.size() : 0));
	}

	private static class InstanceHolder {
		private static PEDRandomPrizesCache instance = new PEDRandomPrizesCache();
	}
}

package com.comviva.api.j4u.utils;

import com.comviva.api.exception.APIException;
import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.model.ProductInfo;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ProductInfoCache {
	private static final Logger LOGGER = Logger.getLogger(ProductInfoCache.class);
	private static final String EXCEPTION_MESSAGE = "Could not find any product info from database";
	private final Map<String, ProductInfo> cache;
	private final Map<String, ProductInfo> locationCache;
	private final Map<String, ProductInfo> socialCache;
	private final Map<String, ProductInfo> morningCache;
	private final Map<String, List<String>> prodTypeCache;
	private final Map<String, List<String>> locationProdTypeCache;
	private final Map<String, List<String>> prodSubTypeCache;
	private final Map<String, List<String>> nonPromotionalProductInfo;
	private final List<String> socialProdList;
	private final List<String> morningProdInfoList;
	private LocalDateTime cacheReloadTime;

	private final BiConsumer<Map<String, ProductInfo>, Map<String, List<String>>> initProductTypeCache = (cachemap,
			cachetype) -> cachemap.forEach((key, value) -> {
				ProductInfo productInfo = value;
				if (productInfo.getLangCode() == 1) {
					if (cachetype.containsKey(productInfo.getProductType())) {
						List<String> prodList = cachetype.get(productInfo.getProductType());
						prodList.add(productInfo.getProductID());
					} else {
						List<String> prodList = new ArrayList<>();
						prodList.add(productInfo.getProductID());
						cachetype.put(productInfo.getProductType(), prodList);
					}
				}
			});

	private final BiConsumer<Map<String, ProductInfo>, Map<String, List<String>>> initProductSubTypeCache = (cachemap,
			cachetype) -> cachemap.forEach((key, value) -> {
				ProductInfo productInfo = value;
				if (productInfo.getLangCode() == 1) {
					if (cachetype.containsKey(productInfo.getProductType() + "_" + productInfo.getProductSubType())) {
						List<String> prodList = cachetype
								.get(productInfo.getProductType() + "_" + productInfo.getProductSubType());
						prodList.add(productInfo.getProductID());
					} else {
						List<String> prodList = new ArrayList<>();
						prodList.add(productInfo.getProductID());
						cachetype.put(productInfo.getProductType() + "_" + productInfo.getProductSubType(), prodList);
					}
				}
			});

	ProductInfoCache() {
		cache = new HashMap<>();
		locationCache = new HashMap<>();
		socialCache = new HashMap<>();
		morningCache = new HashMap<>();
		prodTypeCache = new HashMap<>();
		locationProdTypeCache = new HashMap<>();
		prodSubTypeCache = new HashMap<>();
		socialProdList = new ArrayList<>();
		morningProdInfoList = new ArrayList<>();
		nonPromotionalProductInfo = new HashMap<>();
		initCache();
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Cache reloaded at - ");
		stringBuilder.append(cacheReloadTime.toString());
		stringBuilder.append(" | ML ProdInfo Cache total records - ");
		stringBuilder.append(cache.size());
		stringBuilder.append(" | Location ProdInfo Cache total records - ");
		stringBuilder.append(locationCache.size());
		stringBuilder.append(" | Social ProdInfo Cache total records - ");
		stringBuilder.append(socialCache.size());
		stringBuilder.append(" | Morning ProdInfo Cache total records - ");
		stringBuilder.append(morningCache.size());

		return stringBuilder.toString();
	}

	public static ProductInfoCache instance() {
		return InstanceHolder.cacheInstance;
	}

	public ProductInfo get(String key) {
		ProductInfo productInfo;
		productInfo = cache.get(key);
		if (null == productInfo) {
			productInfo = locationCache.get(key);
		}
		if (null == productInfo) {
			productInfo = socialCache.get(key);
		}
		if (null == productInfo) {
			productInfo = morningCache.get(key);
		}
		return productInfo;
	}

	public ProductInfo getML(String key) {
		return cache.get(key);
	}

	public ProductInfo getLocation(String key) {
		return locationCache.get(key);
	}

	public ProductInfo getSocial(String key) {
		return socialCache.get(key);
	}

	public List<String> getProductIdsList(String key) {
		return prodTypeCache.get(key);
	}

	public List<String> getProductIdsListBySubCategory(String key) {
		return prodSubTypeCache.get(key);
	}

	public ProductInfo getMorning(String key) {
		return morningCache.get(key);
	}

	public List<String> getLocationProdIds(String category, String prodSubType, String poolId, int langCode) {
		List<String> prodIdsList = new ArrayList<>();
		locationCache.forEach((key, value) -> {
			ProductInfo productInfo = value;
			if (langCode == productInfo.getLangCode() && poolId.equalsIgnoreCase(productInfo.getPoolID())
					&& category.equalsIgnoreCase(productInfo.getProductType())) {
				if ((productInfo != null) || (prodSubType.equalsIgnoreCase(productInfo.getProductSubType())))
					prodIdsList.add(productInfo.getProductID());
			}
		});

		return prodIdsList;
	}

	public List<String> getSocialProductIdsList() {
		return socialProdList;
	}

	public List<String> getMorningProductIdsList() {
		return morningProdInfoList;
	}

	public List<String> getNonPromotionalOffer(String key) {
		return nonPromotionalProductInfo.get(key);
	}

	public void reloadCache() {
		initCache();
	}

	private synchronized void initCache() {
		LOGGER.info("ProductInfoCache - Caching START");
		try {
			cacheReloadTime = LocalDateTime.now();
			LookUpDAO dao = new LookUpDAO();
			Map<String, ProductInfo> prodInfoMap = dao
					.getProductInfoMap(PropertiesLoader.getValue(J4UOfferConstants.J4U_PRODINFO_PROC_NAME));
			Map<String, ProductInfo> locationProdInfoMap = dao.getLocationProductInfoMap();
			Map<String, ProductInfo> socialProdInfoMap = dao
					.getProductInfoMap(PropertiesLoader.getValue(J4UOfferConstants.J4U_SOCIAL_PRODINFO_PROC_NAME));
			Map<String, ProductInfo> morningProdInfoMap = dao.getMorningProductInfoMap();
			if (null != prodInfoMap) {
				cache.clear();
				cache.putAll(prodInfoMap);
			} else {
				throw new APIException(EXCEPTION_MESSAGE);
			}

			if (null != locationProdInfoMap) {
				locationCache.clear();
				locationCache.putAll(locationProdInfoMap);
			} else {
				throw new APIException(EXCEPTION_MESSAGE);
			}

			if (null != socialProdInfoMap) {
				socialCache.clear();
				socialCache.putAll(socialProdInfoMap);
				socialProdInfoMap.forEach((k, v) -> {
					if (v.getLangCode() == 1)
						socialProdList.add(v.getProductID());
				});
				LOGGER.debug("Social Product Type Cache - " + socialProdList);
			} else {
				throw new APIException(EXCEPTION_MESSAGE);
			}
			if (null != morningProdInfoMap) {
				morningCache.clear();
				morningCache.putAll(morningProdInfoMap);
				morningProdInfoMap.forEach((k, v) -> {
					morningProdInfoList.add(v.getProductID());
				});
				LOGGER.debug("Morning Product Type Cache - " + morningProdInfoList);
			} else {
				throw new APIException(EXCEPTION_MESSAGE);
			}
			initProductTypeCache.accept(cache, prodTypeCache);
			LOGGER.debug("Product Type Cache - " + prodTypeCache.toString());
			initProductSubTypeCache.accept(cache, prodSubTypeCache);
			LOGGER.debug("Product sub type Type Cache - " + prodSubTypeCache.toString());
			initProductTypeCache.accept(locationCache, locationProdTypeCache);
			LOGGER.debug("Location Product Type Cache - " + locationProdTypeCache.toString());
		} catch (Exception ex) {
			LOGGER.error("Error occured at ==> ", ex);
		}
		LOGGER.info("ProductInfoCache - Caching END");

	}

	private static class InstanceHolder {
		private static ProductInfoCache cacheInstance = new ProductInfoCache();
	}
}

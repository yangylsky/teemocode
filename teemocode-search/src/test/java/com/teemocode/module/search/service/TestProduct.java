package com.teemocode.module.search.service;

import java.util.Date;

import com.teemocode.commons.util.Pinyinable;

public class TestProduct extends TestIndexItem implements Pinyinable {
	private String groupUuid;

	private String userUuid;

	private String name;

	private String no;

	private String description;

	private String pinyin;

	private String fullPinyin;

	private Integer version;

	private Date createDate;

	private Date modifyDate;

	private Integer sortIdx;

	private String country;

	private String destination;

	private String cityStart;

	private Integer days;

	private Double price1;

	private Double price2;

	private Double recommendProfit;

	private String titleImg;

	private String brief;

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getPinyin() {
		return pinyin;
	}

	@Override
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	@Override
	public String getFullPinyin() {
		return fullPinyin;
	}

	@Override
	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getSortIdx() {
		return sortIdx;
	}

	public void setSortIdx(Integer sortIdx) {
		this.sortIdx = sortIdx;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCityStart() {
		return cityStart;
	}

	public void setCityStart(String cityStart) {
		this.cityStart = cityStart;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Double getPrice1() {
		return price1;
	}

	public void setPrice1(Double price1) {
		this.price1 = price1;
	}

	public Double getPrice2() {
		return price2;
	}

	public void setPrice2(Double price2) {
		this.price2 = price2;
	}

	public Double getRecommendProfit() {
		return recommendProfit;
	}

	public void setRecommendProfit(Double recommendProfit) {
		this.recommendProfit = recommendProfit;
	}

	public String getTitleImg() {
		return titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	@Override
	public String getPinyinableValue() {
		return name;
	}
}

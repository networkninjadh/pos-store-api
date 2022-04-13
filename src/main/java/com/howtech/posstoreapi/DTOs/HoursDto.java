package com.howtech.posstoreapi.DTOs;

import lombok.Data;

@Data
public class HoursDto {

	/**
	 * Regular open and close hours Monday through Sunday
	 */
	private int monOpenHour;
	private int monCloseHour;
	private int tueOpenHour;
	private int tueCloseHour;
	private int wedOpenHour;
	private int wedCloseHour;
	private int thuOpenHour;
	private int thuCloseHour;
	private int friOpenHour;
	private int friCloseHour;
	private int satOpenHour;
	private int satCloseHour;
	private int sunOpenHour;
	private int sunCloseHour;

	/**
	 * Store hours and boolean values for common holidays
	 */

	private boolean openForNewYearsEve;
	private int newYearsEveOpen;
	private int newYearsEveClose;
	private boolean openForNewYears;
	private int newYearsOpen;
	private int newYearsClose;
	private boolean openForIndependenceDay;
	private int independenceOpen;
	private int independenceClose;
	private boolean openForMemorialDay;
	private int memorialDayOpen;
	private int memorialDayClose;
	private boolean openForEaster;
	private int easterOpen;
	private int easterClose;
	private boolean openForColumbusDay;
	private int columbusDayOpen;
	private int columbusDayClose;
	private boolean openForThanksgiving;
	private int thanksgivingOpen;
	private int thanksgivingClose;
	private boolean openForChristmasEve;
	private int christmasEveOpen;
	private int christmasEveClose;
	private boolean openForChristmas;
	private int christmasOpen;
	private int christmasClose;

	public void setStoreHours(
			int monOpenHour,
			int monCloseHour,
			int tueOpenHour,
			int tueCloseHour,
			int wedOpenHour,
			int wedCloseHour,
			int thuOpenHour,
			int thuCloseHour,
			int friOpenHour,
			int friCloseHour,
			int satOpenHour,
			int satCloseHour,
			int sunOpenHour,
			int sunCloseHour) {
		this.monOpenHour = monOpenHour;
		this.monCloseHour = monCloseHour;
		this.tueOpenHour = tueOpenHour;
		this.tueCloseHour = tueCloseHour;
		this.wedOpenHour = wedOpenHour;
		this.wedCloseHour = wedCloseHour;
		this.thuOpenHour = thuOpenHour;
		this.thuCloseHour = thuCloseHour;
		this.friOpenHour = friOpenHour;
		this.friCloseHour = friCloseHour;
		this.satOpenHour = satOpenHour;
		this.satCloseHour = satCloseHour;
		this.sunOpenHour = sunOpenHour;
		this.sunCloseHour = sunCloseHour;
	}

	public HoursDto(int monOpenHour, int monCloseHour, int tueOpenHour, int tueCloseHour, int wedOpenHour,
			int wedCloseHour, int thuOpenHour, int thuCloseHour, int friOpenHour, int friCloseHour, int satOpenHour,
			int satCloseHour, int sunOpenHour, int sunCloseHour, boolean openForNewYearsEve, int newYearsEveOpen,
			int newYearsEveClose, boolean openForNewYears, int newYearsOpen, int newYearsClose,
			boolean openForIndependenceDay, int independenceOpen, int independenceClose, boolean openForMemorialDay,
			int memorialDayOpen, int memorialDayClose, boolean openForEaster, int easterOpen, int easterClose,
			boolean openForColumbusDay, int columbusDayOpen, int columbusDayClose, boolean openForThanksgiving,
			int thanksgivingOpen, int thanksgivingClose, boolean openForChristmasEve, int christmasEveOpen,
			int christmasEveClose, boolean openForChristmas, int christmasOpen, int christmasClose) {
		super();
		this.monOpenHour = monOpenHour;
		this.monCloseHour = monCloseHour;
		this.tueOpenHour = tueOpenHour;
		this.tueCloseHour = tueCloseHour;
		this.wedOpenHour = wedOpenHour;
		this.wedCloseHour = wedCloseHour;
		this.thuOpenHour = thuOpenHour;
		this.thuCloseHour = thuCloseHour;
		this.friOpenHour = friOpenHour;
		this.friCloseHour = friCloseHour;
		this.satOpenHour = satOpenHour;
		this.satCloseHour = satCloseHour;
		this.sunOpenHour = sunOpenHour;
		this.sunCloseHour = sunCloseHour;
		this.openForNewYearsEve = openForNewYearsEve;
		this.newYearsEveOpen = newYearsEveOpen;
		this.newYearsEveClose = newYearsEveClose;
		this.openForNewYears = openForNewYears;
		this.newYearsOpen = newYearsOpen;
		this.newYearsClose = newYearsClose;
		this.openForIndependenceDay = openForIndependenceDay;
		this.independenceOpen = independenceOpen;
		this.independenceClose = independenceClose;
		this.openForMemorialDay = openForMemorialDay;
		this.memorialDayOpen = memorialDayOpen;
		this.memorialDayClose = memorialDayClose;
		this.openForEaster = openForEaster;
		this.easterOpen = easterOpen;
		this.easterClose = easterClose;
		this.openForColumbusDay = openForColumbusDay;
		this.columbusDayOpen = columbusDayOpen;
		this.columbusDayClose = columbusDayClose;
		this.openForThanksgiving = openForThanksgiving;
		this.thanksgivingOpen = thanksgivingOpen;
		this.thanksgivingClose = thanksgivingClose;
		this.openForChristmasEve = openForChristmasEve;
		this.christmasEveOpen = christmasEveOpen;
		this.christmasEveClose = christmasEveClose;
		this.openForChristmas = openForChristmas;
		this.christmasOpen = christmasOpen;
		this.christmasClose = christmasClose;
	}
}